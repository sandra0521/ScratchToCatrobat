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

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.content.bricks.PointToBrick;
import org.catrobat.catroid.content.bricks.SceneStartBrick;
import org.catrobat.catroid.content.bricks.SceneTransitionBrick;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.content.bricks.UserListBrick;
import org.catrobat.catroid.content.bricks.UserVariableBrick;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.io.XStreamFieldKeyOrder;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("scene")
// Remove checkstyle disable when https://github.com/checkstyle/checkstyle/issues/1349 is fixed
// CHECKSTYLE DISABLE IndentationCheck FOR 7 LINES
@XStreamFieldKeyOrder({
		"name",
		"objectList",
		"data",
		"originalWidth",
		"originalHeight"
})
public class Scene {

	private static final long serialVersionUID = 1L;

	@XStreamAlias("name")
	private String sceneName;
	@XStreamAlias("objectList")
	private List<Sprite> spriteList = new ArrayList<>();
	@XStreamAlias("data")
	private DataContainer dataContainer = null;
	@XStreamAlias("originalWidth")
	private int originalWidth = 0;
	@XStreamAlias("originalHeight")
	private int originalHeight = 0;

	private transient Project project;
	public class Context{};
	public Scene(Context context, String name, Project project) {
		sceneName = name;
		dataContainer = new DataContainer(project);
		this.project = project;

		if (project != null) {
			originalWidth = project.getXmlHeader().virtualScreenWidth;
			originalHeight = project.getXmlHeader().virtualScreenHeight;
		}

		if (context == null) {
			return;
		}

	}

	public synchronized void addSprite(Sprite sprite) {
		if (spriteList.contains(sprite)) {
			return;
		}
		spriteList.add(sprite);
	}

	public int getOriginalWidth() {
		return originalWidth;
	}

	public int getOriginalHeight() {
		return originalHeight;
	}

	public List<Sprite> getSpriteList() {
		return spriteList;
	}

	public synchronized void setSpriteList(List<Sprite> spriteList) {
		this.spriteList = spriteList;
	}

	public synchronized void setDataContainer(DataContainer container) {
		dataContainer = container;
	}

	public synchronized void setSceneName(String name) {
		sceneName = name;
	}

	// default constructor for XMLParser
	public Scene() {
	}

	public synchronized void setProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}

	public DataContainer getDataContainer() {
		return dataContainer;
	}

	public boolean existSpriteList(UserList list, Sprite sprite) {
		if (!spriteList.contains(sprite)) {
			return false;
		}
		return dataContainer.existSpriteList(list, sprite);
	}
}
