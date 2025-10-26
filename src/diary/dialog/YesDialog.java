package diary.dialog;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import diary.label.DiaryLabel;

/**
 * ダイアログを表示する
 * @author Masato Suzuki
 */
public class YesDialog extends DiaryDialog {
	/**
	 * メッセージ配列
	 */
	private String[] messages;

	/**
	 * フォント
	 */
	private Font font = null;

	/**
	 * コンストラクタ
	 * @param frame 呼び出し元のオブジェクト
	 * @param message 表示するメッセージ<br>メッセージの改行は「＜br＞」（＜＞は半角）
	 */
	public YesDialog(JFrame frame, String message) {
		super(frame, "確認", true);
		this.c = frame;

		messages = message.split("<br>");

		firstLayout();
	}

	/**
	 * コンストラクタ
	 * @param frame 呼び出し元のオブジェクト
	 * @param message 表示するメッセージ<br>メッセージの改行は「＜br＞」（＜＞は半角）
	 * @param font 設定したいフォント
	 */
	public YesDialog(JFrame frame, String message, Font font) {
		super(frame, "確認", true);
		this.c = frame;
		this.font = font;

		messages = message.split("<br>");

		firstLayout();
	}

	/**
	 * コンストラクタ
	 * @param dialog 呼び出し元のオブジェクト
	 * @param message 表示するメッセージ<br>メッセージの改行は「＜br＞」（＜＞は半角）
	 */
	public YesDialog(JDialog dialog, String message) {
		super(dialog, "確認", true);
		this.c = dialog;

		messages = message.split("<br>");

		firstLayout();
	}

	/**
	 * コンストラクタ
	 * @param dialog 呼び出し元のオブジェクト
	 * @param message 表示するメッセージ<br>メッセージの改行は「＜br＞」（＜＞は半角）
	 * @param font 設定したいフォント
	 */
	public YesDialog(JDialog dialog, String message, Font font) {
		super(dialog, "確認", true);
		this.c = dialog;
		this.font = font;

		messages = message.split("<br>");

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		// messagePanelの部品設置
		for(int i = 0; i < messages.length; i++) {
			DiaryLabel label = new DiaryLabel(messages[i], 0, JLabel.CENTER, true);
			if(font != null) {
				label.setFont(font);
			}
			messagePanel.add(label);
		}

		// buttonPanelの生成&部品設置
		JPanel buttonPanel = new JPanel();
		button1.setText("了解");
		if(font != null) {
			button1.setFont(font);
		}
		buttonPanel.add(button1);

		this.setLayout(new BorderLayout(setting.getSize(5), setting.getSize(0)));
		this.add(messagePanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);

		// 位置指定
		resize(c, 100, 0);
	}

	@Override
	protected void processing(Object source) {
		// 了解
		if(source == button1) {
			this.dispose();
		}
	}
}
