package diary.panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import diary.button.IconButton;
import diary.dialog.DiaryDeleteDialog;
import diary.dialog.DropImageDialog;
import diary.dialog.WritingDialog;
import diary.dialog.YesDialog;
import diary.label.DiaryLabel;
import diary.panel.parts.DateControlPanel;
import diary.panel.parts.DiaryPanel;
import diary.panel.parts.ImagePanel;
import diary.panel.parts.TransparentPanel;
import diary.system.calendar.Date;
import diary.system.calendar.MyCalendar;

/**
 * 日記編集画面
 * @author Masato Suzuki
 */
public class DiaryWriter extends DiaryPanel implements ActionListener, MouseListener, KeyListener, ChangeListener {
	/**
	 * 背景画像が描画される
	 */
	private ImagePanel diaryWriterPanel;

	/**
	 * 日記本文
	 */
	private JTextArea diaryText;

	/**
	 * 日付操作パネル
	 */
	private DateControlPanel titlePanel;

	/**
	 * 編集パネル
	 */
	private TransparentPanel writePanel;

	/**
	 * 曜日ラベル
	 */
	private DiaryLabel dayOfTheWeekLabel;

	/**
	 * 文字ラベル
	 */
	private DiaryLabel charLabel;

	/**
	 * 文字数ラベル
	 */
	private DiaryLabel charCountLabel;

	/**
	 * 戻るボタン
	 */
	private IconButton backButton;

	/**
	 * 編集ボタン
	 */
	private IconButton writeButton;

	/**
	 * 写真表示ボタン
	 */
	private IconButton pictureButton;

	/**
	 * 削除ボタン
	 */
	private IconButton deleteButton;

	/**
	 * 保存ボタン
	 */
	private IconButton saveButton;

	/**
	 * 写真追加ボタン
	 */
	private IconButton addPictureButton;

	/**
	 * 表示する日付
	 */
	private int year, month, day;

	/**
	 * 編集中かどうか<br>
	 * 編集中：true<br>
	 * 編集中でない：false
	 */
	private boolean writing = true;

	/**
	 * 編集パネルのカードレイアウト
	 */
	private CardLayout writeCard;

	/**
	 * 編集開始時のテキストエリアの値
	 */
	private String startText;

	/**
	 * ポップアップメニュー
	 */
	private JPopupMenu popup;

	/**
	 * ポップアップメニューの切り取り
	 */
	private JMenuItem cutMenuItem;

	/**
	 * ポップアップメニューのコピー
	 */
	private JMenuItem copyMenuItem;

	/**
	 * ポップアップメニューの貼り付け
	 */
	private JMenuItem pasteMenuItem;

	/**
	 * ポップアップメニューの全て貼り付け
	 */
	private JMenuItem selectAllMenuItem;

	/**
	 * スクロールパネル
	 */
	private JScrollPane scrollpane;

	/**
	 * 編集可能か<br>
	 * 可能：true<br>
	 * 不可能：false
	 */
	private boolean canWrite = true;

	/**
	 * マウスが押されているか
	 */
	private boolean press = false;

	/**
	 * 再描画時間
	 */
	private int repaint;

	/**
	 * 再描画処理時間<br>
	 * 実行環境のスペックによって最適な値は異なると考えられる
	 */
	private final int wait = 100000000;

	/**
	 * クリップボードに情報があるかどうかの判定に用いる
	 */
	private JTextArea tmp;

	/**
	 * 下部のボタン位置調整用のラベル
	 */
	private JLabel nullLabel;

	/**
	 * コンストラクタ
	 */
	public DiaryWriter() {
		setSystem();

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		this.setLayout(new GridLayout(1, 1));
		this.add(getDiaryWriterPanel());

		updateComponents();

		// 初期の編集状況
		changeWrite();
	}

	/**
	 * 必要なものを生成する
	 */
	private void setSystem() {
		popup = new JPopupMenu();

		cutMenuItem = new JMenuItem("切り取り");
		cutMenuItem.setForeground(setting.getDefaultFontColor());
		cutMenuItem.addActionListener(this);
		copyMenuItem = new JMenuItem("コピー");
		copyMenuItem.setForeground(setting.getDefaultFontColor());
		copyMenuItem.addActionListener(this);
		pasteMenuItem = new JMenuItem("貼り付け");
		pasteMenuItem.setForeground(setting.getDefaultFontColor());
		pasteMenuItem.addActionListener(this);
		selectAllMenuItem = new JMenuItem("すべて選択");
		selectAllMenuItem.setForeground(setting.getDefaultFontColor());
		selectAllMenuItem.addActionListener(this);

		popup.add(cutMenuItem);
		popup.add(copyMenuItem);
		popup.add(pasteMenuItem);
		popup.add(selectAllMenuItem);
	}

