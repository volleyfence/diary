package diary.dialog;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import diary.textfield.PasswordField;

/**
 * パスワードダイアログを表示する
 * @author Masato Suzuki
 */
public class PasswordDialog extends DiaryDialog {
	/**
	 * パスワード入力テキストフィールド
	 */
	private JPasswordField passText;

	/**
	 * コンストラクタ
	 */
	public PasswordDialog() {
		super(df, "確認", true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		// messagePanelの生成&部品設置
		label1.setText("この『" + setting.getTitle() + "』はロックされています");
		label2.setText("パスワードを入力して下さい");
		messagePanel.add(label1);
		messagePanel.add(label2);

		// ラベル
		label3.setText("パスワード：", JLabel.RIGHT);
		// テキストフィールド
		passText = new PasswordField(25);
		passText.addKeyListener(this);

		JPanel inputPanel = new JPanel();
		inputPanel.add(label3);
		inputPanel.add(passText);

		// buttonPanelの生成&部品設置
		JPanel buttonPanel = new JPanel();
		button1.setText("解除");
		buttonPanel.add(button1);

		this.setLayout(new BorderLayout(setting.getSize(10), setting.getSize(5)));
		this.add(messagePanel, BorderLayout.NORTH);
		this.add(inputPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		// 適切なフレームサイズ設定
		resize(null, 100, 0);
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	@Override
	protected void processing(Object source) {
		// 解除
		if(source == button1 || source == passText) {
			// 空欄がないかどうか
			if(new String(passText.getPassword()).length() != 0) {
				// 現在のパスワードが正しいか
				if(setting.crypt(new String(passText.getPassword())).equals(setting.getPassword())) {
					this.dispose();
					df.frameShow();
				}
				else {
					new YesDialog(this, "パスワードが異なります");
					passText.setText("");
					passText.requestFocusInWindow();
					return;
				}
			}
			else {
				new YesDialog(this, "空欄があります");
				passText.requestFocusInWindow();
				return;
			}
		}
	}
}