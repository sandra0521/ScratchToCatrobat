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

import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfLogicElseBrick;
import org.catrobat.catroid.content.bricks.IfLogicEndBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicEndBrick;
import org.catrobat.catroid.content.bricks.LoopBeginBrick;
import org.catrobat.catroid.content.bricks.LoopEndBrick;
import org.catrobat.catroid.content.bricks.ScriptBrick;
import org.catrobat.catroid.content.bricks.UserBrick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Script {

	private static final long serialVersionUID = 1L;
	protected ArrayList<Brick> brickList;

	protected transient ScriptBrick brick;

	protected boolean commentedOut = false;

	public Script() {
		brickList = new ArrayList<>();
	}

	public abstract ScriptBrick getScriptBrick();

	public void addBrick(Brick brick) {
		if (brick != null) {
			brickList.add(brick);
			updateUserBricksIfNecessary(brick);
		}
	}

	public void addBrick(int position, Brick brick) {
		if (brick != null) {
			brickList.add(position, brick);
			updateUserBricksIfNecessary(brick);
		}
	}

	private void updateUserBricksIfNecessary(Brick brick) {
		if (brick instanceof UserBrick) {
			UserBrick userBrick = (UserBrick) brick;
			userBrick.updateUserBrickParametersAndVariables();
		}
	}

	public void removeInstancesOfUserBrick(UserBrick userBrickToRemove) {

		LinkedList<Brick> toRemove = new LinkedList<>();

		for (Brick brick : brickList) {
			if (brick instanceof UserBrick) {
				UserBrick userBrick = (UserBrick) brick;
				if (userBrick.getDefinitionBrick() == userBrickToRemove.getDefinitionBrick()) {
					toRemove.add(brick);
				}
			}
		}

		for (Brick brick : toRemove) {
			brickList.remove(brick);
		}
	}

	public void removeBrick(Brick brick) {
		brickList.remove(brick);
	}

	public ArrayList<Brick> getBrickList() {
		return brickList;
	}

	public int getRequiredResources() {
		int resources = Brick.NO_RESOURCES;

		for (Brick brick : brickList) {
			if (!brick.isCommentedOut()) {
				resources |= brick.getRequiredResources();
			}
		}
		return resources;
	}

	

	//
	//	public boolean containsBluetoothBrick() {
	//		for (Brick brick : brickList) {
	//			if ((brick instanceof NXTMotorActionBrick) || (brick instanceof NXTMotorTurnAngleBrick)
	//					|| (brick instanceof NXTMotorStopBrick) || (brick instanceof NXTPlayToneBrick)) {
	//				return true;
	//			}
	//		}
	//		return false;
	//	}

	public Brick getBrick(int index) {
		if (index < 0 || index >= brickList.size()) {
			return null;
		}

		return brickList.get(index);
	}

	public void setBrick(ScriptBrick brick) {
		this.brick = brick;
	}

	public boolean isCommentedOut() {
		return commentedOut;
	}
}
