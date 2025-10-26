package diary.panel.setting.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import diary.dialog.ChangeSettingDialog;
import diary.label.DiaryLabel;
import diary.panel.setting.SettingPanel;

/**
 * フォント名を変更するパネル
 * @author Masato Suzuki
 */
public class ChangeDefaultFontNamePanel extends SettingPanel {
	/**
	 * サンプルフォント名表示用ラベル
	 */
	private DiaryLabel sampleCharLabel;

	/**
	 * 選択肢の表示
	 */
	private JComboBox<String> combo;

	/**
	 * コンストラクタ
	 * @param csd ChangeSettingDialogクラスのオブジェクト
	 * @param message 設定の説明
	 */
	public ChangeDefaultFontNamePanel(ChangeSettingDialog csd, String message) {
		super(message);
		this.csd = csd;

		sampleCharLabel = new DiaryLabel("aAアあ亜", 0, JLabel.CENTER, true);
		sampleCharLabel.setOpaque(true);
		sampleCharLabel.setBackground(Color.WHITE);
		sampleCharLabel.setBorder(new LineBorder(Color.BLACK, 1, false));
		sampleCharLabel.setPreferredSize(new Dimension(setting.getSize(setting.getDefaultFontSize() * 6), setting.getSize(setting.getDefaultFontSize() * 3)));

		JPanel tmp1 = new JPanel();
		tmp1.add(sampleCharLabel);

		String[] tmp = setting.getFontNameList();
		int nowIndex = 0;
		for(int i = 0; i < tmp.length; i++) {
			if(tmp[i].equals(setting.getDefaultFontName())) {
				nowIndex = i;
			}
		}
		combo = new JComboBox<String>(tmp);
		combo.setPreferredSize(null);
		combo.setMaximumRowCount(20);
		combo.setSelectedIndex(nowIndex);
		combo.setFont(setting.getSettingFont(0));
		combo.setForeground(setting.getSettingFontColor());
		combo.addActionListener(this);

		JPanel tmp2 = new JPanel();
		tmp2.add(nowSet);
		tmp2.add(combo);

		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(tmp1, BorderLayout.NORTH);
		inputPanel.add(tmp2, BorderLayout.CENTER);
	}

	@Override
	protected boolean addProcessing(Object source) {
		if(source == combo) {
			sampleCharLabel.setFont(new Font(combo.getSelectedItem().toString(), setting.getDefaultFontStyle(), setting.getFontSize(setting.getDefaultFontSize())));
		}

		return true;
	}

	@Override
	protected boolean changeProcessing() {
		out = combo.getSelectedItem().toString();
		if(!out.equals(setting.getDefaultFontName())) {
			csd.changeSetting(out);
			df.updateComponents();
		}
		return true;
	}
}
