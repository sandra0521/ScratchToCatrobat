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
package org.catrobat.catroid.io;

import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.DroneVideoLookData;
import org.catrobat.catroid.common.FileChecksumContainer;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.common.NfcTagData;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.BroadcastScript;
import org.catrobat.catroid.content.GroupItemSprite;
import org.catrobat.catroid.content.GroupSprite;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Setting;
import org.catrobat.catroid.content.SingleSprite;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.SupportProject;
import org.catrobat.catroid.content.WhenBackgroundChangesScript;
import org.catrobat.catroid.content.WhenClonedScript;
import org.catrobat.catroid.content.WhenConditionScript;
import org.catrobat.catroid.content.WhenScript;
import org.catrobat.catroid.content.WhenTouchDownScript;
import org.catrobat.catroid.content.XmlHeader;
import org.catrobat.catroid.content.bricks.AddItemToUserListBrick;
import org.catrobat.catroid.content.bricks.AskBrick;
import org.catrobat.catroid.content.bricks.AskSpeechBrick;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BrickBaseType;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.BroadcastReceiverBrick;
import org.catrobat.catroid.content.bricks.BroadcastWaitBrick;
import org.catrobat.catroid.content.bricks.CameraBrick;
import org.catrobat.catroid.content.bricks.ChangeBrightnessByNBrick;
import org.catrobat.catroid.content.bricks.ChangeColorByNBrick;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeTransparencyByNBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ChangeVolumeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.ChangeYByNBrick;
import org.catrobat.catroid.content.bricks.ChooseCameraBrick;
import org.catrobat.catroid.content.bricks.ClearBackgroundBrick;
import org.catrobat.catroid.content.bricks.ClearGraphicEffectBrick;
import org.catrobat.catroid.content.bricks.CloneBrick;
import org.catrobat.catroid.content.bricks.ComeToFrontBrick;
import org.catrobat.catroid.content.bricks.DeleteItemOfUserListBrick;
import org.catrobat.catroid.content.bricks.DeleteThisCloneBrick;
import org.catrobat.catroid.content.bricks.FlashBrick;
import org.catrobat.catroid.content.bricks.ForeverBrick;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.content.bricks.GlideToBrick;
import org.catrobat.catroid.content.bricks.GoNStepsBackBrick;
import org.catrobat.catroid.content.bricks.GoToBrick;
import org.catrobat.catroid.content.bricks.HideBrick;
import org.catrobat.catroid.content.bricks.HideTextBrick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfLogicElseBrick;
import org.catrobat.catroid.content.bricks.IfLogicEndBrick;
import org.catrobat.catroid.content.bricks.IfOnEdgeBounceBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicEndBrick;
import org.catrobat.catroid.content.bricks.InsertItemIntoUserListBrick;
import org.catrobat.catroid.content.bricks.LoopBeginBrick;
import org.catrobat.catroid.content.bricks.LoopEndBrick;
import org.catrobat.catroid.content.bricks.LoopEndlessBrick;
import org.catrobat.catroid.content.bricks.MoveNStepsBrick;
import org.catrobat.catroid.content.bricks.NextLookBrick;
import org.catrobat.catroid.content.bricks.NoteBrick;
import org.catrobat.catroid.content.bricks.PenDownBrick;
import org.catrobat.catroid.content.bricks.PenUpBrick;
import org.catrobat.catroid.content.bricks.PlaceAtBrick;
import org.catrobat.catroid.content.bricks.PlaySoundAndWaitBrick;
import org.catrobat.catroid.content.bricks.PlaySoundBrick;
import org.catrobat.catroid.content.bricks.PointInDirectionBrick;
import org.catrobat.catroid.content.bricks.PointToBrick;
import org.catrobat.catroid.content.bricks.PreviousLookBrick;
import org.catrobat.catroid.content.bricks.RaspiIfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.RepeatBrick;
import org.catrobat.catroid.content.bricks.RepeatUntilBrick;
import org.catrobat.catroid.content.bricks.ReplaceItemInUserListBrick;
import org.catrobat.catroid.content.bricks.SayBubbleBrick;
import org.catrobat.catroid.content.bricks.SayForBubbleBrick;
import org.catrobat.catroid.content.bricks.SceneStartBrick;
import org.catrobat.catroid.content.bricks.SceneTransitionBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundAndWaitBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundBrick;
import org.catrobat.catroid.content.bricks.SetBrightnessBrick;
import org.catrobat.catroid.content.bricks.SetColorBrick;
import org.catrobat.catroid.content.bricks.SetLookBrick;
import org.catrobat.catroid.content.bricks.SetPenColorBrick;
import org.catrobat.catroid.content.bricks.SetPenSizeBrick;
import org.catrobat.catroid.content.bricks.SetRotationStyleBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.SetTransparencyBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.SetVolumeToBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.content.bricks.ShowBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.SpeakAndWaitBrick;
import org.catrobat.catroid.content.bricks.SpeakBrick;
import org.catrobat.catroid.content.bricks.StampBrick;
import org.catrobat.catroid.content.bricks.StopAllSoundsBrick;
import org.catrobat.catroid.content.bricks.StopScriptBrick;
import org.catrobat.catroid.content.bricks.ThinkBubbleBrick;
import org.catrobat.catroid.content.bricks.ThinkForBubbleBrick;
import org.catrobat.catroid.content.bricks.TurnLeftBrick;
import org.catrobat.catroid.content.bricks.TurnRightBrick;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.content.bricks.UserBrickParameter;
import org.catrobat.catroid.content.bricks.UserListBrick;
import org.catrobat.catroid.content.bricks.UserScriptDefinitionBrick;
import org.catrobat.catroid.content.bricks.UserScriptDefinitionBrickElement;
import org.catrobat.catroid.content.bricks.UserVariableBrick;
import org.catrobat.catroid.content.bricks.VibrationBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.content.bricks.WaitUntilBrick;
import org.catrobat.catroid.content.bricks.WhenBackgroundChangesBrick;
import org.catrobat.catroid.content.bricks.WhenBrick;
import org.catrobat.catroid.content.bricks.WhenClonedBrick;
import org.catrobat.catroid.content.bricks.WhenConditionBrick;
import org.catrobat.catroid.content.bricks.WhenStartedBrick;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.SupportDataContainer;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.catrobat.catroid.common.Constants.BACKPACK_DIRECTORY;
import static org.catrobat.catroid.common.Constants.BACKPACK_IMAGE_DIRECTORY;
import static org.catrobat.catroid.common.Constants.BACKPACK_SOUND_DIRECTORY;
import static org.catrobat.catroid.common.Constants.IMAGE_DIRECTORY;
import static org.catrobat.catroid.common.Constants.NO_MEDIA_FILE;
import static org.catrobat.catroid.common.Constants.PROJECTCODE_NAME;
import static org.catrobat.catroid.common.Constants.PROJECTCODE_NAME_TMP;
import static org.catrobat.catroid.common.Constants.PROJECTPERMISSIONS_NAME;
import static org.catrobat.catroid.common.Constants.SCENES_DIRECTORY;
import static org.catrobat.catroid.common.Constants.SCENES_ENABLED_TAG;
import static org.catrobat.catroid.common.Constants.SOUND_DIRECTORY;

