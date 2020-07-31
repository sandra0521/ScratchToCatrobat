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
package org.catrobat.catroid.content.bricks;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.ScreenValues;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserVariable;
import java.util.ArrayList;
import java.util.List;

public class UserScriptDefinitionBrick extends BrickBaseType implements ScriptBrick {

	private static final long serialVersionUID = 1L;
	private static final String TAG = UserScriptDefinitionBrick.class.getSimpleName();
	private static final String LINE_BREAK = "linebreak";

	private StartScript script;

	@XStreamAlias("userBrickElements")
	private List<UserScriptDefinitionBrickElement> userScriptDefinitionBrickElements;

	public UserScriptDefinitionBrick() {
		this.script = new StartScript(true);
		this.userScriptDefinitionBrickElements = new ArrayList<>();
	}

	public void appendBrickToScript(Brick brick) {
		this.getScriptSafe().addBrick(brick);
	}

	public Script getScriptSafe() {
		return getUserScript();
	}

	public Script getUserScript() {
		return script;
	}

	public List<UserScriptDefinitionBrickElement> getUserScriptDefinitionBrickElements() {
		return userScriptDefinitionBrickElements;
	}
}
