package diary.image;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * アイコン画像を格納するクラス
 * @author Masato Suzuki
 */
public class Icon {
	/**
	 * アイコン画像
	 */
	private BufferedImage icon = null;

	/**
	 * アイコン画像が存在するか
	 */
	private boolean iconExists = false;

	/**
	 * 指定されたパスからアイコン画像を読み込む
	 * @param name アイコン画像に関するデータ
	 */
	public Icon(String[] name) {
		try {
			URL url = this.getClass().getResource("/icon/" + name[0] + ".png");

			icon = ImageIO.read(url);

			iconExists = true;
		}
		catch(Exception error) {
			icon = null;
			error.printStackTrace();
		}
	}

	/**
	 * アイコン画像を返す
	 * @return アイコン画像
	 */
	public BufferedImage getIcon() {
		return icon;
	}

	/**
	 * アイコン画像が存在しているかを返す
	 * @return アイコン画像が存在しているか
	 */
	public boolean exists() {
		return iconExists;
	}
}
