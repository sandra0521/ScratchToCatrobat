/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid;

import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.FileChecksumContainer;
import org.catrobat.catroid.common.ScreenModes;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfLogicElseBrick;
import org.catrobat.catroid.content.bricks.IfLogicEndBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicEndBrick;
import org.catrobat.catroid.content.bricks.LoopBeginBrick;
import org.catrobat.catroid.content.bricks.LoopEndBrick;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.exceptions.CompatibilityProjectException;
import org.catrobat.catroid.exceptions.LoadingProjectException;
import org.catrobat.catroid.exceptions.OutdatedVersionProjectException;
import org.catrobat.catroid.io.StorageHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ProjectManager {
	private static final ProjectManager INSTANCE = new ProjectManager();
	private static final String TAG = ProjectManager.class.getSimpleName();

	private Project project;
	private Scene currentScene;
	private Scene sceneToPlay;
	private Scene startScene;
	private Script currentScript;
	private Sprite currentSprite;
	private Sprite previousSprite;
	private UserBrick currentUserBrick;
	private boolean asynchronousTask = true;
	private boolean comingFromScriptFragmentToSoundFragment;
	private boolean comingFromScriptFragmentToLooksFragment;
	private boolean handleNewSceneFromScriptActivity;
	private boolean showUploadDialog = false;
	private boolean showLegoSensorInfoDialog = true;

	private FileChecksumContainer fileChecksumContainer = new FileChecksumContainer();

	private ProjectManager() {
		this.comingFromScriptFragmentToSoundFragment = false;
		this.comingFromScriptFragmentToLooksFragment = false;
		this.handleNewSceneFromScriptActivity = false;
	}

	public static ProjectManager getInstance() {
		return INSTANCE;
	}

	public boolean getComingFromScriptFragmentToSoundFragment() {
		return this.comingFromScriptFragmentToSoundFragment;
	}

	public void setComingFromScriptFragmentToSoundFragment(boolean value) {
		this.comingFromScriptFragmentToSoundFragment = value;
	}

	public boolean getComingFromScriptFragmentToLooksFragment() {
		return this.comingFromScriptFragmentToLooksFragment;
	}

	public void setComingFromScriptFragmentToLooksFragment(boolean value) {
		this.comingFromScriptFragmentToLooksFragment = value;
	}

	public void setHandleNewSceneFromScriptActivity() {
		handleNewSceneFromScriptActivity = true;
	}

	public boolean getHandleNewSceneFromScriptActivity() {
		if (handleNewSceneFromScriptActivity) {
			handleNewSceneFromScriptActivity = false;
			return true;
		}
		return false;
	}

	public Project getCurrentProject() {
		return project;
	}

	public Scene getSceneToPlay() {
		if (sceneToPlay == null) {
			sceneToPlay = getCurrentScene();
		}
		return sceneToPlay;
	}

	public void setSceneToPlay(Scene scene) {
		sceneToPlay = scene;
	}

	public Scene getStartScene() {
		if (startScene == null) {
			startScene = getCurrentScene();
		}
		return startScene;
	}

	public void setStartScene(Scene scene) {
		startScene = scene;
	}

	public Scene getCurrentScene() {
		if (currentScene == null) {
			currentScene = project.getDefaultScene();
		}
		return currentScene;
	}

	public void setProject(Project project) {
		currentScript = null;
		currentSprite = null;

		this.project = project;
		if (project != null) {
			currentScene = project.getDefaultScene();
			sceneToPlay = currentScene;
		}
	}

	public void setCurrentProject(Project project) {
		this.project = project;
	}

	public Sprite getCurrentSprite() {
		return currentSprite;
	}

	public void setCurrentSprite(Sprite sprite) {
		previousSprite = currentSprite;
		currentSprite = sprite;
	}

	public Sprite getPreviousSprite() {
		return previousSprite;
	}

	public Script getCurrentScript() {
		return currentScript;
	}

	public void setCurrentScene(Scene scene) {
		this.currentScene = scene;
		sceneToPlay = scene;
	}

	public void setCurrentScript(Script script) {
		if (script == null) {
			currentScript = null;
		} else if (currentSprite.getScriptIndex(script) != -1) {
			currentScript = script;
		}
	}

	public UserBrick getCurrentUserBrick() {
		return currentUserBrick;
	}

	public void setCurrentUserBrick(UserBrick brick) {
		currentUserBrick = brick;
	}

	public void addSprite(Sprite sprite) {
		getCurrentScene().addSprite(sprite);
	}

	public boolean spriteExists(String spriteName) {
		for (Sprite sprite : getCurrentScene().getSpriteList()) {
			if (sprite.getName().equalsIgnoreCase(spriteName)) {
				return true;
			}
		}
		return false;
	}

	public FileChecksumContainer getFileChecksumContainer() {
		return this.fileChecksumContainer;
	}

	public void setFileChecksumContainer(FileChecksumContainer fileChecksumContainer) {
		this.fileChecksumContainer = fileChecksumContainer;
	}

	public boolean getShowLegoSensorInfoDialog() {
		return showLegoSensorInfoDialog;
	}

	public void setShowLegoSensorInfoDialog(boolean showLegoSensorInfoDialogFlag) {
		showLegoSensorInfoDialog = showLegoSensorInfoDialogFlag;
	}
}
