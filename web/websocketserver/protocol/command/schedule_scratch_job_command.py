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
import os
from datetime import datetime as dt, timedelta
from rq import Queue, use_connection
from worker.convertscratchjob import convert_scratch_project
from command import Command
from websocketserver.protocol import job
from scratchtocatrobat.tools import helpers
import helpers as webhelpers
from websocketserver.protocol.message.base.error_message import ErrorMessage
from websocketserver.protocol.message.job.job_already_running_message import JobAlreadyRunningMessage
from websocketserver.protocol.message.job.job_finished_message import JobFinishedMessage
from websocketserver.protocol.message.job.job_failed_message import JobFailedMessage
from websocketserver.protocol.message.job.job_ready_message import JobReadyMessage

CATROBAT_FILE_EXT = helpers.config.get("CATROBAT", "file_extension")
MAX_NUM_SCHEDULED_JOBS_PER_CLIENT = int(helpers.config.get("CONVERTER_JOB", "max_num_scheduled_jobs_per_client"))
JOB_TIMEOUT = int(helpers.config.get("CONVERTER_JOB", "timeout"))

_logger = logging.getLogger(__name__)


class ScheduleScratchJobCommand(Command):

    def execute(self, ctxt, args):
        client_ID = ctxt.handler.get_client_ID()
        assert self.is_valid_client_ID(ctxt.redis_connection, client_ID)

        # validate job_ID
        job_ID = args[Command.ArgumentType.JOB_ID]
        if not self.is_valid_job_ID(job_ID):
            _logger.error("Invalid jobID given!")
            return ErrorMessage("Invalid jobID given!")

        force = False
        if Command.ArgumentType.FORCE in args:
            force_param_str = str(args[Command.ArgumentType.FORCE]).lower()
            force = force_param_str == "true" or force_param_str == "1"

        verbose = False
        if Command.ArgumentType.VERBOSE in args:
            verbose_param_str = str(args[Command.ArgumentType.VERBOSE]).lower()
            verbose = verbose_param_str == "true" or verbose_param_str == "1"

        redis_conn = ctxt.redis_connection
        jobs_of_client = job.get_jobs_of_client(redis_conn, job.Job.CategoryType.SCRATCH, client_ID)
        jobs_of_client_in_progress = filter(lambda job: job.is_in_progress(), jobs_of_client)
        if len(jobs_of_client_in_progress) >= MAX_NUM_SCHEDULED_JOBS_PER_CLIENT:
            return ErrorMessage("Maximum number of jobs per client limit exceeded: {}"
                                .format(MAX_NUM_SCHEDULED_JOBS_PER_CLIENT))

        # TODO: lock.acquire() => use python's context-handler (i.e. "with"-keyword) and file lock!
        job.assign_job_to_client(redis_conn, job.Job.CategoryType.SCRATCH, job_ID, client_ID)
        job_key = webhelpers.REDIS_JOB_KEY_TEMPLATE.format(job.Job.CategoryType.SCRATCH, job_ID)
        scratch_job = job.Job.from_redis(redis_conn, job_key)

        if scratch_job != None:
            if scratch_job.is_in_progress():
                # TODO: lock.release()
                _logger.info("Job already scheduled (scratch project with ID: %d)", job_ID)
                if not job.add_listening_client_to_job(redis_conn, job.Job.CategoryType.SCRATCH,
                                                       client_ID, job_ID):
                    return JobFailedMessage(job_ID, "Cannot add client as listener to job!")
                return JobAlreadyRunningMessage(job_ID, scratch_job.title, scratch_job.imageURL)

            elif scratch_job.state == job.Job.State.FINISHED and not force:
                assert scratch_job.archiveCachedUTCDate is not None
                archive_cached_utc_date = dt.strptime(scratch_job.archiveCachedUTCDate, job.Job.DATETIME_FORMAT)
                download_valid_until_utc = archive_cached_utc_date + timedelta(seconds=job.Job.CACHE_ENTRY_VALID_FOR)

                if dt.utcnow() <= download_valid_until_utc:
                    file_name = str(job_ID) + CATROBAT_FILE_EXT
                    file_path = "%s/%s" % (ctxt.jobmonitorserver_settings["download_dir"], file_name)
                    if file_name and os.path.exists(file_path):
                        download_url = webhelpers.create_download_url(job_ID, client_ID, scratch_job.title)
                        # TODO: lock.release()
                        return JobFinishedMessage(job_ID, download_url, scratch_job.archiveCachedUTCDate)

            else:
                assert scratch_job.state == job.Job.State.FAILED or force

        scratch_job = job.ScratchJob(job_ID, "-", job.Job.State.READY)
        if not scratch_job.save_to_redis(redis_conn, job_key):
            # TODO: lock.release()
            return JobFailedMessage(job_ID, "Cannot schedule job!")

        if not scratch_job.add_listening_client_to_job(redis_conn, job.Job.CategoryType.SCRATCH,
                                                       client_ID, job_ID):
            return JobFailedMessage(job_ID, "Cannot add client as listener to job!")

        # schedule this job
        use_connection(redis_conn)
        q = Queue(connection=redis_conn)
        host, port = ctxt.jobmonitorserver_settings["host"], ctxt.jobmonitorserver_settings["port"]
        _logger.info("Scheduled new scratch job (host: %s, port: %s, scratch project ID: %d)", host, port, job_ID)
        #q.enqueue(convert_scratch_project, scratch_project_ID, host, port)
        q.enqueue_call(func=convert_scratch_project, args=(job_ID, host, port, verbose,), timeout=JOB_TIMEOUT)
        # TODO: lock.release()
        return JobReadyMessage(job_ID)
