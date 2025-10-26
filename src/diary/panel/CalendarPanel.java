package diary.panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import diary.button.DayButton;
import diary.button.IconButton;
import diary.dialog.YesDialog;
import diary.label.TransparentLabel;
import diary.panel.parts.DateControlPanel;
import diary.panel.parts.DiaryPanel;
import diary.panel.parts.ImagePanel;
import diary.panel.parts.TransparentPanel;
import diary.system.calendar.Date;
import diary.system.calendar.MyCalendar;

/**
 * カレンダー画面
 * @author Masato Suzuki
 */
public class CalendarPanel extends DiaryPanel implements MouseListener {
	/**
	 * CalendarPanelの本体<br>
	 * 背景画像が描画される
	 */
	private ImagePanel calendarPanel;

	/**
	 * 日付操作パネル
	 */
	private DateControlPanel titlePanel;

	/**
	 * 日付部分のパネル（カードレイアウト）
	 */
	private TransparentPanel cardPanel;

	/**
	 * 曜日ラベル
	 */
	private JLabel[] dayOfTheWeek;

	/**
	 * 日付ボタン
	 */
	private DayButton[] dayButton;

	/**
	 * 検索ボタン
	 */
	private IconButton searchButton;

	/**
	 * 設定ボタン
	 */
	private IconButton settingButton;

	/**
	 * ヘルプボタン
	 */
	private IconButton helpButton;

	/**
	 * カードレイアウト
	 */
	private CardLayout card;

	/**
	 * 今日の日付
	 */
	private Date todayDate;

	/**
	 * 表示する日付
	 */
	private int year, month;

	/**
	 * CalendarPanelクラスのコンストラクタ
	 */
	public CalendarPanel(){
		setSystem();

		firstLayout();
	}

	/**
	 * 必要なものを生成する
	 */
	private void setSystem() {
		// 今日の日付の取得
		todayDate = MyCalendar.getToday();
		year = todayDate.getYear();
		month = todayDate.getMonth();

		// 日付ボタンの生成
		dayButton = new DayButton[31];
		for(int i = 0; i < 31; i++){
			dayButton[i] = new DayButton(df, i + 1);
		}
	}

	/**
	 * CalendarPanelのレイアウトを行う
	 */
	private void firstLayout() {
		this.setLayout(new GridLayout(1, 1));
		this.add(getCalendarPanel());

		// 表示年月の変更
		titlePanel.setDate(year, month);

		updateComponents();

		// カレンダーのセット
		changeCalendar();
	}

	/**
	 * カレンダーパネルを返す
	 * @return カレンダーパネル
	 */
	private JPanel getCalendarPanel() {
		calendarPanel = new ImagePanel();

		// 全体的にBorderLayout
		calendarPanel.setLayout(new BorderLayout());
		calendarPanel.setBackground(setting.getDefaultBackColor());
		calendarPanel.setOpaque(true);

		calendarPanel.add(getTitlePanel(), BorderLayout.NORTH);
		calendarPanel.add(new TransparentLabel("　　", 80), BorderLayout.WEST);
		calendarPanel.add(getDayPanel(), BorderLayout.CENTER);
		calendarPanel.add(new TransparentLabel("　　", 80), BorderLayout.EAST);
		calendarPanel.add(getCommandPanel(), BorderLayout.SOUTH);

		return calendarPanel;
	}

	/**
	 * 日付コントロール部分のパネルを返す
	 * @return 日付コントロールパネル
	 */
	private JPanel getTitlePanel() {
		// titlePanelの生成, 設置（thisのNORTH）
		titlePanel = new DateControlPanel(df, false) {
			@Override
			protected void firstButtonProcessing() {
				// カレンダーのセット
				changeCalendar();
			}

			@Override
			protected void preButtonProcessing() {
				this.preMonth();

				// カレンダーのセット
				changeCalendar();
			}

			@Override
			protected void nextButtonProcessing() {
				this.nextMonth();

				// カレンダーのセット
				changeCalendar();
			}

			@Override
			protected void nowButtonProcessing() {
				// カレンダーのセット
				changeCalendar();
			}

			@Override
			protected void enterProcessing() {
				// カレンダーのセット
				changeCalendar();
			}
		};
		titlePanel.showBeforePanel();

		// firstButtonの取得
		titlePanel.getFirstButton().setText("最初の月");
		titlePanel.getFirstButton().setToolTipText("最初の月を表示します");

		// preButtonの取得
		titlePanel.getPreButton().setText("前 の 月");
		titlePanel.getPreButton().setToolTipText("前の月を表示します");

		// nextButtonの取得
		titlePanel.getNextButton().setText("次 の 月");
		titlePanel.getNextButton().setToolTipText("次の月を表示します");

		// nowButtonの取得
		titlePanel.getNowButton().setText("今   月");
		titlePanel.getNowButton().setToolTipText("今月を表示します");

		return titlePanel;
	}

