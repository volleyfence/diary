package diary.panel.setting.common;

import javax.swing.JComboBox;

import diary.dialog.ChangeSettingDialog;
import diary.panel.setting.SettingPanel;

/**
 * 文字コードを変更するパネル
 * @author Masato Suzuki
 */
public class ChangeCharCodePanel extends SettingPanel {
	/**
	 * 選択肢の表示
	 */
	private JComboBox<String> combo;

	/**
	 * コンストラクタ
	 * @param csd ChangeSettingDialogクラスのオブジェクト
	 * @param message 設定の説明
	 */
	public ChangeCharCodePanel(ChangeSettingDialog csd, String message) {
		super(message);
		this.csd = csd;

		String[] tmp = setting.getCharCodeList();
		combo = new JComboBox<String>(tmp);
		combo.setPreferredSize(null);
		for(int i = 0; i < tmp.length; i++) {
			if(setting.getCharCode().toLowerCase().equals(tmp[i].toLowerCase())) {
				combo.setSelectedIndex(i);
			}
		}
		combo.setFont(setting.getSettingFont(0));
		combo.setForeground(setting.getSettingFontColor());
		inputPanel.add(nowSet);
		inputPanel.add(combo);
	}

	@Override
	protected boolean changeProcessing() {
		out = combo.getSelectedItem().toString();
		if(!out.equals(setting.getCharCode())) {
			csd.changeSetting(out);
		}
		return true;
	}
}