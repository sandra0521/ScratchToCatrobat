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
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.InterpretationException;
import org.catrobat.catroid.formulaeditor.UserVariable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserBrick extends BrickBaseType {
	private static final long serialVersionUID = 1L;
	private static final String TAG = UserBrick.class.getSimpleName();

	@XStreamAlias("definitionBrick")
	private UserScriptDefinitionBrick definitionBrick;
	@XStreamAlias("userBrickParameters")
	private List<UserBrickParameter> userBrickParameters = new ArrayList<>();

	public UserBrick() {
		this.definitionBrick = new UserScriptDefinitionBrick();
	}

	public UserBrick(UserScriptDefinitionBrick definitionBrick) {
		this.definitionBrick = definitionBrick;
	}

	public List<UserBrickParameter> getUserBrickParameters() {
		return userBrickParameters;
	}

	private UserBrickParameter getUserBrickParameterByUserBrickElement(UserScriptDefinitionBrickElement element) {
		if (userBrickParameters == null) {
			return null;
		}
		for (UserBrickParameter parameter : userBrickParameters) {
			if (parameter.getElement().equals(element)) {
				return parameter;
			}
		}
		return null;
	}

	public void updateUserBrickParametersAndVariables() {
		updateUserBrickParameters();
		updateUserVariableValues();
	}

	public void updateUserBrickParameters() {
		List<UserBrickParameter> newParameters = new ArrayList<>();
		List<UserScriptDefinitionBrickElement> elements = getUserScriptDefinitionBrickElements();

		for (UserScriptDefinitionBrickElement element : elements) {
			if (!element.isVariable()) {
				continue;
			}
			UserBrickParameter parameter = getUserBrickParameterByUserBrickElement(element);
			if (parameter == null) {
				parameter = new UserBrickParameter(this, element);
				parameter.setFormulaWithBrickField(BrickField.USER_BRICK, new Formula(0));
			}
			newParameters.add(parameter);
		}

		if (userBrickParameters != null) {
			copyFormulasMatchingNames(userBrickParameters, newParameters);
		}

		userBrickParameters = newParameters;
	}

	public void copyFormulasMatchingNames(List<UserBrickParameter> originalParameters, List<UserBrickParameter> copiedParameters) {
		for (UserBrickParameter originalParameter : originalParameters) {
			UserScriptDefinitionBrickElement originalElement = originalParameter.getElement();
			if (!originalElement.isVariable()) {
				return;
			}

			for (UserBrickParameter copiedParameter : copiedParameters) {
				UserScriptDefinitionBrickElement copiedElement = copiedParameter.getElement();
				if (originalElement.equals(copiedElement)) {
					Formula formula = originalParameter.getFormulaWithBrickField(BrickField.USER_BRICK);
					copiedParameter.setFormulaWithBrickField(BrickField.USER_BRICK, formula.clone());
				}
			}
		}
	}

	private void updateUserVariableValues() {
		DataContainer dataContainer = ProjectManager.getInstance().getCurrentScene().getDataContainer();
		List<UserVariable> variables = new ArrayList<>();

		for (UserBrickParameter userBrickParameter : userBrickParameters) {
			UserScriptDefinitionBrickElement element = userBrickParameter.getElement();
			if (element != null) {
				List<Formula> formulas = userBrickParameter.getFormulas();
				Sprite sprite = ProjectManager.getInstance().getCurrentSprite();
				for (Formula formula : formulas) {
					variables.add(new UserVariable(element.getText()));
				}
			}
		}

		if (variables.isEmpty()) {
			return;
		}

		dataContainer.setUserBrickVariables(this, variables);
		Sprite currentSprite = ProjectManager.getInstance().getCurrentSprite();
		if (currentSprite != null) {
			currentSprite.updateUserVariableReferencesInUserVariableBricks(variables);
		}
	}

	public void appendBrickToScript(Brick brick) {
		definitionBrick.appendBrickToScript(brick);
	}

	public UserScriptDefinitionBrick getDefinitionBrick() {
		return definitionBrick;
	}

	public void setDefinitionBrick(UserScriptDefinitionBrick definitionBrick) {
		this.definitionBrick = definitionBrick;
	}

	public List<UserScriptDefinitionBrickElement> getUserScriptDefinitionBrickElements() {
		return definitionBrick.getUserScriptDefinitionBrickElements();
	}
}