	/**
	 * カレンダー表示パネルを返す
	 * @return カレンダー表示パネル
	 */
	private JPanel getDayPanel() {
		// 曜日ラベルの設定
		TransparentPanel dayOfTheWeekPanel = new TransparentPanel(0);
		dayOfTheWeekPanel.setOpaque(false);
		dayOfTheWeekPanel.setLayout(new GridLayout(1, 7));
		dayOfTheWeek = new JLabel[7];
		for(int i = 0; i < 7; i++) {
			dayOfTheWeek[i] = new JLabel(MyCalendar.getDayOfTheWeek(i), JLabel.CENTER);
			dayOfTheWeek[i].setBorder(new LineBorder(Color.BLACK, 1, false));
			dayOfTheWeek[i].setOpaque(true);
			dayOfTheWeek[i].setForeground(setting.getDayOfTheWeekFontColor(i));
			dayOfTheWeek[i].setBackground(setting.getDayOfTheWeekBackColor(i));
			dayOfTheWeekPanel.add(dayOfTheWeek[i]);
		}

		// CardLayoutの設定
		card = new CardLayout();
		cardPanel = new TransparentPanel(0);
		cardPanel.setOpaque(false);
		cardPanel.setLayout(card);

		TransparentPanel dayPanel = new TransparentPanel(0);
		dayPanel.setOpaque(false);
		dayPanel.setLayout(new BorderLayout());
		dayPanel.add(dayOfTheWeekPanel, BorderLayout.NORTH);
		dayPanel.add(cardPanel, BorderLayout.CENTER);

		return dayPanel;
	}

	/**
	 * 下部のボタンパネルを返す
	 * @return ボタンパネル
	 */
	private JPanel getCommandPanel() {
		// searchButtonの生成
		searchButton = new IconButton(fc.getIconImage("search"), setting.getSearchButtonBeforeBackColor(), setting.getSearchButtonAfterBackColor(), "検   索", "『" + setting.getTitle() + "』を検索します", 40, 20, 20, 20, true);
		searchButton.addMouseListener(this);
		iconButton.add(searchButton);

		// settingButtonの生成
		settingButton = new IconButton(fc.getIconImage("setting"), setting.getSettingButtonBeforeBackColor(), setting.getSettingButtonAfterBackColor(), "設   定", "『" + setting.getTitle() + "』の設定を変更します", 40, 20, 20, 20, true);
		settingButton.addMouseListener(this);
		iconButton.add(settingButton);

		// helpButtonの生成
		helpButton = new IconButton(fc.getIconImage("help"), setting.getHelpButtonBeforeBackColor(), setting.getHelpButtonAfterBackColor(), "ヘ ル プ", "『" + setting.getTitle() + "』の取扱説明書を開きます", 40, 20, 20, 20, true);
		helpButton.addMouseListener(this);
		iconButton.add(helpButton);

		// commandPanelの生成
		TransparentPanel commandPanel = new TransparentPanel(80);
		commandPanel.setOpaque(false);

		commandPanel.add(searchButton);
		commandPanel.add(settingButton);
		commandPanel.add(helpButton);

		return commandPanel;
	}

	/**
	 * カレンダーの変更
	 */
	public void changeCalendar() {
		// 今日の日付の取得
		todayDate = MyCalendar.getToday();

		// スクリーンショット用
		/*
		todayDate = new Date(2018, 8, 22);
		 */

		year = titlePanel.getYear();
		month = titlePanel.getMonth();

		changeImage();
		// カードレイアウトにパネルを追加, 表示
		cardPanel.add("calender", setCalendar());
		card.show(cardPanel, "calender");
	}

	/**
	 * 初期フォーカスをセットする
	 */
	public void setFocus() {
		titlePanel.setFocus();
	}

