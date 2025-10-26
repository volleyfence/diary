package diary.dialog;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import diary.panel.DiaryWriter;
import diary.system.DiaryFrameHolder;

/**
 * 編集中の警告ダイアログを表示する
 * @author Masato Suzuki
 */
public class WritingDialog extends DiaryDialog {
	/**
	 * DiaryWriterクラスのオブジェクト
	 */
	private DiaryWriter dw;

	/**
	 * オプション
	 */
	private int option;

	/**
	 * コンストラクタ
	 * @param dw DiaryWriterクラスのオブジェクト
	 * @param option 0なら保存して終了、1なら保存して戻る
	 */
	public WritingDialog(DiaryWriter dw, int option) {
		super(df, "確認", true);
		this.dw = dw;
		this.option = option;

		firstLayout();

	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		// messagePanelの生成&部品設置
		label1.setText("内容が変更されています");
		if(option == 0) {
			label2.setText("『" + setting.getTitle() + "』を保存して終了しますか？");
		}
		else {
			label2.setText("『" + setting.getTitle() + "』を保存して戻りますか？");
		}
		messagePanel.add(label1);
		messagePanel.add(label2);

		button1.setText("保存する");
		button2.setText("保存しない");

		// buttonPanelの生成&部品設置
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);

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
		// はい
		if(source == button1) {
			dw.save();
			if(option == 0) {
				DiaryFrameHolder.getDiaryFrame().diaryExit(false);
			}
			else {
				this.dispose();
				df.back();
			}
		}

		// いいえ
		else if(source == button2) {
			if(option == 0) {
				DiaryFrameHolder.getDiaryFrame().diaryExit(false);
			}
			else {
				this.dispose();
				df.back();
			}
		}

		// キャンセル
		else if(source == button3) {
			this.dispose();
		}
	}
}
