package diary.dialog;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import diary.panel.SearchPanel;

/**
 * 削除の警告ダイアログを表示する
 * @author Masato Suzuki
 */
public class DeleteDialog extends DiaryDialog {
	/**
	 * SearchPanelクラスのオブジェクト
	 */
	private SearchPanel sp;

	/**
	 * 削除件数
	 */
	private int num;

	/**
	 * コンストラクタ
	 * @param sp SearchPanelクラスのオブジェクト
	 * @param num 削除件数
	 */
	public DeleteDialog(SearchPanel sp, int num) {
		super(df, "確認", true);
		this.sp = sp;
		this.num = num;

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		// messagePanelの部品設置
		label1.setText(num + "件の『" + setting.getTitle() + "』が対象です");
		label2.setText(setting.getTitle() + "を削除しますか？");
		messagePanel.add(label1);
		messagePanel.add(label2);

		// buttonPanelの生成&部品設置
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button1);
		buttonPanel.add(button2);

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
			sp.delete();
		}
		this.dispose();
	}
}
