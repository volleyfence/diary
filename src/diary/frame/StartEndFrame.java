package diary.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import diary.system.Setting;
import diary.system.file.FileControl;

/**
 * StartFrameとEndFrameの共通処理を記述する
 * @author Masato Suzuki
 */
public class StartEndFrame extends JFrame {
	/**
	 * Settingクラスのオブジェクト
	 */
	private Setting setting = Setting.getInstance();

	/**
	 * FileControlクラスのオブジェクト
	 */
	private FileControl fc = FileControl.getInstance();

	/**
	 * 進捗パネル
	 */
	private JPanel progressPanel;

	/**
	 * アニメーションラベル
	 */
	private JLabel animationLabel;

	/**
	 * ロゴアイコン
	 */
	private Icon logoIcon;

	/**
	 * アニメーションアイコン
	 */
	private Icon[] animationIcon;

	/**
	 * 進捗バー
	 */
	private JProgressBar progress;

	/**
	 * 処理件数
	 */
	private int done = 0;

	/**
	 * 進捗バーに設定する最大件数の倍率<br>
	 * 件数が少ないと進捗バーが滑らかに動かないためこの値を掛けたものを最大件数にする
	 */
	private int progressRatio = 100;

	/**
	 * ロゴが表示可能か
	 */
	private boolean canShowLogo = true;

	/**
	 * フレームが使用可能化
	 */
	private boolean isAvailable = true;

	/**
	 * アニメーションアイコンの画像数
	 */
	private int animationIconNum = 10;

	/**
	 * 開始時かどうか
	 */
	private boolean start;

	/**
	 * コンストラクタ
	 * @param start 開始時かどうか
	 */
	public StartEndFrame(boolean start) {
		this.start = start;
		init();
	}

	private void init() {
		try {
			// ロゴの1辺の長さ（ディスプレイの縦のサイズの1/4にする）
			int side = setting.rounding(setting.getDisplayHeight() / 4.0);

			animationIcon = new Icon[animationIconNum];

			logoIcon = new ImageIcon(fc.getIconImage("logo").getScaledInstance(side, side, java.awt.Image.SCALE_SMOOTH ));

			// アニメーションアイコンの縦の長さ（ディスプレイの縦のサイズの1/3にする）
			side = setting.rounding(setting.getDisplayHeight() / 3.0);

			for(int i = 0; i < animationIconNum; i++) {
				URL url = this.getClass().getResource("/animation/animation" + (i + 1) + ".png");
				BufferedImage image = ImageIO.read(url);

				animationIcon[i] = new ImageIcon(image.getScaledInstance(setting.rounding(side * ((double)image.getWidth() / image.getHeight())), side, java.awt.Image.SCALE_SMOOTH ));
			}
		}
		catch(Exception error) {
			error.printStackTrace();
			canShowLogo = false;
		}

		// ラベルにロゴアイコンを貼り付ける
		animationLabel = new JLabel("", JLabel.CENTER);
		animationLabel.setPreferredSize(new Dimension(animationIcon[0].getIconWidth(), animationIcon[0].getIconHeight()));
		if(canShowLogo) {
			animationLabel.setIcon(logoIcon);
		}

		// プログレスバーの生成
		progress = new JProgressBar();
		progress.setStringPainted(true);
		progress.setFont(setting.getFirstSizeFont(-5));

		// プログレスバーを貼り付けるパネルの生成
		progressPanel = new JPanel();
		progressPanel.setOpaque(false);
		progressPanel.setLayout(new BorderLayout());
		progressPanel.add(new JLabel("  "), BorderLayout.WEST);
		progressPanel.add(progress, BorderLayout.CENTER);
		progressPanel.add(new JLabel("  "), BorderLayout.EAST);

		// 開始時と終了時の処理
		if(canShowLogo) {
			if(start) {
				progress.setString("システムチェック中");
				animationLabel.setIcon(logoIcon);
			}
			else {
				progress.setString("終了中");
				animationLabel.setIcon(animationIcon[animationIconNum - 1]);
			}
		}

		// 部品の設置
		this.setLayout(new BorderLayout());
		if(canShowLogo) {
			this.add(animationLabel, BorderLayout.CENTER);
		}
		this.add(progressPanel, BorderLayout.SOUTH);

		// フレームの設定
		frameSetting();
	}

	/**
	 * フレームの設定
	 */
	private void frameSetting() {
		this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setIconImage(fc.getIconImage("favicon"));
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * 最大件数のセット
	 * @param max 最大件数
	 */
	public void setMax(int max) {
		progress.setMinimum(0);
		progress.setMaximum(max * progressRatio);
	}

	/**
	 * 処理済み件数の加算
	 */
	synchronized public void add() {
		for(int i = 1; i <= progressRatio; i++) {
			progress.setValue(done * progressRatio + i);
			try {
				Thread.sleep(2);
			}
			catch(Exception error) {
				error.printStackTrace();
			}
		}
		done++;
	}

	/**
	 * フレームが使用可能かを返す
	 * @return フレームが使用可能か
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * 日記開始アニメーションの表示
	 */
	private void animation() {
		for(int i = 1; i < animationIconNum; i++) {
			try {
				Thread.sleep(150);
			}
			catch(Exception error) {
				error.printStackTrace();
			}
			int index;
			if(start) {
				index = i;
			}
			else {
				index = animationIconNum - i - 1;
			}

			animationLabel.setIcon(animationIcon[index]);
		}

		try {
			Thread.sleep(1500);
		}
		catch(Exception error) {
			error.printStackTrace();
		}
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		isAvailable = b;
	}

	@Override
	public void dispose() {
		if(start) {
			progress.setString("ようこそ");
		}
		else {
			progress.setString("さようなら");
		}

		if(canShowLogo) {
			animation();
		}

		super.dispose();
		isAvailable = false;
	}
}
