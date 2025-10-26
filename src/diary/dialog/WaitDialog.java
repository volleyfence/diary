package diary.dialog;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * 処理待ちダイアログを表示する
 * @author Masato Suzuki
 */
public class WaitDialog extends DiaryDialog {
	/**
	 * 最大件数
	 */
	private int max;

	/**
	 * 処理件数
	 */
	private int done = 0;

	/**
	 * 進捗バー
	 */
	private JProgressBar progress;

	/**
	 * コンストラクタ
	 */
	public WaitDialog() {
		super(df, "", false);
		this.addWindowListener(this);

		firstLayout();

	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		// messagePanelの生成&部品設置
		label1.setText("少々お待ちください");
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BorderLayout());
		progress = new JProgressBar();
		progress.setIndeterminate(true);
		progress.setFont(setting.getFont(0));
		label2.setText("  件/  件");
		messagePanel.add(label1);
		messagePanel.add(progressPanel);
		progressPanel.add(new JLabel("  "), BorderLayout.WEST);
		progressPanel.add(progress, BorderLayout.CENTER);
		progressPanel.add(new JLabel("  "), BorderLayout.EAST);
		messagePanel.add(label2);

		// buttonPanelの生成&部品設置
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button3);

		this.setLayout(new BorderLayout(setting.getSize(10), setting.getSize(5)));
		this.add(messagePanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		resize(df, 100, 0);
	}

	/**
	 * 最大件数のセット
	 * @param max 最大件数
	 */
	public void setMax(int max) {
		this.max = max;
		progress.setIndeterminate(false);
		progress.setStringPainted(true);
		progress.setMinimum(0);
		progress.setMaximum(max);
		//progress.setString(done + "件/" + max + "件");
		label2.setText(done + "件/" + max + "件");
	}

	/**
	 * 処理済み件数の加算
	 */
	public void add() {
		done++;
		progress.setValue(done);
		//progress.setString(done + "件/" + max + "件");
		label2.setText(done + "件/" + max + "件");
	}

	/**
	 * 処理をキャンセル
	 */
	private void cancel() {
		df.searchStop();
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	@Override
	protected void processing(Object source) {
		// キャンセル
		if(source == button3) {
			this.dispose();
			cancel();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		cancel();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
