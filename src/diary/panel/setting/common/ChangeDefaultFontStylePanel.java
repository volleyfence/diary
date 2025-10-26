package diary.panel.setting.common;

import java.awt.Font;

import javax.swing.JComboBox;

import diary.dialog.ChangeSettingDialog;
import diary.panel.setting.SettingPanel;

/**
 * フォントの太さを変更するパネル
 * @author Masato Suzuki
 */
public class ChangeDefaultFontStylePanel extends SettingPanel {
	/**
	 * 選択肢の表示
	 */
	private JComboBox<String> combo;

	/**
	 * コンストラクタ
	 * @param csd ChangeSettingDialogクラスのオブジェクト
	 * @param message 設定の説明
	 */
	public ChangeDefaultFontStylePanel(ChangeSettingDialog csd, String message) {
		super(message);
		this.csd = csd;

		label1.setText("フォント名によっては変化しない場合もあります");
		messagePanel.add(label1);

		String[] tmp = {"太い", "普通"};
		combo = new JComboBox<String>(tmp);
		combo.setPreferredSize(null);
		if(setting.getDefaultFontStyle() == Font.BOLD) {
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
		if(!((out.equals("普通") && setting.getDefaultFontStyle() == Font.PLAIN) || (out.equals("太い") && setting.getDefaultFontStyle() == Font.BOLD))) {
			if(out.equals("普通")) {
				out = "PLAIN";
			}
			else {
				out = "BOLD";
			}
			csd.changeSetting(out);
			df.updateComponents();
		}
		return true;
	}
}
