package diary.system;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import diary.system.calendar.Date;

/**
 * 設定クラス
 * @author Masato Suzuki
 */
public class Setting {
	/**
	 * Settingクラスのオブジェクト
	 */
	private static Setting setting = new Setting();

	/**
	 * 実行環境のdpi
	 */
	private int dpi;

	/**
	 * 開発環境のdpi
	 */
	private int developerDpi = 192;

	/**
	 * 開発環境と実行環境の比率
	 */
	private double dpiRatio;

	/**
	 * ツールキットの取得
	 */
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	/**
	 * ディスプレイサイズ（JREに依存する）
	 */
	private Dimension displaySize;

	/**
	 * デスクトップサイズ
	 */
	private Dimension desktopSize;

	/**
	 * 実行環境ディスプレイの拡大率
	 */
	private double scaleRatio;

	/**
	 * システム情報
	 */
	private String basePath, osName, fileSeparator, userName, userDir, charCode;

	/**
	 * システム情報
	 */
	private double javaVersion;

	/**
	 * フォント名一覧
	 */
	private String[] fontFamilyNames;

	/**
	 * デフォルトフォント名
	 */
	private String defaultFontName;

	/**
	 * デフォルトフォントスタイル
	 */
	private int defaultFontStyle;

	/**
	 * 初期のフォントサイズ
	 */
	private int firstFontSize;

	/**
	 * デフォルトフォントサイズ
	 */
	private int defaultFontSize;

	/**
	 * 使用できる文字コード
	 */
	private String[] charCodes = {
			"MS932",
			"Shift_JIS",
			"UTF-8",
			"EUC_JP"
	};

	/**
	 * デフォルトアイコン画像表示の有無
	 */
	private boolean showIcon;

	/**
	 * ツールチップ表示の有無
	 */
	private boolean showToolTip;

	/**
	 * パスワードロックの有無
	 */
	private boolean lock;

	/**
	 * パスワード
	 */
	private String password;

	/**
	 * タイトル
	 */
	private String title;

	/**
	 * 日記の写真を表示するか
	 */
	private boolean showDiaryImage;

	/**
	 * 連続実行回数
	 */
	private int frameCount = 0;

	/**
	 * フォントサイズの幅
	 */
	private int fontSizeRange;

	/**
	 * Settingクラスのコンストラクタ
	 */
	private Setting() {
		setComponentData();

		setSystemInfo();

		setFont();

		resetSetting();

		setJarPath();
	}

	/**
	 * インスタンスを返す
	 * @return インスタンス
	 */
	public static Setting getInstance() {
		return setting;
	}

	/**
	 * コンポーネントサイズ関連のデータを取得
	 */
	private void setComponentData() {
		// ディスプレイサイズの取得（JREに依存する）
		displaySize = toolkit.getScreenSize();

		// dpi取得（1インチあたりのドット数）
		dpi = toolkit.getScreenResolution();

		// 比率の計算
		dpiRatio = ((double)dpi / developerDpi);

		// 実行環境のディスプレイ状態を取得（JREに依存しない）
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

		// 実行環境ディスプレイの拡大率(おそらくJRE9以降で関係し、ディスプレイサイズが変わる)
		DisplayMode displayMode = env.getDefaultScreenDevice().getDisplayMode();
		scaleRatio = displayMode.getWidth() / displaySize.getWidth();

		// 実行環境デスクトップのサイズを取得
		desktopSize = env.getMaximumWindowBounds().getSize();
	}

	/**
	 * システム情報の取得
	 */
	private void setSystemInfo() {
		osName = System.getProperty("os.name");
		fileSeparator = System.getProperty("file.separator");
		userName = System.getProperty("user.name");
		userDir = System.getProperty("user.dir");
		javaVersion = Double.parseDouble(System.getProperty("java.specification.version"));
	}

	/**
	 * dataディレクトリの親ディレクトリの取得
	 */
	private void setJarPath() {
		// Settingクラスのあるディレクトリパスの取得（file:/C:/Users/Masato/program(Java)/diary/bin/diary/system/）
		try {
			basePath = URLDecoder.decode(this.getClass().getResource("/").getPath(), "utf-8");
		}
		catch (Exception error) {
			error.printStackTrace();
		}

		basePath = basePath.replaceAll("file:", "");

		// ファイルの区切り記号を実行環境に合わせる
		if(fileSeparator.equals("\\")) {
			basePath = basePath.replaceAll("/", "\\\\");
		}
		else {
			basePath = basePath.replaceAll("\\\\", "/");
		}

		/*
		 * jarファイル以外の実行の場合、クラスファイルが格納されたディレクトリのパスになる
		 * 現在、クラスファイル名が「bin」であり、その親ディレクトリを指定する
		 */
		String classDirName = "bin" + fileSeparator;
		if(basePath.lastIndexOf(classDirName) == basePath.length() - classDirName.length()) {
			basePath = basePath.substring(0, basePath.length() - 4);
		}
	}

	/**
	 * 使用できるフォントの取得
	 */
	private void setFont() {
		// フォント名一覧取得
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fontFamilyNames = ge.getAvailableFontFamilyNames();
		Font [] fonts = new Font[fontFamilyNames.length];
		for(int i = 0; i < fontFamilyNames.length; i++) {
			fonts[i] = new Font(fontFamilyNames[i], Font.PLAIN, 30);
		}

		// 日本語を表示できるフォント名を判別
		ArrayList<String> tmpArray = new ArrayList<String>();
		for(int i = 0; i < fontFamilyNames.length; i++) {
			if(fonts[i].canDisplay('あ') && fonts[i].canDisplay('検')) {
				tmpArray.add(fontFamilyNames[i]);
			}
		}
		fontFamilyNames = new String[tmpArray.size()];
		for(int i = 0; i < tmpArray.size(); i++) {
			fontFamilyNames[i] = tmpArray.get(i);
		}
	}

