package diary.system.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * カレンダー計算クラス
 * @author Masato Suzuki
 */
public class MyCalendar {
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
	private static SimpleDateFormat sdf3 = new SimpleDateFormat("dd");

	/**
	 * 指定された月の末日を返す
	 * @param year 年
	 * @param month 月
	 * @return 指定された月の末日
	 */
	public static int getLastDay(int year, int month){
		if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
			return 31;
		}
		else if(month == 2){
			if(year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)){
				return 29;
			}
			else{
				return 28;
			}
		}
		else{
			return 30;
		}
	}

	/**
	 * 指定された日の曜日（数値）を返す
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 指定された日の曜日（数値）<br>
	 * 0～6:日～土
	 */
	public static int getOffset(int year, int month, int day){
		// 1月と2月はそれぞれ前年の13月、14月として計算する
		if(month == 1 || month == 2)
			return (((year - 1) + (year - 1) / 4 - (year - 1) / 100 + (year - 1) / 400 + (13 * (month + 12) + 8) / 5 + day) % 7);

		else
			return ((year + year / 4 - year / 100 + year / 400 + (13 * month + 8) / 5 + day) % 7);
	}

	/**
	 * 指定された日の曜日（数値）を返す
	 * @param date 日付
	 * @return 指定された日の曜日（数値）<br>
	 * 0～6:日～土
	 */
	public static int getOffset(Date date) {
		return getOffset(date.getYear(), date.getMonth(), date.getDay());
	}

	/**
	 * 曜日（文字）を返す
	 * @param i 曜日<br>
	 * 0～6:日～土
	 * @return 曜日（文字）
	 */
	public static String getDayOfTheWeek(int i) {
		if(i == 0)
			return "日";
		else if(i == 1)
			return "月";
		else if(i == 2)
			return "火";
		else if(i == 3)
			return "水";
		else if(i == 4)
			return "木";
		else if(i == 5)
			return "金";
		else
			return "土";
	}

	/**
	 * 今日の日付を返す
	 * @return 今日の日付
	 */
	public static Date getToday() {
		Calendar calendar = Calendar.getInstance();
		return new Date(
				Integer.parseInt(sdf1.format(calendar.getTime())),
				Integer.parseInt(sdf2.format(calendar.getTime())),
				Integer.parseInt(sdf3.format(calendar.getTime()))
				);
	}

	/**
	 * 今日の日付を返す
	 * @param dayShow 日付を表示するか
	 * @return 今日の日付
	 */
	public static Date getToday(boolean dayShow) {
		Date date = getToday();

		if(!dayShow) {
			date.setDate(date.getYear(), date.getMonth(), 1);
		}

		return date;
	}
}
