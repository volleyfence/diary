package diary.system.file;

import java.io.File;

import diary.system.image.ImageType;

/**
 * ファイルのタイプを判別するクラス
 * @author Masato Suzuki
 */
public class TypeCheck {
	/**
	 * 本ソフトで用いるテキスト判定
	 * @param path 判定するテキストファイルパス
	 * @return 判定結果
	 */
	public static boolean checkText(String path) {
		boolean result = false;

		String[] acceptType = {"txt"};
		try {
			String type = path.substring(path.lastIndexOf(".") + 1);
			for(int i = 0; i < acceptType.length; i++) {
				if(type.toLowerCase().equals(acceptType[i])) {
					result = true;
				}
			}

			acceptType = null;
			type = null;

		}
		catch(Exception error) {
			error.printStackTrace();
		}

		return result;
	}

	/**
	 * 本ソフトで用いる画像判定
	 * @param path 判定する画像ファイルパス
	 * @return 判定結果
	 */
	public static boolean checkImage(String path) {
		boolean result = false;

		String[] acceptType = {"png", "jpeg", "jpg", "gif", "bmp"};
		try {
			String type1 = path.substring(path.lastIndexOf(".") + 1);
			File file = new File(path);
			String type2 = ImageType.getFormat(file).toString().toLowerCase();
			for(int i = 0; i < acceptType.length; i++) {
				if(type1.toLowerCase().equals(acceptType[i])) {
					for(int j = 0; j < acceptType.length; j++) {
						if(type2.toLowerCase().equals(acceptType[j])) {
							result = true;
						}
					}
				}
			}
			type1 = null;
			type2 = null;
			acceptType = null;
		}
		catch(Exception error) {
			error.printStackTrace();
		}

		return result;
	}

	/**
	 * 本ソフトで用いるファイル判定
	 * @param path 判定するファイルパス
	 * @return 判定結果
	 */
	public static boolean checkFile(String path) {
		boolean result = false;

		try {
			if(checkText(path) || checkImage(path)) {
				result = true;
			}
		}
		catch(Exception error) {
			error.printStackTrace();
		}

		return result;
	}
}
