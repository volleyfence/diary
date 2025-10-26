package diary.system.file;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;

import diary.frame.StartEndFrame;
import diary.image.BackgroundImage;
import diary.image.Icon;
import diary.system.Setting;
import diary.system.calendar.Date;
import diary.system.calendar.MyCalendar;
import diary.system.image.ImageData;
import diary.system.image.ImageProcessing;
import diary.system.image.ImageType;
import diary.system.search.SearchData;
import diary.system.search.SearchResult;

/**
 * ファイル操作を行うクラス
 * @author Masato Suzuki
 */
public class FileControl {
	/**
	 * FileControlクラスのオブジェクト
	 */
	private static FileControl fc = new FileControl();

	/**
	 * Settingクラスのオブジェクト
	 */
	private Setting setting = Setting.getInstance();

	/**
	 * 日付ボタンの描画情報を格納する<br>
	 * 配列には2進数の考えで格納する<br><br>
	 * 0:何もなし<br>
	 * 1:今日<br>
	 * 2:テキスト記録済み<br>
	 * 3:今日&テキスト記録済み<br>
	 * 4:写真記録済み<br>
	 * 5:今日&写真記録済み<br>
	 * 6:テキスト記録済み&写真記録済み<br>
	 * 7:今日&テキスト記録済み&写真記録済み
	 */
	private BufferedImage[] dayImage;

	/**
	 *  パネルの背景画像を格納<br><br>
	 *  calendar:カレンダー画面背景画像<br>
	 *  diaryWrite:日記編集画面背景画像<br>
	 *  search:検索画面背景画像<br>
	 *  setting:設定画面の背景画像<br>
	 *  default:デフォルトの背景画像
	 */
	private Hashtable<String, BackgroundImage> backgroundImage;

	/**
	 *  ボタンのアイコン画像を格納<br><br>
	 *  ボタンの名前を引数にする<br>
	 *  ex) 「○○.png」では○○を引数にする
	 */
	private Hashtable<String, Icon> icon;

	/**
	 *  本ソフトウェアで用いるディレクトリのパス<br><br>
	 *  0:ソフトのレイアウトに用いる情報が格納されているディレクトリのパス<br>
	 *  1:日記情報（テキスト）が格納されているディレクトリのパス<br>
	 *  2:背景画像が格納されているディレクトリのパス<br>
	 *  3:カレンダー画面背景画像が格納されているディレクトリのパス<br>
	 *  4:日記編集画面背景画像が格納されているディレクトリのパス<br>
	 *  5:検索画面背景画像が格納されているディレクトリのパス<br>
	 *  6:設定画面の背景画像が格納されているディレクトリのパス<br>
	 *  7:デフォルトの背景画像が格納されているディレクトリのパス<br>
	 *  8:日記の写真が格納されているディレクトリのパス<br>
	 *  9:設定情報が格納されているディレクトリのパス<br>
	 *  10:全般設定情報が格納されているディレクトリのパス<br>
	 *  11:日記編集設定情報が格納されているディレクトリのパス
	 */
	private String[] directoryPath  = {
			setting.getSystemPath(),
			setting.getTextPath(),
			setting.getBackgroundPath(),
			setting.getCalendarBackgroundPath(),
			setting.getDiaryWriteBackgroundPath(),
			setting.getSearchBackgroundPath(),
			setting.getSettingBackgroundPath(),
			setting.getDefaultBackgroundPath(),
			setting.getImagePath(),
			setting.getSettingPath(),
			setting.getCommonSettingPath(),
			setting.getDiaryWriteSettingPath()
	};

	/**
	 *  各パネルの背景画像が格納されているディレクトリ名<br><br>
	 *  0:カレンダー画面<br>
	 *  1:日記編集画面<br>
	 *  2:検索画面<br>
	 *  3:設定画面<br>
	 *  4:デフォルト
	 */
	private String[] backgroundName = {
			setting.getName(setting.getCalendarBackgroundPath()),
			setting.getName(setting.getDiaryWriteBackgroundPath()),
			setting.getName(setting.getSearchBackgroundPath()),
			setting.getName(setting.getSettingBackgroundPath()),
			setting.getName(setting.getDefaultBackgroundPath())
	};

	/**
	 * アイコン画像の名前とアイコン画像の種類<br><br>
	 * 0:最初アイコン<br>
	 * 1:前アイコン<br>
	 * 2:次アイコン<br>
	 * 3:現在アイコン<br>
	 * 4:検索アイコン<br>
	 * 5:設定アイコン<br>
	 * 6:編集アイコン<br>
	 * 7:削除アイコン<br>
	 * 8:保存アイコン<br>
	 * 9:写真アイコン<br>
	 * 10:戻るアイコン<br>
	 * 11:日付順アイコン<br>
	 * 12:文字数順アイコン<br>
	 * 13:ソートアイコン<br>
	 * 14:今日<br>
	 * 15:記録済みテキスト<br>
	 * 16:記録済み写真<br>
	 * 17:ファビコン<br>
	 * 18:ロゴ
	 */
	private String[][] iconName = {
			// {"アイコン名", "アイコン画像の種類"}
			{"first", "0"},
			{"pre", "0"},
			{"next", "0"},
			{"now", "0"},
			{"search", "0"},
			{"setting", "0"},
			{"help", "0"},
			{"write", "0"},
			{"picture", "0"},
			{"delete", "0"},
			{"save", "0"},
			{"addPicture", "0"},
			{"back", "0"},
			{"dateSort", "0"},
			{"charSort", "0"},
			{"upDownSort", "0"},
			{"today", "1"},
			{"doneText", "1"},
			{"donePicture", "1"},
			{"favicon", "2"},
			{"logo", "2"}
	};

