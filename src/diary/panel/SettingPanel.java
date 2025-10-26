package diary.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import diary.button.IconButton;
import diary.button.TextButton;
import diary.dialog.ChangeSettingDialog;
import diary.label.DiaryLabel;
import diary.label.TransparentLabel;
import diary.panel.parts.DiaryPanel;
import diary.panel.parts.ImagePanel;
import diary.panel.parts.TransparentPanel;

/**
 * 日記の設定を行う
 * @author Masato Suzuki
 */
public class SettingPanel extends DiaryPanel implements ActionListener, MouseListener, ChangeListener {
	/**
	 * SettingPanelの本体<br>
	 * 背景画像が描画される
	 */
	private ImagePanel settingPanel;

	/**
	 * タブ
	 */
	private JTabbedPane tab;

	/**
	 * 戻るボタン
	 */
	private IconButton backButton;

	/**
	 * タイトルを記憶する
	 */
	private String title = "title";

	/**
	 * 各種設定のタイトル<br>
	 * タブではそのまま、パネルには○○設定と表示される
	 */
	private String[] settingTitle = {
			"全般",
			"日記編集"
	};

	/**
	 * タブの数
	 */
	private int tabNum = settingTitle.length;

	/**
	 * 設定項目<br>
	 * タイトルは最初は"title"と書いて後で置き換える
	 */
	private String[][][] settingString = {
			// 全般設定
			{
				{"defaultFontName", "文字フォント名", "文字フォント名を変更します"},
				{"defaultFontStyle", "文字の太さ", "文字の太さを変更します"},
				{"defaultFontSize", "文字の大きさ", "文字の大きさを変更します"},
				{"title", "タイトル", "タイトルを変更します"},
				{"showIcon", "ボタンのアイコン画像表示", "ボタンのアイコン画像を表示するかを変更します"},
				{"showToolTip", "ボタンの説明表示", "ボタンの説明を表示するかを変更します"},
				{"charCode", "文字コード", "文字コードを変更します"},
				{"passwordLock", "パスワードロック", "『title』をパスワードでロックします<br>　簡単なものなので完全には守れません"},
				{"resetSetting", "設定リセット", "全ての設定をリセットします"},
				{"reset", "ソフトウェアリセット", "ソフトウェアをリセットします"}
			},
			// 日記編集設定
			{
				{"showDiaryImage", "日記の写真表示", "日記の写真を表示するかを変更します"}
			}
	};

	/**
	 * 設定項目ボタン
	 */
	private TextButton[][] settingButton;

	/**
	 * 選択されているタブのインデックス
	 */
	private int selectTab = 0;

	/**
	 * 全般設定のスクロールパネル
	 */
	private JScrollPane[] commonScroll;

	/**
	 * 全般設定のメッセージ
	 */
	private DiaryLabel[] label1, label2;

