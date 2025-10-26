package diary.system.calendar;

/**
 * 日付情報の格納
 * @author Masato Suzuki
 */
public class Date {
	/**
	 * 日付
	 */
	private int year, month, day;

	/**
	 * 日付
	 */
	private int[] date;

	/**
	 * 比較用の日付<br>
	 * 月と日は1桁の場合0で2桁にして全体的に8桁にする<br>
	 * 例：20180818
	 */
	private int compareDate;

	/**
	 * 表示用の日付（年月日付き）<br>
	 * 月と日は1桁の場合半角スペースで桁を合わせる<br>
	 * 例：2018年 8月18日
	 */
	private String showDate;

	/**
	 * コンストラクタ
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 */
	public Date(int year, int month, int day) {
		setDate(year, month, day);
	}

	/**
	 * 日付をセットする
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 */
	public void setDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;

		setDate();
		setCompareDate();
		setShowDate();
	}

	/**
	 * 日付をセットする
	 */
	private void setDate() {
		int[] date = {year, month, day};
		this.date = date;
	}

	/**
	 * 比較用の日付をセットする
	 */
	private void setCompareDate() {
		compareDate = Integer.parseInt(String.format("%d%02d%02d", year, month, day));
	}

	/**
	 * 表示用の日付をセットする
	 */
	private void setShowDate() {
		showDate = String.format("%d年%2d月%2d日", year, month, day);
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

	public int[] getDate() {
		return date;
	}

	/**
	 * 比較用の日付を返す
	 * @return 比較用の日付（月と日は1桁なら0で桁合わせ）
	 */
	public int getCompareDate() {
		return compareDate;
	}

	/**
	 * 表示用の日付を返す
	 * @return 表示用の日付（月と日は1桁ならスペースで桁合わせ）
	 */
	public String getShowDate() {
		return showDate;
	}
}
