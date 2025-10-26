package diary.panel.parts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * 指定されたアルファ値を背景に設定するパネル
 * @author Masato Suzuki
 */
public class TransparentPanel extends JPanel {
	/**
	 * パネルの背景に設定する
	 */
	private int alpha = 0;

	/**
	 * コンストラクタ
	 * @param text 初期のテキスト
	 * @param alpha アルファ値
	 */
	public TransparentPanel(int alpha) {
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
