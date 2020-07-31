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

import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.DroneVideoLookData;
import org.catrobat.catroid.common.LookData;
import java.util.ArrayList;
import java.util.Iterator;

public class Look {
	private static final float DEGREE_UI_OFFSET = 90.0f;
	private static final float COLOR_SCALE = 200.0f;
	private boolean lookVisible = true;
	protected boolean imageChanged = false;
	protected boolean brightnessChanged = false;
	protected boolean colorChanged = false;
	protected LookData lookData;
	protected Sprite sprite;
	protected float alpha = 1f;
	protected float brightness = 1f;
	protected float hue = 0f;
	private boolean allActionsAreFinished = false;
	public static final int ROTATION_STYLE_ALL_AROUND = 1;
	public static final int ROTATION_STYLE_LEFT_RIGHT_ONLY = 0;
	public static final int ROTATION_STYLE_NONE = 2;
	private int rotationMode = ROTATION_STYLE_ALL_AROUND;
	private float rotation = 90f;
	private float realRotation = rotation;

	public Look(final Sprite sprite) {
		this.sprite = sprite;
		rotation = getDirectionInUserInterfaceDimensionUnit();
	}

	public boolean isLookVisible() {
		return lookVisible;
	}

	public void setLookVisible(boolean lookVisible) {
		this.lookVisible = lookVisible;
	}

	public LookData getLookData() {
		return lookData;
	}

	public void setLookData(LookData lookData) {
		this.lookData = lookData;
		imageChanged = true;

	}

	public boolean getAllActionsAreFinished() {
		return allActionsAreFinished;
	}

	public float getDirectionInUserInterfaceDimensionUnit() {
		return realRotation;
	}

	public void setRotationMode(int mode) {
		rotationMode = mode;
	}

	public int getRotationMode() {
		return rotationMode;
	}

	public float getRealRotation() {
		return realRotation;
	}
}
