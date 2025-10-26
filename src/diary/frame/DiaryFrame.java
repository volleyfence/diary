package diary.frame;

import java.awt.CardLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import diary.dialog.PasswordDialog;
import diary.dialog.WritingDialog;
import diary.panel.CalendarPanel;
import diary.panel.DiaryWriter;
import diary.panel.SearchPanel;
import diary.panel.SettingPanel;
import diary.system.DiaryFrameHolder;
import diary.system.Setting;
import diary.system.calendar.Date;
import diary.system.file.FileControl;

/**
 * 日記フレーム<br>
 * カードレイアウトによる日記画面遷移の制御を行う<br>
 * また、各画面間での操作も制御する
 * @author Masato Suzuki
 */
public class DiaryFrame extends JFrame implements WindowListener {
	/**
	 * Settingクラスのオブジェクト
	 */
	private Setting setting = Setting.getInstance();

	/**
	 * FileControlクラスのオブジェクト
	 */
	private FileControl fc = FileControl.getInstance();

	/**
	 * 日記パネル
	 */
	private JPanel diaryPanel;

	/**
	 * カードレイアウト
	 */
	private CardLayout card;

	/**
	 * CalendarPanelのオブジェクト
	 */
	private CalendarPanel cp;

	/**
	 * DiaryWriterクラスのオブジェクト
	 */
	private DiaryWriter dw;

	/**
	 * SearchPanelクラスのオブジェクト
	 */
	private SearchPanel sp;

	/**
	 * SettingPanelクラスのオブジェクト
	 */
	private SettingPanel settingp;

	/**
	 * パネル遷移履歴<br>
	 * リングバッファ
	 */
	private String[] history = new String[10];

	/**
	 * 履歴における現在のインデックス
	 */
	private int historyIndex = 0;

	/**
	 * フレームの状態
	 */
	private int frameState = JFrame.NORMAL;

	/**
	 * DiaryFrameが使用可能か
	 */
	private boolean isAvailable = true;

	/**
	 * DiaryFrameクラスのコンストラクタ
	 */
	public DiaryFrame() {
		super();

		firstLayout();
	}

