package diary.button;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import diary.system.Setting;
import diary.system.image.ImageProcessing;

/**
 * アイコンボタンクラス
 * @author Masato Suzuki
 */
public class IconButton extends JButton implements MouseListener, MouseMotionListener {
	/**
	 * フレームサイズや文字サイズなどの設定
	 */
	private Setting setting = Setting.getInstance();

	/**
	 * 通常のアイコン
	 */
	private Image beforeImage;

	/**
	 * オンマウス中のアイコン
	 */
	private Image afterImage;

	/**
	 * 通常のアイコンカラー
	 */
	private Color beforeColor;

	/**
	 * オンマウス中のアイコンカラー
	 */
	private Color afterColor;

	/**
	 * アイコン画像を表示しない時のテキスト
	 */
	private String text;

	/**
	 * ボタンの説明テキスト
	 */
	private String toolTip;

	/**
	 * ボタンの横幅
	 */
	private int addButtonWidth;

	/**
	 * ボタンの縦幅
	 */
	private int addButtonHeight;

	/**
	 * 画像の横幅
	 */
	private int addImageWidth;

	/**
	 * 画像の縦幅
	 */
	private int addImageHeight;

	/**
	 * 設定どおりにフォントを変更できるか
	 */
	private boolean font;

	/**
	 * アイコンが表示可能かどうか
	 */
	private boolean show;

	/**
	 * ボタンを押しているかどうか
	 */
	private boolean press = false;

	/**
	 * ボタンにマウスが乗っているか
	 */
	private boolean onMouse = false;

	/**
	 * アイコンボタンを生成する
	 * @param image アイコン
	 * @param beforeColor 通常のアイコンカラー
	 * @param afterColor オンマウス中のアイコンカラー
	 * @param text アイコン画像を表示しない時のテキスト
	 * @param toolTip ボタンの説明テキスト
	 * @param addButtonWidth ボタンの横幅
	 * @param addButtonHeight ボタンの縦幅
	 * @param addImageWidth 画像の横幅
	 * @param addImageHeight 画像の縦幅
	 * @param font 設定どおりにフォントを変更できるか<br>
	 * true:設定どおりにフォントを変更する<br>
	 * false:設定用のフォントを使用する
	 */
	public IconButton(BufferedImage image, Color beforeColor, Color afterColor, String text, String toolTip, int addButtonWidth, int addButtonHeight, int addImageWidth, int addImageHeight, boolean font) {
		this.beforeImage = ImageProcessing.imageProcessing(image, beforeColor);
		this.afterImage = ImageProcessing.imageProcessing(image, afterColor);
		this.beforeColor = beforeColor;
		this.afterColor = afterColor;
		this.text = text;
		this.toolTip = toolTip;
		this.addButtonWidth = addButtonWidth;
		this.addButtonHeight = addButtonHeight;
		this.addImageWidth = addImageWidth;
		this.addImageHeight = addImageHeight;
		this.font = font;

		this.setFocusPainted(false);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		updateComponents();
	}

