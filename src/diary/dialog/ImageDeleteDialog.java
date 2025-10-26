package diary.dialog;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;

import diary.panel.DiaryWriter;

/**
 * 画像削除の警告ダイアログを表示する
 * @author Masato Suzuki
 */
public class ImageDeleteDialog extends DiaryDialog {
	/**
	 * DiaryWriterクラスのオブジェクト
	 */
	private DiaryWriter dw;

	/**
	 * DropImageDialogのオブジェクト
	 */
	private DropImageDialog di;

	/**
	 * 削除する写真の日付
	 */
	private int year, month, day;

	/**
	 * 削除する写真ファイル
	 */
	private File imageFile;

	/**
	 * コンストラクタ
	 * @param dw DiaryWriterクラスのオブジェクト
	 * @param di DropImageDialogクラスのオブジェクト
	 * @param year 削除する写真の年
	 * @param month 削除する写真の月
	 * @param day 削除する写真の日
	 * @param imageFile 削除する写真ファイル
	 */
	public ImageDeleteDialog(DiaryWriter dw, DropImageDialog di, int year, int month, int day, File imageFile) {
		super(di, "確認", true);
		this.di = di;
		this.dw = dw;
		this.di = di;
		this.year = year;
		this.month = month;
		this.day = day;
		this.imageFile = imageFile;

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		// messagePanelの生成&部品設置
		label1.setText("写真は1枚しか登録できません");
		label2.setText("写真を置き換えますか？");
		messagePanel.add(label1);
		messagePanel.add(label2);

		// buttonPanelの生成&部品設置
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button1);
		buttonPanel.add(button2);

		this.setLayout(new BorderLayout(setting.getSize(10), setting.getSize(5)));
		this.add(messagePanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);

		resize(di, 100, 0);
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	@Override
	protected void processing(Object source) {
		// はい
		if(source == button1) {
			fc.deleteImage(year, month, day);
			fc.saveImage(year, month, day, imageFile);
			dw.changeImage();
			di.dispose();
		}
		this.dispose();
	}
}
