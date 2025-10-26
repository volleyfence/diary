package diary.button;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import diary.system.Setting;

/**
 * テキストボタン<br>
 * 基本的に2行以上表示するときに使用する
 * @author Masato Suzuki
 */
public class TextButton extends JButton implements MouseListener {
	/**
	 * フレームサイズや文字サイズなどの設定
	 */
	private Setting setting = Setting.getInstance();

	/**
	 * ボタンの説明テキスト
	 */
	private String toolTip;

	/**
	 * 設定どおりにフォントを変更できるか
	 */
	private boolean font;

	/**
	 * ボタンを押しているか
	 */
	private boolean press = false;

	/**
	 * コンストラクタ
	 * @param font 設定どおりにフォントを変更できるか<br>
	 * true:設定どおりにフォントを変更する<br>
	 * false:設定用のフォントを使用する
	 */
	public TextButton(boolean font) {
		super();
		this.font = font;

		init();
	}

	/**
	 * コンストラクタ
	 * @param text ボタンのテキスト
	 * @param font 設定どおりにフォントを変更できるか<br>
	 * true:設定どおりにフォントを変更する<br>
	 * false:設定用のフォントを使用する
	 */
	public TextButton(String text, boolean font) {
		super();
		this.font = font;

		setText(text);

		init();
	}

	/**
	 * 初期化
	 */
	private void init() {
		this.setHorizontalAlignment(JButton.LEFT);
		this.setFocusPainted(false);
		this.addMouseListener(this);

		updateComponents();
	}

	/**
	 * ボタンのテキストをセットする
	 * @param text ボタンのテキスト
	 */
	@Override
	public void setText(String text) {
		if(font) {
			super.setText(setting.getDefaultButtonText(text));
		}
		else {
			super.setText(setting.getSettingButtonText(text));
		}
	}

	/**
	 * ボタンにツールチップを設定する
	 */
	private void setToolTipText() {
		if(setting.getShowToolTip()) {
			if(font) {
				super.setToolTipText(setting.getDefaultToolTipText(toolTip));
			}
			else {
				super.setToolTipText(setting.getSettingToolTipText(toolTip));
			}
		}
		else {
			super.setToolTipText(null);
		}
	}

	/**
	 * ボタンのツールチップテキストを設定する
	 * @param toolTip ボタンのツールチップテキスト
	 */
	@Override
	public void setToolTipText(String toolTip) {
		this.toolTip = toolTip;
		setToolTipText();
	}

	/**
	 * コンポーネントの更新
	 */
	public void updateComponents() {
		if(font) {
			this.setFont(setting.getFont(0));
			this.setForeground(setting.getDefaultFontColor());
		}
		else {
			this.setFont(setting.getSettingFont(0));
			this.setForeground(setting.getSettingFontColor());
		}

		this.setBackground(setting.getTextButtonBackColor());

		setToolTipText();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(press) {
			this.setBackground(new Color(209, 226, 242));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.setBackground(setting.getTextButtonBackColor());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			press = true;
			this.setBackground(new Color(209, 226, 242));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			press = false;
			this.setBackground(setting.getTextButtonBackColor());
		}
	}
}