	/**
	 * 日記編集パネルを返す
	 * @return 日記編集パネル
	 */
	private JPanel getDiaryWriterPanel() {
		diaryWriterPanel = new ImagePanel();

		// 全体的にBorderLayout
		diaryWriterPanel.setLayout(new BorderLayout());
		diaryWriterPanel.setOpaque(true);
		diaryWriterPanel.setBackground(setting.getDefaultBackColor());

		diaryWriterPanel.add(getTitlePanel(), BorderLayout.NORTH);
		diaryWriterPanel.add(getDiaryPanel(), BorderLayout.CENTER);
		diaryWriterPanel.add(getWritePanel(), BorderLayout.SOUTH);

		return diaryWriterPanel;
	}

	/**
	 * 日付コントロール部分のパネルを返す(部品設置は別メソッド)
	 * @return 日付コントロールパネル
	 */
	private JPanel getTitlePanel() {
		// titlePanelの生成, 設置（thisのNORTH）
		titlePanel = new DateControlPanel(df, true)  {
			@Override
			protected void firstButtonProcessing() {
				// 日記のセット
				changeDay();
			}

			@Override
			protected void preButtonProcessing() {
				titlePanel.preDay();

				// 日記のセット
				changeDay();
			}

			@Override
			protected void nextButtonProcessing() {
				titlePanel.nextDay();

				// 日記のセット
				changeDay();
			}

			@Override
			protected void nowButtonProcessing() {
				// 日記のセット
				changeDay();
			}

			@Override
			protected void enterProcessing() {
				// 日記のセット
				changeDay();
			}
		};
		titlePanel.showBeforePanel();

		// firstButtonの設定
		titlePanel.getFirstButton().setText("最初の日");
		titlePanel.getFirstButton().setToolTipText("最初の『" + setting.getTitle() + "』を表示します");

		// preButtonの設定
		titlePanel.getPreButton().setText("前 の 日");
		titlePanel.getPreButton().setToolTipText("前の日の『" + setting.getTitle() + "』を表示します");

		// nextButtonの設定
		titlePanel.getNextButton().setText("次 の 日");
		titlePanel.getNextButton().setToolTipText("次の日の『" + setting.getTitle() + "』を表示します");

		// nowButtonの設定
		titlePanel.getNowButton().setText("今   日");
		titlePanel.getNowButton().setToolTipText("今日の『" + setting.getTitle() + "』を表示します");

		return titlePanel;
	}

	/**
	 * 日記表示部分のパネルを返す
	 * @return 日記表示パネル
	 */
	private JPanel getDiaryPanel() {
		// diaryTextの生成, スクロールバーの設置等
		diaryText = new JTextArea();
		diaryText.setForeground(setting.getDefaultFontColor());
		diaryText.setMargin(new Insets(setting.getSize(5), setting.getSize(5), setting.getSize(5), setting.getSize(5)));
		diaryText.setLineWrap(true); // 文字列の折り返し
		diaryText.setWrapStyleWord(false); // 単語ごとに折り返し
		diaryText.addMouseListener(this);
		diaryText.addKeyListener(this);
		scrollpane = new JScrollPane(diaryText){
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(255, 255, 255, 100));
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
		scrollpane.setOpaque(false);
		scrollpane.setBorder(new LineBorder(Color.BLACK, 1, false));
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpane.getViewport().addChangeListener(this);
		scrollpane.getVerticalScrollBar().addMouseListener(this);

		// 曜日&文字数パネル
		JPanel dayOfTheWeekPanel = new JPanel();
		dayOfTheWeekPanel.setOpaque(false);
		dayOfTheWeekPanel.setLayout(new GridLayout(1, 3));
		dayOfTheWeekPanel.add(new JLabel(""));
		dayOfTheWeekLabel = new DiaryLabel("", 10, JLabel.CENTER, true);
		dayOfTheWeekPanel.add(dayOfTheWeekLabel);
		JPanel charCountPanel = new JPanel();
		charCountPanel.setOpaque(false);
		charCountPanel.setLayout(new GridLayout(1, 2));
		dayOfTheWeekPanel.add(charCountPanel);
		charCountLabel = new DiaryLabel("", 10, JLabel.RIGHT, true);
		charCountPanel.add(charCountLabel);
		charLabel = new DiaryLabel("文字", 10, JLabel.LEFT, true);
		charCountPanel.add(charLabel);

		// diaryPanelの生成, 設置（thisのCENTER）
		TransparentPanel diaryPanel = new TransparentPanel(80);
		diaryPanel.setOpaque(false);
		diaryPanel.setLayout(new BorderLayout());

		diaryPanel.add(new JLabel("　　"), BorderLayout.WEST);
		diaryPanel.add(new JLabel("　　"), BorderLayout.EAST);
		diaryPanel.add(dayOfTheWeekPanel, BorderLayout.NORTH);
		diaryPanel.add(scrollpane, BorderLayout.CENTER);

		return diaryPanel;
	}

