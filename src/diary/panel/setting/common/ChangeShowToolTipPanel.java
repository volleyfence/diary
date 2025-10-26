package diary.panel.setting.common;

import javax.swing.JComboBox;

import diary.dialog.ChangeSettingDialog;
import diary.panel.setting.SettingPanel;

/**
 * ツールチップ表示可否を変更するパネル
 * @author Masato Suzuki
 */
public class ChangeShowToolTipPanel extends SettingPanel {
	/**
	 * 選択肢の表示
	 */
	private JComboBox<String> combo;

	/**
	 * コンストラクタ
	 * @param csd ChangeSettingDialogクラスのオブジェクト
	 * @param message 設定の説明
	 */
	public ChangeShowToolTipPanel(ChangeSettingDialog csd, String message) {
		super(message);
		this.csd = csd;

		String[] tmp = {"表示する", "表示しない"};
		combo = new JComboBox<String>(tmp);
		combo.setPreferredSize(null);
		if(setting.getShowToolTip() == true) {
			combo.setSelectedIndex(0);
		}
		else {
			combo.setSelectedIndex(1);
		}
		combo.setFont(setting.getSettingFont(0));
		combo.setForeground(setting.getSettingFontColor());
		inputPanel.add(nowSet);
		inputPanel.add(combo);
	}

	@Override
	protected boolean changeProcessing() {
		out = combo.getSelectedItem().toString();
		if(!((out.equals("表示する") && setting.getShowToolTip() == true) || (out.equals("表示しない") && setting.getShowToolTip() == false))) {
			if(out.equals("表示する")) {
				out = "TRUE";
			}
			else {
				out = "FALSE";
			}
			csd.changeSetting(out);
			df.updateComponents();
		}
		return true;
	}
}