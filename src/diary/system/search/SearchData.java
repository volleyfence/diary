package diary.system.search;

import diary.system.calendar.Date;

/**
 * 検索で用いるデータ<br>
 * 1つの日記の日付、本文、文字数を記憶します
 * @author Masato Suzuki
 */
public class SearchData {
	/**
	 * 日記の日付
	 */
	private Date date = null;

	/**
	 * 日記の本文
	 */
	private String text = "<br>";

	/**
	 * 日記の文字数
	 */
	private int charCount = 0;

	/**
	 * コンストラクタ
	 * @param date 日付
	 */
	public SearchData(Date date) {
		this.date = date;
	}

	/**
	 * テキストの追加
	 * @param addText 追加するテキスト
	 */
	public void addText(String add) {
		text += add;
	}

	/**
	 * 文字数の追加
	 * @param add 追加する文字数
	 */
	public void addCharCount(int add) {
		charCount += add;
	}

	/**
	 * 日付データを返す
	 * @return 日付データ
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * テキストを返す
	 * @return テキスト
	 */
	public String getText() {
		return text;
	}

	/**
	 * 文字数を返す
	 * @return 文字数
	 */
	public int getCharCount() {
		return charCount;
	}
}
