package diary.panel.setting.common;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import diary.dialog.ChangeSettingDialog;
import diary.dialog.YesDialog;
import diary.label.DiaryLabel;
import diary.panel.setting.SettingPanel;
import diary.textfield.PasswordField;

/**
 * ソフトウェア設定をリセットするパネル
 * @author Masato Suzuki
 */
public class ResetSettingPanel extends SettingPanel {
	/**
	 * パスワード入力用
	 */
	private PasswordField resetPassText;

	/**
	 * コンストラクタ
	 * @param csd ChangeSettingDialogクラスのオブジェクト
	 * @param message 設定の説明
	 */
	public ResetSettingPanel(ChangeSettingDialog csd, String message) {
		super(message);
		this.csd = csd;

		label1.setText("※リセット後ソフトウェアを再起動します");
		messagePanel.add(label1);

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
			DiaryLabel msg2 = new DiaryLabel("リセット後はロックも削除されます", 0, JLabel.CENTER, false);
			tmp1.add(msg1);
			tmp1.add(msg2);
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

		df.setVisible(false);
		fc.resetSettingFile();
		setting.resetSetting();
		fc.checkSettingFile();

		// ソフトウェアを再起動しないとレイアウトが崩れる恐れがあるため再起動する
		df.diaryExit(true);

		return true;
	}
}