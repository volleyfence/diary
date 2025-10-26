package diary.dialog;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import diary.panel.DiaryWriter;

/**
 * 日記の削除ダイアログを表示する
 * @author Masato Suzuki
 */
public class DiaryDeleteDialog extends DiaryDialog {
	/**
	 * DiaryWriterクラスのオブジェクト
	 */
	private DiaryWriter dw;

	/**
	 * 削除する日記の日付
	 */
	private int year, month, day;

	/**
	 * コンストラクタ
	 * @param dw DiaryWriterクラスのオブジェクト
	 * @param year 削除する日記の年
	 * @param month 削除する日記の月
	 * @param day 削除する日記の日
	 */
	public DiaryDeleteDialog(DiaryWriter dw, int year, int month, int day) {
		super(df, year + "年" + month + "月" + day + "日", true);
		this.dw = dw;
		this.year = year;
		this.month = month;
		this.day = day;

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		// messagePanelの生成&部品設置
		label1.setText("削除する項目を選択してください");
		label2.setText("選択した時点で削除されます");
		messagePanel.add(label1);
		messagePanel.add(label2);

		// buttonPanelの生成&部品設置
		JPanel buttonPanel = new JPanel();

		boolean textExists = fc.getDiaryTextExists(year, month, day);
		boolean pictureExists = fc.getDiaryImageExists(year, month, day);

		if(textExists) {
			button1.setText("テキスト");
			buttonPanel.add(button1);
		}

		if(pictureExists) {
			button2.setText("写真");
			buttonPanel.add(button2);
		}

		if(textExists && pictureExists) {
			button3.setText("どちらも");
			buttonPanel.add(button3);
		}

		this.setLayout(new BorderLayout(setting.getSize(10), setting.getSize(5)));
		this.add(messagePanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);

		resize(df, 100, 0);
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	@Override
	protected void processing(Object source) {
		// テキスト
		if(source == button1) {
			deleteText();
		}

		// 写真
		else if(source == button2) {
			deleteImage();
		}

		// どちらも
		else if(source == button3) {
			deleteText();
			deleteImage();
		}
		this.dispose();
	}

	/**
	 * 日記のテキストを削除する
	 */
	private void deleteText() {
		fc.deleteText(year, month, day);
		dw.resetText();
		dw.repaint();
	}

	/**
	 * 日記の写真を削除する
	 */
	private void deleteImage() {
		fc.deleteImage(year, month, day);
		dw.changeImage();
	}
}