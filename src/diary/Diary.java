package diary;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import diary.frame.DiaryFrame;
import diary.system.Setting;

/**
 * 本ソフトウェアを実行する
 * @author Masato Suzuki
 */
public class Diary {
	/**
	 * メインメソッド
	 * @param args 引数
	 */
	public static void main(String[] args) {
		/**
		 * Settingクラスのオブジェクト
		 */
		Setting setting = Setting.getInstance();

		try {
			// OSに関わらず見た目を統一する
			UIManager.setLookAndFeel(new MetalLookAndFeel());

			// 各コンポーネントの見た目の調整
			UIManager.put("Button.select", new Color(0, 0, 0, 0));
			UIManager.put("TextField.selectionBackground", new Color(51, 153, 255));
			UIManager.put("TextField.selectionForeground", Color.WHITE);
			UIManager.put("PasswordField.selectionBackground", new Color(51, 153, 255));
			UIManager.put("PasswordField.selectionForeground", Color.WHITE);
			UIManager.put("TextArea.selectionBackground", new Color(51, 153, 255));
			UIManager.put("TextArea.selectionForeground", Color.WHITE);
			UIManager.put("ScrollBar.width", setting.getFontSize(setting.getFirstFontSize()));
			UIManager.put("ScrollBarUI", "com.sun.java.swing.plaf.windows.WindowsScrollBarUI");
			UIManager.put("SliderUI", "com.sun.java.swing.plaf.windows.WindowsSliderUI");
			UIManager.put("MenuItem.selectionBackground", new Color(209, 226, 242));
			UIManager.put("MenuItem.background", new Color(240, 240, 240));
			UIManager.put("ComboBox.selectionBackground", new Color(209, 226, 242));
			UIManager.put("ComboBox.selectionForeground", setting.getSettingFontColor());
			UIManager.put("ComboBox.background", new Color(240, 240, 240));
			UIManager.put("ComboBox.foreground", setting.getSettingFontColor());
		}
		catch (Exception error) {
			error.printStackTrace();
		}

		// フレームの生成
		new DiaryFrame();
	}
}
