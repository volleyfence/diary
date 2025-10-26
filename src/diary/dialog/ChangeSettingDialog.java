package diary.dialog;

import java.awt.GridLayout;

import diary.panel.setting.common.ChangeCharCodePanel;
import diary.panel.setting.common.ChangeDefaultFontNamePanel;
import diary.panel.setting.common.ChangeDefaultFontSizePanel;
import diary.panel.setting.common.ChangeDefaultFontStylePanel;
import diary.panel.setting.common.ChangePasswordPanel;
import diary.panel.setting.common.ChangeShowIconPanel;
import diary.panel.setting.common.ChangeShowToolTipPanel;
import diary.panel.setting.common.ChangeTitlePanel;
import diary.panel.setting.common.ResetPanel;
import diary.panel.setting.common.ResetSettingPanel;
import diary.panel.setting.diaryWrite.ChangeShowDiaryImagePanel;

/**
 * 設定変更ダイアログを表示する
 * @author Masato Suzuki
 */
public class ChangeSettingDialog extends DiaryDialog {
	/**
	 * 設定内容の判別
	 */
	private String key;

	/**
	 * 設定の説明
	 */
	private String message;

	/**
	 * コンストラクタ
	 * @param message 設定の説明
	 * @param key 設定の判別キー
	 */
	public ChangeSettingDialog(String message, String key) {
		super(df, "設定", true);
		this.key = key;
		this.message = message.replaceAll("<br>　", "<br>");

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		this.setLayout(new GridLayout(1, 1));

		if(key.equals("defaultFontName")) {
			this.add(new ChangeDefaultFontNamePanel(this, message));
		}
		else if(key.equals("defaultFontStyle")) {
			this.add(new ChangeDefaultFontStylePanel(this, message));
		}
		else if(key.equals("defaultFontSize")) {
			this.add(new ChangeDefaultFontSizePanel(this, message));
		}
		else if(key.equals("title")) {
			this.add(new ChangeTitlePanel(this, message));
		}
		else if(key.equals("showIcon")) {
			this.add(new ChangeShowIconPanel(this, message));
		}
		else if(key.equals("showToolTip")) {
			this.add(new ChangeShowToolTipPanel(this, message));
		}
		else if(key.equals("charCode")) {
			this.add(new ChangeCharCodePanel(this, message));
		}
		else if(key.equals("passwordLock")) {
			this.add(new ChangePasswordPanel(this, message));
		}
		else if(key.equals("resetSetting")) {
			this.add(new ResetSettingPanel(this, message));
		}
		else if(key.equals("reset")) {
			this.add(new ResetPanel(this, message));
		}

		else if(key.equals("showDiaryImage")) {
			this.add(new ChangeShowDiaryImagePanel(this, message));
		}

		resize(df, 100, 0);
	}

	/**
	 * ソフトウェア設定を変更する
	 * @param set セットする設定
	 */
	public void changeSetting(String set) {
		setting.changeSetting(key, set);
		fc.updateSettingFile();
	}
}
