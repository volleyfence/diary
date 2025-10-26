package diary.panel.setting;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import diary.dialog.ChangeSettingDialog;
import diary.frame.DiaryFrame;
import diary.label.DiaryLabel;
import diary.system.DiaryFrameHolder;
import diary.system.Setting;
import diary.system.file.FileControl;

/**
 * 各種設定パネルはこのクラスを継承する
 * @author Masato Suzuki
 */
public class SettingPanel extends JPanel implements ActionListener, KeyListener, ChangeListener {
	/**
	 * DiaryFrameクラスのオブジェクト
	 */
	protected DiaryFrame df = DiaryFrameHolder.getDiaryFrame();

	/**
	 * Settingクラスのオブジェクト
	 */
	protected Setting setting = Setting.getInstance();

	/**
	 * FileControlクラスのオブジェクト
	 */
	protected FileControl fc = FileControl.getInstance();


	/**
	 * ChangeSettingDialogクラスのオブジェクト
	 */
	protected ChangeSettingDialog csd;

	/**
	 * メッセージ本文パネル
	 */
	protected JPanel messagePanel;

	/**
	 * 設定変更パネル
	 */
	protected JPanel inputPanel;

	/**
	 * 現在の設定ラベル
	 */
	protected DiaryLabel nowSet;

	/**
	 * ラベル1
	 */
	protected DiaryLabel label1;

	/**
	 * ラベル2
	 */
	protected DiaryLabel label2;

	/**
	 * ラベル3
	 */
	protected DiaryLabel label3;

	/**
	 * ラベル4
	 */
	protected DiaryLabel label4;

	/**
	 * 変更ボタン
	 */
	protected JButton changeButton;

	/**
	 * キャンセルボタン
	 */
	protected JButton cancelButton;

	/**
	 * 出力する設定
	 */
	protected String out = "";

	/**
	 * ボタン長押し防止<br>
	 * keyPressedに処理を記述した場合キーを長押しすると連続で処理が行われるためその対策
	 */
	protected boolean press = false;

	/**
	 * コンストラクタ<br>
	 * 以下のメソッドをオーバーライドすることで各ボタンの処理を変更できます<br>
	 * ・protected boolean changeProcessing()：変更ボタンを押したときの処理<br>
	 * なお、フレームサイズを変える場合はchangeProcessing内でダイアログを閉じる処理を記述するように<br>
	 * また、状況によっては以下のメソッドをオーバーライドすることでchangeButtonとcancelButton以外の各コンポーネントの処理を変更できます<br>
	 * ・protected void addProcessing(Object source)
	 * @param message 設定の説明
	 */
	public SettingPanel(String message) {
		nowSet = new DiaryLabel("現在の設定：", 0, JLabel.RIGHT, false);

		// ラベルの生成
		label1 = new DiaryLabel(0, JLabel.CENTER, false);
		label2 = new DiaryLabel(0, JLabel.CENTER, false);
		label3 = new DiaryLabel(0, JLabel.CENTER, false);
		label4 = new DiaryLabel(0, JLabel.CENTER, false);

		// messagePanelの生成
		messagePanel = new JPanel();
		messagePanel.setLayout(new GridLayout(0,1));
		messagePanel.add(new JLabel(""));
		String[] messages = message.split("<br>");
		for(int i = 0; i < messages.length; i++) {
			DiaryLabel label = new DiaryLabel(messages[i], 0, JLabel.CENTER, false);
			messagePanel.add(label);
		}

		// inputPanelの生成
		inputPanel = new JPanel();

		changeButton = new JButton("変更");
		changeButton.setUI(new com.sun.java.swing.plaf.windows.WindowsButtonUI());
		changeButton.setFocusPainted(false);
		changeButton.setFont(setting.getSettingFont(0));
		changeButton.setForeground(setting.getSettingFontColor());
		changeButton.addActionListener(this);
		changeButton.addKeyListener(this);

		cancelButton = new JButton("キャンセル");
		cancelButton.setUI(new com.sun.java.swing.plaf.windows.WindowsButtonUI());
		cancelButton.setFocusPainted(false);
		cancelButton.setFont(setting.getSettingFont(0));
		cancelButton.setForeground(setting.getSettingFontColor());
		cancelButton.addActionListener(this);
		cancelButton.addKeyListener(this);

		// buttonPanelの生成&部品設置
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(changeButton);
		buttonPanel.add(cancelButton);

		this.setLayout(new BorderLayout(setting.getSize(10), setting.getSize(5)));
		this.add(messagePanel, BorderLayout.NORTH);
		this.add(inputPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * 変更ボタンを押したときの処理
	 * @return 処理が完了したか<br>
	 * true：完了、キャンセル処理を行います<b>
	 * false：未完了、何もしません
	 */
	protected boolean changeProcessing() {
		return false;
	}

	/**
	 * キャンセルボタンを押したときの処理<br>
	 * 閉じるのみの処理
	 */
	protected void cancelProcessing() {
		csd.dispose();
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	protected void processing(Object source) {
		if(source != cancelButton) {
			if(!changeProcessing()) {
				return;
			}
		}
		cancelProcessing();
	}

	/**
	 * 追加の処理<br>
	 * changeButtonとcancelButton以外の処理はここに記述する<br>
	 * オーバーライドした場合は必ずtrueを返すように
	 * @param source イベントの発生源
	 * @return オーバーライドしたか
	 */
	protected boolean addProcessing(Object source) {
		return false;
	}

	/**
	 * 適切なメソッドを実行する<br>
	 * addProcessingがオーバーライドされている場合、changeButtonとcancelボタンの処理が行われていなかったため実装
	 */
	protected void methodControl(Object source) {
		if(source == changeButton || source == cancelButton || !addProcessing(source)) {
			processing(source);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		methodControl(e.getSource());
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(KeyEvent.VK_ENTER == e.getKeyCode()) {
			methodControl(e.getSource());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void stateChanged(ChangeEvent e) {

	}
}
