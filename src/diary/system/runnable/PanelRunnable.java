package diary.system.runnable;

/**
 * PanelRunnable.java
 * @author Masato Suzuki
 */

import diary.frame.DiaryFrame;

/**
 * 各日記パネルを生成する際に使用するクラス<br>
 * パネル生成をスレッド化して並列処理をするために作成した(スレッド内で引数にthisが使えなかったため)
 */
public class PanelRunnable implements Runnable{
	/**
	 * DiaryFrameクラスのオブジェクト
	 */
	protected DiaryFrame df;

	@Override
	public void run() {

	}

	/**
	 * DiaryFrameクラスのオブジェクトのセット
	 * @param df DiaryFrameクラスのオブジェクト
	 */
	public void setDiaryFrame(DiaryFrame df) {
		this.df = df;
	}
}
