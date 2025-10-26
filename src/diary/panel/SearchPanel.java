package diary.panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import diary.button.IconButton;
import diary.button.TextButton;
import diary.dialog.DeleteDialog;
import diary.dialog.WaitDialog;
import diary.dialog.YesDialog;
import diary.label.DiaryLabel;
import diary.label.TransparentLabel;
import diary.panel.parts.DiaryPanel;
import diary.panel.parts.ImagePanel;
import diary.system.calendar.MyCalendar;
import diary.system.search.SearchData;
import diary.system.search.SearchResult;

/**
 * 検索画面
 * @author Masato Suzuki
 */
public class SearchPanel extends DiaryPanel implements ActionListener, MouseListener, KeyListener {
	/**
	 * SearchPanelの本体<br>
	 * 背景画像が描画される
	 */
	private ImagePanel searchPanel;

	/**
	 * 検索結果表示パネル
	 */
	private JPanel resultShowPanel;

	/**
	 * ボタンパネル
	 */
	private JPanel buttonPanel;

	/**
	 * 検索文字列入力
	 */
	private JTextField searchText;

	/**
	 * カードレイアウト
	 */
	private CardLayout searchCard;

	/**
	 * カードレイアウト
	 */
	private CardLayout buttonCard;

	/**
	 * 検索ボタン
	 */
	private IconButton searchButton;

	//------------自分で使う時だけ---------------
	/**
	 * 削除ボタン
	 */
	private IconButton deleteButton;
	//-------------------------------------------

	/**
	 * 戻るボタン
	 */
	private IconButton backButton;

	/**
	 * ソート順ボタン
	 */
	private IconButton upDownSortButton;

	/**
	 * 日付ソートボタン
	 */
	private IconButton dateSortButton;

	/**
	 * 文字数ソートボタン
	 */
	private IconButton charSortButton;

	/**
	 * 検索結果ボタン
	 */
	private TextButton[] resultButton;

	/**
	 * メッセージラベル
	 */
	private DiaryLabel messageLabel;

	/**
	 * 検索結果ラベル
	 */
	private DiaryLabel resultLabel;

	/**
	 * 検索件数ラベル
	 */
	private DiaryLabel resultNumberLabel;

	/**
	 * 件ラベル
	 */
	private DiaryLabel numberLabel;

	/**
	 * 検索結果
	 */
	private SearchResult sr;

	/**
	 * 日記情報
	 */
	private SearchData[] searchData = null;

	/**
	 * 削除情報
	 */
	private SearchData[] deleteData = null;

	/**
	 * 検索結果のインデックス
	 */
	private int[][] sortIndex;

	/**
	 * ソートが昇順か<br>
	 * 昇順：true<br>
	 * 降順：false
	 */
	private boolean asc = false;

	/**
	 * ソートの種類<br>
	 * 日付順：0<br>
	 * 文字数順：1
	 */
	private int sort = -1;

	/**
	 * WaitDialogクラスのオブジェクト
	 */
	private WaitDialog wd;

	/**
	 * ダブルクリック防止
	 */
	private int doubleClick = 0;

	/**
	 * 検索中止の判別に用いる
	 */
	private boolean searchContinue;

	/**
	 * ボタン長押し防止<br>
	 * 検索ボタンを押しっぱなしで連続で検索される不具合の対策
	 */
	protected boolean press = false;

	/**
	 * コンストラクタ
	 */
	public SearchPanel() {
		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		this.setLayout(new GridLayout(1, 1));
		this.add(getSearchPanel());

		reset();

		updateComponents();

		focus();
	}

	/**
	 * 検索パネルを返す
	 * @return 検索パネル
	 */
	private JPanel getSearchPanel() {
		searchPanel = new ImagePanel();

		// 全体的にBorderLayout
		searchPanel.setLayout(new BorderLayout());
		searchPanel.setBackground(setting.getDefaultBackColor());
		searchPanel.setOpaque(true);

		searchPanel.add(getInputPanel(), BorderLayout.NORTH);
		searchPanel.add(new TransparentLabel("　　", 80), BorderLayout.WEST);
		searchPanel.add(getResultPanel(), BorderLayout.CENTER);
		searchPanel.add(new TransparentLabel("　　", 80), BorderLayout.EAST);
		searchPanel.add(getButtonPanel(), BorderLayout.SOUTH);

		return searchPanel;
	}

