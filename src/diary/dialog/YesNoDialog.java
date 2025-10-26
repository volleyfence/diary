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
public class YesNoDialog extends DiaryDialog {
	/**
	 * メッセージ配列
	 */
	private String[] messages;

	/**
	 * フォント
	 */
	private Font font = null;

	/**
	 * コンストラクタ<br>
	 * 以下のメソッドをオーバーライドすることで各ボタンの処理を変更できます<br>
	 * ・protected void yesProcessing()：はいボタンを押したときの処理
	 * @param frame 呼び出し元のオブジェクト
	 * @param message 表示するメッセージ<br>メッセージの改行は「＜br＞」（＜＞は半角）
	 */
	public YesNoDialog(JFrame frame, String message) {
		super(frame, "確認", true);
		this.c = frame;

		messages = message.split("<br>");

		firstLayout();
	}

	/**
	 * コンストラクタ<br>
	 * 以下のメソッドをオーバーライドすることで各ボタンの処理を変更できます<br>
	 * ・protected void yesProcessing()：はいボタンを押したときの処理
	 * @param frame 呼び出し元のオブジェクト
	 * @param message 表示するメッセージ<br>メッセージの改行は「＜br＞」（＜＞は半角）
	 * @param font 設定したいフォント
	 */
	public YesNoDialog(JFrame frame, String message, Font font) {
		super(frame, "確認", true);
		this.c = frame;
		this.font = font;

		messages = message.split("<br>");

		firstLayout();
	}

	/**
	 * コンストラクタ<br>
	 * 以下のメソッドをオーバーライドすることで各ボタンの処理を変更できます<br>
	 * ・protected void yesProcessing()：はいボタンを押したときの処理
	 * @param dialog 呼び出し元のオブジェクト
	 * @param message 表示するメッセージ<br>メッセージの改行は「＜br＞」（＜＞は半角）
	 */
	public YesNoDialog(JDialog dialog, String message) {
		super(dialog, "確認", true);
		this.c = dialog;

		messages = message.split("<br>");

		firstLayout();
	}

	/**
	 * コンストラクタ<br>
	 * 以下のメソッドをオーバーライドすることで各ボタンの処理を変更できます<br>
	 * ・protected void yesProcessing()：はいボタンを押したときの処理
	 * @param dialog 呼び出し元のオブジェクト
	 * @param message 表示するメッセージ<br>メッセージの改行は「＜br＞」（＜＞は半角）
	 * @param font 設定したいフォント
	 */
	public YesNoDialog(JDialog dialog, String message, Font font) {
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
		if(font != null) {
			button1.setFont(font);
			button2.setFont(font);
		}
		buttonPanel.add(button1);
		buttonPanel.add(button2);

		this.setLayout(new BorderLayout(setting.getSize(5), setting.getSize(0)));
		this.add(messagePanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);

		addLayoutProcessing();

		// 位置指定
		resize(c, 100, 0);
	}

	/**
	 * 追加の初期化処理
	 */
	protected void addLayoutProcessing() {

	}

	/**
	 * はいボタンの処理<br>
	 * インスタンス化する際にオーバーライドして処理を記述してください
	 */
	protected void yesProcessing() {

	}

	/**
	 * いいえボタンの処理<br>
	 * ダイアログを閉じるのみの処理
	 */
	private void noProcessing() {
		this.dispose();
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	@Override
	protected void processing(Object source) {
		if(source == button1) {
			yesProcessing();
		}
		noProcessing();
	}
}