	/**
	 * 下部のボタンパネルを返す
	 * @return ボタンパネル
	 */
	private JPanel getWritePanel() {
		// backButtonの生成
		backButton = new IconButton(fc.getIconImage("back"), setting.getBackButtonBeforeBackColor(), setting.getBackButtonAfterBackColor(), "戻   る", "前の画面に戻ります", 40, 20, 20, 20, true);
		backButton.addMouseListener(this);
		iconButton.add(backButton);

		// writeButtonの生成
		writeButton = new IconButton(fc.getIconImage("write"), setting.getWriteButtonBeforeBackColor(), setting.getWriteButtonAfterBackColor(), "編   集", "『" + setting.getTitle() + "』を編集します", 40, 20, 30, 30, true);
		writeButton.addMouseListener(this);
		iconButton.add(writeButton);

		// pictureButtonの生成
		pictureButton = new IconButton(fc.getIconImage("picture"), setting.getPictureButtonBeforeBackColor(), setting.getPictureButtonAfterBackColor(), "写真表示", "", 40, 20, 30, 30, true);
		pictureButton.addMouseListener(this);
		iconButton.add(pictureButton);

		// nullLabelの生成
		nullLabel = new JLabel();

		// deleteButtonの生成
		deleteButton = new IconButton(fc.getIconImage("delete"), setting.getDeleteButtonBeforeBackColor(), setting.getDeleteButtonAfterBackColor(), "削   除", "『" + setting.getTitle() + "』を削除します", 40, 20, 10, 10, true);
		deleteButton.addMouseListener(this);
		iconButton.add(deleteButton);

		// saveButtonの生成
		saveButton = new IconButton(fc.getIconImage("save"), setting.getSaveButtonBeforeBackColor(), setting.getSaveButtonAfterBackColor(), "保   存", "『" + setting.getTitle() + "』を保存します", 40, 20, 10, 10, true);
		saveButton.addMouseListener(this);
		iconButton.add(saveButton);

		// addPictureButtonの生成
		addPictureButton = new IconButton(fc.getIconImage("addPicture"), setting.getAddPictureButtonBeforeBackColor(), setting.getAddPictureButtonAfterBackColor(), "写真保存", "写真を保存します", 40, 20, 30, 30, true);
		addPictureButton.addMouseListener(this);
		iconButton.add(addPictureButton);

		// CardLayoutの設定
		writeCard = new CardLayout();
		writePanel = new TransparentPanel(80);
		writePanel.setOpaque(false);
		writePanel.setLayout(writeCard);

		return writePanel;
	}

	/**
	 * 表示する日付の変更
	 */
	private void changeDay() {
		year = titlePanel.getYear();
		month = titlePanel.getMonth();
		day = titlePanel.getDay();

		scrollRepaint();

		changeImage();

		dayOfTheWeekLabel.setText("(" + MyCalendar.getDayOfTheWeek(MyCalendar.getOffset(year, month, day)) + ")");

		pictureButton.setToolTipText(year + "年" + month + "月" + day + "日の写真を表示します");

		int result = fc.read(year, month, day, diaryText);

		startText = diaryText.getText();

		changeCharCount();

		// カーソルを先頭に
		diaryText.setCaretPosition(0);

		if(result == 0) {
			canWrite = true;
		}
		else if(result == -1) {
			canWrite = false;
			titlePanel.setPress(false);
			new YesDialog(df, "この『" + setting.getTitle() + "』は文字化けしています<br>" + setting.getCharCode() + "で読み込めません<br>設定から文字コードを変更するか、<br>ファイルを" + setting.getCharCode() + "に変換してください");
		}
	}

	/**
	 * 表示する日記の日付の変更
	 * @param year 表示する日記の年
	 * @param month 表示する日記の月
	 * @param day 表示する日記の日
	 */
	public void changeDay(int year, int month, int day) {
		titlePanel.setDate(year, month, day);
		changeDay();
	}

