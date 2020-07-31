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

import org.catrobat.catroid.content.Sprite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LoopEndBrick extends BrickBaseType {
	static final int FOREVER = -1;

	private static final long serialVersionUID = 1L;
	private static final String TAG = LoopEndBrick.class.getSimpleName();
	private transient LoopBeginBrick loopBeginBrick;

	public LoopEndBrick(LoopBeginBrick loopStartingBrick) {
		this.loopBeginBrick = loopStartingBrick;
		if (loopStartingBrick != null) {
			loopStartingBrick.setLoopEndBrick(this);
		}
	}

	public LoopEndBrick() {
	}

	public LoopBeginBrick getLoopBeginBrick() {
		return loopBeginBrick;
	}

	public void setLoopBeginBrick(LoopBeginBrick loopBeginBrick) {
		this.loopBeginBrick = loopBeginBrick;
	}
}