	/**
	 * 設定のリセット
	 */
	public void resetSetting() {
		defaultFontName = "ＭＳ ゴシック";

		defaultFontStyle = Font.BOLD;

		// 理由は不明だが最適なフォントサイズがJREのバージョンによって異なっている
		firstFontSize = defaultFontSize = (getJavaVersion() <= 1.8) ? 30 : 15;

		// 設定できるフォントサイズの範囲の設定
		fontSizeRange = (getJavaVersion() <= 1.8) ? 20 : 10;

		title = "日記";

		showIcon = true;

		showToolTip = true;

		charCode = System.getProperty("file.encoding");

		lock = false;

		password = "";

		showDiaryImage = true;
	}

	/**
	 * フレーム生成回数を追加する
	 */
	public void addFrameCount() {
		frameCount++;
	}

	/**
	 * フレーム生成回数を初期化する<br>
	 * 再起動する場合はこのメソッドを実行することでアニメーションが表示される
	 */
	public void resetFrameCount() {
		frameCount = 0;
	}

	/**
	 * フレーム生成回数を返す
	 * @return フレーム生成回数
	 */
	public int getFrameCount() {
		return frameCount;
	}

	/**
	 * 開発環境から見た実行環境のdpi比率を返す
	 * @return 開発環境から見た実行環境のdpi比率
	 */
	public double getDpiRatio() {
		return dpiRatio;
	}

	/**
	 * システムの拡大率を返す
	 * @return システムの拡大率
	 */
	public double getScaleRatio() {
		return scaleRatio;
	}

	/**
	 * サイズを調整して返す
	 * @param size 調整前のサイズ
	 * @return 調整後のサイズ
	 */
	public int getSize(int size) {
		if(getJavaVersion() <= 1.8) {
			return rounding(size * dpiRatio);
		}
		else {
			return rounding(size);
		}
	}

	/**
	 * フレームの最小サイズを調整して返す
	 * @param size 調整前のサイズ
	 * @return 調整後のサイズ
	 */
	public Dimension getMinimumFrameSize(Dimension size) {
		if(getJavaVersion() <= 1.8) {
			return new Dimension(size);
		}
		else {
			return new Dimension(size);
			//return new Dimension(rounding(size.width * scaleRatio), rounding(size.height * scaleRatio));
		}
	}

	/**
	 * 横幅と縦幅が4:3になるように横幅を調整する
	 * @param width 調整前の横幅
	 * @return 調整後の横幅
	 */
	public int getWidth(int width) {
		return (int)(width * ((double)4 / 3));
	}

	/**
	 * 横幅と縦幅が4:3になるように縦幅を調整する
	 * @param height 調整前の縦幅
	 * @return 調整された縦幅
	 */
	public int getHeight(int height) {
		return (int)(height * ((double)3 / 4));
	}

	/**
	 * 初期のフォントサイズを返す
	 * @return 初期のフォントサイズ
	 */
	public int getFirstFontSize() {
		return firstFontSize;
	}

	/**
	 * 普通のボタンサイズを返す
	 * @param add サイズの基準
	 * @return 普通のボタンサイズ
	 */
	public Dimension getDefaultButtonSize(int add) {
		int width;
		if(this.getJavaVersion() <= 1.8) {
			width = getSize(getDefaultFontSize() * 4 + (80 + add));
		}
		else {
			width = getSize(rounding(getDefaultFontSize() * 4 + (80 + add) * 0.8));
		}
		return new Dimension(width, (int)(width * 0.4));
	}

	/**
	 * アイコンボタンサイズを返す
	 * @param addWidth 横幅の基準
	 * @param addHeight 縦幅の基準
	 * @return アイコンボタンサイズ
	 */
	public Dimension getDefaultIconButtonSize(int addWidth, int addHeight) {
		int width;
		int height;
		if(this.getJavaVersion() <= 1.8) {
			width = getSize(getDefaultFontSize() * 2 + addWidth);
			height = getSize(getDefaultFontSize() * 2 + addHeight);
		}
		else {
			width = getSize(rounding(getDefaultFontSize() * 2 + addWidth * 0.7));
			height = getSize(rounding(getDefaultFontSize() * 2 + addHeight * 0.8));
		}

		return new Dimension(width, height);
	}