	/**
	 * 設定項目と設定の分類<br><br>
	 * 0:文字コード<br>
	 * 1:フォント名<br>
	 * 2:文字の太さ<br>
	 * 3:文字サイズ<br>
	 * 4:タイトル<br>
	 * 5:アイコン画像の表示可否<br>
	 * 6:ツールチップの表示可否<br>
	 * 7:パスワード<br>
	 * 8:日記の写真の表示可否
	 */
	private String[][] settingName = {
			// {"設定識別キー", "設定の分類"}
			// 全般設定
			{"charCode", setting.getName(setting.getCommonSettingPath())},
			{"defaultFontName", setting.getName(setting.getCommonSettingPath())},
			{"defaultFontStyle", setting.getName(setting.getCommonSettingPath())},
			{"defaultFontSize", setting.getName(setting.getCommonSettingPath())},
			{"title", setting.getName(setting.getCommonSettingPath())},
			{"showIcon", setting.getName(setting.getCommonSettingPath())},
			{"showToolTip", setting.getName(setting.getCommonSettingPath())},
			{"passwordLock", setting.getName(setting.getCommonSettingPath())},

			// 日記編集設定
			{"showDiaryImage", setting.getName(setting.getDiaryWriteSettingPath())}
	};

	/**
	 * コンストラクタ
	 */
	private FileControl() {
		// 日記に必要なデータの生成
		createDiaryFile();

		// アイコン画像の取得
		icon = new Hashtable<String, Icon>();
		for(int i = 0; i < iconName.length; i++) {
			icon.put(iconName[i][0], new Icon(iconName[i]));
		}

		// 日付ボタンの背景の生成、配列には2進数の考えで格納する
		dayImage = new BufferedImage[8];
		dayImage[0] = getDayButtonImage(false, false, false);
		dayImage[1] = getDayButtonImage(true, false, false);
		dayImage[2] = getDayButtonImage(false, true, false);
		dayImage[3] = getDayButtonImage(true, true, false);
		dayImage[4] = getDayButtonImage(false, false, true);
		dayImage[5] = getDayButtonImage(true, false, true);
		dayImage[6] = getDayButtonImage(false, true, true);
		dayImage[7] = getDayButtonImage(true, true, true);

		// 各パネルの背景画像の取得
		backgroundImage = new Hashtable<String, BackgroundImage>();
		for(int i = 0; i < backgroundName.length; i++) {
			backgroundImage.put(backgroundName[i], new BackgroundImage(setting.getBackgroundPath() + backgroundName[i]));
		}

		checkSettingFile();
	}

	/**
	 * インスタンスを返す
	 * @return インスタンス
	 */
	public static FileControl getInstance() {
		return fc;
	}

	/**
	 * 本ソフトウェアで用いるディレクトリ、ファイルの作成
	 */
	public void createDiaryFile(){
		for(int i = 0; i < directoryPath.length; i++) {
			File file = new File(directoryPath[i]);
			file.mkdirs();
		}

		try {
			File[] files = (new File(setting.getDefaultBackgroundPath())).listFiles();
			if(files == null || files.length == 0) {
				OutputStream out = new FileOutputStream(setting.getDefaultBackgroundPath() + "background.gif");
				ImageIO.write(createIcon(ImageData.getDefaultBackground()), "gif", out);
			}
		}
		catch(Exception error) {
			error.printStackTrace();
		}

		if(!new File(setting.getPdfPath()).exists()) {
			createPdf();
		}
	}

	/**
	 * 設定ファイルの読み込み&新規作成
	 */
	public void checkSettingFile() {
		for(int i = 0; i < settingName.length; i++) {
			String tmpString = readSettingString(settingName[i][0], settingName[i][1]);
			try {
				if(tmpString != null) {
					setting.changeSetting(settingName[i][0], tmpString);
				}
			}
			catch(Exception error) {
				error.printStackTrace();
			}
		}

		updateSettingFile();
	}

	/**
	 * 設定ファイルを読み込む
	 * @param key 設定の識別キー
	 * @param dirName 設定ファイルが格納されているディレクトリ名
	 * @return 読み取ったデータ
	 */
	private String readSettingString(String key, String dirName) {
		File file = null;
		String tmp = null;
		file = new File(setting.getSettingPath() + dirName + setting.getFileSeparator() + key + ".txt");

		// ファイルの読み込み
		if(file.exists()) {
			InputStreamReader fr;
			BufferedReader br;
			try {
				fr = new InputStreamReader(new FileInputStream(file.toString()), setting.getCharCode());
				br = new BufferedReader(fr);
				try {
					String line;
					String[] str;
					do {
						line = br.readLine();
						if (line == null) {
							tmp = null;
							break;
						}
						str = line.split(":");
						if(str.length == 2) {
							if(str[0].replaceAll("　", "").trim().equals(key)) {
								tmp = str[1].replaceAll("　", "").trim();
								break;
							}
						}

					} while(true);
				}
				catch(Exception error) {
					error.printStackTrace();
				}
				// ファイルを閉じる
				br.close();
			}
			catch(Exception error) {
				error.printStackTrace();
			}
			br = null;
			fr = null;
		}

		return tmp;
	}

