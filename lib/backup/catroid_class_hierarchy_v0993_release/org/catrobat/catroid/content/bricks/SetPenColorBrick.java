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

import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import java.util.List;

public class SetPenColorBrick extends FormulaBrick {
	private static final long serialVersionUID = 1L;

	public SetPenColorBrick() {
		addAllowedBrickField(BrickField.PHIRO_LIGHT_RED);
		addAllowedBrickField(BrickField.PHIRO_LIGHT_GREEN);
		addAllowedBrickField(BrickField.PHIRO_LIGHT_BLUE);
	}

	public SetPenColorBrick(int red, int green, int blue) {
		initializeBrickFields(new Formula(red), new Formula(green), new Formula(blue));
	}

	public SetPenColorBrick(Formula red, Formula green, Formula blue) {
		initializeBrickFields(red, green, blue);
	}

	private void initializeBrickFields(Formula red, Formula green, Formula blue) {
		addAllowedBrickField(BrickField.PHIRO_LIGHT_RED);
		addAllowedBrickField(BrickField.PHIRO_LIGHT_GREEN);
		addAllowedBrickField(BrickField.PHIRO_LIGHT_BLUE);
		setFormulaWithBrickField(BrickField.PHIRO_LIGHT_RED, red);
		setFormulaWithBrickField(BrickField.PHIRO_LIGHT_GREEN, green);
		setFormulaWithBrickField(BrickField.PHIRO_LIGHT_BLUE, blue);
	}
}