	/**
	 * アイコンをリサイズして返す
	 * @param image リサイズするアイコン
	 * @param addWidth 横幅の基準
	 * @param addHeight 縦幅の基準
	 * @return リサイズされたアイコン
	 */
	public ImageIcon getDefaultIcon(Image image, int addWidth, int addHeight) {
		int width;
		int height;
		if(this.getJavaVersion() <= 1.8) {
			width = getSize(getDefaultFontSize() * 2 + addWidth);
			height = getSize(getDefaultFontSize() * 2 + addHeight);
		}
		else {
			width = getSize(rounding(getDefaultFontSize() * 2 + addWidth * 0.8));
			height = getSize(rounding(getDefaultFontSize() * 2 + addHeight * 0.8));
		}

		return new ImageIcon(image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH ));
	}

	/**
	 * フォントを返す
	 * @param add デフォルトフォントサイズからの増減
	 * @return フォント
	 */
	public Font getFont(int add) {
		if(this.getJavaVersion() <= 1.8) {
			return new Font(getDefaultFontName(), getDefaultFontStyle(), getFontSize(getDefaultFontSize() + add));
		}
		else {
			return new Font(getDefaultFontName(), getDefaultFontStyle(), getFontSize(rounding(getDefaultFontSize() + add * 0.8)));
		}
	}

	/**
	 * 初期サイズのフォントを返す
	 * @param add 初期フォントサイズからの増減
	 * @return 初期サイズフォント
	 */
	public Font getFirstSizeFont(int add) {
		if(this.getJavaVersion() <= 1.8) {
			return new Font(getDefaultFontName(), getDefaultFontStyle(), getFontSize(getFirstFontSize() + add));
		}
		else {
			return new Font(getDefaultFontName(), getDefaultFontStyle(), getFontSize(rounding(getFirstFontSize() + add * 0.8)));
		}
	}

	/**
	 * 設定画面用フォントを返す<br>
	 * フォントサイズだけ変化させます
	 * @param add デフォルトフォントサイズからの増減
	 * @return フォント
	 */
	public Font getSettingFont(int add) {
		if(this.getJavaVersion() <= 1.8) {
			return new Font("ＭＳ ゴシック", Font.PLAIN, getFontSize(getDefaultFontSize() + add));
		}
		else {
			return new Font("ＭＳ ゴシック", Font.PLAIN, getFontSize(rounding(getDefaultFontSize() + add * 0.8)));
		}
	}

	/**
	 * 端末に合う文字サイズ
	 * @param size 調整前の文字サイズ
	 * @return 文字サイズ
	 */
	public int getFontSize(int size) {
		if(this.getJavaVersion() <= 1.8) {
			return rounding(size * dpiRatio);
		}
		else {
			return rounding(size);
		}
	}

	/**
	 * デフォルト文字色を返す
	 * @return デフォルト文字色
	 */
	public Color getDefaultFontColor() {
		return new Color(0, 0, 0);
	}

	/**
	 * デフォルト文字色をHTML用の16進数で返す<br>
	 * 例：#000000
	 * @return デフォルト文字色（HTML用）
	 */
	public String getDefaultFontColorToHexString() {
		return getColorToHexString(getDefaultFontColor());
	}

	/**
	 * 設定画面の文字色を返す
	 * @return 設定画面の文字色
	 */
	public Color getSettingFontColor() {
		return new Color(0, 0, 0);
	}

	/**
	 * 設定画面の文字色をHTML用の16進数で返す<br>
	 * 例：#000000
	 * @return 設定画面の文字色（HTML用）
	 */
	public String getSettingFontColorToHexString() {
		return getColorToHexString(getSettingFontColor());
	}

	/**
	 * 文字色をHTML用の16進数で返す<br>
	 * 例：#000000
	 * @return 文字色（HTML用）
	 */
	public String getColorToHexString(Color color) {
		String r = Integer.toHexString(color.getRed());
		String g = Integer.toHexString(color.getGreen());
		String b = Integer.toHexString(color.getBlue());
		return "#" + ((r.length() == 1) ? "0" : "") + r + ((g.length() == 1) ? "0" : "") + g + ((b.length() == 1) ? "0" : "") + b;
	}

	/**
	 * javaのバージョンを返す
	 * @return javaのバージョン
	 */
	public double getJavaVersion() {
		return javaVersion;
	}

	/**
	 * OS情報を返す
	 * @return OS情報
	 */
	public String getOsName() {
		return osName;
	}

	/**
	 * 改行コードを返す
	 * @return 改行コード
	 */
	public String getLineSeparator() {
		return "\n";
	}

	/**
	 * ファイルパスの区切り記号を返す
	 * @return ファイルパスの区切り記号
	 */
	public String getFileSeparator() {
		return fileSeparator;
	}

	/**
	 * ユーザーネームを返す
	 * @return ユーザーネーム
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * カレントディレクトリの絶対パスを返す
	 * @return カレントディレクトリの絶対パス
	 */
	public String getUserDir() {
		return userDir;
	}

	/**
	 * フォント名一覧を返す
	 * @return フォント名一覧
	 */
	public String[] getFontNameList() {
		return fontFamilyNames;
	}

	/**
	 * 文字コード一覧を返す
	 * @return 文字コード一覧
	 */
	public String[] getCharCodeList() {
		return charCodes;
	}

	/**
	 * 通常のボタンテキストを返す
	 * @param text 表示するメッセージ
	 * @return ボタンテキスト
	 */
	public String getDefaultButtonText(String text) {
		return getButtonText(text, getFontWeight(), getDefaultFontName());
	}

	/**
	 * 設定画面用のボタンテキストを返す
	 * @param text 表示するメッセージ
	 * @return 設定画面用のボタンテキスト
	 */
	public String getSettingButtonText(String text) {
		return getButtonText(text, "normal", "ＭＳ ゴシック");
	}

	/**
	 * 適切なボタンテキストを返す
	 * @param text 表示するメッセージ
	 * @param fontWeight 文字の太さ
	 * @param fontName フォント名
	 * @return 適切なボタンテキスト
	 */
	private String getButtonText(String text, String fontWeight, String fontName) {
		if(this.getJavaVersion() <= 1.8) {
			return "<html><div style=\"font-size:" + setting.getFontSize((int)(setting.getDefaultFontSize() * 0.73 + 0.5)) + "px;font-weight: " + fontWeight + ";font-family:'" + fontName + "';\">" + text + "</div></html>";
		}
		else {
			return "<html><div style=\"font-size:" + setting.getFontSize((int)(setting.getDefaultFontSize() * 0.73 + 0.5)) + "px;font-weight: " + fontWeight + ";font-family:'" + fontName + "';\">" + text + "</div></html>";
		}
	}

	/**
	 * 通常のツールチップテキストを返す
	 * @param text 表示するメッセージ
	 * @return ツールチップテキスト
	 */
	public String getDefaultToolTipText(String text) {
		return getToolTipText(text, getFontWeight(), getDefaultFontName(), getDefaultFontColorToHexString());
	}

	/**
	 * 設定画面用のツールチップテキストを返す
	 * @param text 表示するメッセージ
	 * @return 設定画面用のツールチップテキスト
	 */
	public String getSettingToolTipText(String text) {
		return getToolTipText(text, "normal", "ＭＳ ゴシック", getSettingFontColorToHexString());
	}

	/**
	 * 適切なツールチップテキストを返す
	 * @param text 表示するメッセージ
	 * @param fontWeight 文字の太さ
	 * @param fontName フォント名
	 * @param fontColor 文字色（HTML用の16進数）
	 * @return 適切なツールチップテキスト
	 */
	private String getToolTipText(String text, String fontWeight, String fontName, String fontColor) {
		if(this.getJavaVersion() <= 1.8) {
			return "<html><div style=\"font-size:" + getFontSize((int)(getDefaultFontSize() / 1.5 + 0.5)) + "px;font-weight:" + fontWeight + ";font-family:'" + fontName + "';background-color: white;\"><font color='" + fontColor + "'>" + text + "</font></div></html>";
		}
		else {
			return "<html><div style=\"font-size:" + getFontSize((int)(getDefaultFontSize() / 1.5 + 0.5)) + "px;font-weight:" + fontWeight + ";font-family:'" + fontName + "';background-color: white;\"><font color='" + fontColor + "'>" + text + "</font></div></html>";
		}
	}

	/**
	 * 長押し時の画面遷移のスピードを返す
	 * @return 長押し時の画面遷移のスピード
	 */
	public int getScreenTransition() {
		return 300;
	}

	/**
	 * ディスプレイサイズを返す<br>
	 * JRE9以降でシステムの拡大率が適用される
	 * @return ディスプレイサイズ
	 */
	public Dimension getDisplaySize() {
		return displaySize;
	}

	/**
	 * ディスプレイの横幅を返す
	 * @return ディスプレイの横幅
	 */
	public int getDisplayWidth() {
		return displaySize.width;
	}

	/**
	 * ディスプレイの縦幅を返す
	 * @return ディスプレイの縦幅
	 */
	public int getDisplayHeight() {
		return displaySize.height;
	}

	/**
	 * デスクトップサイズを返す
	 * @return デスクトップサイズ
	 */
	public Dimension getDesktopSize() {
		return desktopSize;
	}

	/**
	 * デスクトップの横幅を返す
	 * @return デスクトップの横幅
	 */
	public int getDesktopWidth() {
		return desktopSize.width;
	}

	/**
	 * デスクトップの縦幅を返す
	 * @return デスクトップの縦幅
	 */
	public int getDesktopHeight() {
		return desktopSize.height;
	}

	/**
	 * 最初ボタンの背景色を返す
	 * @return 最初ボタンの背景色
	 */
	public Color getFirstButtonBeforeBackColor() {
		return new Color(173, 216, 230);
	}

	/**
	 * 最初ボタンの背景色を返す
	 * @return 最初ボタンの背景色
	 */
	public Color getFirstButtonAfterBackColor() {
		return new Color(0, 0, 255);
	}

	/**
	 * 前ボタンの背景色を返す
	 * @return 前ボタンの背景色
	 */
	public Color getPreButtonBeforeBackColor() {
		return new Color(173, 216, 230);
	}

	/**
	 * 前ボタンの背景色を返す
	 * @return 前ボタンの背景色
	 */
	public Color getPreButtonAfterBackColor() {
		return new Color(0, 0, 255);
	}

	/**
	 * 次ボタンの背景色を返す
	 * @return 次ボタンの背景色
	 */
	public Color getNextButtonBeforeBackColor() {
		return new Color(173, 216, 230);
	}

	/**
	 * 次ボタンの背景色を返す
	 * @return 次ボタンの背景色
	 */
	public Color getNextButtonAfterBackColor() {
		return new Color(0, 0, 255);
	}

	/**
	 * 現在ボタンの背景色を返す
	 * @return 現在ボタンの背景色
	 */
	public Color getNowButtonBeforeBackColor() {
		return new Color(173, 216, 230);
	}

	/**
	 * 現在ボタンの背景色を返す
	 * @return 現在ボタンの背景色
	 */
	public Color getNowButtonAfterBackColor() {
		return new Color(0, 0, 255);
	}

	/**
	 * 検索ボタンの背景色を返す
	 * @return 検索ボタンの背景色
	 */
	public Color getSearchButtonBeforeBackColor() {
		return new Color(144, 238, 144);
	}

	/**
	 * 検索ボタンの背景色を返す
	 * @return 検索ボタンの背景色
	 */
	public Color getSearchButtonAfterBackColor() {
		return new Color(0, 123, 0);
	}

	/**
	 * 設定ボタンの背景色を返す
	 * @return 設定ボタンの背景色
	 */
	public Color getSettingButtonBeforeBackColor() {
		return new Color(173, 216, 230);
	}

	/**
	 * 設定ボタンの背景色を返す
	 * @return 設定ボタンの背景色
	 */
	public Color getSettingButtonAfterBackColor() {
		return new Color(0, 0, 255);
	}

	/**
	 * ヘルプボタンの背景色を返す
	 * @return ヘルプボタンの背景色
	 */
	public Color getHelpButtonBeforeBackColor() {
		return new Color(250, 128, 114);
	}

	/**
	 * ヘルプボタンの背景色を返す
	 * @return ヘルプボタンの背景色
	 */
	public Color getHelpButtonAfterBackColor() {
		return new Color(255, 0, 0);
	}

	/**
	 * 編集ボタンの背景色を返す
	 * @return 編集ボタンの背景色
	 */
	public Color getWriteButtonBeforeBackColor() {
		return new Color(144, 238, 144);
	}

	/**
	 * 編集ボタンの背景色を返す
	 * @return 編集ボタンの背景色
	 */
	public Color getWriteButtonAfterBackColor() {
		return new Color(0, 123, 0);
	}

	/**
	 * 画像表示ボタンの背景色を返す
	 * @return 画像表示ボタンの背景色
	 */
	public Color getPictureButtonBeforeBackColor() {
		return new Color(173, 216, 230);
	}

	/**
	 * 画像表示ボタンの背景色を返す
	 * @return 画像表示ボタンの背景色
	 */
	public Color getPictureButtonAfterBackColor() {
		return new Color(0, 0, 255);
	}

	/**
	 * 削除ボタンの背景色を返す
	 * @return 削除ボタンの背景色
	 */
	public Color getDeleteButtonBeforeBackColor() {
		return new Color(250, 128, 114);
	}

	/**
	 * 削除ボタンの背景色を返す
	 * @return 削除ボタンの背景色
	 */
	public Color getDeleteButtonAfterBackColor() {
		return new Color(255, 0, 0);
	}

	/**
	 * 保存ボタンの背景色を返す
	 * @return 保存ボタンの背景色
	 */
	public Color getSaveButtonBeforeBackColor() {
		return new Color(144, 238, 144);
	}

	/**
	 * 保存ボタンの背景色を返す
	 * @return 保存ボタンの背景色
	 */
	public Color getSaveButtonAfterBackColor() {
		return new Color(0, 123, 0);
	}

	/**
	 * 画像追加ボタンの背景色を返す
	 * @return 画像追加ボタンの背景色
	 */
	public Color getAddPictureButtonBeforeBackColor() {
		return new Color(173, 216, 230);
	}

	/**
	 * 画像追加ボタンの背景色を返す
	 * @return 画像追加ボタンの背景色
	 */
	public Color getAddPictureButtonAfterBackColor() {
		return new Color(0, 0, 255);
	}

	/**
	 * 戻るボタンの背景色を返す
	 * @return 戻るボタンの背景色
	 */
	public Color getBackButtonBeforeBackColor() {
		return new Color(250, 128, 114);
	}

	/**
	 * 戻るボタンの背景色を返す
	 * @return 戻るボタンの背景色
	 */
	public Color getBackButtonAfterBackColor() {
		return new Color(255, 0, 0);
	}

	/**
	 * 日付ソートボタンの背景色を返す
	 * @return 日付ソートボタンの背景色
	 */
	public Color getDateSortButtonBeforeBackColor() {
		return getSortButtonBeforeBackColor();
	}

	/**
	 * 日付ソートボタンの背景色を返す
	 * @return 日付ソートボタンの背景色
	 */
	public Color getDateSortButtonAfterBackColor() {
		return getSortButtonAfterBackColor();
	}

	/**
	 * 文字数ソートボタンの背景色を返す
	 * @return 文字数ソートボタンの背景色
	 */
	public Color getCharSortButtonBeforeBackColor() {
		return getSortButtonBeforeBackColor();
	}

	/**
	 * 文字数ソートボタンの背景色を返す
	 * @return 文字数ソートボタンの背景色
	 */
	public Color getCharSortButtonAfterBackColor() {
		return getSortButtonAfterBackColor();
	}

	/**
	 * 並び替えボタンの背景色を返す
	 * @return 並び替えボタンの背景色
	 */
	public Color getSortButtonBeforeBackColor() {
		return new Color(173, 216, 230);
	}

	/**
	 * 並び替えボタンの背景色を返す
	 * @return 並び替えボタンの背景色
	 */
	public Color getSortButtonAfterBackColor() {
		return new Color(0, 0, 255);
	}

	/**
	 * ソート順ボタンの背景色を返す
	 * @return ソート順ボタンの背景色
	 */
	public Color getUpDownSortButtonBeforeBackColor() {
		return new Color(144, 238, 144);
	}

	/**
	 * ソート順ボタンの背景色を返す
	 * @return ソート順ボタンの背景色
	 */
	public Color getUpDownSortButtonAfterBackColor() {
		return new Color(0, 123, 0);
	}

	/**
	 * 今日のボタンの背景色を返す
	 * @return 今日のボタンの背景色
	 */
	public Color getTodayButtonBackColor() {
		return new Color(0, 255, 65);
	}

	/**
	 * 今日のボタンの文字色を返す
	 * @return 今日のボタンの文字色
	 */
	public Color getTodayButtonFontColor() {
		return new Color(0, 0, 0);
	}

	/**
	 * 記録済みテキストの日の背景色を返す
	 * @return 記録済みテキストの日の背景色
	 */
	public Color getDoneTextButtonBackColor() {
		return new Color(127, 191, 255);
	}

	/**
	 * 記録済みテキストの日の文字色を返す
	 * @return 記録済みテキストの日の文字色
	 */
	public Color getDoneTextButtonFontColor() {
		return new Color(0, 0, 0);
	}

	/**
	 * 記録済み写真の日の背景色を返す
	 * @return 記録済み写真の日の背景色
	 */
	public Color getDonePictureButtonBackColor() {
		return new Color(250, 128, 114);
	}

	/**
	 * 記録済み写真の日の文字色を返す
	 * @return 記録済み写真の日の文字色
	 */
	public Color getDonePictureButtonFontColor() {
		return new Color(0, 0, 0);
	}

	/**
	 * 指定された設定を返す
	 * @param key 設定識別キー
	 * @return 指定された設定
	 */
	public String getSetting(String key) {
		if(key.equals("charCode")) {
			return getCharCode();
		}
		else if(key.equals("defaultFontName")) {
			return getDefaultFontName();
		}
		else if(key.equals("defaultFontStyle")) {
			return (getDefaultFontStyle() == Font.PLAIN) ? "PLAIN" : "BOLD";
		}
		else if(key.equals("defaultFontSize")) {
			return Integer.toString(getDefaultFontSize());
		}
		else if(key.equals("title")) {
			return getTitle();
		}
		else if(key.equals("showIcon")) {
			return Boolean.toString(getShowIcon()).toUpperCase();
		}
		else if(key.equals("showToolTip")) {
			return Boolean.toString(getShowToolTip()).toUpperCase();
		}
		else if(key.equals("passwordLock")) {
			return getPassword();
		}
		else if(key.equals("showDiaryImage")) {
			return Boolean.toString(getShowDiaryImage()).toUpperCase();
		}
		else {
			return null;
		}
	}

	/**
	 * 指定された設定をセットする
	 * @param key 設定識別キー
	 * @param set セットする設定
	 * @return 変更できたか
	 */
	public boolean changeSetting(String key, String set) {
		if(key.equals("charCode")) {
			return changeCharCode(set);
		}
		else if(key.equals("defaultFontName")) {
			return changeDefaultFontName(set);
		}
		else if(key.equals("defaultFontStyle")) {
			return changeDefaultFontStyle(set);
		}
		else if(key.equals("defaultFontSize")) {
			return changeDefaultFontSize(set);
		}
		else if(key.equals("title")) {
			return changeTitle(set);
		}
		else if(key.equals("showIcon")) {
			return changeShowIcon(set);
		}
		else if(key.equals("showToolTip")) {
			return changeShowToolTip(set);
		}
		else if(key.equals("passwordLock")) {
			return changePasswordLock(set);
		}
		else if(key.equals("showDiaryImage")) {
			return changeShowDiaryImage(set);
		}
		else {
			return false;
		}
	}

	/**
	 * フォント名を返す
	 * @return フォント名
	 */
	public String getDefaultFontName() {
		return defaultFontName;
	}

	/**
	 * フォント名変更
	 * @param set セットするフォント名
	 * @return 変更できたか
	 */
	public boolean changeDefaultFontName(String set) {
		for(int i = 0; i < fontFamilyNames.length; i++) {
			if(set.replaceAll("　", " ").toUpperCase().equals(fontFamilyNames[i].replaceAll("　", " ").toUpperCase())) {
				defaultFontName = fontFamilyNames[i];
				return true;
			}
		}
		return false;
	}

	/**
	 * フォントスタイルを返す
	 * @return フォントスタイル
	 */
	public int getDefaultFontStyle() {
		return defaultFontStyle;
	}

	/**
	 * フォントスタイル変更
	 * @param set セットするフォントスタイル
	 * @return 変更できたか
	 */
	public boolean changeDefaultFontStyle(String set) {
		if(set.toUpperCase().equals("BOLD")) {
			defaultFontStyle = Font.BOLD;
			return true;
		}
		else if(set.toUpperCase().equals("PLAIN")){
			defaultFontStyle = Font.PLAIN;
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * フォントスタイルを返す（HTML用）
	 * @return フォントスタイル（HTML用）
	 */
	public String getFontWeight() {
		if(getDefaultFontStyle() == Font.BOLD) {
			return "bold";
		}
		else {
			return "normal";
		}
	}

	/**
	 * 文字サイズを返す
	 * @return 文字サイズ
	 */
	public int getDefaultFontSize() {
		return defaultFontSize;
	}

	/**
	 * 文字サイズの変更
	 * @param set セットする文字サイズ
	 * @return 変更できたか
	 */
	public boolean changeDefaultFontSize(String set) {
		try {
			int tmp = Integer.parseInt(set);
			if(getMinFontSize() <= tmp &&  tmp <= getMaxFontSize()) {
				defaultFontSize = tmp;
				return true;
			}
		}
		catch(Exception error) {
			error.printStackTrace();
		}
		return false;
	}

	/**
	 * 受け入れるフォントの最小サイズを返す
	 * @return 受け入れるフォントの最小サイズ
	 */
	public int getMinFontSize() {
		// 最小が5かつ5の倍数になるように合わせる
		int minSize = firstFontSize - fontSizeRange;
		return (minSize < 5) ? 5 : (minSize - (minSize % 5));
	}

	/**
	 * 受け入れるフォントの最大サイズを返す
	 * @return 受け入れるフォントの最大サイズ
	 */
	public int getMaxFontSize() {
		return (getMinFontSize() + fontSizeRange * 2);
	}

	/**
	 * タイトルを返す
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * タイトルを変更
	 * @param set セットするタイトル
	 * @return 変更できたか
	 */
	public boolean changeTitle(String set) {
		if(set != null && set.replaceAll(" ", "").replaceAll("　", "").replaceAll("\n", "").replaceAll("\t", "").length() != 0 && set.indexOf((char)(0xfffd)) == -1) {
			title = set;
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * アイコン画像を表示するかを返す
	 * @return アイコン画像を表示するか
	 */
	public boolean getShowIcon() {
		return showIcon;
	}

	/**
	 * アイコン画像を表示するかを変更
	 * @param set セットするアイコン画像の表示可否
	 * @return 変更できたか
	 */
	public boolean changeShowIcon(String set) {
		if(set.toUpperCase().equals("TRUE")) {
			showIcon = true;
			return true;
		}
		else if(set.toUpperCase().equals("FALSE")){
			showIcon = false;
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * ツールチップを表示するかを返す
	 * @return ツールチップを表示するか
	 */
	public boolean getShowToolTip() {
		return showToolTip;
	}

	/**
	 * ツールチップを表示するかを変更
	 * @param set セットするツールチップの表示可否
	 * @return 変更できたか
	 */
	public boolean changeShowToolTip(String set) {
		if(set.toUpperCase().equals("TRUE")) {
			showToolTip = true;
			return true;
		}
		else if(set.toUpperCase().equals("FALSE")){
			showToolTip = false;
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 文字コードを返す
	 * @return 文字コード
	 */
	public String getCharCode() {
		return charCode;
	}

	/**
	 * 文字コードを変更
	 * @param set セットする文字コード
	 * @return 変更できたか
	 */
	public boolean changeCharCode(String set) {
		for(int i = 0; i < charCodes.length; i++) {
			if(set.replaceAll("　", " ").toUpperCase().equals(charCodes[i].replaceAll("　", " ").toUpperCase())) {
				charCode = set;
				return true;
			}
		}
		return false;
	}

	/**
	 * パスワードロックの有無を返す
	 * @return パスワードロックの有無
	 */
	public boolean getPasswordLock() {
		return lock;
	}

	/**
	 * パスワードをセットor変更
	 * @param set セットするパスワード
	 * @return 変更できたか
	 */
	public boolean changePasswordLock(String set) {
		boolean result = false;
		if(set != null && set.length() != 0) {
			password = set;
			lock = true;
			result = true;
		}
		else {
			password = "";
			lock = false;
			result = true;
		}
		return result;
	}

	/**
	 * パスワードを返す
	 * @return パスワード
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 日記の写真を表示するかを返す
	 * @return 日記の写真を表示するか
	 */
	public boolean getShowDiaryImage() {
		return showDiaryImage;
	}

	/**
	 * 日記の写真を表示するかを変更
	 * @param set セットする日記の写真の表示可否
	 * @return 変更できたか
	 */
	public boolean changeShowDiaryImage(String set) {
		if(set.toUpperCase().equals("TRUE")) {
			showDiaryImage = true;
			return true;
		}
		else if(set.toUpperCase().equals("FALSE")){
			showDiaryImage = false;
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * デフォルトの背景色を返す
	 * @return デフォルトの背景色
	 */
	public Color getDefaultBackColor() {
		return new Color(255,245,238);
	}

	/**
	 * 日付ボタンの基本背景色を返す
	 * @return 日付ボタンの基本背景色
	 */
	public Color getDayButtonBackColor() {
		return new Color(255, 255, 255);
	}

	/**
	 * 日付ボタンの基本文字色を返す
	 * @return 日付ボタンの基本文字色
	 */
	public Color getDayButtonFontColor() {
		return new Color(0, 0, 0);
	}

	/**
	 * 曜日ラベルの背景色を返す
	 * @param i 曜日を表す数値<br>
	 * 0～6：日～土
	 * @return 曜日ラベルの背景色
	 */
	public Color getDayOfTheWeekBackColor(int i) {
		Color color = null;
		if(i == 0) {
			color = new Color(240, 240, 240);
		}
		else if(i == 1) {
			color = new Color(240, 240, 240);
		}
		else if(i == 2) {
			color = new Color(240, 240, 240);
		}
		else if(i == 3) {
			color = new Color(240, 240, 240);
		}
		else if(i == 4) {
			color = new Color(240, 240, 240);
		}
		else if(i == 5) {
			color = new Color(240, 240, 240);
		}
		else if(i == 6) {
			color = new Color(240, 240, 240);
		}

		return color;
	}

	/**
	 * 曜日ラベルの文字色を返す
	 * @param i 曜日を表す数値<br>
	 * 0～6：日～土
	 * @return 曜日ラベルの文字色
	 */
	public Color getDayOfTheWeekFontColor(int i) {
		Color color = null;
		if(i == 0) {
			color = new Color(255, 0, 0);
		}
		else if(i == 1) {
			color = getDefaultFontColor();
		}
		else if(i == 2) {
			color = getDefaultFontColor();
		}
		else if(i == 3) {
			color = getDefaultFontColor();
		}
		else if(i == 4) {
			color = getDefaultFontColor();
		}
		else if(i == 5) {
			color = getDefaultFontColor();
		}
		else if(i == 6) {
			color = new Color(0, 0, 255);
		}

		return color;
	}

	/**
	 * 日付ボタンの土曜背景色を返す
	 * @return 日付ボタンの土曜背景色
	 */
	public Color getSaturdayButtonBackColor() {
		return new Color(255, 255, 255);
	}

	/**
	 * 日付ボタンの土曜文字色を返す
	 * @return 日付ボタンの土曜文字色
	 */
	public Color getSaturdayButtonFontColor() {
		return new Color(0, 0, 255);
	}

	/**
	 * 日付ボタンの日曜背景色を返す
	 * @return 日付ボタンの日曜背景色
	 */
	public Color getSundayButtonBackColor() {
		return new Color(255, 255, 255);
	}

	/**
	 * 日付ボタンの日曜文字色を返す
	 * @return 日付ボタンの日曜文字色
	 */
	public Color getSundayButtonFontColor() {
		return new Color(255, 0, 0);
	}

	/**
	 * 検索画面のボックス背景色を返す
	 * @return 検索画面のボックス背景色
	 */
	public Color getBoxColor() {
		return new Color(240, 240, 240);
	}

	/**
	 * テキストボタンの基本背景色を返す
	 * @return 設定選択ボタンの基本背景色
	 */
	public Color getTextButtonBackColor() {
		return new Color(255, 255, 255);
	}

	/**
	 * 基盤となるディレクトリパスを返す
	 * @return 基盤となるディレクトリパス
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * 日記情報が格納されているディレクトリのパスを返す
	 * @return 日記情報が格納されているディレクトリのパス
	 */
	public String getDataPath() {
		return getBasePath() + "data" + fileSeparator;
	}

	/**
	 * 日記情報（テキスト）が格納されているディレクトリのパスを返す
	 * @return 日記情報（テキスト）が格納されているディレクトリのパス
	 */
	public String getTextPath() {
		return getDataPath() + "text" + fileSeparator;
	}

	/**
	 * ソフトのレイアウトに用いる情報が格納されているディレクトリのパスを返す
	 * @return ソフトのレイアウトに用いる情報が格納されているディレクトリのパス
	 */
	public String getSystemPath() {
		return getDataPath() + "system" + fileSeparator;
	}

	/**
	 * 背景画像が格納されているディレクトリのパスを返す
	 * @return 背景画像が格納されているディレクトリのパス
	 */
	public String getBackgroundPath() {
		return getSystemPath() + "background" + fileSeparator;
	}

	/**
	 * カレンダー画面背景画像が格納されているディレクトリのパスを返す
	 * @return カレンダー画面背景画像が格納されているディレクトリのパス
	 */
	public String getCalendarBackgroundPath() {
		return getBackgroundPath() + "calendar" + fileSeparator;
	}

	/**
	 * 日記編集画面背景画像が格納されているディレクトリのパスを返す
	 * @return 日記編集画面背景画像が格納されているディレクトリのパス
	 */
	public String getDiaryWriteBackgroundPath() {
		return getBackgroundPath() + "diaryWrite" + fileSeparator;
	}

	/**
	 * 検索画面背景画像が格納されているディレクトリのパスを返す
	 * @return 検索画面背景画像が格納されているディレクトリのパス
	 */
	public String getSearchBackgroundPath() {
		return getBackgroundPath() + "search" + fileSeparator;
	}

	/**
	 * 設定画面の背景画像が格納されているディレクトリのパスを返す
	 * @return 設定画面の背景画像が格納されているディレクトリのパス
	 */
	public String getSettingBackgroundPath() {
		return getBackgroundPath() + "setting" + fileSeparator;
	}

	/**
	 * デフォルトの背景画像が格納されているディレクトリのパスを返す
	 * @return デフォルトの背景画像が格納されているディレクトリのパス
	 */
	public String getDefaultBackgroundPath() {
		return getBackgroundPath() + "default" + fileSeparator;
	}

	/**
	 * 設定情報が格納されているディレクトリのパスを返す
	 * @return 設定情報が格納されているディレクトリのパス
	 */
	public String getSettingPath() {
		return getSystemPath() + "setting" + fileSeparator;
	}

	/**
	 * 全般設定情報が格納されているディレクトリのパスを返す
	 * @return 全般設定情報が格納されているディレクトリのパス
	 */
	public String getCommonSettingPath() {
		return getSettingPath() + "common" + fileSeparator;
	}

	/**
	 * 日記編集設定情報が格納されているディレクトリのパスを返す
	 * @return 日記編集設定情報が格納されているディレクトリのパス
	 */
	public String getDiaryWriteSettingPath() {
		return getSettingPath() + "diaryWrite" + fileSeparator;
	}

	/**
	 * 日記の写真が格納されているディレクトリのパスを返す
	 * @return 日記の写真が格納されているディレクトリのパス
	 */
	public String getImagePath() {
		return getDataPath() + "image" + fileSeparator;
	}

	/**
	 * 取扱説明書のパスを返す
	 * @return 取扱説明書のパス
	 */
	public String getPdfPath() {
		return getBasePath() + "取扱説明書.pdf";
	}

	/**
	 * テキストの指定された年のパスを返す
	 * @param year 年
	 * @return テキストの指定された年のパス
	 */
	public String getYearTextPath(int year) {
		return getTextPath() + year + "年" + fileSeparator;
	}

	/**
	 * テキストの指定された月のパスを返す
	 * @param year 年
	 * @param month 月
	 * @return テキストの指定された年月のパス
	 */
	public String getMonthTextPath(int year, int month) {
		return getYearTextPath(year) + month + "月" + fileSeparator;
	}

	/**
	 * 指定された日付のテキストパスを返す
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return テキストの指定された日付のパス
	 */
	public String getDiaryTextPath(int year, int month, int day) {
		return getMonthTextPath(year, month) + year + "年" + month + "月" + day + "日.txt";
	}

	/**
	 * 指定された日付のテキストパスを返す
	 * @param date 日付
	 * @return テキストの指定された日付のパス
	 */
	public String getDiaryTextPath(Date date) {
		return getDiaryTextPath(date.getYear(), date.getMonth(), date.getDay());
	}

	/**
	 * 四捨五入
	 * @param a 四捨五入したい値
	 * @return 四捨五入した結果
	 */
	public int rounding(double a) {
		return (int)(a + 0.5);
	}

	/**
	 * ハッシュ化
	 * @param a ハッシュ化したい文字列
	 * @return ハッシュ化した結果の文字列
	 */
	public String crypt(String a) {
		MessageDigest md = null;
		StringBuilder sb = null;
		try{
			// SHA-1でハッシュ化
			md = MessageDigest.getInstance("SHA-1");
			md.update(a.getBytes());
			sb = new StringBuilder();
			for (byte b : md.digest()) {
				String hex = String.format("%02x", b);
				sb.append(hex);
			}
		}
		catch(Exception error) {
			error.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * ディレクトリ名もしくはファイル名を返す
	 * @param path パスのディレクトリ名もしくはファイル名を抜き取る<br>
	 * 拡張子も込みで返します
	 * @return ディレクトリ名もしくはファイル名
	 */
	public String getName(String path) {
		String tmp = path;
		if(tmp.lastIndexOf(setting.getFileSeparator()) == (tmp.length() - 1)) {
			tmp = tmp.substring(0, tmp.length() - 1);
		}
		return tmp.toString().substring(tmp.toString().lastIndexOf(setting.getFileSeparator()) + 1);
	}
}
