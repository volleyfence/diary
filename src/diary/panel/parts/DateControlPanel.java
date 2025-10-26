package diary.panel.parts;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import diary.button.IconButton;
import diary.dialog.YesDialog;
import diary.frame.DiaryFrame;
import diary.label.DiaryLabel;
import diary.system.Setting;
import diary.system.calendar.Date;
import diary.system.calendar.MyCalendar;
import diary.system.file.FileControl;

/**
 * 日付操作パネル
 * @author Masato Suzuki
 */
public class DateControlPanel extends TransparentPanel implements ActionListener, MouseListener{
	/**
	 * DiaryFrameクラスのオブジェクト
	 */
	private DiaryFrame df;

	/**
	 * Settingクラスのオブジェクト
	 */
	private Setting setting = Setting.getInstance();

	/**
	 * FileControlクラスのオブジェクト
	 */
	private FileControl fc = FileControl.getInstance();

	/**
	 * 日付表示パネル
	 */
	private JPanel datePanel;

	/**
	 * 最初ボタン
	 */
	private IconButton firstButton;

	/**
	 * 前ボタン
	 */
	private IconButton preButton;

	/**
	 * 次ボタン
	 */
	private IconButton nextButton;

	/**
	 * 現在ボタン
	 */
	private IconButton nowButton;

	/**
	 * 年ラベル
	 */
	private DiaryLabel yearLabel;

	/**
	 * 月ラベル
	 */
	private DiaryLabel monthLabel;

	/**
	 * 日ラベル
	 */
	private DiaryLabel dayLabel;

	/**
	 * 年テキスト
	 */
	private JTextField yearText;

	/**
	 * 月テキスト
	 */
	private JTextField monthText;

	/**
	 * 日テキスト
	 */
	private JTextField dayText;

	/**
	 * 年
	 */
	private int year;

	/**
	 * 月
	 */
	private int month;

	/**
	 * 日
	 */
	private int day = 1;

	/**
	 * タイトルパネルのカードレイアウト
	 */
	private CardLayout titleCard;

	/**
	 * マウスが押されているか
	 */
	private boolean press = false;

	/**
	 * 今日の日付
	 */
	private Date todayDate;

	/**
	 * 日付を表示するか
	 */
	private boolean dayShow = false;

	/**
	 * コンストラクタ<br>
	 * 以下のメソッドをオーバーライドすることで各コンポーネントの処理を変更できます<br>
	 * ・protected void firstButtonProcessing()：最初ボタンが押されたときの処理<br>
	 * ・protected void preButtonProcessing()：前ボタンが押されたときの処理、長押しで一定時間おきに繰り返し処理されます<br>
	 * ・protected void nextButtonProcessing()：次ボタンが押されたときの処理、長押しで一定時間おきに繰り返し処理されます<br>
	 * ・protected void nowButtonProcessing()：現在ボタンが押されたときの処理<br>
	 * ・protected void enterProcessing()：日付テキストにおいてEnterキーが押されたときの処理
	 * @param df DiaryFrameクラスのオブジェクト
	 * @param dayShow 日付を表示するか
	 */
	public DateControlPanel(DiaryFrame df, boolean dayShow) {
		super(80);
		this.df = df;
		this.dayShow = dayShow;
		init();
	}

