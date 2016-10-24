#  ScratchToCatrobat: A tool for converting Scratch projects into Catrobat programs.
#  Copyright (C) 2013-2016 The Catrobat Team
#  (<http://developer.catrobat.org/credits>)
#
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU Affero General Public License as
#  published by the Free Software Foundation, either version 3 of the
#  License, or (at your option) any later version.
#
#  An additional term exception under section 7 of the GNU Affero
#  General Public License, version 3, is available at
#  http://developer.catrobat.org/license_additional_term
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
#  GNU Affero General Public License for more details.
#
#  You should have received a copy of the GNU Affero General Public License
#  along with this program.  If not, see <http://www.gnu.org/licenses/>.

import logging
import ast
import helpers as webhelpers

_logger = logging.getLogger(__name__)


class Job(object):
    class CategoryType(object):
        SCRATCH          =  0
        SOUND            =  1

        @classmethod
        def category_for_type(cls, job_obj):
            category_type_map = {
                ScratchJob: cls.SCRATCH,
                SoundJob:   cls.SOUND,
            }
            for category_class, category in category_type_map.iteritems():
                if isinstance(job_obj, category_class):
                    return category
            raise RuntimeError("Unknown type of job: " + job_obj.__class__.__name__)

    class State(object):
        READY = 0
        RUNNING = 1
        FINISHED = 2
        FAILED = 3


    DATETIME_FORMAT = "%Y-%m-%d %H:%M:%S"
    CACHE_ENTRY_VALID_FOR = 600

    def __init__(self, job_ID=0, title=None, state=State.READY, progress=0, output=None,
                 archive_cached_utc_date=None):
        self.jobCategory = Job.CategoryType.category_for_type(self)
        self.jobID = job_ID
        self.title = title
        self.state = state
        self.progress = progress
        self.output = output
        self.archiveCachedUTCDate = archive_cached_utc_date


    def is_in_progress(self):
        return self.state in (Job.State.READY, Job.State.RUNNING)


    def save_to_redis(self, redis_connection, key):
        return redis_connection.set(key, self.__dict__)


    @classmethod
    def from_redis(cls, redis_connection, key):
        dict_string = redis_connection.get(key)
        if dict_string == None:
            return None
        job = cls()
        for (key, value) in ast.literal_eval(dict_string).items():
            setattr(job, key, value)
        return job


class ScratchJob(Job):

    def __init__(self, job_ID=0, title=None, state=Job.State.READY, progress=0, output=None,
                 image_url=None, archive_cached_utc_date=None):
        super(ScratchJob, self).__init__(job_ID, title, state, progress, output, archive_cached_utc_date)
        self.imageURL = image_url


class SoundJob(Job):

    def __init__(self, job_ID=0, title=None, sound_urls=[], state=Job.State.READY, progress=0,
                 output=None, archive_cached_utc_date=None):
        super(SoundJob, self).__init__(job_ID, title, state, progress, output, archive_cached_utc_date)
        assert isinstance(sound_urls, list)
        self.sound_urls = sound_urls


def add_listening_client_to_job(redis_connection, category_type, client_ID, job_ID):
    client_job_key = webhelpers.REDIS_LISTENING_CLIENT_JOB_KEY_TEMPLATE.format(category_type, job_ID)
    clients_of_job = redis_connection.get(client_job_key)
    clients_of_job = ast.literal_eval(clients_of_job) if clients_of_job != None else []

    assert isinstance(clients_of_job, list)
    if client_ID not in clients_of_job:
        clients_of_job.append(client_ID)
        return redis_connection.set(client_job_key, clients_of_job)
    return True


def remove_all_listening_clients_from_job(redis_connection, category_type, job_ID):
    client_job_key = webhelpers.REDIS_LISTENING_CLIENT_JOB_KEY_TEMPLATE.format(category_type, job_ID)
    return redis_connection.delete(client_job_key)


def assign_job_to_client(redis_connection, category_type, job_ID, client_ID):
    job_client_key = webhelpers.REDIS_JOB_CLIENT_KEY_TEMPLATE.format(category_type, client_ID)
    jobs_of_client = redis_connection.get(job_client_key)
    jobs_of_client = ast.literal_eval(jobs_of_client) if jobs_of_client != None else []

    assert isinstance(jobs_of_client, list)
    if job_ID in jobs_of_client:
        jobs_of_client.remove(job_ID)

    jobs_of_client.insert(0, job_ID)
    return redis_connection.set(job_client_key, jobs_of_client)


def get_jobs_of_client(redis_connection, category_type, client_ID):
    job_client_key = webhelpers.REDIS_JOB_CLIENT_KEY_TEMPLATE.format(category_type, client_ID)
    jobs_of_client = redis_connection.get(job_client_key)
    jobs_of_client = ast.literal_eval(jobs_of_client) if jobs_of_client != None else []
    assert isinstance(jobs_of_client, list)

    jobs = []
    for job_ID in jobs_of_client:
        project_key = webhelpers.REDIS_JOB_KEY_TEMPLATE.format(category_type, job_ID)
        job = Job.from_redis(redis_connection, project_key)
        if job == None:
            _logger.warn("Ignoring missing job for scratch project ID {}".format(job_ID))
            continue

        jobs.append(job)
    return jobs

