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

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import java.util.ArrayList;
import java.util.List;

public abstract class FormulaBrick extends BrickBaseType {

	ConcurrentFormulaHashMap formulaMap;

	public Formula getFormulaWithBrickField(BrickField brickField) throws IllegalArgumentException {
		if (formulaMap != null && formulaMap.containsKey(brickField)) {
			return formulaMap.get(brickField);
		} else {
			throw new IllegalArgumentException("Incompatible Brick Field : " + brickField.toString());
		}
	}

	public void setFormulaWithBrickField(BrickField brickField, Formula formula) throws IllegalArgumentException {
		if (formulaMap != null && formulaMap.containsKey(brickField)) {
			formulaMap.replace(brickField, formula);
		} else {
			throw new IllegalArgumentException("Incompatible Brick Field : " + brickField.toString());
		}
	}

	protected void addAllowedBrickField(BrickField brickField) {
		if (formulaMap == null) {
			formulaMap = new ConcurrentFormulaHashMap();
		}
		formulaMap.putIfAbsent(brickField, new Formula(0));
	}

	public List<Formula> getFormulas() {
		if (formulaMap == null) {
			return null;
		}
		List<Formula> formulas = new ArrayList<>();

		for (BrickField brickField : formulaMap.keySet()) {
			formulas.add(formulaMap.get(brickField));
		}
		return formulas;
	}
}