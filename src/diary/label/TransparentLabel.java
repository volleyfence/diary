package diary.label;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

/**
 * 指定されたアルファ値を背景に設定するラベル
 * @author Masato Suzuki
 */
public class TransparentLabel extends JLabel {
	/**
	 * ラベルの背景に設定するアルファ値
	 */
	private int alpha = 0;

	/**
	 * コンストラクタ
	 * @param text 初期のテキスト
	 * @param alpha アルファ値
	 */
	public TransparentLabel(String text, int alpha) {
		super(text);
		this.alpha = alpha;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setPaint(new Color(255, 255, 255, alpha));
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.dispose();
		super.paintComponent(g);
	}
}