	/**
	 * DiaryFrameクラスのコンストラクタ
	 * @param frameState フレーム状態
	 */
	public DiaryFrame(int frameState) {
		super();
		this.frameState = frameState;

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		// 日記システムにDiaryFrameオブジェクトをセット
		DiaryFrameHolder.setDiaryFrame(this);

		// フレーム生成回数の追加
		setting.addFrameCount();

		// DiaryFrameが表示されるまではロゴと進捗を表示する
		StartEndFrame sef = null;

		// 最初の生成時はディレクトリチェックを行う
		if(setting.getFrameCount() == 1) {
			sef = new StartEndFrame(true);

			// 空ディレクトリの削除
			fc.deleteAllEmptyDirectory(sef);
		}

		// 履歴を初期化（NullPointerExceptionを防ぐ）
		for(int i = 0; i < 10; i++) {
			history[i] = "CalendarPanel";
		}

		// カードレイアウトの設定
		card = new CardLayout();
		diaryPanel = new JPanel();
		diaryPanel.setLayout(card);

		Thread calendarThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// カレンダーパネルの生成&登録
				cp = new CalendarPanel();
				diaryPanel.add("CalendarPanel", cp);
			}
		});
		calendarThread.start();

		Thread diaryWriteThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 日記編集パネルの生成&登録
				dw = new DiaryWriter();
				diaryPanel.add("DiaryWriter", dw);
			}
		});
		diaryWriteThread.start();

		Thread searchThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 検索パネルの生成&登録
				sp = new SearchPanel();
				diaryPanel.add("SearchPanel", sp);
			}
		});
		searchThread.start();

		Thread settingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 設定パネルの生成&登録
				settingp = new SettingPanel();
				diaryPanel.add("SettingPanel", settingp);
			}
		});
		settingThread.start();

		try {
			calendarThread.join();
			diaryWriteThread.join();
			searchThread.join();
			settingThread.join();
		}
		catch(Exception error) {
			error.printStackTrace();
		}

		historyAdd("CalendarPanel");

		// フレームに設置
		this.add(diaryPanel);

		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(setting.getTitle());
		this.setIconImage(fc.getIconImage("favicon"));

		if(setting.getFrameCount() == 1) {
			sef.dispose();
		}

		// 最初の起動時のみパスワード入力が求められる（設定している場合のみ）
		if(setting.getPasswordLock() && setting.getFrameCount() == 1) {
			new PasswordDialog();
		}
		else {
			this.frameShow();
		}

		updateComponents();
	}

	/**
	 * フレームサイズの計算
	 */
	private void calcFrameSize() {
		// 適切なフレームサイズ設定
		this.pack();
		int width = this.getWidth();
		this.setSize(width, setting.getHeight(width));
		this.setMinimumSize(setting.getMinimumFrameSize(this.getSize()));
	}

	/**
	 * フレームサイズを指定して表示
	 */
	public void frameShow() {
		this.calcFrameSize();

		// スクリーンショットを撮るならこの行をコメントアウトする
		this.setLocationRelativeTo(null);

		// フレームサイズがデスクトップサイズを上回るなら最大化する
		if(this.getWidth() > setting.getDesktopWidth() || this.getHeight() > setting.getDesktopHeight()) {
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else {
			this.setExtendedState(frameState);
		}

		this.setVisible(true);
	}

	/**
	 * コンポーネントの更新&フレームサイズを更新<br>
	 * 最小サイズが前回の最小サイズを下回って設定できていなかったためフレーム状態を維持して新しくフレームを生成する
	 */
	public void updateFrameSize() {
		this.dispose();
		DiaryFrame df = new DiaryFrame(this.getExtendedState());
		df.changeSettingPanel();
	}

	/**
	 * コンポーネントの更新
	 */
	public void updateComponents() {
		this.setTitle(setting.getTitle());
		cp.updateComponents();
		dw.updateComponents();
		sp.updateComponents();
		settingp.updateComponents();
	}

	/**
	 * カレンダー画面へ
	 */
	public void changeCalendarPanel() {
		if(!history[historyIndex].equals("SettingPanel")) {
			cp.changeCalendar();
		}
		historyAdd("CalendarPanel");
	}

	/**
	 * 日記編集画面へ
	 * @param year 表示する日記の年
	 * @param month 表示する日記の月
	 * @param day 表示する日記の日
	 */
	public void changeDiaryWriter(int year, int month, int day) {
		if(dw.getWriteState() == true) {
			dw.changeWrite();
		}
		historyAdd("DiaryWriter");
		dw.changeDay(year, month, day);
	}

	/**
	 * 日記編集画面へ
	 * @param date 表示する日記の日付
	 */
	public void changeDiaryWriter(Date date) {
		changeDiaryWriter(date.getYear(), date.getMonth(), date.getDay());
	}

	/**
	 * 検索画面へ
	 */
	public void changeSearchPanel() {
		sp.reset();
		sp.changeImage();
		historyAdd("SearchPanel");
		sp.focus();
	}

	/**
	 * 設定画面へ
	 */
	public void changeSettingPanel() {
		settingp.reset();
		settingp.changeImage();
		historyAdd("SettingPanel");
	}

	/**
	 * 履歴を追加して表示
	 * @param next 次のページ名
	 */
	public void historyAdd(String next) {
		if(history[historyIndex].equals("diaryWriter")) {
			dw.removeBackgroundImage();
		}

		historyIndex++;
		if(historyIndex == 10) {
			historyIndex = 0;
		}
		history[historyIndex] = next;
		card.show(diaryPanel, next);
	}

	/**
	 * 戻る
	 */
	public void back() {
		historyIndex--;
		if(historyIndex == -1) {
			historyIndex = 9;
		}

		card.show(diaryPanel, history[historyIndex]);

		if(history[historyIndex].equals("CalendarPanel")) {
			cp.changeCalendar();
			cp.setFocus();
		}

		else if(history[historyIndex].equals("DiaryWriter")) {
			if(dw.getWriteState() == true) {
				dw.changeWrite();
				dw.setFocus();
			}
			dw.repaint();
		}

		else if(history[historyIndex].equals("SearchPanel")) {
			sp.changeImage();
			sp.focus();
		}
	}

	/**
	 * 検索中止
	 */
	public void searchStop() {
		sp.searchStop();
	}

	/**
	 * 保存
	 */
	public void save() {
		dw.save();
	}

	/**
	 * ソフトウェア終了
	 * @param 再起動するか
	 */
	public void diaryExit(boolean reboot) {
		// 旧フレームの破棄
		this.dispose();

		new Thread(new Runnable() {
			@Override
			public void run() {
				StartEndFrame sef = new StartEndFrame(false);

				// 空ディレクトリの削除
				fc.deleteAllEmptyDirectory(sef);

				sef.dispose();

				// 再起動する場合
				if(reboot) {
					// フレーム生成回数のリセット
					setting.resetFrameCount();

					// フレームの生成
					new DiaryFrame();

					return;
				}

				System.exit(0);
			}
		}).start();
	}

	@Override
	public void dispose() {
		isAvailable = false;
		super.dispose();
	}

	/**
	 * DiaryFrameが使用可能かを返す<br>
	 * disposeした後に使用しないようにするために実装
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// 日記が編集中かつ、テキストが変化しているならダイアログを表示
		if(history[historyIndex].equals("DiaryWriter")  && dw.getWriteState() && dw.getTextChange()) {
			new WritingDialog(dw, 0);
		}
		else {
			diaryExit(false);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}
}