	/**
	 * 設定ファイルの更新
	 */
	public void updateSettingFile() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < settingName.length; i++) {
					createSettingFile(settingName[i][0], setting.getSetting(settingName[i][0]));
				}
			}
		}).start();
	}

	/**
	 * 設定ファイルを作成する
	 * @param key 設定の識別キー
	 * @param out 変更する値
	 */
	public void createSettingFile(String key, String out) {
		File file = null;
		String dirName = null;
		for(int i = 0; i < settingName.length; i++) {
			if(settingName[i][0].equals(key)) {
				dirName = settingName[i][1];
			}
		}
		file = new File(setting.getSettingPath() + dirName + setting.getFileSeparator() + key + ".txt");

		PrintWriter pw = null;
		try {
			if(out != null && !out.equals("")) {
				pw = new PrintWriter(file, setting.getCharCode());
				if(key.toLowerCase().indexOf("color") >= 0) {

				}
				else {
					if(key.equals("defaultFontName")) {
						pw.println("以下のフォント名に設定します");
					}
					else if(key.equals("defaultFontStyle")) {
						pw.println("以下の文字の太さに設定します");
						pw.println("太いなら「BOLD」, 普通なら「PLAIN」です");
					}
					else if(key.equals("defaultFontSize")) {
						pw.println("以下の文字の大きさに設定します");
						pw.println(setting.getMinFontSize() + "～" + setting.getMaxFontSize() + "の範囲で設定してください");
					}
					else if(key.equals("title")) {
						pw.println("本ソフトウェアのタイトルを以下の通り設定します");
					}
					else if(key.equals("showIcon")) {
						pw.println("ボタンのアイコン画像を表示するか設定します");
						pw.println("表示するなら「TRUE」, 表示しないなら「FALSE」です");
					}
					else if(key.equals("showToolTip")) {
						pw.println("ボタンの説明を表示するか設定します");
						pw.println("表示するなら「TRUE」, 表示しないなら「FALSE」です");
					}
					else if(key.equals("charCode")) {
						pw.println("以下の文字コードでファイルを読み書きします");
						pw.println("このファイルを編集すると設定がリセットされることがあります");
					}
					else if(key.equals("passwordLock")) {
						pw.println("パスワードを設定します");
						pw.println("このファイルは絶対に編集しないでください");
					}
					else if(key.equals("showDiaryImage")) {
						pw.println("日記の写真をを表示するか設定します");
						pw.println("表示するなら「TRUE」, 表示しないなら「FALSE」です");
					}
					pw.println();
					pw.println(key + " : " + out);
				}
				pw.close();
				pw = null;
			}
			else {
				file.delete();
			}
		}
		catch(Exception error) {
			error.printStackTrace();
		}
		file = null;
	}

	/**
	 * ボタンに用いるアイコン画像を作成します
	 * @param a イメージデータ
	 * @return アイコン画像
	 */
	public BufferedImage createIcon(int[][] a) {
		BufferedImage image = new BufferedImage(a.length, a[0].length, BufferedImage.TYPE_INT_ARGB_PRE);
		for(int y = 0; y < a.length; y++){
			for(int x = 0; x < a[0].length; x++){
				image.setRGB(x, y, a[y][x]);
			}
		}

		return image;
	}

	/**
	 * 取扱説明書を生成する
	 * @return 作成に成功したかどうか
	 */
	public boolean createPdf() {
		return createFromJar("/取扱説明書.pdf", setting.getPdfPath());
	}

	/**
	 * jarファイルからファイルをコピーする
	 * @param resource コピー元のパス（jarファイル内の絶対パス）
	 * @param outPath コピー先のパス（絶対パス）
	 * @return 作成に成功したかどうか
	 */
	public boolean createFromJar(String resource, String outPath) {
		boolean result = false;
		try {
			InputStream input = getClass().getResourceAsStream(resource);
			if(input != null) {
				File file = new File(outPath);
				OutputStream out = new FileOutputStream(file);
				int read;
				byte[] bytes = new byte[1024];

				while ((read = input.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.close();
				result = true;
			}
		}
		catch (Exception error) {
			error.printStackTrace();
		}

		return result;
	}

	/**
	 * 本ソフトウェアで用いるディレクトリかどうか
	 * @param key 判別するファイルのパス
	 * @return 本ソフトウェアで用いるディレクトリかどうか
	 */
	public boolean checkDirectory(String key) {
		for(int i = 0; i < directoryPath.length; i++) {
			// 末尾に区切りがある時に一致しないためその対策
			if(new File(key).toString().equals(new File(directoryPath[i]).toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 不要なディレクトリ&ファイルの全削除
	 * @param sef StartEndFrameクラスのオブジェクト
	 */
	public void deleteAllEmptyDirectory(StartEndFrame sef) {
		if(sef != null) {
			/*
			 * FileControlで8工程（ディレクトリ削除）
			 * ※他の処理も加えてもよいが面倒&これで十分なので除外
			 */
			sef.setMax(8);
		}

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				deleteEmptyDirectory(setting.getTextPath(), 0);
				if(sef != null) {
					sef.add();
				}
			}
		});
		t1.start();

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				deleteEmptyDirectory(setting.getImagePath(), 1);
				if(sef != null) {
					sef.add();
				}
			}
		});
		t2.start();

		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				deleteEmptyDirectory(setting.getSystemPath(), 2);
				if(sef != null) {
					sef.add();
				}
			}
		});
		t3.start();

		try {
			t1.join();
			t2.join();
			t3.join();
		}
		catch(Exception error) {

		}

		// 空ディレクトリの削除
		for(int i = 0; i < 5; i++) {
			deleteEmptyDirectory(setting.getDataPath(), 4);
			if(sef != null) {
				sef.add();
			}
		}
	}

	/**
	 * 不要なディレクトリ&ファイルの削除<br>
	 * 指定されたディレクトリの最下層までの不要なディレクトリ&ファイルを削除します<br>
	 * なお、オプションによって削除するものの基準を設定できます<br>
	 * 削除基準によらず、空のディレクトリ、
	 * @param target 対象のディレクトリパス
	 * @param option 削除するものの基準<br>
	 * 0：本ソフトウェアで取り扱うことのできる日記テキストファイル以外は全て削除します<br>
	 * なお、空の日記テキストファイルも削除されます<br>
	 * 1：本ソフトウェアで取り扱うことのできる日記の写真以外は全て削除します<br>
	 * 2：本ソフトウェアで取り扱うことのできる形式のファイル以外全て削除します<br>
	 * 3：全てのファイルを削除します<br>
	 * 4：空ディレクトリの削除のみ行います
	 */
	public void deleteEmptyDirectory(String target, int option) {
		try {
			File dir = new File(target);
			File[] files = dir.listFiles();

			// 存在しない場合
			if (files == null) {
				return;
			}

			// 中に何も入っていなく、本ソフトウェアで用いるディレクトリでない場合
			else if (files.length == 0 && !checkDirectory(target)) {
				dir.delete();
				return;
			}

			// オプション4（空ディレクトリ削除のみ）以外の場合
			else if(option != 4) {
				for(int i = 0; i < files.length; i++) {
					// ファイルかどうかの判定
					if(files[i].isFile()) {
						String filePath = files[i].toString();

						// 本ソフトウェアで取り扱うことのできる日記テキストファイル以外は全て削除
						if(option == 0) {
							if(setting.getName(filePath).matches("^[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日.txt")) {
								InputStreamReader fr  = new InputStreamReader(new FileInputStream(filePath), setting.getCharCode());
								BufferedReader br = new BufferedReader(fr);
								String line;
								int charCount = 0;
								try {
									do {
										line = br.readLine();
										if (line == null || charCount > 0) {
											if(charCount == 0) {
												br.close();
												files[i].delete();
											}
											break;
										}
										charCount += line.replaceAll("　", "").replaceAll(" ", "").trim().length();
									} while(true);
								}
								catch(Exception error) {

								}
								br.close();
							}
							else {
								files[i].delete();
							}
						}

						// 本ソフトウェアで取り扱うことのできる日記の写真以外は全て削除
						else if(option == 1) {
							if(!(TypeCheck.checkImage(filePath) && setting.getName(filePath).substring(0, setting.getName(filePath).indexOf(".")).matches("^[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日"))) {
								files[i].delete();
							}
						}

						// 本ソフトウェアで取り扱うことのできる形式のファイル以外全て削除
						else if(option == 2) {
							if(!(TypeCheck.checkImage(filePath) || TypeCheck.checkText(filePath))) {
								files[i].delete();
							}
						}

						// 全てのファイルを削除
						else if(option == 3) {
							files[i].delete();
						}
					}
				}
			}

			for(File file : files) {
				if (file.isDirectory()) {
					deleteEmptyDirectory(file.getPath(), option);
				}
			}

			// 再帰をスレッド処理しようと思ったが断念
			/*Thread[] t = new Thread[files.length];
			int i;

			for(i = 0; i < files.length; i++) {
				File[] filesTmp = files;
				int index = i;
				t[i] = new Thread(new Runnable() {
					@Override
					public void run() {
						if (filesTmp[index].isDirectory()) {
							deleteEmptyDirectory(filesTmp[index].getPath(), option);
						}
					}
				});
				t[i].start();
			}

			for(i = 0; i < files.length; i++) {
				try {
					t[i].join();
				}
				catch(Exception error) {
					error.printStackTrace();
				}
			}*/

			files = null;
			dir = null;
		}
		catch(Exception error) {
			error.printStackTrace();
		}
	}

	/**
	 * 日記を読み込む
	 * @param year 読み込む日記の年
	 * @param month 読み込む日記の月
	 * @param day 読み込む日記の日
	 * @param diaryText 読み込んだテキストを出力するテキストエリア
	 * @return 読み込み処理の結果<br>
	 * 0：正常終了<br>
	 * -1：文字化け
	 */
	public int read(int year, int month, int day, JTextArea diaryText) {
		int frag = 0;
		// テキストエリアの削除
		diaryText.setText("");

		File dataFile = new File(setting.getTextPath() + year + "年" + setting.getFileSeparator() + month + "月" + setting.getFileSeparator() + year + "年" + month + "月" + day + "日.txt");
		if(dataFile.exists() == true) {
			// 日記の読み込み
			InputStreamReader fr;
			BufferedReader br;
			try {
				fr  = new InputStreamReader(new FileInputStream(dataFile.toString()), setting.getCharCode());
				br = new BufferedReader(fr);
				try {
					String line;
					do {
						line = br.readLine();
						if (line == null) {
							break;
						}
						if(line.indexOf((char)(0xfffd)) >= 0) {
							frag = -1;
						}
						diaryText.append(new String(line.getBytes(setting.getCharCode()), setting.getCharCode()) + setting.getLineSeparator());
					} while(true);
				}
				catch(Exception error) {
					error.printStackTrace();
				}
				// ファイルを閉じる
				br.close();
			}
			catch(Exception error) {
				error.printStackTrace();
			}
			br = null;
			fr = null;
		}
		dataFile = null;
		return frag;
	}

	/**
	 * 日記を保存する
	 * @param year 保存する日記の年
	 * @param month 保存する日記の月
	 * @param day 保存する日記の日
	 * @param diaryText 保存する日記のテキストエリア
	 */
	public void save(int year, int month, int day, JTextArea diaryText) {
		try {
			if(diaryText.getText().replaceAll("　", "").trim().equals("") == false) {
				// 日記を保存するディレクトリの作成
				File diaryDirectory = new File(setting.getMonthTextPath(year, month));
				diaryDirectory.mkdirs();

				// ファイルに書き込む準備
				File diaryFile = new File(setting.getDiaryTextPath(year, month, day));
				PrintWriter pw = new PrintWriter(diaryFile, setting.getCharCode());

				try {
					// 入力された日記を取得
					String diary = diaryText.getText();

					// 改行コードで区切る
					String[] strs = diary.split(setting.getLineSeparator());

					// ファイルへ書き込み
					int last = strs.length - 1;
					for (int i = 0 ; i < last; i++){
						pw.println(new String(strs[i].getBytes(setting.getCharCode()), setting.getCharCode()));
					}
					// 最後の1行のみ別に処理（改行なしの出力）
					if(strs.length >= 1) {
						pw.write(new String(strs[last].getBytes(setting.getCharCode()), setting.getCharCode()));
					}

					/*
					 * 最後の改行の連続はstrs.lengthではカウントされていなく、
					 * diaryText.getLineCount()が正確な行数なのでその差分を改行することで行数を合わせる
					 */
					for(int j = strs.length; j < diaryText.getLineCount(); j++) {
						pw.println();
					}

					diary = null;
					strs = null;
				}
				catch(Exception error) {
					error.printStackTrace();
				}

				// ファイルを閉じる
				pw.close();

				pw = null;
				diaryFile = null;
				diaryDirectory = null;
			}

			// 保存される日記が空のとき
			else {
				deleteText(year, month, day);
			}

		}
		catch(Exception error) {
			error.printStackTrace();
		}
	}

	/**
	 * 写真を保存する
	 * @param year 保存する写真の年
	 * @param month 保存する写真の月
	 * @param day 保存する写真の日
	 * @param imageFile 保存する写真のファイル
	 */
	public void saveImage(int year, int month, int day, File imageFile) {
		File saveFile = new File(setting.getImagePath() + year + "年" + setting.getFileSeparator() + month + "月");
		saveFile.mkdirs();
		try {
			String type = ImageType.getFormat(imageFile).toString().toLowerCase();
			OutputStream out = new FileOutputStream(saveFile.toString() + setting.getFileSeparator() + year + "年" + month + "月" + day + "日." + type);
			ImageIO.write(ImageIO.read(imageFile), type, out);
			out.close();
		}
		catch(Exception error) {
			error.printStackTrace();
		}
	}

	/**
	 * 存在する全ての日記の日付を返す
	 * @return 存在する全ての日記の日付
	 */
	public Date[] getAllTextDate() {
		// 配列を動的に確保する
		ArrayList<Date> result = new ArrayList<Date>();

		Date[] resultDate = null;

		try {
			// textディレクトリ内のリスト
			File textDir = new File(setting.getTextPath());
			File[] yearDirs = textDir.listFiles();

			int minYear = 9999;
			int maxYear = 0;

			if(yearDirs != null) {
				// 最小の年と最大の年を取得
				for(int i = 0; i < yearDirs.length; i++) {
					String name = setting.getName(yearDirs[i].toString());
					if(yearDirs[i].isDirectory() && name.matches("^[0-9]{4}年")) {
						int year = Integer.parseInt(name.replaceAll("年", ""));
						if(year < minYear) {
							minYear = year;
						}
						if(year > maxYear) {
							maxYear = year;
						}
					}
				}
			}

			yearDirs = null;
			textDir = null;

			for(int year = minYear; year <= maxYear; year++) {
				File yearDir = new File(setting.getYearTextPath(year));
				if(yearDir.exists()) {
					for(int month = 1; month <= 12; month++) {
						File monthDir = new File(setting.getMonthTextPath(year, month));
						if(monthDir.exists()) {
							for(int day = 1; day <= MyCalendar.getLastDay(year, month); day++) {
								File diaryFile = new File(setting.getDiaryTextPath(year, month, day));
								if(diaryFile.exists() && diaryFile.isFile() && diaryFile.length() != 0) {
									// 日記の追加
									Date diaryDate = new Date(year, month, day);
									result.add(diaryDate);
								}
							}
						}
					}
				}
			}

			if(result.size() > 0) {
				resultDate = new Date[result.size()];
				for(int i = 0; i < result.size(); i++) {
					resultDate[i] = result.get(i);
				}
			}
		}
		catch(Exception error) {
			error.printStackTrace();
		}

		return resultDate;
	}

	/**
	 * 検索
	 * @param key 検索キー
	 * @return 検索結果
	 */
	public SearchResult search(String key) {
		// 配列を動的に確保する
		ArrayList<SearchData> result = new ArrayList<SearchData>();

		// ソートを行うための配列
		int[][] resultStringCharTmp = null;

		// ソートに用いるインデックスを格納する
		int[] dateSortIndex = null;
		int[] charSortIndex = null;
		int[][] sortIndex = null;

		// 日記データを格納する
		SearchData[] searchData = null;

		try {
			Date[] diaryDate = getAllTextDate();

			if(diaryDate != null) {
				for(int i = 0; i < diaryDate.length; i++) {
					String path = setting.getDiaryTextPath(diaryDate[i]);
					InputStreamReader fr = new InputStreamReader(new FileInputStream(path), setting.getCharCode());
					BufferedReader br = new BufferedReader(fr);

					// {日付, 本文, 文字数}
					SearchData tmp = new SearchData(diaryDate[i]);
					String line;

					int frag = 0;
					do {
						line = br.readLine();
						if(line == null) {
							break;
						}
						tmp.addText(line + "<br>");
						tmp.addCharCount(line.replaceAll("　", "").trim().replaceAll(" ", "").replaceAll("\t", "").length());
						if (line.indexOf(key) >= 0) {
							frag++;
						}
					} while(true);

					// keyが含まれる日記なら検索結果に追加する
					if(frag > 0) {
						result.add(tmp);
					}

					// ファイルを閉じる
					br.close();

					line = null;
					tmp = null;
					fr = null;
					br = null;
				}
			}

			if(result.size() > 0) {
				// {日付, 本文, 文字数}
				searchData = new SearchData[result.size()];

				for(int i = 0; i < result.size(); i++) {
					searchData[i] = result.get(i);
				}

				dateSortIndex = new int[searchData.length];
				charSortIndex = new int[searchData.length];

				// ソートのための配列生成 {比較用日付, 文字数, インデックス}
				resultStringCharTmp = new int[searchData.length][3];

				for(int i = 0; i < searchData.length; i++) {
					resultStringCharTmp[i][0] = searchData[i].getDate().getCompareDate();
					resultStringCharTmp[i][1] = searchData[i].getCharCount();
					resultStringCharTmp[i][2] = i;
					dateSortIndex[i] = i;
				}

				if(resultStringCharTmp.length > 1) {
					// 文字数ソート
					sort(resultStringCharTmp, 0, resultStringCharTmp.length - 1, 1);
				}

				// インデックスだけを抜き取る
				for(int i = 0; i < resultStringCharTmp.length; i++) {
					charSortIndex[i] = resultStringCharTmp[i][2];
				}

				sortIndex = new int[2][searchData.length];
				sortIndex[0] = dateSortIndex;
				sortIndex[1] = charSortIndex;

				result = null;
				resultStringCharTmp = null;
				diaryDate = null;
				dateSortIndex = null;
				charSortIndex = null;
			}
		}
		catch (Exception error) {
			error.printStackTrace();
		}
		return new SearchResult(searchData, sortIndex);
	}

	/**
	 * 検索ヒットデータ
	 * @param key 検索キー
	 * @return 検索ヒットデータ
	 */
	public SearchData[] searchData(String key) {
		// 配列を動的に確保する
		ArrayList<SearchData> result = new ArrayList<SearchData>();

		// 日記データを格納する
		SearchData[] searchData = null;

		try {
			Date[] diaryDate = getAllTextDate();

			if(diaryDate != null) {
				for(int i = 0; i < diaryDate.length; i++) {
					String path = setting.getDiaryTextPath(diaryDate[i]);
					InputStreamReader fr = new InputStreamReader(new FileInputStream(path), setting.getCharCode());
					BufferedReader br = new BufferedReader(fr);

					// {日付, 本文, 文字数} 今回は本文と文字数は無視
					SearchData tmp = new SearchData(diaryDate[i]);
					String line;

					do {
						line = br.readLine();

						if(line == null) {
							break;
						}

						if (line.indexOf(key) >= 0) {
							result.add(tmp);
							break;
						}
					} while(true);

					// ファイルを閉じる
					br.close();

					line = null;
					tmp = null;
					fr = null;
					br = null;
				}

				if(result.size() > 0) {
					searchData = new SearchData[result.size()];
					for(int i = 0; i < result.size(); i++) {
						searchData[i] = result.get(i);
					}
				}
			}
		}
		catch (Exception error) {
			error.printStackTrace();
		}
		return searchData;
	}

	/**
	 * 最初の日記の日付を返す
	 * @return 最初の日記の日付
	 */
	public Date getFirstDiary() {
		Date firstDay = null;

		try {
			// textディレクトリ内のリスト
			File textDir = new File(setting.getTextPath());
			File[] yearDirs = textDir.listFiles();

			int year = 9999;
			File minYearFile = null;

			if(yearDirs != null) {
				// yearディレクトリ内のリスト
				for(int i = 0; i < yearDirs.length; i++) {
					String name = setting.getName(yearDirs[i].toString());
					if(yearDirs[i].isDirectory() && name.matches("^[0-9]{4}年")) {
						if(Integer.parseInt(name.replaceAll("年", "")) < year) {
							year = Integer.parseInt(name.replaceAll("年", ""));
							minYearFile = yearDirs[i];
						}
					}
				}
			}

			File[] monthDirs = minYearFile.listFiles();
			File minMonthFile = null;

			int month = 99;

			if(monthDirs != null) {
				// monthディレクトリ内のリスト
				for(int i = 0; i < monthDirs.length; i++) {
					String name = setting.getName(monthDirs[i].toString());
					if(monthDirs[i].isDirectory() && name.matches("^[0-9]{1,2}月")) {
						if(Integer.parseInt(name.replaceAll("月", "")) < month) {
							month = Integer.parseInt(name.replaceAll("月", ""));
							minMonthFile = monthDirs[i];
						}
					}
				}
			}

			File[] diaryFiles = minMonthFile.listFiles();

			int day = 99;

			if(diaryFiles != null) {
				// 日記の読み込み
				for(int i = 0; i < diaryFiles.length; i++) {
					String name = setting.getName(diaryFiles[i].toString());

					// ファイルであること, テキストファイルであること, ファイルが正しいディレクトリにあること
					if(diaryFiles[i].isFile() && TypeCheck.checkText(diaryFiles[i].toString()) && name.substring(0, name.indexOf(".")).matches("^" + year + "年" + month + "月[0-9]{1,2}日")) {
						if(Integer.parseInt(name.substring(name.lastIndexOf("月") + 1, name.lastIndexOf("日"))) < day) {
							day = Integer.parseInt(name.substring(name.lastIndexOf("月") + 1, name.lastIndexOf("日")));
						}
					}
				}
			}

			if(year != 9999 && month != 99 && day != 99) {
				firstDay = new Date(year, month, day);
			}

			minYearFile = null;
			minMonthFile = null;
			diaryFiles = null;
			monthDirs = null;
			textDir = null;
			yearDirs = null;
		}
		catch(Exception error) {
			error.printStackTrace();
		}

		return firstDay;
	}

	//-------------------------------自分で使う時だけ------------------------------------
	/**
	 * 検索された文字列がある日記の削除（開発者専用機能）
	 * @param data 削除する日記のデータ
	 * @return 削除できたか<br>
	 * おそらく失敗することはないはず
	 */
	public boolean delete(SearchData[] data) {
		boolean result = false;
		try {
			for(int i = 0; i < data.length; i++) {
				deleteText(data[i].getDate());
			}
			result = true;
		}
		catch (Exception error) {
			error.printStackTrace();
		}

		return result;
	}
	//------------------------------------------------------------------------------------

	/**
	 * 指定された日付のテキストを削除します
	 * @param year 削除する日記の年
	 * @param month 削除する日記の月
	 * @param day 削除する日記の日
	 */
	public void deleteText(int year, int month, int day) {
		// ファイルの削除
		File diaryFile = new File(setting.getTextPath() + year + "年" + setting.getFileSeparator() + month + "月" + setting.getFileSeparator() + year + "年" + month + "月" + day + "日.txt");
		diaryFile.delete();
		diaryFile = null;
	}

	/**
	 * 指定された日付のテキストを削除します
	 * @param date 削除する日記の日付
	 */
	public void deleteText(Date date) {
		deleteText(date.getYear(), date.getMonth(), date.getDay());
	}

	/**
	 * 指定された日付の写真を削除します<br>
	 * 日付は同じで拡張子が同じ写真も全て削除される仕様です
	 * @param year 削除する写真の年
	 * @param month 削除する写真の月
	 * @param day 削除する写真の日
	 */
	public void deleteImage(int year, int month, int day) {
		//フィルタを作成する
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File file, String str){
				//指定文字列でフィルタする
				//indexOfは指定した文字列が見つからなかったら-1を返す
				if (str.indexOf(year + "年" + month + "月" + day + "日") != -1 && TypeCheck.checkImage(file.toString() + setting.getFileSeparator() + str)){
					return true;
				}
				else{
					return false;
				}
			}
		};

		File[] imageFiles = (new File(setting.getImagePath() + year + "年" + setting.getFileSeparator() + month + "月" + setting.getFileSeparator())).listFiles(filter);
		if(imageFiles != null) {
			for(File file : imageFiles) {
				file.delete();
			}
		}
	}

	/**
	 * 配列のソート(クイックソート)
	 * @param a ソートの対象
	 * @param left ソートする左端のインデックス
	 * @param right ソートする右端のインデックス
	 * @param index ソートのキーとなるインデックス
	 */
	public void sort(int[][] a, int left, int right, int index){
		try {
			int pl = left;
			int pr = right;

			int pivot = (a[pl][index] + a[pr][index]) / 2;

			while(true) {
				while(a[pl][index] < pivot) {
					pl++;
				}

				while(a[pr][index] > pivot) {
					pr--;
				}

				if(pr < pl) {
					break;
				}

				int[] tmp = a[pl];
				a[pl] = a[pr];
				a[pr] = tmp;

				pl++;
				pr--;
			}

			if(left < pr) {
				sort(a, left, pr, index);
			}

			if(pl < right) {
				sort(a, pl, right, index);
			}
		}
		catch(Exception error) {
			error.printStackTrace();
		}
	}

	/**
	 * カレンダー画面の背景画像が存在しているかを返す
	 * @return カレンダー画面の背景画像が存在しているか
	 */
	public boolean getCalendarBackgroundExists() {
		return backgroundImage.get("calendar").exists();
	}

	/**
	 * 日記編集画面の背景画像が存在しているかを返す
	 * @return 日記編集画面の背景画像が存在しているか
	 */
	public boolean getDiaryWriteBackgroundExists() {
		return backgroundImage.get("diaryWrite").exists();
	}

	/**
	 * 検索画面の背景画像が存在しているかを返す
	 * @return 検索画面の背景画像が存在しているか
	 */
	public boolean getSearchBackgroundExists() {
		return backgroundImage.get("search").exists();
	}

	/**
	 * 設定画面の背景画像が存在しているかを返す
	 * @return 設定画面の背景画像が存在しているか
	 */
	public boolean getSettingBackgroundExists() {
		return backgroundImage.get("setting").exists();
	}

	/**
	 * デフォルトの背景画像が存在しているかを返す
	 * @return デフォルトの背景画像が存在しているか
	 */
	public boolean getDefaultBackgroundExists() {
		return backgroundImage.get("default").exists();
	}

	/**
	 * 指定された日の日記テキストが存在しているかを返す
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 指定された日の日記テキストが存在しているか
	 */
	public boolean getDiaryTextExists(int year, int month, int day) {
		File file = new File(setting.getTextPath() + year + "年" + setting.getFileSeparator() + month + "月" + setting.getFileSeparator() + year + "年" + month + "月" + day + "日.txt");
		return file.exists();
	}

	/**
	 * 日記の写真が存在しているかを一月分返す
	 * @param year 日
	 * @param month 月
	 * @return 日記の写真が存在しているか（一月分）
	 */
	public boolean[] getDiaryImageExists(int year, int month) {
		File imageDir = new File(setting.getImagePath() + year + "年" + setting.getFileSeparator() + month + "月" + setting.getFileSeparator());

		// 月の写真の存在を日ごとに格納する（インデックス+1が日にち）
		boolean[] imageBoolean = new boolean[MyCalendar.getLastDay(year, month)];

		// falseで初期化
		for(int i = 0; i < imageBoolean.length; i++) {
			imageBoolean[i] = false;
		}

		// 月のディレクトリが存在するか
		if(imageDir.exists()) {
			//フィルタを作成する
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File file, String str){
					//指定文字列でフィルタする
					//indexOfは指定した文字列が見つからなかったら-1を返す
					if (str.indexOf(year + "年" + month + "月") != -1){
						return true;
					}
					else{
						return false;
					}
				}
			};

			// フィルタの条件に合致するファイルリスト
			File[] imageFiles = imageDir.listFiles(filter);

			if(imageFiles != null) {
				// 1行ずつ確認する
				for(int i = 0; i < imageFiles.length; i++) {
					try {
						// 雅号ファイルかどうか
						if(TypeCheck.checkImage(imageFiles[i].toString())) {
							String str = imageFiles[i].toString();

							// 何日の写真か
							int index = Integer.parseInt(str.substring(str.lastIndexOf("月") + 1, str.lastIndexOf("日"))) - 1;

							imageBoolean[index] = true;
						}
					} catch(Exception error) {
						error.printStackTrace();
					}
				}
			}

		}
		return imageBoolean;
	}

	/**
	 * 指定された日の写真が存在しているかを返す
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 指定された日の写真が存在しているか
	 */
	public boolean getDiaryImageExists(int year, int month, int day) {
		boolean[] imageExists = getDiaryImageExists(year, month);

		return imageExists[day - 1];
	}

	/**
	 * 指定された日記の写真ファイルを返す
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 指定された日記の写真ファイル
	 */
	public File getDiaryImageFile(int year, int month, int day) {
		File imageDir = new File(setting.getImagePath() + year + "年" + setting.getFileSeparator() + month + "月" + setting.getFileSeparator());
		if(imageDir.exists()) {
			//フィルタを作成する
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File file, String str){
					//指定文字列でフィルタする
					//indexOfは指定した文字列が見つからなかったら-1を返す
					if (str.indexOf(year + "年" + month + "月" + day + "日") != -1){
						return true;
					}
					else{
						return false;
					}
				}
			};

			File[] imageFiles = imageDir.listFiles(filter);
			for(int i = 0; i < imageFiles.length; i++) {
				if(TypeCheck.checkImage(imageFiles[i].toString())) {
					return imageFiles[i];
				}
			}
		}
		return null;
	}

	/**
	 * 日記の写真を返す
	 * @param diaryImage 日記の写真ファイル
	 * @return 日記の写真
	 */
	public BufferedImage getDiaryImage(File diaryImage) {
		try {
			BufferedImage image = ImageIO.read(diaryImage);
			return image;
		}
		catch(Exception error) {
			error.printStackTrace();
		}
		return null;
	}

	/**
	 * 指定されたアイコン画像を返す<br>
	 * 何も処理もしていないため色等の処理は受け取り先で行うように
	 * @param name アイコン画像のキー
	 * @return アイコン画像
	 */
	public BufferedImage getIconImage(String name) {
		return icon.get(name).getIcon();
	}

	/**
	 * カレンダーの日付ボタンの背景画像を返す
	 * @param today 今日かどうか
	 * @param doneText テキストが記録済みかどうか
	 * @param donePicture 写真が記録済みかどうか
	 * @return 日付ボタンの背景画像
	 */
	public synchronized Image getDayImage(boolean today, boolean doneText, boolean donePicture) {
		int index = 0;
		if(today) {
			index += 1;
		}

		if(doneText) {
			index += 2;
		}

		if(donePicture) {
			index += 4;
		}

		return dayImage[index];
	}

	/**
	 * カレンダー画面の背景画像を返す
	 * @return カレンダー画面の背景画像
	 */
	public BufferedImage getCalendarBackgroundImage() {
		return backgroundImage.get(backgroundName[0]).nextImage();
	}

	/**
	 * 日記編集画面の背景画像を返す
	 * @return 日記編集画面の背景画像
	 */
	public BufferedImage getDiaryWriteBackgroundImage() {
		return backgroundImage.get(backgroundName[1]).nextImage();
	}

	/**
	 * 検索画面の背景画像を返す
	 * @return 検索画面の背景画像
	 */
	public BufferedImage getSearchBackgroundImage() {
		return backgroundImage.get(backgroundName[2]).nextImage();
	}

	/**
	 * 設定画面の背景画像を返す
	 * @return 設定画面の背景画像
	 */
	public BufferedImage getSettingBackgroundImage() {
		return backgroundImage.get(backgroundName[3]).nextImage();
	}

	/**
	 * デフォルトの背景画像を返す
	 * @return デフォルトの背景画像
	 */
	public BufferedImage getDefaultBackgroundImage() {
		return backgroundImage.get(backgroundName[4]).nextImage();
	}

	/**
	 * 日付ボタンの背景画像処理
	 * @param today 今日の画像を重ねるか
	 * @param doneText 記録済みテキストの画像を重ねるか
	 * @param donePicture 記録済み写真の画像を重ねるか
	 * @return 処理された画像
	 */
	public BufferedImage getDayButtonImage(boolean today, boolean doneText, boolean donePicture) {
		BufferedImage image = null;
		try {
			image = new BufferedImage(icon.get("today").getIcon().getWidth(), icon.get("today").getIcon().getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);

			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();

			Graphics g = image.getGraphics();
			g.setColor(setting.getDayButtonBackColor());
			g.fillRect(0, 0, imageWidth, imageHeight);
			g.dispose();

			if(today) {
				overlaidImage(image, ImageProcessing.imageProcessing(icon.get("today").getIcon(), setting.getTodayButtonBackColor()), imageWidth, imageHeight);
			}

			if(donePicture) {
				overlaidImage(image, ImageProcessing.imageProcessing(icon.get("donePicture").getIcon(), setting.getDonePictureButtonBackColor()), imageWidth, imageHeight);
			}

			if(doneText) {
				overlaidImage(image, ImageProcessing.imageProcessing(icon.get("doneText").getIcon(), setting.getDoneTextButtonBackColor()), imageWidth, imageHeight);
			}
		}
		catch(Exception error) {
			error.printStackTrace();
			image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics g = image.getGraphics();
			g.setColor(setting.getDayButtonBackColor());
			g.fillRect(0, 0, 100, 100);
			g.dispose();
		}

		return image;
	}

	/**
	 * イメージを重ねる<br>
	 * image1にimage2を重ねます<br>
	 * 2つのイメージのサイズは同じにしてください
	 * @param image1 重ねられるイメージ
	 * @param image2 重ねるイメージ
	 * @param imageWidth イメージの横幅
	 * @param imageHeight イメージの縦幅
	 */
	private void overlaidImage(BufferedImage image1, BufferedImage image2, int imageWidth, int imageHeight) {
		for(int y = 0; y < imageHeight; y++){
			for(int x = 0; x < imageWidth; x++){
				if (!(image2.getRGB(x,y) == Color.WHITE.getRGB() || image2.getRGB(x,y) == 0)) {
					image1.setRGB(x,y,image2.getRGB(x,y));
				}
			}
		}
	}

	/**
	 * 設定を初期化する<br>
	 * 設定ディレクトリ内のファイルをすべて削除します
	 */
	public void resetSettingFile() {
		for(int i = 0; i < 2; i++) {
			deleteEmptyDirectory(setting.getSettingPath(), 3);
		}
	}

	/**
	 * ソフトウェアオールリセット
	 */
	public void allReset() {
		for(int i = 0; i < 5; i++) {
			deleteEmptyDirectory(setting.getDataPath(), 3);
		}
	}
}
