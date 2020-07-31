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
package org.catrobat.catroid.content;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.ScreenModes;
import org.catrobat.catroid.common.ScreenValues;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.formulaeditor.BaseDataContainer;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.io.XStreamFieldKeyOrder;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@XStreamAlias("program")
// Remove checkstyle disable when https://github.com/checkstyle/checkstyle/issues/1349 is fixed
// CHECKSTYLE DISABLE IndentationCheck FOR 7 LINES
@XStreamFieldKeyOrder({
		"header",
		"settings",
		"scenes",
		"programVariableList",
		"programListOfLists"
})
public class Project {

	private static final long serialVersionUID = 1L;

	@XStreamAlias("header")
	private XmlHeader xmlHeader = new XmlHeader();
	@XStreamAlias("settings")
	private List<Setting> settings = new ArrayList<>();
	@XStreamAlias("programVariableList")
	private List<UserVariable> projectVariables = new ArrayList<>();
	@XStreamAlias("programListOfLists")
	private List<UserList> projectLists = new ArrayList<>();
	@XStreamAlias("scenes")
	private List<Scene> sceneList = new ArrayList<>();

	public class Context {}
	public Project(Context context, String name, boolean landscapeMode) {
		xmlHeader.setProgramName(name);
		xmlHeader.setDescription("");

		xmlHeader.setlandscapeMode(landscapeMode);

		if (landscapeMode) {
			ifPortraitSwitchWidthAndHeight();
		} else {
			ifLandscapeSwitchWidthAndHeight();
		}
		xmlHeader.virtualScreenWidth = ScreenValues.SCREEN_WIDTH;
		xmlHeader.virtualScreenHeight = ScreenValues.SCREEN_HEIGHT;
		xmlHeader.scenesEnabled = true;
	}

	public Project(Context context, String name) {
		this(context, name, false);
	}

	public Project(SupportProject oldProject, Context context) {
		xmlHeader = oldProject.xmlHeader;
		settings = oldProject.settings;
		projectVariables = oldProject.dataContainer.projectVariables;
		projectLists = oldProject.dataContainer.projectLists;
		Scene scene = new Scene(null, "Scene 1", this);

		DataContainer container = new DataContainer(this);
		container.setSpriteVariablesForSupportContainer(oldProject.dataContainer);
		scene.setDataContainer(container);
		scene.setSpriteList(oldProject.spriteList);
		sceneList.add(scene);
	}

	public List<Scene> getSceneList() {
		return sceneList;
	}

	public void setSceneList(List<Scene> scenes) {
		sceneList = scenes;
	}

	public Scene getDefaultScene() {
		return sceneList.get(0);
	}

	public List<UserVariable> getProjectVariables() {
		if (projectVariables == null) {
			projectVariables = new ArrayList<>();
		}
		return projectVariables;
	}

	public List<UserList> getProjectLists() {
		if (projectLists == null) {
			projectLists = new ArrayList<>();
		}
		return projectLists;
	}

	private void ifLandscapeSwitchWidthAndHeight() {
		if (ScreenValues.SCREEN_WIDTH > ScreenValues.SCREEN_HEIGHT) {
			int tmp = ScreenValues.SCREEN_HEIGHT;
			ScreenValues.SCREEN_HEIGHT = ScreenValues.SCREEN_WIDTH;
			ScreenValues.SCREEN_WIDTH = tmp;
		}
	}

	private void ifPortraitSwitchWidthAndHeight() {
		if (ScreenValues.SCREEN_WIDTH < ScreenValues.SCREEN_HEIGHT) {
			int tmp = ScreenValues.SCREEN_HEIGHT;
			ScreenValues.SCREEN_HEIGHT = ScreenValues.SCREEN_WIDTH;
			ScreenValues.SCREEN_WIDTH = tmp;
		}
	}

	public void setName(String name) {
		xmlHeader.setProgramName(name);
	}

	public String getName() {
		return xmlHeader.getProgramName();
	}

	public XmlHeader getXmlHeader() {
		return this.xmlHeader;
	}

	// default constructor for XMLParser
	public Project() {
	}

	public List<Setting> getSettings() {
		return settings;
	}

	public void setXmlHeader(XmlHeader xmlHeader) {
		this.xmlHeader = xmlHeader;
	}
}
