package diary.label;

import javax.swing.JLabel;

import diary.system.Setting;

/**
 * 日記ソフトウェア用のラベル
 * @author Masato Suzuki
 */
public class DiaryLabel extends JLabel {
	/**
	 * Settingクラスのオブジェクト
	 */
	protected Setting setting = Setting.getInstance();

	/**
	 * サイズの増減
	 */
	private int addSize;

	/**
	 * 設定どおりにフォントを変更できるか
	 */
	private boolean font = true;

	/**
	 * コンストラクタ
	 * @param addSize デフォルトフォントサイズからの増減
	 * @param horizontalAlignment ラベルのテキストの水平位置
	 * @param font 設定どおりにフォントを変更できるか<br>
	 * true:設定どおりにフォントを変更する<br>
	 * false:設定用のフォントを使用する
	 */
	public DiaryLabel(int addSize, int horizontalAlignment, boolean font) {
		super("", horizontalAlignment);
		this.font = font;
		setSize(addSize);
	}

	/**
	 * コンストラクタ
	 * @param text ラベルに表示されるテキスト
	 * @param horizontalAlignment ラベルのテキストの水平位置
	 * @param font 設定どおりにフォントを変更できるか<br>
	 * true:設定どおりにフォントを変更する<br>
	 * false:設定用のフォントを使用する
	 */
	public DiaryLabel(String text, int horizontalAlignment, boolean font) {
		super(text, horizontalAlignment);
		this.font = font;
	}

	/**
	 * コンストラクタ
	 * @param text ラベルに表示されるテキスト
	 * @param addSize デフォルトフォントサイズからの増減
	 * @param horizontalAlignment ラベルのテキストの水平位置
	 * @param font 設定どおりにフォントを変更できるか<br>
	 * true:設定どおりにフォントを変更する<br>
	 * false:設定用のフォントを使用する
	 */
	public DiaryLabel(String text, int addSize, int horizontalAlignment, boolean font) {
		super(text, horizontalAlignment);
		this.font = font;
		setSize(addSize);
	}

	/**
	 * ラベルのフォントサイズの変更
	 * @param addSize デフォルトフォントサイズからの増減
	 */
	public void setSize(int addSize) {
		this.addSize = addSize;
		updateComponents();
	}

	/**
	 * ラベルテキスト&水平位置の変更
	 * @param text ラベルに表示されるテキスト
	 * @param horizontalAlignment ラベルのテキストの水平位置
	 */
	public void setText(String text, int horizontalAlignment) {
		super.setText(text);
		super.setHorizontalAlignment(horizontalAlignment);
	}

	/**
	 * フォントの更新
	 */
	public void updateComponents() {
		if(font) {
			this.setFont(setting.getFont(addSize));
			this.setForeground(setting.getDefaultFontColor());
		}
		else {
			this.setFont(setting.getSettingFont(addSize));
			this.setForeground(setting.getSettingFontColor());
		}
	}
}