public final class StorageHandler {
	private static final StorageHandler INSTANCE;
	private static final String TAG = StorageHandler.class.getSimpleName();
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
	private static final int JPG_COMPRESSION_SETTING = 95;
	public static final String BACKPACK_FILENAME = "backpack.json";

	private BackwardCompatibleCatrobatLanguageXStream xstream;
	private FileInputStream fileInputStream;

	private File backPackSoundDirectory;
	private File backPackImageDirectory;

	private Lock loadSaveLock = new ReentrantLock();
	// TODO: Since the StorageHandler constructor throws an exception, the member INSTANCE couldn't be assigned
	// directly and therefore we need this static block. Should be refactored and removed in the future.
	static {
		try {
			INSTANCE = new StorageHandler();
		} catch (IOException ioException) {
			throw new RuntimeException("Initialize StorageHandler failed");
		}
	}
	private StorageHandler() throws IOException {
		prepareProgramXstream(false);
	}

	public static StorageHandler getInstance() {
		return INSTANCE;
	}

	private void prepareProgramXstream(boolean forSupportProject) {
		xstream = new BackwardCompatibleCatrobatLanguageXStream(new PureJavaReflectionProvider(new
				FieldDictionary(new CatroidFieldKeySorter())));
		if (forSupportProject) {
			xstream.processAnnotations(SupportProject.class);
			xstream.processAnnotations(SupportDataContainer.class);
		} else {
			xstream.processAnnotations(Project.class);
			xstream.processAnnotations(DataContainer.class);
		}
		xstream.processAnnotations(Scene.class);
		xstream.processAnnotations(Sprite.class);
		xstream.processAnnotations(XmlHeader.class);
		xstream.processAnnotations(Setting.class);
		xstream.processAnnotations(UserVariableBrick.class);
		xstream.processAnnotations(UserListBrick.class);
		xstream.registerConverter(new XStreamConcurrentFormulaHashMapConverter());
		xstream.registerConverter(new XStreamUserVariableConverter());
		xstream.registerConverter(new XStreamBrickConverter(xstream.getMapper(), xstream.getReflectionProvider()));
		xstream.registerConverter(new XStreamScriptConverter(xstream.getMapper(), xstream.getReflectionProvider()));
		xstream.registerConverter(new XStreamSpriteConverter(xstream.getMapper(), xstream.getReflectionProvider()));
		xstream.registerConverter(new XStreamSettingConverter(xstream.getMapper(), xstream.getReflectionProvider()));

		xstream.omitField(CameraBrick.class, "spinnerValues");
		xstream.omitField(ChooseCameraBrick.class, "spinnerValues");
		xstream.omitField(FlashBrick.class, "spinnerValues");
		xstream.omitField(StopScriptBrick.class, "spinnerValue");

		setProgramXstreamAliases();
	}

