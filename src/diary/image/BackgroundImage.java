package diary.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import diary.system.file.TypeCheck;

/**
 * パネルの背景画像を格納するクラス
 */
public class BackgroundImage {
	/**
	 * 全ての背景画像のファイルパス
	 */
	private String[] path;

	/**
	 * 全ての背景画像
	 */
	private BufferedImage[] image;

	/**
	 * 背景画像が1つでも存在するか
	 */
	private boolean exists = false;

	/**
	 * 現在表示している背景画像のインデックス
	 */
	private int index = -1;

	/**
	 * 指定されたパスから背景画像を読み込む
	 * @param path 背景画像が格納されているディレクトリの絶対パス
	 */
	public BackgroundImage(String path) {
		File[] files = (new File(path)).listFiles();
		if(files != null && files.length > 0) {
			ArrayList<BufferedImage> tmpList1 = new ArrayList<BufferedImage>();
			ArrayList<String> tmpList2 = new ArrayList<String>();
			BufferedImage tmpImage;

			for(int i = 0; i < files.length; i++) {
				try {
					if(TypeCheck.checkImage(files[i].toString())) {
						tmpImage = ImageIO.read(files[i]);
						tmpList1.add(tmpImage);
						tmpList2.add(files[i].toString());
					}
				}
				catch(Exception error) {
					error.printStackTrace();
				}
			}
			image = new BufferedImage[tmpList1.size()];
			this.path = new String[tmpList1.size()];
			for(int i = 0; i < tmpList1.size(); i++) {
				image[i] = tmpList1.get(i);
				this.path[i] = tmpList2.get(i);
			}

			if(image.length > 0) {
				exists = true;
			}

			tmpList1 = null;
			tmpList2 = null;
		}
	}

	/**
	 * 現在表示されている画像のパスを返す
	 * @return 現在表示されている画像のパス
	 */
	public String getPath() {
		return path[index];
	}

	/**
	 * 次の背景画像を返す
	 * @return 次の背景画像
	 */
	public BufferedImage nextImage() {
		index++;
		if(index == image.length) {
			index = 0;
		}

		return image[index];
	}

	/**
	 * 背景画像が存在しているか
	 * @return 背景画像が存在しているか
	 */
	public boolean exists() {
		return exists;
	}
}