	/**
	 * 表示する日付の変更
	 * @param date 表示する日記の日付
	 */
	public void changeDay(Date date) {
		changeDay(date.getMonth(), date.getMonth(), date.getDay());
	}

	/**
	 * 日記の保存
	 */
	public void save() {
		if(!this.startText.equals(diaryText.getText())) {
			fc.save(year, month, day, diaryText);
			startText = diaryText.getText();
		}
		changeWrite();
	}

	/**
	 * 編集状況の切り替え
	 */
	public void changeWrite() {
		// 日付を空白にした場合の対策
		titlePanel.setDate(year, month, day);

		JPanel writeTmp = new JPanel();
		writeTmp.setOpaque(false);

		// 編集終了にする
		if(writing == true) {
			// タイトルパネル
			titlePanel.showBeforePanel();

			// 編集パネル
			writeTmp.add(nullLabel);
			writeTmp.add(backButton);
			writeTmp.add(writeButton);
			writeTmp.add(pictureButton);
			writeTmp.add(deleteButton);
			writePanel.add("beforeWrite", writeTmp);
			writeCard.show(writePanel, "beforeWrite");

			// 日記テキストの変更
			diaryText.setBackground(new Color(255, 255, 255, 0));
			diaryText.setEditable(false);
		}

		// 編集開始にする
		else {
			// タイトルパネル
			titlePanel.showAfterPanel();

			// 編集パネル
			writeTmp.add(backButton);
			writeTmp.add(saveButton);
			writeTmp.add(addPictureButton);
			writePanel.add("writing", writeTmp);
			writeCard.show(writePanel, "writing");

			// 日記テキストの変更
			diaryText.setBackground(new Color(255, 255, 255));
			diaryText.setEditable(true);

			// カーソルを先頭に
			diaryText.setCaretPosition(0);
			diaryText.requestFocusInWindow();
		}

		scrollpane.getViewport().setViewPosition(new Point(0, 0));

		// 編集状況切り替え
		writing = !writing;
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
		diaryText.setFont(setting.getFont(0));
		cutMenuItem.setFont(setting.getFont(0));
		copyMenuItem.setFont(setting.getFont(0));
		pasteMenuItem.setFont(setting.getFont(0));
		selectAllMenuItem.setFont(setting.getFont(0));
		dayOfTheWeekLabel.updateComponents();
		charCountLabel.updateComponents();
		charLabel.updateComponents();
		backButton.updateComponents();
		writeButton.updateComponents();
		pictureButton.updateComponents();
		deleteButton.updateComponents();
		saveButton.updateComponents();
		addPictureButton.updateComponents();

		nullLabel.setPreferredSize(deleteButton.getPreferredSize());
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
				File diaryImageFile = fc.getDiaryImageFile(year, month, day);
				if(diaryImageFile != null && setting.getShowDiaryImage()) {
					image = fc.getDiaryImage(diaryImageFile);
				}
				else {
					diaryImageFile = null;
					if(fc.getDiaryWriteBackgroundExists()) {
						image = fc.getDiaryWriteBackgroundImage();
					}
					else if(fc.getDefaultBackgroundExists()) {
						image = fc.getDefaultBackgroundImage();
					}
				}

				// この処理が正しい日付で行われているか（スレッド開始時の番号と現在の番号が一致しているか）
				if(threadNum == backgroundCount) {
					if((image.getWidth() < 400 || image.getHeight() < 400) && diaryImageFile == null) {
						// このときの第2引数は例外（気にしない）
						diaryWriterPanel.setImage(image, true, true);
					}
					else {
						diaryWriterPanel.setImage(image, false, false);
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
		diaryWriterPanel.removeBackgroundImage();
	}

	/**
	 * 日記画面の再描画
	 */
	public void scrollRepaint() {
		if(scrollpane != null) {
			if(repaint == 0) {
				repaint = wait;
				new Thread(new Runnable() {
					@Override
					public void run() {
						while(repaint != 0) {
							repaint--;
							scrollpane.repaint();
						}
					}
				}).start();
			}
			else {
				repaint = wait;
			}
		}
	}

	/**
	 * テキストリセット
	 */
	public void resetText() {
		diaryText.setText("");
		startText = "";
		charCountLabel.setText("0");
	}

	/**
	 * 編集状況を返す
	 * @return 編集状況<br>
	 * 編集中ならtrue<br>
	 * そうでないならfalse
	 */
	public boolean getWriteState() {
		return writing;
	}

	/**
	 * テキストエリアが変更されたかを返す
	 */
	public boolean getTextChange() {
		if(startText.replaceAll("　", "").trim().length() > 0) {
			return !diaryText.getText().equals(startText);
		}
		else {
			return !diaryText.getText().replaceAll("　", "").trim().equals(startText);
		}
	}

	/**
	 * 文字数をセットする（改行文字、空白を除く）
	 */
	public void changeCharCount() {
		charCountLabel.setText(Integer.toString(diaryText.getText().replaceAll("　", "").replaceAll(" ", "").replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "").trim().length()));
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	private void processing(Object source) {
		// 戻る
		if(source == backButton){
			if(getWriteState() && getTextChange()) {
				new WritingDialog(this, 1);
			}
			else {
				df.back();
			}
		}

		// 編集
		else if(source == writeButton) {
			if(canWrite == true) {
				changeWrite();
			}
			else {
				new YesDialog(df, "文字化けにより編集を開始できません");
			}
		}

		// 写真
		else if(source == pictureButton) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(fc.getDiaryImageExists(year, month, day)) {
						try {
							Desktop.getDesktop().open(fc.getDiaryImageFile(year, month, day));
						}
						catch(Exception error) {
							error.printStackTrace();
						}
					}
					else {
						new YesDialog(df, year + "年" + month + "月" + day + "日の写真はありません");
					}
				}
			}).start();
		}

		// 削除
		else if(source == deleteButton) {
			if(fc.getDiaryTextExists(year, month, day) || fc.getDiaryImageExists(year, month, day)) {
				new DiaryDeleteDialog(this, year, month, day);
			}
			else {
				new YesDialog(df, "削除するものがありません");
			}
		}

		// 保存
		else if(source == saveButton) {
			save();
		}

		// 写真保存
		else if(source == addPictureButton) {
			new DropImageDialog(this, year, month, day);
		}

		// 切り取り
		else if(source == cutMenuItem) {
			diaryText.cut();
		}

		// コピー
		else if(source == copyMenuItem) {
			diaryText.copy();
		}

		// 貼り付け
		else if(source == pasteMenuItem) {
			diaryText.paste();
			changeCharCount();
		}

		// 全選択
		else if(source == selectAllMenuItem) {
			diaryText.selectAll();
		}

		if(!writing) {
			scrollRepaint();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 登録されたボタン以外
		if(!iconButton.contains(e.getSource())) {
			processing(e.getSource());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			if(e.getSource() == diaryText || e.getSource() == scrollpane.getVerticalScrollBar()) {
				if(!writing) {
					press = true;
					new Thread(new Runnable() {
						@Override
						public void run() {
							while(press == true) {
								scrollRepaint();
								try {
									Thread.sleep(wait - 100);
								}
								catch(Exception error) {
									error.printStackTrace();
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
		if(SwingUtilities.isRightMouseButton(e)) {
			if(e.getSource() == diaryText) {

				// 編集中の時
				if(writing) {
					// 切り取りできるか
					if(diaryText.getSelectedText() != null) {
						cutMenuItem.setEnabled(true);
					}
					else {
						cutMenuItem.setEnabled(false);
					}

					// 貼り付けできるか
					tmp = new JTextArea();
					tmp.paste();
					if(tmp.getText().replaceAll("　", "").replaceAll(" ", "").replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "").trim().length() != 0) {
						pasteMenuItem.setEnabled(true);
					}
					else {
						pasteMenuItem.setEnabled(false);
					}
				}

				else {
					// 編集前は使用不可
					cutMenuItem.setEnabled(false);
					pasteMenuItem.setEnabled(false);
				}

				// コピーできるか
				if(diaryText.getSelectedText() != null) {
					copyMenuItem.setEnabled(true);
				}
				else {
					copyMenuItem.setEnabled(false);
				}

				// 全て選択できるか
				if(diaryText.getText().replaceAll("　", "").replaceAll(" ", "").replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "").trim().length() != 0) {
					selectAllMenuItem.setEnabled(true);
				}
				else {
					selectAllMenuItem.setEnabled(false);
				}

				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		else if(SwingUtilities.isLeftMouseButton(e)) {
			if(iconButton.contains(e.getSource()) && iconButton.get(iconButton.indexOf(e.getSource())).pressed(e)) {
				processing(e.getSource());
			}

			else if(e.getSource() == diaryText || e.getSource() == scrollpane.getVerticalScrollBar()) {
				if(!writing) {
					press = false;
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(!writing) {
			scrollRepaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!writing) {
			scrollRepaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		changeCharCount();
		if(!writing) {
			scrollRepaint();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == scrollpane.getViewport()) {
			if(!writing) {
				scrollRepaint();
			}
		}
	}
}