	private void setProgramXstreamAliases() {
		xstream.alias("look", LookData.class);
		xstream.alias("droneLook", DroneVideoLookData.class);
		xstream.alias("sound", SoundInfo.class);
		xstream.alias("userVariable", UserVariable.class);
		xstream.alias("userList", UserList.class);

		xstream.alias("script", Script.class);
		xstream.alias("object", Sprite.class);
		xstream.alias("object", SingleSprite.class);
		xstream.alias("object", GroupSprite.class);
		xstream.alias("object", GroupItemSprite.class);

		xstream.alias("script", StartScript.class);
		xstream.alias("script", WhenClonedScript.class);
		xstream.alias("script", WhenScript.class);
		xstream.alias("script", WhenConditionScript.class);
		xstream.alias("script", BroadcastScript.class);
		xstream.alias("script", WhenTouchDownScript.class);
		xstream.alias("script", WhenBackgroundChangesScript.class);

		xstream.alias("brick", AddItemToUserListBrick.class);
		xstream.alias("brick", AskBrick.class);
		xstream.alias("brick", AskSpeechBrick.class);
		xstream.alias("brick", BroadcastBrick.class);
		xstream.alias("brick", BroadcastReceiverBrick.class);
		xstream.alias("brick", BroadcastWaitBrick.class);
		xstream.alias("brick", ChangeBrightnessByNBrick.class);
		xstream.alias("brick", ChangeColorByNBrick.class);
		xstream.alias("brick", ChangeTransparencyByNBrick.class);
		xstream.alias("brick", ChangeSizeByNBrick.class);
		xstream.alias("brick", ChangeVariableBrick.class);
		xstream.alias("brick", ChangeVolumeByNBrick.class);
		xstream.alias("brick", ChangeXByNBrick.class);
		xstream.alias("brick", ChangeYByNBrick.class);
		xstream.alias("brick", ClearBackgroundBrick.class);
		xstream.alias("brick", ClearGraphicEffectBrick.class);
		xstream.alias("brick", CloneBrick.class);
		xstream.alias("brick", ComeToFrontBrick.class);
		xstream.alias("brick", DeleteItemOfUserListBrick.class);
		xstream.alias("brick", DeleteThisCloneBrick.class);
		xstream.alias("brick", ForeverBrick.class);
		xstream.alias("brick", GlideToBrick.class);
		xstream.alias("brick", GoNStepsBackBrick.class);
		xstream.alias("brick", HideBrick.class);
		xstream.alias("brick", HideTextBrick.class);
		xstream.alias("brick", IfLogicBeginBrick.class);
		xstream.alias("brick", IfLogicElseBrick.class);
		xstream.alias("brick", IfLogicEndBrick.class);
		xstream.alias("brick", IfThenLogicBeginBrick.class);
		xstream.alias("brick", IfThenLogicEndBrick .class);
		xstream.alias("brick", IfOnEdgeBounceBrick.class);
		xstream.alias("brick", InsertItemIntoUserListBrick.class);
		xstream.alias("brick", FlashBrick.class);
		xstream.alias("brick", ChooseCameraBrick.class);
		xstream.alias("brick", CameraBrick.class);
		xstream.alias("brick", LoopBeginBrick.class);
		xstream.alias("brick", LoopEndBrick.class);
		xstream.alias("brick", LoopEndlessBrick.class);
		xstream.alias("brick", MoveNStepsBrick.class);
		xstream.alias("brick", NextLookBrick.class);
		xstream.alias("brick", NoteBrick.class);
		xstream.alias("brick", PenDownBrick.class);
		xstream.alias("brick", PenUpBrick.class);
		xstream.alias("brick", PlaceAtBrick.class);
		xstream.alias("brick", GoToBrick.class);
		xstream.alias("brick", PlaySoundBrick.class);
		xstream.alias("brick", PlaySoundAndWaitBrick.class);
		xstream.alias("brick", PointInDirectionBrick.class);
		xstream.alias("brick", PointToBrick.class);
		xstream.alias("brick", PreviousLookBrick.class);
		xstream.alias("brick", RepeatBrick.class);
		xstream.alias("brick", RepeatUntilBrick.class);
		xstream.alias("brick", ReplaceItemInUserListBrick.class);
		xstream.alias("brick", SceneTransitionBrick.class);
		xstream.alias("brick", SceneStartBrick.class);
		xstream.alias("brick", SetBrightnessBrick.class);
		xstream.alias("brick", SetColorBrick.class);
		xstream.alias("brick", SetTransparencyBrick.class);
		xstream.alias("brick", SetLookBrick.class);
		xstream.alias("brick", SetBackgroundBrick.class);
		xstream.alias("brick", SetBackgroundAndWaitBrick.class);
		xstream.alias("brick", SetPenColorBrick.class);
		xstream.alias("brick", SetPenSizeBrick.class);
		xstream.alias("brick", SetRotationStyleBrick.class);
		xstream.alias("brick", SetSizeToBrick.class);
		xstream.alias("brick", SetVariableBrick.class);
		xstream.alias("brick", SetVolumeToBrick.class);
		xstream.alias("brick", SetXBrick.class);
		xstream.alias("brick", SetYBrick.class);
		xstream.alias("brick", ShowBrick.class);
		xstream.alias("brick", ShowTextBrick.class);
		xstream.alias("brick", SpeakBrick.class);
		xstream.alias("brick", SpeakAndWaitBrick.class);
		xstream.alias("brick", StampBrick.class);
		xstream.alias("brick", StopAllSoundsBrick.class);
		xstream.alias("brick", ThinkBubbleBrick.class);
		xstream.alias("brick", SayBubbleBrick.class);
		xstream.alias("brick", ThinkForBubbleBrick.class);
		xstream.alias("brick", SayForBubbleBrick.class);
		xstream.alias("brick", TurnLeftBrick.class);
		xstream.alias("brick", TurnRightBrick.class);
		xstream.alias("brick", UserBrick.class);
		xstream.alias("brick", UserScriptDefinitionBrick.class);
		xstream.alias("brick", VibrationBrick.class);
		xstream.alias("brick", WaitBrick.class);
		xstream.alias("brick", WaitUntilBrick.class);
		xstream.alias("brick", WhenBrick.class);
		xstream.alias("brick", WhenConditionBrick.class);
		xstream.alias("brick", WhenBackgroundChangesBrick.class);
		xstream.alias("brick", WhenStartedBrick.class);
		xstream.alias("brick", WhenClonedBrick.class);
		xstream.alias("brick", StopScriptBrick.class);


		xstream.alias("userBrickElement", UserScriptDefinitionBrickElement.class);
		xstream.alias("userBrickParameter", UserBrickParameter.class);

		xstream.aliasField("formulaList", FormulaBrick.class, "formulaMap");
		xstream.aliasField("object", BrickBaseType.class, "sprite");
	}

	public void clearBackPackSoundDirectory() {
		if (backPackSoundDirectory == null) {
			return;
		}
		File[] backPackFiles = backPackSoundDirectory.listFiles();
		if (backPackFiles != null && backPackFiles.length > 1) {
			for (File node : backPackSoundDirectory.listFiles()) {
				if (!(node.getName().equals(".nomedia"))) {
					node.delete();
				}
			}
		}
	}

	public String getXMLStringOfAProject(Project project) {
		loadSaveLock.lock();
		String xmlProject = "";
		try {
			xmlProject = xstream.toXML(project);
		} finally {
			loadSaveLock.unlock();
		}
		return xmlProject;
	}
}
