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

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.common.NfcTagData;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.content.bricks.PlaySoundBrick;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.content.bricks.UserScriptDefinitionBrick;
import org.catrobat.catroid.content.bricks.UserVariableBrick;
import org.catrobat.catroid.content.bricks.WhenConditionBrick;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.InterpretationException;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.io.XStreamFieldKeyOrder;
import org.catrobat.catroid.ui.fragment.SpriteFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Remove checkstyle disable when https://github.com/checkstyle/checkstyle/issues/1349 is fixed
// CHECKSTYLE DISABLE IndentationCheck FOR 8 LINES
@XStreamFieldKeyOrder({
		"name",
		"lookList",
		"soundList",
		"scriptList",
		"userBricks",
		"nfcTagList"
})
public class Sprite {
	private static final long serialVersionUID = 1L;
	private static final String TAG = Sprite.class.getSimpleName();

	private static SpriteFactory spriteFactory = new SpriteFactory();

	public transient Look look = new Look(this);
	public transient boolean isBackpackObject = false;
	@XStreamAsAttribute
	private String name;
	private List<Script> scriptList = new ArrayList<>();
	private List<LookData> lookList = new ArrayList<>();
	private List<SoundInfo> soundList = new ArrayList<>();
	private List<UserBrick> userBricks = new ArrayList<>();
	private List<NfcTagData> nfcTagList = new ArrayList<>();
	public Sprite(String name) {
		this.name = name;
	}

	public Sprite() {
	}

	public List<Script> getScriptList() {
		return scriptList;
	}

	public List<Brick> getListWithAllBricks() {
		List<Brick> allBricks = new ArrayList<>();
		for (Script script : scriptList) {
			allBricks.add(script.getScriptBrick());
			allBricks.addAll(script.getBrickList());
		}
		for (UserBrick userBrick : userBricks) {
			allBricks.add(userBrick);
			Script userScript = userBrick.getDefinitionBrick().getUserScript();
			if (userScript != null) {
				allBricks.addAll(userScript.getBrickList());
			}
		}
		return allBricks;
	}

	public UserBrick addUserBrick(UserBrick brick) {
		if (userBricks == null) {
			userBricks = new ArrayList<>();
		}
		userBricks.add(brick);
		return brick;
	}

	public List<UserBrick> getUserBrickList() {
		if (userBricks == null) {
			userBricks = new ArrayList<>();
		}
		return userBricks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addScript(Script script) {
		if (script != null && !scriptList.contains(script)) {
			scriptList.add(script);
		}
	}

	public void addScript(int index, Script script) {
		if (script != null && !scriptList.contains(script)) {
			scriptList.add(index, script);
		}
	}

	public Script getScript(int index) {
		if (index < 0 || index >= scriptList.size()) {
			return null;
		}
		return scriptList.get(index);
	}

	public int getNumberOfScripts() {
		if (scriptList != null) {
			return scriptList.size();
		}
		return 0;
	}

	public int getScriptIndex(Script script) {
		return scriptList.indexOf(script);
	}

	public List<LookData> getLookDataList() {
		return lookList;
	}

	public boolean existLookDataByName(LookData look) {
		for (LookData lookdata : lookList) {
			if (lookdata.getLookName().equals(look.getLookName())) {
				return true;
			}
		}
		return false;
	}

	public boolean existLookDataByFileName(LookData look) {
		for (LookData lookdata : lookList) {
			if (lookdata.getLookFileName().equals(look.getLookFileName())) {
				return true;
			}
		}
		return false;
	}

	public List<SoundInfo> getSoundList() {
		return soundList;
	}

	public void setSoundList(List<SoundInfo> list) {
		soundList = list;
	}

	public List<NfcTagData> getNfcTagList() {
		return nfcTagList;
	}

	public void setNfcTagList(List<NfcTagData> list) {
		nfcTagList = list;
	}

	public boolean existSoundInfoByName(SoundInfo sound) {
		for (SoundInfo soundInfo : soundList) {
			if (soundInfo.getTitle().equals(sound.getTitle())) {
				return true;
			}
		}
		return false;
	}

	public boolean existSoundInfoByFileName(SoundInfo sound) {
		for (SoundInfo soundInfo : soundList) {
			if (soundInfo.getSoundFileName().equals(sound.getSoundFileName())) {
				return true;
			}
		}
		return false;
	}

	public void updateUserVariableReferencesInUserVariableBricks(List<UserVariable> variables) {
		for (Brick brick : getListWithAllBricks()) {
			if (brick instanceof UserVariableBrick) {
				UserVariableBrick userVariableBrick = (UserVariableBrick) brick;
				for (UserVariable variable : variables) {
					UserVariable userVariableBrickVariable = userVariableBrick.getUserVariable();
					if (userVariableBrickVariable != null
							&& variable.getName().equals(userVariableBrickVariable.getName())) {
						userVariableBrick.setUserVariable(variable);
						break;
					}
				}
			}
		}
	}
}