	/**
	 * パネル生成
	 */
	private void init() {
		// 今日の日付の取得
		todayDate = MyCalendar.getToday(dayShow);

		// firstButtonの生成
		firstButton = new IconButton(fc.getIconImage("first"), setting.getFirstButtonBeforeBackColor(), setting.getFirstButtonAfterBackColor(), "", "", 40, 20, 20, 20, true);
		firstButton.addMouseListener(this);

		// preButtonの生成
		preButton = new IconButton(fc.getIconImage("pre"), setting.getPreButtonBeforeBackColor(), setting.getPreButtonAfterBackColor(), "", "", 40, 20, 20, 20, true);
		preButton.addMouseListener(this);

		// nextButtonの生成
		nextButton = new IconButton(fc.getIconImage("next"), setting.getNextButtonBeforeBackColor(), setting.getNextButtonAfterBackColor(), "", "", 40, 20, 20, 20, true);
		nextButton.addMouseListener(this);

		// nowButtonの生成
		nowButton = new IconButton(fc.getIconImage("now"), setting.getNowButtonBeforeBackColor(), setting.getNowButtonAfterBackColor(), "", "", 40, 20, 20, 20, true);
		nowButton.addMouseListener(this);

		// yearTextの生成
		yearText = new JTextField("", 5);
		yearText.setForeground(setting.getDefaultFontColor());
		yearText.setHorizontalAlignment(JTextField.RIGHT);
		yearText.addActionListener(this);

		// yearLabelの生成
		yearLabel = new DiaryLabel("年", 10, JLabel.LEFT, true);

		// monthTextの生成
		monthText = new JTextField("", 3);
		monthText.setForeground(setting.getDefaultFontColor());
		monthText.setHorizontalAlignment(JTextField.RIGHT);
		monthText.addActionListener(this);
		monthText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)e.getComponent()).selectAll();
			}
		});

		// monthLabelの生成
		monthLabel = new DiaryLabel("月", 10, JLabel.LEFT, true);

		// dayTextの生成
		dayText = new JTextField("", 3);
		dayText.setForeground(setting.getDefaultFontColor());
		dayText.setHorizontalAlignment(JTextField.RIGHT);
		dayText.addActionListener(this);
		dayText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)e.getComponent()).selectAll();
			}
		});

		// dayLabelの生成
		dayLabel = new DiaryLabel("日", 10, JLabel.LEFT, true);

		// 年月表示パネルの生成, 部品設置
		datePanel = new JPanel();
		datePanel.setOpaque(false);
		datePanel.setLayout(new GridLayout(2, 1));
		JPanel dateTopPanel = new JPanel();
		dateTopPanel.setOpaque(false);
		JPanel dateBottomPanel = new JPanel();
		dateBottomPanel.setOpaque(false);
		datePanel.add(dateTopPanel);
		datePanel.add(dateBottomPanel);
		dateTopPanel.add(yearText);
		dateTopPanel.add(yearLabel);
		dateBottomPanel.add(monthText);
		dateBottomPanel.add(monthLabel);

		if(dayShow) {
			dateBottomPanel.add(dayText);
			dateBottomPanel.add(dayLabel);
		}

		// CardLayoutの設定
		titleCard = new CardLayout();

		this.setOpaque(false);
		this.setLayout(titleCard);
	}

	/**
	 * 通常の画面にする
	 */
	public void showBeforePanel() {
		JPanel titleTmp = new JPanel();
		titleTmp.setOpaque(false);

		titleTmp.add(firstButton);
		titleTmp.add(preButton);
		titleTmp.add(datePanel);
		titleTmp.add(nextButton);
		titleTmp.add(nowButton);
		this.add("before", titleTmp);
		titleCard.show(this, "before");

		// 日付テキストの変更
		yearText.setBackground(Color.WHITE);
		yearText.setOpaque(true);
		yearText.setEditable(true);
		monthText.setBackground(Color.WHITE);
		monthText.setOpaque(true);
		monthText.setEditable(true);
		if(dayShow) {
			dayText.setBackground(Color.WHITE);
			dayText.setOpaque(true);
			dayText.setEditable(true);
		}
	}

	/**
	 * 操作できなくする
	 */
	public void showAfterPanel() {
		JPanel titleTmp = new JPanel();
		titleTmp.setOpaque(false);

		titleTmp.add(datePanel);
		this.add("after", titleTmp);
		titleCard.show(this, "after");

		// 日付テキストの変更
		yearText.setBackground(null);
		yearText.setOpaque(false);
		yearText.setEditable(false);
		monthText.setBackground(null);
		monthText.setOpaque(false);
		monthText.setEditable(false);
		if(dayShow) {
			dayText.setBackground(null);
			dayText.setOpaque(false);
			dayText.setEditable(false);
		}
	}

	/**
	 * 最初ボタンを返す
	 * @return 最初ボタン
	 */
	public IconButton getFirstButton() {
		return firstButton;
	}

	/**
	 * 前ボタンを返す
	 * @return 前ボタン
	 */
	public IconButton getPreButton() {
		return preButton;
	}

	/**
	 * 次ボタンを返す
	 * @return 次ボタン
	 */
	public IconButton getNextButton() {
		return nextButton;
	}

	/**
	 * 現在ボタンを返す
	 * @return 現在ボタン
	 */
	public IconButton getNowButton() {
		return nowButton;
	}

	/**
	 * 年テキストフィールドを返す
	 * @return 年テキストフィールド
	 */
	public JTextField getYearText() {
		return yearText;
	}

	/**
	 * 月テキストフィールドを返す
	 * @return 月テキストフィールド
	 */
	public JTextField getMonthText() {
		return monthText;
	}

	/**
	 * 日テキストフィールドを返す
	 * @return 日テキストフィールド
	 */
	public JTextField getDayText() {
		return dayText;
	}

	/**
	 * 年を返す
	 * @return 年
	 */
	public int getYear() {
		return year;
	}

	/**
	 * 月を返す
	 * @return 月
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * 日を返す
	 * @return 日
	 */
	public int getDay() {
		return day;
	}

	/**
	 * 日付をセットします
	 * @param year 年
	 * @param month 月
	 * @return 日付
	 */
	public boolean setDate(int year, int month) {
		this.year = year;
		this.month = month;

		if(checkDate()) {
			setDateText();
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 日付をセットします
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 日付
	 */
	public boolean setDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;

		if(checkDate()) {
			setDateText();
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 前の月
	 */
	public void preMonth() {
		month--;
		decrement();
		checkDate();
		setDateText();
	}

	/**
	 * 次の月
	 */
	public void nextMonth() {
		month++;
		increment();
		checkDate();
		setDateText();
	}

	/**
	 * 前の日
	 */
	public void preDay() {
		day--;
		decrement();
		checkDate();
		setDateText();
	}

	/**
	 * 次の日
	 */
	public void nextDay() {
		day++;
		increment();
		checkDate();
		setDateText();
	}

	/**
	 * 日付チェック
	 */
	private boolean checkDate() {
		boolean result = true;

		// グレゴリオ暦(1582年10月15日制定)の範囲外
		if(year < 1582 || (year < 1583 && month < 11)) {
			year = 1582;
			month = 11;
			day = 1;
			result = false;
		}

		// 9999年以降は対応しない
		if(year > 9999) {
			year = 9999;
			month = 12;
			day = 31;
			result = false;
		}

		if(month < 1 || 12 < month) {
			result = false;
		}

		if(day < 1 || MyCalendar.getLastDay(year, month) < day) {
			result = false;
		}

		return result;
	}

	/**
	 * 日付テキストのセット
	 */
	private void setDateText() {
		yearText.setText(Integer.toString(year));
		monthText.setText(Integer.toString(month));
		dayText.setText(Integer.toString(day));

		setFocus();
	}

	/**
	 * 日付のインクリメント
	 */
	private void increment() {
		if(dayShow && day >= MyCalendar.getLastDay(year, month) + 1) {
			month++;
			day = 1;
		}

		if(month >= 13) {
			year++;
			month = 1;
		}
	}

	/**
	 * 日付のデクリメント
	 */
	private void decrement() {
		boolean change_m = false;

		// この時の日は年月が確定しないと決められないため確定次第決める
		if(dayShow && day <= 0) {
			change_m = true;
			month--;
		}

		if(month <= 0) {
			year--;
			month = 12;
		}

		if(dayShow && change_m) {
			day = MyCalendar.getLastDay(year, month);
		}
	}

	/**
	 * マウス押下状況をセットする<br>
	 * 呼び出し元で前ボタン、次ボタン押下中にエラーが発生した場合に処理を行うようにする
	 * @param press マウス押下状況
	 */
	public void setPress(boolean press) {
		this.press = press;
	}

	/**
	 * フォーカスをセットする
	 */
	public void setFocus() {
		yearText.setCaretPosition(4);
		yearText.requestFocusInWindow();
	}

	/**
	 * コンポーネントの更新
	 */
	public void updateComponents() {
		firstButton.updateComponents();
		preButton.updateComponents();
		nextButton.updateComponents();
		nowButton.updateComponents();
		yearText.setFont(setting.getFont(10));
		yearLabel.updateComponents();
		monthText.setFont(setting.getFont(10));
		monthLabel.updateComponents();
		if(dayShow) {
			dayText.setFont(setting.getFont(10));
			dayLabel.updateComponents();
		}
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	private void processing(Object source) {
		// 今日の日付の取得
		todayDate = MyCalendar.getToday(dayShow);

		if(source == firstButton) {
			if(setFirst()) {
				firstButtonProcessing();
			}
		}

		else if(source == preButton) {
			preButtonProcessing();
		}

		else if(source == nextButton) {
			nextButtonProcessing();
		}

		else if(source == nowButton) {
			if(resetDate()) {
				nowButtonProcessing();
			}
		}

		else if(source == yearText || source == monthText || source == dayText) {
			readText();
			enterProcessing();
		}
	}

	/**
	 * テキストから日付を読み込んでセットする
	 */
	private void readText() {
		try {
			// テキストから年月を取得
			int tmp_y = Integer.parseInt(this.yearText.getText().replaceAll("　", "").trim());
			int tmp_m = Integer.parseInt(this.monthText.getText().replaceAll("　", "").trim());
			int tmp_d = Integer.parseInt(this.dayText.getText().replaceAll("　", "").trim());

			// 日付がおかしい場合
			if(!setDate(tmp_y, tmp_m, tmp_d)) {
				resetDate();
			}
		}

		// 数字以外が入力された
		catch(Exception error) {
			// 現在の年月を表示させる
			resetDate();

			error.printStackTrace();
		}
	}

	/**
	 * 最初ボタンが押されたときの処理
	 */
	protected void firstButtonProcessing() {

	}

	/**
	 * 前ボタンが押されたときの処理
	 */
	protected void preButtonProcessing() {

	}

	/**
	 * 次ボタンが押されたときの処理
	 */
	protected void nextButtonProcessing() {

	}

	/**
	 * 現在ボタンが押されたときの処理
	 */
	protected void nowButtonProcessing() {

	}

	/**
	 * 日付テキストにおいてEnterキーが押されたときの処理
	 */
	protected void enterProcessing() {

	}

	/**
	 * 最初の日記が存在する月（日）にする
	 */
	private boolean setFirst() {
		Date date = fc.getFirstDiary();
		if(date != null) {
			try {
				if(date.getYear() == year && date.getMonth() == month && ((dayShow) ? date.getDay() == day : true)) {
					return false;
				}
				setDate(date.getYear(), date.getMonth(), (dayShow) ? date.getDay() : 1);
			}
			catch(Exception error) {
				resetDate();

				error.printStackTrace();
			}
		}
		else {
			new YesDialog(df, "『" + setting.getTitle() + "』が存在しません");
			return false;
		}

		return true;
	}

	/**
	 * 日付のリセット
	 */
	private boolean resetDate() {
		try {
			if(todayDate.getYear() == year && todayDate.getMonth() == month && ((dayShow) ? todayDate.getDay() == day : true)) {
				return false;
			}
			setDate(todayDate.getYear(), todayDate.getMonth(), todayDate.getDay());
		}
		catch(Exception error) {
			error.printStackTrace();
		}

		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		processing(e.getSource());
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
		if(SwingUtilities.isLeftMouseButton(e)) {
			if(preButton.pressed(e) || nextButton.pressed(e)) {
				processing(e.getSource());
				if(press != true) {
					press = true;
					new Thread(new Runnable() {
						@Override
						public void run() {
							int count = 0;

							while(press == true) {
								try {
									Thread.sleep(1);
									count++;
								}
								catch(Exception error) {
									error.printStackTrace();
								}
								if(count >= 500 && (count - 500) == setting.getScreenTransition()) {
									processing(e.getSource());
									count = 500;
								}
							}
						}
					}).start();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			press = false;
			if(firstButton.pressed(e) || nowButton.pressed(e)) {
				processing(e.getSource());
			}
		}
	}
}