	/**
	 * コンポーネントの更新
	 */
	public void updateComponents() {
		titlePanel.updateComponents();
		searchButton.updateComponents();
		settingButton.updateComponents();
		helpButton.updateComponents();

		for(int i = 0; i < 7; i++) {
			dayOfTheWeek[i].setFont(setting.getFont(10));
		}
	}

	/**
	 * 指定された年月のカレンダーパネルの生成
	 * @return カレンダーパネル
	 */
	private JPanel setCalendar(){
		int day, count;
		TransparentPanel tmp = new TransparentPanel(0);
		tmp.setOpaque(false);
		tmp.setLayout(new GridLayout(0,7));

		boolean[] diaryImageExists = fc.getDiaryImageExists(year, month);

		// 日付ボタンの設置, 日付以外は空にする(countはパネルのインデックス、dayは日付)
		day = -MyCalendar.getOffset(year, month, 1);
		for(count = 0; count < 42; count++, day++){ // カレンダーの最大マス数は42
			// countが日付の範囲か
			boolean isDay = (1 <= (day + 1) && (day + 1) <= MyCalendar.getLastDay(year, month));

			// 各パネルの背景画像を決めるための条件判定
			boolean today = year == todayDate.getYear() && month == todayDate.getMonth() && (day + 1) == todayDate.getDay();
			boolean diaryTextExist = fc.getDiaryTextExists(year, month, day + 1);
			boolean diaryImageExist = false;
			if(isDay) {
				diaryImageExist = diaryImageExists[day];
			}

			// 日付の背景画像の設定
			ImagePanel dayTmp = new ImagePanel(fc.getDayImage(today, diaryTextExist, diaryImageExist), false, true);
			dayTmp.setBorder(new LineBorder(Color.BLACK, 1, false));
			dayTmp.setLayout(new GridLayout(1, 1));
			// 日付ボタンの設置
			if(isDay) {
				dayButton[day].reset(year, month);

				int offset = count % 7;

				// 日曜のボタン
				if(offset == 0) {
					dayButton[day].setForeground(setting.getSundayButtonFontColor());
				}

				// 土曜のボタン
				else if(offset == 6) {
					dayButton[day].setForeground(setting.getSaturdayButtonFontColor());
				}

				dayTmp.add(dayButton[day]);
			}

			tmp.add(dayTmp);
		}

		// 日付パネルを返す
		return tmp;
	}

	/**
	 * 背景画像を変更する
	 */
	public void changeImage() {
		backgroundCount++;

		Thread backgroundThread = new Thread(new Runnable() {
			private int threadNum = backgroundCount;
			@Override
			public void run() {
				BufferedImage image = null;

				removeBackgroundImage();
				if(fc.getCalendarBackgroundExists()) {
					image = fc.getCalendarBackgroundImage();
				}
				else if(fc.getDefaultBackgroundExists()) {
					image = fc.getDefaultBackgroundImage();
				}

				// この処理が正しい日付で行われているか（スレッド開始時の番号と現在の番号が一致しているか）
				if(threadNum == backgroundCount) {
					if(image.getWidth() < 400 || image.getHeight() < 400) {
						// このときの第2引数は例外（気にしない）
						calendarPanel.setImage(image, true, true);
					}
					else {
						calendarPanel.setImage(image, false, false);
					}
				}
				image.flush();
			}
		});
		backgroundThread.start();
	}

	/**
	 * 背景を削除
	 */
	public void removeBackgroundImage() {
		calendarPanel.removeBackgroundImage();
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	private void processing(Object source) {
		// 検索
		if(source == searchButton) {
			df.changeSearchPanel();
		}

		// 設定
		else if(source == settingButton) {
			df.changeSettingPanel();
		}

		// ヘルプ
		else if(source == helpButton) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String pdfPath = setting.getPdfPath();
					File pdfFile = new File(pdfPath);

					// なければ作る
					if(!pdfFile.exists()) {
						fc.createPdf();
					}

					try {
						Desktop.getDesktop().open(pdfFile);
					}
					catch(Exception error) {
						error.printStackTrace();
						new YesDialog(df, "取扱説明書がありません<br>jarファイルに欠陥があります");
					}
				}
			}).start();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			if(iconButton.contains(e.getSource()) && iconButton.get(iconButton.indexOf(e.getSource())).pressed(e)) {
				processing(e.getSource());
			}
		}
	}
}
