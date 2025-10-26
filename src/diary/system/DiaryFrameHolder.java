package diary.system;

import diary.frame.DiaryFrame;

/**
 * DiaryFrameのオブジェクトを保持するクラス
 * @author Masato Suzuki
 */
public class DiaryFrameHolder {
	/**
	 * DiaryFrameクラスのオブジェクト
	 */
	private static DiaryFrame df;

	/**
	 * DiaryFrameクラスのオブジェクトのセット
	 * @param df DiaryFrameクラスのオブジェクト
	 */
	public static void setDiaryFrame(DiaryFrame df) {
		DiaryFrameHolder.df = df;
	}

	/**
	 * セットされているDiaryFrameオブジェクトを得る
	 * @return DiaryFrameクラスのオブジェクト
	 */
	public static DiaryFrame getDiaryFrame() {
		return df;
	}
}