	/**
	 * 検索文字列入力パネルを返す
	 * @return 検索文字列入力パネル
	 */
	private JPanel getInputPanel() {
		/*
		 * パネルの深さ
		 * outInputPanel1→outInputPanel1Panel→outInputPanel2Panel→outInputPanel2→inputPanel{messageLabel,searchText,searchButtonPanel}
		 */

		messageLabel = new DiaryLabel("検索する文字列を入力して下さい", 0, JLabel.CENTER, true);

		searchText = new JTextField("", 40);
		searchText.setMargin(new Insets(setting.getSize(5), setting.getSize(5), setting.getSize(5), setting.getSize(5)));
		searchText.setForeground(setting.getDefaultFontColor());
		searchText.setHorizontalAlignment(JTextField.CENTER);
		searchText.addActionListener(this);
		searchText.addKeyListener(this);

		searchButton = new IconButton(fc.getIconImage("search"), setting.getSearchButtonBeforeBackColor(), setting.getSearchButtonAfterBackColor(), "検 索", "日記を検索します<br>何も入力されない場合は全ての日記を表示します", 20, 0, 0, 0, true);
		searchButton.addMouseListener(this);
		iconButton.add(searchButton);

		//----------------------自分で使う時だけ--------------------------------
		deleteButton = new IconButton(fc.getIconImage("delete"), setting.getDeleteButtonBeforeBackColor(), setting.getDeleteButtonAfterBackColor(), "削 除", "入力された文字を含む日記を全て削除します", 20, 0, 0, 0, true);
		deleteButton.addMouseListener(this);
		iconButton.add(deleteButton);
		//----------------------------------------------------------------------

		JPanel searchButtonPanel = new JPanel();
		searchButtonPanel.setOpaque(false);

		searchButtonPanel.add(searchButton);

		//----------------------自分で使う時だけ--------------------------------
		searchButtonPanel.add(deleteButton);
		//----------------------------------------------------------------------

		JPanel inputPanel = new JPanel();
		inputPanel.setOpaque(false);
		inputPanel.setLayout(new GridLayout(3, 1));

		inputPanel.add(messageLabel);
		inputPanel.add(searchText);
		inputPanel.add(searchButtonPanel);

		JPanel outInputPanel2 = new JPanel();
		outInputPanel2.setOpaque(false);
		outInputPanel2.setLayout(new BorderLayout());

		outInputPanel2.add(inputPanel, BorderLayout.CENTER);
		outInputPanel2.add(new JLabel("　　"), BorderLayout.EAST);
		outInputPanel2.add(new JLabel("　　"), BorderLayout.WEST);

		JPanel outInputPanel2Panel = new JPanel();
		outInputPanel2Panel.setBackground(setting.getBoxColor());
		outInputPanel2Panel.setLayout(new GridLayout(1, 1));
		outInputPanel2Panel.setBorder(new LineBorder(Color.BLACK, 1, false));
		outInputPanel2Panel.add(outInputPanel2);

		JPanel outInputPanel1Panel = new JPanel();
		outInputPanel1Panel.setOpaque(false);
		outInputPanel1Panel.add(outInputPanel2Panel);

		JPanel outInputPanel1 = new JPanel(){
			@Override protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setPaint(new Color(255, 255, 255, 80));
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.dispose();
				super.paintComponent(g);
			}
		};
		outInputPanel1.setOpaque(false);
		outInputPanel1.setLayout(new BorderLayout());

		outInputPanel1.add(outInputPanel1Panel, BorderLayout.CENTER);
		outInputPanel1.add(new JLabel("　　"), BorderLayout.NORTH);
		outInputPanel1.add(new JLabel("　　"), BorderLayout.SOUTH);