	/**
	 * コンポーネントの更新
	 */
	public void updateComponents() {
		show = setting.getShowIcon();

		if(beforeImage == null || afterImage == null) {
			show = false;
		}

		if(font) {
			this.setFont(setting.getFont(0));
			this.setForeground(setting.getDefaultFontColor());
		}
		else {
			this.setFont(setting.getSettingFont(0));
			this.setForeground(setting.getSettingFontColor());
		}

		if(show) {
			super.setText(null);
			this.setContentAreaFilled(false);
			this.setBorderPainted(false);
			this.setPreferredSize(setting.getDefaultIconButtonSize(addButtonWidth, addButtonHeight));
			this.setIcon(setting.getDefaultIcon(beforeImage, addImageWidth, addImageHeight));
		}
		else {
			super.setText(text);
			this.setContentAreaFilled(true);
			this.setBorderPainted(true);
			this.setBackground(beforeColor);
			this.setPreferredSize(setting.getDefaultButtonSize(0));
			super.setIcon(null);
		}

		setToolTipText();
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
	 * ボタンのテキストをセットする
	 * @param text ボタンのテキスト
	 */
	public void setText(String text) {
		this.text = text;
		if(!show) {
			super.setText(text);
		}
	}

	/**
	 * ボタンのツールチップテキストを設定する
	 * @param toolTip ボタンのツールチップテキスト
	 */
	public void setToolTipText(String toolTip) {
		this.toolTip = toolTip;
		setToolTipText();
	}

	/**
	 * このアイコン画像の表示可否を返す
	 * @return アイコン画像の表示可否
	 */
	public boolean getShowIcon() {
		return show;
	}

	/**
	 * ボタンが押されているかを返す
	 * @param e マウスイベント
	 * @return ボタンが押されているか
	 */
	public boolean pressed(MouseEvent e) {
		if(e.getSource() == this && getOnMouse(e)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * マウスがボタンの上に乗っているかを返す<br>
	 * ボタンの形を中心を原点とした円と考えてその範囲にあるかどうかを返す
	 * @param e マウスイベント
	 * @return マウスがボタンの上に乗っているか<br>
	 * アイコン画像を表示しないにしている場合は全てtrue
	 */
	private boolean getOnMouse(MouseEvent e) {
		if(show) {
			return (
					Math.sqrt(
							// クリック点のx座標と中心のx座標の差の2乗
							Math.pow((e.getX() - (this.getWidth()) / 2.0), 2)
							// クリック点のy座標と中心のy座標の差の2乗
							+ Math.pow((e.getY() - (this.getHeight()) / 2.0), 2)
							)
					// 円の直径をセットされているアイコンの辺の長さにする（少し余裕を持たせる）
					<= (this.getIcon().getIconWidth() + 2) / 2.0
					);
		}
		else {
			return onMouse;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(show) {
			if(press) {
				this.setIcon(setting.getDefaultIcon(afterImage, addImageWidth - 5, addImageHeight - 5));
			}
		}
		else {
			onMouse = true;
			this.setBackground(afterColor);
			if(press) {
				if(font) {
					this.setFont(setting.getFont(-2));
				}
				else {
					this.setFont(setting.getSettingFont(-2));
				}
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		onMouse = false;
		if(show) {
			this.setIcon(setting.getDefaultIcon(beforeImage, addImageWidth, addImageHeight));
		}
		else {
			if(font) {
				this.setFont(setting.getFont(0));
			}
			else {
				this.setFont(setting.getSettingFont(0));
			}
			this.setBackground(beforeColor);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(getOnMouse(e)) {
			if(SwingUtilities.isLeftMouseButton(e)) {
				press = true;
				if(show) {
					this.setIcon(setting.getDefaultIcon(afterImage, addImageWidth - 5, addImageHeight - 5));
				}
				else {
					if(font) {
						this.setFont(setting.getFont(-2));
					}
					else {
						this.setFont(setting.getSettingFont(-2));
					}
					this.setBackground(afterColor);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			press = false;
			if(show) {
				if(getOnMouse(e)) {
					this.setIcon(setting.getDefaultIcon(afterImage, addImageWidth, addImageHeight));
				}
				else {
					this.setIcon(setting.getDefaultIcon(beforeImage, addImageWidth, addImageHeight));
				}
			}
			else {
				if(font) {
					this.setFont(setting.getFont(0));
				}
				else {
					this.setFont(setting.getSettingFont(0));
				}

				if(onMouse) {
					this.setBackground(afterColor);
				}
				else {
					this.setBackground(beforeColor);
				}
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// ボタンを押している間は処理されない
		if(show) {
			if(getOnMouse(e)) {
				if(!onMouse) {
					onMouse = true;
					this.setIcon(setting.getDefaultIcon(afterImage, addImageWidth, addImageHeight));
				}
			}
			else {
				onMouse = false;
				this.setIcon(setting.getDefaultIcon(beforeImage, addImageWidth, addImageHeight));
			}
		}
	}
}
