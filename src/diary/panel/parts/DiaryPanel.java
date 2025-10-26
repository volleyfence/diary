package diary.panel.parts;

import java.util.ArrayList;

import diary.button.IconButton;
import diary.frame.DiaryFrame;
import diary.system.DiaryFrameHolder;
import diary.system.Setting;
import diary.system.file.FileControl;

/**
 * 本ソフトウェアで使用するパネル<br>
 * 主要パネルはこのクラスを継承する
 * @author Masato Suzuki
 */
public class DiaryPanel extends ImagePanel {
	/**
	 * DiaryFrameクラスのオブジェクト
	 */
	protected DiaryFrame df = DiaryFrameHolder.getDiaryFrame();

	/**
	 * Settingクラスのオブジェクト
	 */
	protected Setting setting = Setting.getInstance();

	/**
	 * FileControlクラスのオブジェクト
	 */
	protected FileControl fc = FileControl.getInstance();

	/**
	 * 背景画像を変更する処理に用いる
	 */
	protected int backgroundCount = 0;

	/**
	 * 使用するボタン配列
	 */
	protected ArrayList<IconButton> iconButton = new ArrayList<IconButton>();
}
