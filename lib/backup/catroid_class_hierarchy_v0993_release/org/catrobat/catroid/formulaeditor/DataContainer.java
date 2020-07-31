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
package org.catrobat.catroid.formulaeditor;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.content.bricks.UserScriptDefinitionBrickElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataContainer extends BaseDataContainer {
	private transient Project project;

	public DataContainer(Project project) {
		spriteVariables = new HashMap<>();
		spriteListOfLists = new HashMap<>();

		this.project = project;
	}

	private DataContainer() {
	}

	public void setSpriteVariablesForSupportContainer(SupportDataContainer container) {
		if (container.spriteVariables != null) {
			spriteVariables = container.spriteVariables;
		}
		if (container.userBrickVariables != null) {
			userBrickVariables = container.userBrickVariables;
		}
		if (container.spriteListOfLists != null) {
			spriteListOfLists = container.spriteListOfLists;
		}
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<UserVariable> getProjectVariables() {
		if (project == null) {
			project = ProjectManager.getInstance().getCurrentProject();
		}
		return project.getProjectVariables();
	}

	public List<UserList> getProjectLists() {
		if (project == null) {
			project = ProjectManager.getInstance().getCurrentProject();
		}
		return project.getProjectLists();
	}

	public UserVariable getUserVariable(String userVariableName, Sprite sprite) {
		UserVariable userVariable;
		userVariable = findUserVariable(userVariableName, getOrCreateVariableListForSprite(sprite));
		if (userVariable == null) {
			userVariable = findUserVariable(userVariableName, getProjectVariables());
		}

		UserBrick userBrick = getCurrentUserBrick();
		if (userVariable == null && userBrick != null) {
			userVariable = findUserVariable(userVariableName, getOrCreateVariableListForUserBrick(userBrick));
		}
		return userVariable;
	}

	public UserVariable addSpriteUserVariable(String userVariableName) {
		Sprite currentSprite = ProjectManager.getInstance().getCurrentSprite();
		return addSpriteUserVariableToSprite(currentSprite, userVariableName);
	}

	public UserVariable addSpriteUserVariableToSprite(Sprite sprite, String userVariableName) {
		List<UserVariable> varList = getOrCreateVariableListForSprite(sprite);
		UserVariable userVariableToAdd = new UserVariable(userVariableName);
		varList.add(userVariableToAdd);
		return userVariableToAdd;
	}

	public UserVariable addProjectUserVariable(String userVariableName) {
		UserVariable userVariableToAdd = new UserVariable(userVariableName);
		getProjectVariables().add(userVariableToAdd);
		return userVariableToAdd;
	}

	public List<UserVariable> getOrCreateVariableListForSprite(Sprite sprite) {
		List<UserVariable> variables = spriteVariables.get(sprite);

		if (variables == null) {
			variables = new ArrayList<>();
			spriteVariables.put(sprite, variables);
			removeSpriteVariableWithSameSpriteName(sprite);
		}
		return variables;
	}

	private void removeSpriteVariableWithSameSpriteName(Sprite spriteToKeep) {
		if (spriteVariables == null || spriteToKeep == null) {
			return;
		}

		Iterator iterator = spriteVariables.keySet().iterator();
		while (iterator.hasNext()) {
			Sprite sprite = (Sprite) iterator.next();
			if (sprite == null || !(sprite == spriteToKeep)
					&& spriteVariables.get(sprite).size() == 0
					&& sprite.getName().equals(spriteToKeep.getName())) {
				iterator.remove();
			}
		}
	}

	public void cleanVariableListForSprite(Sprite sprite) {
		List<UserVariable> variables = spriteVariables.get(sprite);
		if (variables != null) {
			variables.clear();
		}
		spriteVariables.remove(sprite);
	}

	public UserVariable getUserVariable(String name, UserBrick userBrick, Sprite currentSprite) {
		List<UserVariable> contextList = getUserVariableContext(name, userBrick, currentSprite);
		return findUserVariable(name, contextList);
	}

	/**
	 * This function finds the user variable with userVariableName in the current context.
	 *
	 * The current context consists of all global variables, the sprite variables for the current sprite,
	 * and the user brick variables for the current user brick.
	 */
	public List<UserVariable> getUserVariableContext(String name, UserBrick userBrick, Sprite currentSprite) {
		UserVariable variableToReturn;
		List<UserVariable> spriteVariables = getOrCreateVariableListForSprite(currentSprite);
		variableToReturn = findUserVariable(name, spriteVariables);
		if (variableToReturn != null) {
			return spriteVariables;
		}

		if (userBrick != null) {
			List<UserVariable> userBrickVariables = getOrCreateVariableListForUserBrick(userBrick);
			variableToReturn = findUserVariable(name, userBrickVariables);
			if (variableToReturn != null) {
				return userBrickVariables;
			}
		}

		variableToReturn = findUserVariable(name, getProjectVariables());
		if (variableToReturn != null) {
			return getProjectVariables();
		}
		return null;
	}

	public UserVariable findUserVariable(String name, List<UserVariable> variables) {
		if (variables == null) {
			return null;
		}
		for (UserVariable variable : variables) {
			if (variable.getName().equals(name)) {
				return variable;
			}
		}
		return null;
	}

	public UserList getUserList(String userListName, Sprite sprite) {
		UserList userList;
		userList = findUserList(userListName, getOrCreateUserListListForSprite(sprite));
		if (userList == null) {
			userList = findUserList(userListName, getProjectLists());
		}
		return userList;
	}

	public UserList addSpriteUserList(String userListName) {
		Sprite currentSprite = ProjectManager.getInstance().getCurrentSprite();
		return addSpriteUserListToSprite(currentSprite, userListName);
	}

	public UserList addSpriteUserListToSprite(Sprite sprite, String userListName) {
		UserList userListToAdd = new UserList(userListName);
		List<UserList> listOfUserLists = getOrCreateUserListListForSprite(sprite);
		listOfUserLists.add(userListToAdd);
		return userListToAdd;
	}

	public UserList addProjectUserList(String userListName) {
		UserList userListToAdd = new UserList(userListName);
		getProjectLists().add(userListToAdd);
		return userListToAdd;
	}

	public List<UserList> getOrCreateUserListListForSprite(Sprite sprite) {
		List<UserList> userLists = spriteListOfLists.get(sprite);
		if (userLists == null) {
			userLists = new ArrayList<>();
			spriteListOfLists.put(sprite, userLists);
		}
		return userLists;
	}

	public UserList findUserList(String name, List<UserList> userLists) {
		if (userLists == null) {
			return null;
		}
		for (UserList userList : userLists) {
			if (userList.getName().equals(name)) {
				return userList;
			}
		}
		return null;
	}

	public UserList getUserList() {
		if (getProjectLists().size() > 0) {
			return getProjectLists().get(0);
		}

		for (Sprite currentSprite : spriteListOfLists.keySet()) {
			if (spriteListOfLists.get(currentSprite).size() > 0) {
				return spriteListOfLists.get(currentSprite).get(0);
			}
		}
		return null;
	}

	public boolean existSpriteList(UserList userList, Sprite sprite) {
		List<UserList> list = spriteListOfLists.get(sprite);
		if (list == null) {
			return false;
		}
		return list.contains(userList);
	}

	public UserBrick getCurrentUserBrick() {
		return ProjectManager.getInstance().getCurrentUserBrick();
	}

	public List<UserVariable> getOrCreateVariableListForUserBrick(UserBrick userBrick) {
		if (userBrick == null) {
			return new ArrayList<>();
		}
		List<UserVariable> variables = userBrickVariables.get(userBrick);

		if (variables == null) {
			variables = new ArrayList<>();
			userBrickVariables.put(userBrick, variables);
		}

		return variables;
	}

	public void setUserBrickVariables(UserBrick key, List<UserVariable> userVariables) {
		userBrickVariables.put(key, userVariables);
	}
}
