package diary.panel.setting.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;

import diary.dialog.ChangeSettingDialog;
import diary.label.DiaryLabel;
import diary.panel.setting.SettingPanel;

/**
 * フォントサイズを変更するパネル
 * @author Masato Suzuki
 */
public class ChangeDefaultFontSizePanel extends SettingPanel {
	/**
	 * サンプルフォントサイズ表示用ラベル
	 */
	private DiaryLabel sampleCharLabel;

	/**
	 * 現在のフォントサイズを表示する
	 */
	private DiaryLabel nowSizeLabel;

	/**
	 * フォントサイズの調節スライダー
	 */
	private JSlider defaultFontSizeSlider;

	/**
	 * コンストラクタ
	 * @param csd ChangeSettingDialogクラスのオブジェクト
	 * @param message 設定の説明
	 */
	public ChangeDefaultFontSizePanel(ChangeSettingDialog csd, String message) {
		super(message);
		this.csd = csd;

		label1.setText("変更後フレームサイズが調整されます");
		label2.setText("再起動までレイアウトが崩れることがあります");

		messagePanel.add(label1);
		messagePanel.add(label2);

		sampleCharLabel = new DiaryLabel("aAアあ亜", 0, JLabel.CENTER, true);
		sampleCharLabel.setOpaque(true);
		sampleCharLabel.setBackground(Color.WHITE);
		sampleCharLabel.setBorder(new LineBorder(Color.BLACK, 1, false));
		sampleCharLabel.setPreferredSize(new Dimension(setting.getSize(setting.getMaxFontSize() * 6), setting.getSize(setting.getMaxFontSize() * 3)));

		nowSizeLabel = new DiaryLabel(Integer.toString(setting.getDefaultFontSize()), 0, JLabel.CENTER, false);
		nowSizeLabel.setOpaque(true);
		nowSizeLabel.setBackground(Color.WHITE);
		nowSizeLabel.setBorder(new LineBorder(Color.BLACK, 1, false));
		nowSizeLabel.setPreferredSize(new Dimension(setting.getSize(setting.getDefaultFontSize() * 3), setting.getHeight(setting.getSize(setting.getDefaultFontSize() * 3))));

		JPanel tmp1 = new JPanel();
		tmp1.add(sampleCharLabel);

		JPanel tmp2 = new JPanel();
		tmp2.add(nowSizeLabel);

		defaultFontSizeSlider = new JSlider(setting.getMinFontSize(), setting.getMaxFontSize(), setting.getDefaultFontSize());
		defaultFontSizeSlider.setPaintLabels(true);
		defaultFontSizeSlider.setLabelTable(defaultFontSizeSlider.createStandardLabels(5));
		defaultFontSizeSlider.setFocusable(false);
		defaultFontSizeSlider.putClientProperty( "Slider.paintThumbArrowShape", Boolean.TRUE );
		defaultFontSizeSlider.setFont(setting.getSettingFont(0));
		defaultFontSizeSlider.setForeground(setting.getSettingFontColor());
		defaultFontSizeSlider.addChangeListener(this);

		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(tmp1, BorderLayout.NORTH);
		inputPanel.add(defaultFontSizeSlider, BorderLayout.CENTER);
		inputPanel.add(tmp2, BorderLayout.SOUTH);
		this.add(new JLabel("　"), BorderLayout.WEST);
		this.add(new JLabel("　"), BorderLayout.EAST);
	}

	@Override
	protected boolean changeProcessing() {
		int preSize = setting.getDefaultFontSize();
		out = Integer.toString(defaultFontSizeSlider.getValue());
		if(defaultFontSizeSlider.getValue() != preSize) {
			csd.changeSetting(out);
			csd.dispose();
			df.updateFrameSize();
		}
		return true;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == defaultFontSizeSlider) {
			sampleCharLabel.setFont(new Font(setting.getDefaultFontName(), setting.getDefaultFontStyle(), setting.getFontSize(defaultFontSizeSlider.getValue())));
			nowSizeLabel.setText(Integer.toString(defaultFontSizeSlider.getValue()));
		}
	}
}
