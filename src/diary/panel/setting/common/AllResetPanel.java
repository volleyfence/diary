package diary.panel.setting.common;

/**
 * AllResetPanel.java
 * @author Masato Suzuki
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import diary.dialog.ChangeSettingDialog;
import diary.dialog.YesDialog;
import diary.dialog.YesNoDialog;
import diary.frame.DiaryFrame;
import diary.label.DiaryLabel;
import diary.panel.setting.SettingPanel;
import diary.textfield.PasswordField;

/**
 * ソフトウェアをリセットするパネル
 */
public class AllResetPanel extends SettingPanel {
	/**
	 * パスワード入力用
	 */
	private PasswordField resetPassText;

	/**
	 * コンストラクタ
	 * @param df DiaryFrameクラスのオブジェクト
	 * @param csd ChangeSettingDialogクラスのオブジェクト
	 * @param message 設定の説明
	 */
	public AllResetPanel(DiaryFrame df, ChangeSettingDialog csd, String message) {
		super(message);
		this.df = df;
		this.csd = csd;

		label1.setText("全ての日記と設定が削除されます");
		label1.setForeground(Color.RED);
		label2.setText("削除後復元はできません");
		label2.setForeground(Color.RED);
		label3.setText("※リセット後ソフトウェアを終了します");
		messagePanel.add(label1);
		messagePanel.add(label2);
		messagePanel.add(label3);

		if(setting.getPasswordLock()) {
			// inputPanelの生成&部品設置
			inputPanel = new JPanel();

			// ラベル
			DiaryLabel passLabel = new DiaryLabel("パスワード：", 0, JLabel.RIGHT, false);

			// テキストフィールド
			resetPassText = new PasswordField(20);
			resetPassText.addKeyListener(this);

			// パスワード入力
			JPanel tmp = new JPanel();
			tmp.setLayout(new GridLayout(1, 2));
			tmp.add(passLabel);
			tmp.add(resetPassText);

			// パスワード入力画面
			JPanel tmp1 = new JPanel();
			tmp1.setLayout(new GridLayout(0, 1));
			DiaryLabel msg1 = new DiaryLabel("リセットにはパスワードが必要です", 0, JLabel.CENTER, false);
			tmp1.add(msg1);
			tmp1.add(tmp);

			JPanel tmpPanel = new JPanel();
			tmpPanel.setLayout(new BorderLayout());
			tmpPanel.add(tmp1, BorderLayout.CENTER);
			tmpPanel.add(new JLabel("　　"), BorderLayout.WEST);
			tmpPanel.add(new JLabel("　　"), BorderLayout.EAST);

			inputPanel.add(tmpPanel);

			this.add(inputPanel, BorderLayout.CENTER);
		}

		changeButton.setText("リセット");
	}

	@Override
	protected boolean addProcessing(Object source) {
		if(source == resetPassText) {
			this.processing(source);
		}
		return true;
	}

	@Override
	protected boolean changeProcessing() {
		if(setting.getPasswordLock()) {
			// 空欄がないかどうか
			if(new String(resetPassText.getPassword()).length() != 0) {
				// 現在のパスワードが正しいか
				if(!(setting.crypt(new String(resetPassText.getPassword())).equals(setting.getPassword()))) {
					new YesDialog(csd, "パスワードが異なります", setting.getSettingFont(0));
					resetPassText.setText("");
					resetPassText.requestFocusInWindow();
					return false;
				}
			}
			else {
				new YesDialog(csd, "空欄があります", setting.getSettingFont(0));
				resetPassText.requestFocusInWindow();
				return false;
			}
		}

		new YesNoDialog(csd, "本当にリセットしますか？", setting.getSettingFont(0)) {
			@Override
			protected void yesProcessing() {
				fc.allReset();
				setting.resetSetting();
				fc.checkSettingFile();

				// ソフトウェアを終了する
				System.exit(0);
			}
		};
		return true;
	}
}