	/**
	 * コンストラクタ
	 */
	public SettingPanel() {
		setSystem();

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	private void firstLayout() {
		this.setLayout(new GridLayout(1, 1));
		this.add(getSettingPanel());

		updateComponents();
	}

	/**
	 * 必要なものを生成する
	 */
	private void setSystem() {
		label1 = new DiaryLabel[tabNum];
		label2 = new DiaryLabel[tabNum];

		settingButton = new TextButton[tabNum][];

		for(int i = 0; i < tabNum; i++) {
			settingButton[i] = new TextButton[settingString[i].length];
			for(int j = 0; j < settingString[i].length; j++) {
				settingButton[i][j] = new TextButton(false);
				settingButton[i][j].addActionListener(this);
			}
		}

		commonScroll = new JScrollPane[tabNum];
	}

	/**
	 * 設定パネルを返す
	 * @return 設定パネル
	 */
	private JPanel getSettingPanel() {
		settingPanel = new ImagePanel();

		// 全体的にBorderLayout
		settingPanel.setLayout(new BorderLayout());
		settingPanel.setBackground(setting.getDefaultBackColor());
		settingPanel.setOpaque(true);

		settingPanel.add(new TransparentLabel("　　", 80), BorderLayout.NORTH);
		settingPanel.add(new TransparentLabel("　　", 80), BorderLayout.WEST);
		settingPanel.add(getCenterPanel(), BorderLayout.CENTER);
		settingPanel.add(new TransparentLabel("　　", 80), BorderLayout.EAST);
		settingPanel.add(getButtonPanel(), BorderLayout.SOUTH);

		return settingPanel;
	}

	/**
	 * 設定選択パネルを返す
	 * @return 設定選択パネル
	 */
	private JPanel getCenterPanel() {
		tab = new JTabbedPane();
		tab.addChangeListener(this);
		for(int i = 0; i < tabNum; i++) {
			tab.add(settingTitle[i], getSelectSettingPanel(settingTitle[i] + "設定", i));
		}

		TransparentPanel settingCenterPanel = new TransparentPanel(80);
		settingCenterPanel.setOpaque(true);
		settingCenterPanel.setOpaque(false);
		settingCenterPanel.setLayout(new GridLayout(1, 1));

		settingCenterPanel.add(tab);

		return settingCenterPanel;
	}

	/**
	 * 設定選択パネルを返す
	 * @param title タイトル
	 * @param tab この設定の種類
	 * @return 設定選択パネル
	 */
	private JPanel getSelectSettingPanel(String title, int tab) {
		// タイトルの設定
		label1[tab] = new DiaryLabel(title, 10, JLabel.CENTER, false);

		// パネルの生成
		JPanel tmpPanel = new JPanel();
		tmpPanel.setBackground(Color.WHITE);
		tmpPanel.setLayout(new BoxLayout(tmpPanel, BoxLayout.Y_AXIS));
		for(int i = 0; i < settingButton[tab].length; i++) {
			tmpPanel.add(settingButton[tab][i]);
		}

		// スクロールの生成
		commonScroll[tab] = new JScrollPane(tmpPanel);
		commonScroll[tab].setOpaque(false);
		commonScroll[tab].getVerticalScrollBar().setUnitIncrement(15);
		commonScroll[tab].setBorder(new LineBorder(Color.BLACK, 1, false));
		commonScroll[tab].setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		// 注意書きの設定
		label2[tab] = new DiaryLabel("※一部の変更はソフトウェア再起動後に適用されます", 0, JLabel.CENTER, false);

		// 返すパネルの生成
		JPanel selectSettingPanel = new JPanel();
		selectSettingPanel.setBackground(setting.getBoxColor());
		selectSettingPanel.setLayout(new BorderLayout());
		selectSettingPanel.add(label1[tab], BorderLayout.NORTH);
		selectSettingPanel.add(commonScroll[tab], BorderLayout.CENTER);
		selectSettingPanel.add(label2[tab], BorderLayout.SOUTH);

		return selectSettingPanel;
	}

	/**
	 * 下部のボタンパネルを返す
	 * @return ボタンパネル
	 */
	private JPanel getButtonPanel() {
		// backButtonの生成
		backButton = new IconButton(fc.getIconImage("back"), setting.getBackButtonBeforeBackColor(), setting.getBackButtonAfterBackColor(), "戻   る", "前の画面に戻ります", 40, 20, 20, 20, false);
		backButton.addMouseListener(this);
		iconButton.add(backButton);

		TransparentPanel buttonPanel = new TransparentPanel(80);
		buttonPanel.setOpaque(false);

		buttonPanel.add(backButton);

		return buttonPanel;
	}

	/**
	 * 最初の設定画面に戻る
	 */
	public void reset(){
		tab.setSelectedIndex(0);
		resetScroll();
	}

	/**
	 * スクロールを一番上にする
	 */
	private void resetScroll() {
		for(int i = 0; i < settingString.length; i++) {
			if(commonScroll[i] != null) {
				commonScroll[i].getVerticalScrollBar().setValue(0);
			}
		}
	}

	/**
	 * コンポーネントの更新
	 */
	public void updateComponents() {
		tab.setFont(setting.getSettingFont(5));
		tab.setForeground(setting.getSettingFontColor());

		for(int i = 0; i < tabNum; i++) {
			// タブのツールチップ更新
			if(setting.getShowToolTip()) {
				tab.setToolTipTextAt(i, setting.getSettingToolTipText(settingTitle[i] + "設定を表示します"));
			}
			else {
				tab.setToolTipTextAt(i, null);
			}

			// ラベルの更新
			label1[i].updateComponents();
			label1[i].setFont(setting.getSettingFont(10));
			label1[i].setForeground(setting.getSettingFontColor());
			label1[i].setPreferredSize(new Dimension(setting.getSize(setting.getDefaultFontSize() * 5), setting.getSize(setting.getDefaultFontSize() * 3)));
			label2[i].updateComponents();
			label2[i].setFont(setting.getSettingFont(0));
			label2[i].setForeground(setting.getSettingFontColor());
			label2[i].setPreferredSize(new Dimension(setting.getSize(setting.getDefaultFontSize() * 25), setting.getFontSize(setting.getDefaultFontSize() + 10)));

			for(int j = 0; j < settingString[i].length; j++) {
				// 設定テキストの『タイトル』更新
				settingString[i][j][2] = settingString[i][j][2].replaceAll("『" + title + "』", "『" + setting.getTitle() + "』");

				// 設定ボタン更新
				settingButton[i][j].setText("【" + settingString[i][j][1] + "】<br>　" + settingString[i][j][2]);
				settingButton[i][j].setToolTipText(settingString[i][j][2].replaceAll("　", ""));
				settingButton[i][j].updateComponents();
			}
		}

		// 戻るボタンの更新
		backButton.updateComponents();

		// タイトル更新
		title = setting.getTitle();
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
				if(fc.getSettingBackgroundExists()) {
					image = fc.getSettingBackgroundImage();
				}
				else if(fc.getDefaultBackgroundExists()) {
					image = fc.getDefaultBackgroundImage();
				}

				// この処理が正しい日付で行われているか（スレッド開始時の番号と現在の番号が一致しているか）
				if(threadNum == backgroundCount) {
					if(image.getWidth() < 400 || image.getHeight() < 400) {
						// このときの第2引数は例外（気にしない）
						settingPanel.setImage(image, true, true);
					}
					else {
						settingPanel.setImage(image, false, false);
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
		settingPanel.removeBackgroundImage();
	}

	/**
	 * ボタン処理
	 * @param source イベントの発生源
	 */
	private void processing(Object source) {
		// 戻る
		if(source == backButton) {
			df.back();
		}
		else {
			for(int i = 0; i < settingString[selectTab].length; i++) {
				if(source == settingButton[selectTab][i]) {
					new ChangeSettingDialog(settingString[selectTab][i][2], settingString[selectTab][i][0]);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!iconButton.contains(e.getSource())) {
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
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == tab) {
			selectTab = tab.getSelectedIndex();
			resetScroll();
		}
	}
}
