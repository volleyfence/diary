package diary.dialog;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import diary.frame.DiaryFrame;
import diary.label.DiaryLabel;
import diary.system.DiaryFrameHolder;
import diary.system.Setting;
import diary.system.file.FileControl;

/**
 * 日記ダイアログ（すべてのダイアログはこれを継承する）
 * @author Masato Suzuki
 */
public class DiaryDialog extends JDialog implements ActionListener, KeyListener, WindowListener {
	/**
	 * コンポーネント
	 */
	protected Component c = null;

	/**
	 * DiaryFrameクラスのオブジェクト
	 */
	protected static DiaryFrame df = DiaryFrameHolder.getDiaryFrame();

	/**
	 * Settingクラスのオブジェクト
	 */
	protected Setting setting = Setting.getInstance();

	/**
	 * FileControlクラスのオブジェクト
	 */
	protected FileControl fc = FileControl.getInstance();

	/**
	 * メッセージパネル
	 */
	protected JPanel messagePanel;

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
	 * ボタン1
	 */
	protected JButton button1;

	/**
	 * ボタン2
	 */
	protected JButton button2;

	/**
	 * ボタン3
	 */
	protected JButton button3;

	/**
	 * 呼び出し元がJFrameの場合のコンストラクタ
	 * @param frame 呼び出し元のオブジェクト
	 * @param title ダイアログのタイトル
	 * @param modal 呼び出し元の操作を禁止するか
	 */
	public DiaryDialog(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		init();
	}

	/**
	 * 呼び出し元がJDialogの場合のコンストラクタ
	 * @param dialog 呼び出し元のオブジェクト
	 * @param title ダイアログのタイトル
	 * @param modal 呼び出し元の操作を禁止するか
	 */
	public DiaryDialog(JDialog dialog, String title, boolean modal) {
		super(dialog, title, modal);
		init();
	}

	/**
	 * 初期設定を行う
	 */
	private void init() {
		// messagePanelの生成
		messagePanel = new JPanel();
		messagePanel.setLayout(new GridLayout(0,1));
		messagePanel.add(new JLabel(""));

		// ラベルの生成
		label1 = new DiaryLabel(0, JLabel.CENTER, true);
		label2 = new DiaryLabel(0, JLabel.CENTER, true);
		label3 = new DiaryLabel(0, JLabel.CENTER, true);
		label4 = new DiaryLabel(0, JLabel.CENTER, true);

		// button1の生成
		button1 = new JButton("はい");
		button1.setUI(new com.sun.java.swing.plaf.windows.WindowsButtonUI());
		button1.setFocusPainted(false);
		button1.setFont(new Font(setting.getDefaultFontName(), setting.getDefaultFontStyle(), setting.getFontSize(setting.getDefaultFontSize())));
		button1.setForeground(setting.getDefaultFontColor());
		button1.addActionListener(this);
		button1.addKeyListener(this);

		// button2の生成
		button2 = new JButton("いいえ");
		button2.setUI(new com.sun.java.swing.plaf.windows.WindowsButtonUI());
		button2.setFocusPainted(false);
		button2.setFont(new Font(setting.getDefaultFontName(), setting.getDefaultFontStyle(), setting.getFontSize(setting.getDefaultFontSize())));
		button2.setForeground(setting.getDefaultFontColor());
		button2.addActionListener(this);
		button2.addKeyListener(this);

		// button3の生成
		button3 = new JButton("キャンセル");
		button3.setUI(new com.sun.java.swing.plaf.windows.WindowsButtonUI());
		button3.setFocusPainted(false);
		button3.setFont(new Font(setting.getDefaultFontName(), setting.getDefaultFontStyle(), setting.getFontSize(setting.getDefaultFontSize())));
		button3.setForeground(setting.getDefaultFontColor());
		button3.addActionListener(this);
		button3.addKeyListener(this);
	}

	/**
	 * サイズ調整
	 * @param c このコンポーネントの中心にダイアログを表示させます<br>
	 * なお、nulにすると画面の中心にダイアログを表示させます
	 * @param addWidth 横幅の余白（÷2されます）
	 * @param addHeight 縦幅の余白（÷2されます）
	 */
	protected void resize(Component c, int addWidth, int addHeight) {
		// 適切なフレームサイズ設定
		this.pack();
		this.setSize(this.getWidth() + setting.getSize(addWidth), this.getHeight() + setting.getSize(addHeight));

		// 位置指定
		this.setLocationRelativeTo(c);

		this.setResizable(false);

		this.setVisible(true);
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	protected void processing(Object source) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		processing(e.getSource());
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			processing(e.getSource());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}
}
