package diary.system.image;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 画像処理を行う
 * @author Masato Suzuki
 */
public class ImageProcessing {
	/**
	 * 画像の処理<br>
	 * 受け取ったイメージにおいて白(255, 255, 255)と透明は透明、それ以外は指定された色に置き換えます
	 * @param image 処理されるイメージ
	 * @param color 引数のイメージの白や透明以外のピクセルの色
	 * @return 処理されたイメージ
	 */
	public static BufferedImage imageProcessing(BufferedImage image, Color color){
		BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

		// Image の幅
		int imageWidth = tmp.getWidth();
		// Image の高さ
		int imageHeight = tmp.getHeight();

		// 置き換える色のRGB値
		int replacementColor = color.getRGB();

		//RGB値を置換
		for(int y = 0; y < imageHeight; y++){
			for(int x = 0; x < imageWidth; x++){
				// 透明に
				if (image.getRGB(x, y) == Color.WHITE.getRGB() || image.getRGB(x, y) == 0) {
					tmp.setRGB(x, y, 0);
				}

				else {
					tmp.setRGB(x, y, replacementColor);
				}
			}
		}

		return tmp;
	}
}