		return outInputPanel1;
	}

	/**
	 * 検索結果パネルを返す
	 * @return 検索結果パネル
	 */
	private JPanel getResultPanel() {
		/*
		 * パネルの深さ
		 * outResultPanel1→resultPanel{resultMessagePanel{resultLabel,resultNumberPanel{resultNumberLabel,numberLabel}},resultShowPanel}
		 */

		resultNumberLabel = new DiaryLabel("", 0, JLabel.RIGHT, true);

		numberLabel = new DiaryLabel("件", 0, JLabel.LEFT, true);

		resultLabel = new DiaryLabel("", 0, JLabel.CENTER, true);

		JPanel resultNumberPanel = new JPanel();
		resultNumberPanel.setOpaque(false);
		resultNumberPanel.setLayout(new GridLayout(1, 2));

		resultNumberPanel.add(resultNumberLabel);
		resultNumberPanel.add(numberLabel);

		JPanel resultMessagePanel = new JPanel();
		resultMessagePanel.setOpaque(false);
		resultMessagePanel.setLayout(new GridLayout(1, 3));

		resultMessagePanel.add(new JLabel(""));
		resultMessagePanel.add(resultLabel);
		resultMessagePanel.add(resultNumberPanel);

		searchCard = new CardLayout();

		JPanel resultFirstPanel = new JPanel();
		resultFirstPanel.setBorder(new LineBorder(Color.BLACK, 1, false));
		resultFirstPanel.setOpaque(false);

		resultShowPanel = new JPanel();
		resultShowPanel.setBackground(setting.getBoxColor());

		resultShowPanel.setLayout(searchCard);

		resultShowPanel.add("reset", resultFirstPanel);

		JPanel resultPanel = new JPanel();
		resultPanel.setOpaque(false);
		resultPanel.setLayout(new BorderLayout());

		resultPanel.add(resultMessagePanel, BorderLayout.NORTH);
		resultPanel.add(resultShowPanel, BorderLayout.CENTER);

		JPanel outResultPanel1 = new JPanel(){
			@Override protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setPaint(new Color(255, 255, 255, 80));
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.dispose();
				super.paintComponent(g);
			}
		};
		outResultPanel1.setOpaque(false);
		outResultPanel1.setLayout(new BorderLayout());

		outResultPanel1.add(resultPanel, BorderLayout.CENTER);
		outResultPanel1.add(new JLabel(""), BorderLayout.EAST);
		outResultPanel1.add(new JLabel(""), BorderLayout.WEST);

		return outResultPanel1;
	}

	/**
	 * 下部のボタンパネルを返す
	 * @return ボタンパネル
	 */
	private JPanel getButtonPanel() {
		// backButtonの生成
		backButton = new IconButton(fc.getIconImage("back"), setting.getBackButtonBeforeBackColor(), setting.getBackButtonAfterBackColor(), "戻   る", "前の画面に戻ります", 40, 20, 20, 20, true);
		backButton.addMouseListener(this);
		iconButton.add(backButton);

		// dateSortButtonの生成
		dateSortButton = new IconButton(fc.getIconImage("dateSort"), setting.getSortButtonBeforeBackColor(), setting.getSortButtonAfterBackColor(), "日 付 順", "日付順に並び替えます", 40, 20, 20, 20, true);
		dateSortButton.addMouseListener(this);
		iconButton.add(dateSortButton);

		// charSortButtonの生成
		charSortButton = new IconButton(fc.getIconImage("charSort"), setting.getSortButtonBeforeBackColor(), setting.getSortButtonAfterBackColor(), "文字数順", "文字数順に並び替えます", 40, 20, 20, 20, true);
		charSortButton.addMouseListener(this);
		iconButton.add(charSortButton);

		// upDownSortButtonの生成
		upDownSortButton = new IconButton(fc.getIconImage("upDownSort"), setting.getUpDownSortButtonBeforeBackColor(), setting.getUpDownSortButtonAfterBackColor(), "", "", 40, 20, 20, 20, true);
		upDownSortButton.addMouseListener(this);
		iconButton.add(upDownSortButton);

		buttonPanel = new JPanel(){
			@Override protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setPaint(new Color(255, 255, 255, 80));
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.dispose();
				super.paintComponent(g);
			}
		};
		buttonPanel.setOpaque(false);

		buttonCard = new CardLayout();

		buttonPanel.setLayout(buttonCard);

		return buttonPanel;
	}

	/**
	 * 検索
	 */
	private void search() {
		searchContinue = true;
		resultButton = null;
		// 検索結果の取得
		sr = fc.search(searchText.getText());
		searchData = sr.getSearchData();
		if(searchData != null) {
			if(searchData.length != 0) {
				sortIndex = sr.getsortIndex();
				resultButton = new TextButton[searchData.length];
				wd.setMax(searchData.length);
				for(int i = 0; i < searchData.length; i++) {
					if(searchContinue) {
						wd.add();
						setResultButton(i);
					}
					else {
						new YesDialog(df, "検索が中止されました");
						reset();
						return;
					}
				}
				showResult();
			}
			else {
				searchCard.show(resultShowPanel, "reset");
				resultNumberLabel.setText("0");
				wd.dispose();
				new YesDialog(df, "該当する『" + setting.getTitle() + "』はありません");
			}
		}
		else {
			searchCard.show(resultShowPanel, "reset");
			resultNumberLabel.setText("0");
			wd.dispose();
			new YesDialog(df, "該当する『" + setting.getTitle() + "』はありません");
		}

		sr = null;
		searchContinue = false;
	}

	/**
	 * 検索結果ボタンの設定
	 * @param i 検索結果ボタンのインデックス
	 */
	private void setResultButton(int i) {
		resultButton[i] = new TextButton("【" + searchData[i].getDate().getShowDate() + "(" + MyCalendar.getDayOfTheWeek(MyCalendar.getOffset(searchData[i].getDate())) + ")】" + searchData[i].getCharCount() + "文字" + searchData[i].getText(), true);
		resultButton[i].setToolTipText(searchData[i].getDate().getShowDate().replaceAll(" ", "") + "の『" + setting.getTitle() + "』を表示します");
		resultButton[i].addActionListener(this);
	}

	/**
	 * 検索画面の表示
	 */
	private void showResult() {
		JPanel tmp = new JPanel();
		tmp.setOpaque(false);
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.Y_AXIS));
		JScrollPane scrollpane = new JScrollPane(tmp);
		scrollpane.getVerticalScrollBar().setUnitIncrement(15);
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		resultNumberLabel.setText(Integer.toString(searchData.length));

		if(asc) {
			for(int i = 0; i < searchData.length; i++) {
				tmp.add(resultButton[sortIndex[sort][i]]);
			}
		}
		else {
			for(int i = searchData.length - 1; i >= 0; i--) {
				tmp.add(resultButton[sortIndex[sort][i]]);
			}
		}

		resultShowPanel.add("result", scrollpane);
		searchCard.show(resultShowPanel, "result");
	}

	//-------------自分で使う時のみ------------
	/**
	 * 削除
	 */
	public void delete() {
		if(deleteData != null && deleteData.length > 0) {
			int count = deleteData.length;
			if(fc.delete(deleteData)) {
				new YesDialog(df, count + "件の『" + setting.getTitle() + "』を削除しました");
				searchCard.show(resultShowPanel, "reset");
			}
			else {
				new YesDialog(df, "『" + setting.getTitle() + "』を削除できませんでした");
			}
		}
		deleteData = null;
	}
	//-----------------------------------------

	/**
	 * コンポーネントの更新
	 */
	public void updateComponents() {
		messageLabel.updateComponents();
		searchText.setFont(setting.getFont(0));
		searchButton.setFont(setting.getFont(0));
		deleteButton.setFont(setting.getFont(0));
		resultLabel.updateComponents();
		resultNumberLabel.updateComponents();
		numberLabel.updateComponents();
		searchButton.updateComponents();
		deleteButton.updateComponents();
		backButton.updateComponents();
		dateSortButton.updateComponents();
		charSortButton.updateComponents();
		upDownSortButton.updateComponents();
	}

	/**
	 * 背景画像の変更
	 */
	public void changeImage() {
		backgroundCount++;

		Thread backgroundThread = new Thread(new Runnable() {
			private int threadNum = backgroundCount;
			@Override
			public void run() {
				BufferedImage image = null;

				removeBackgroundImage();
				if(fc.getSearchBackgroundExists()) {
					image = fc.getSearchBackgroundImage();
				}
				else if(fc.getDefaultBackgroundExists()) {
					image = fc.getDefaultBackgroundImage();
				}

				// この処理が正しい日付で行われているか（スレッド開始時の番号と現在の番号が一致しているか）
				if(threadNum == backgroundCount) {
					if(image.getWidth() < 400 || image.getHeight() < 400) {
						// このときの第2引数は例外（気にしない）
						searchPanel.setImage(image, true, true);
					}
					else {
						searchPanel.setImage(image, false, false);
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
		searchPanel.removeBackgroundImage();
	}

	/**
	 * フレームの初期フォーカス
	 */
	public void focus() {
		// 日記にフォーカスを当てる
		searchText.requestFocusInWindow();
	}

	/**
	 * 昇順降順の変更
	 */
	private void changeUpDownSort() {
		asc = !asc;

		String text = "検索結果(";
		if(sort == 0) {
			text += "日付順";
		}
		else if(sort == 1) {
			text += "文字数順";
		}

		if(asc) {
			if(!setting.getShowIcon()) {
				upDownSortButton.setText("降 順 に");
			}

			text += "昇順";

			upDownSortButton.setToolTipText(setting.getDefaultToolTipText("ソート順を降順に変更します"));
		}
		else {
			if(!setting.getShowIcon()) {
				upDownSortButton.setText("昇 順 に");
			}

			text += "降順";

			upDownSortButton.setToolTipText(setting.getDefaultToolTipText("ソート順を昇順に変更します"));
		}

		resultLabel.setText(text + ")");

		if(searchData != null) {
			showResult();
		}
	}

	/**
	 * 並び替えの変更
	 */
	private void changeSort() {
		String text = "検索結果(";

		sort++;
		if(sort == 2) {
			sort = 0;
		}

		JPanel tmp = new JPanel();
		tmp.setOpaque(false);

		tmp.add(backButton);
		if(sort == 0) {
			tmp.add(charSortButton);

			text += "日付順";
		}
		else if(sort == 1){
			tmp.add(dateSortButton);

			text += "文字数順";
		}

		if(asc) {
			text += "昇順";
		}
		else {
			text += "降順";
		}

		resultLabel.setText(text + ")");

		tmp.add(upDownSortButton);

		buttonPanel.add("buttonPanel", tmp);
		buttonCard.show(buttonPanel, "buttonPanel");

		if(searchData != null) {
			showResult();
		}
	}

	/**
	 * 初期画面へ変更
	 */
	public void reset() {
		searchText.setText("");
		sr = null;
		searchData = null;
		sortIndex = null;
		if(asc == false) {
			changeUpDownSort();
		}
		if(sort != 0) {
			sort = -1;
			changeSort();
		}
		resultLabel.setText("検索結果(日付順昇順)");
		upDownSortButton.setToolTipText(setting.getDefaultToolTipText("ソート順を降順に変更します"));
		resultNumberLabel.setText("0");
		searchCard.show(resultShowPanel, "reset");
	}

	/**
	 * 検索を中止する
	 */
	public void searchStop() {
		searchContinue = false;
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	private void processing(Object source) {
		// 検索
		if(source == searchButton || source == searchText) {
			if(source == searchText) {
				press = true;
			}

			if(doubleClick == 0) {
				doubleClick = 1;
				wd = null;
				new Thread(new Runnable() {
					@Override
					public void run() {
						df.setEnabled(false);
						wd = new WaitDialog();
						search();
						wd.dispose();
						df.setEnabled(true);
						df.toFront();
						doubleClick = 0;
					}
				}).start();
			}
		}

		// 戻る
		else if(source == backButton) {
			df.back();
		}

		// ソート順
		else if(source == upDownSortButton) {
			changeUpDownSort();
		}

		// ソート順
		else if(source == dateSortButton || source == charSortButton) {
			changeSort();
		}

		//-----------自分で使う時のみ-------------
		// 削除
		else if(source == deleteButton) {
			if(searchText.getText().replaceAll("　", "").replaceAll("\t", "").trim().length() != 0) {
				deleteData = fc.searchData(searchText.getText());
				if(deleteData != null && deleteData.length != 0) {
					new DeleteDialog(this, deleteData.length);
				}
				else {
					new YesDialog(df, "該当する『" + setting.getTitle() + "』はありません");
				}
			}
			else {
				new YesDialog(df, "何も入力されていません");
				searchText.setText("");
			}
		}
		//----------------------------------------

		else {
			for(int i = 0; i < searchData.length; i++) {
				if(source == resultButton[i]) {
					df.changeDiaryWriter(searchData[i].getDate());
				}
			}
		}

		focus();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!((e.getSource() == searchText && press) || iconButton.contains(e.getSource()))) {
			processing(e.getSource());
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

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(KeyEvent.VK_ENTER == e.getKeyCode()) {
			if(e.getSource() == searchText) {
				press = false;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
