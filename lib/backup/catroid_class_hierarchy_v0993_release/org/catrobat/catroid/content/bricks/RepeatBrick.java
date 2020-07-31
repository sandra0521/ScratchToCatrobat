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

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.InterpretationException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RepeatBrick extends FormulaBrick implements LoopBeginBrick {
	private static final long serialVersionUID = 1L;

	protected transient LoopEndBrick loopEndBrick;
	public RepeatBrick() {
		addAllowedBrickField(BrickField.TIMES_TO_REPEAT);
	}

	public RepeatBrick(int timesToRepeatValue) {
		initializeBrickFields(new Formula(timesToRepeatValue));
	}

	public RepeatBrick(Formula timesToRepeat) {
		initializeBrickFields(timesToRepeat);
	}

	private void initializeBrickFields(Formula timesToRepeat) {
		addAllowedBrickField(BrickField.TIMES_TO_REPEAT);
		setFormulaWithBrickField(BrickField.TIMES_TO_REPEAT, timesToRepeat);
	}

	@Override
	public LoopEndBrick getLoopEndBrick() {
		return loopEndBrick;
	}

	@Override
	public void setLoopEndBrick(LoopEndBrick loopEndBrick) {
		this.loopEndBrick = loopEndBrick;
	}
}
