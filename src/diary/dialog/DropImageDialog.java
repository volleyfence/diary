package diary.dialog;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import diary.panel.DiaryWriter;
import diary.system.file.TypeCheck;

/**
 * ドロップされた写真を受け取るクラス
 * @author Masato Suzuki
 */
public class DropImageDialog extends DiaryDialog implements DropTargetListener {
	/**
	 * DiaryWriterクラスのオブジェクト
	 */
	private DiaryWriter dw;

	/**
	 * 日付を格納
	 */
	private int year, month, day;

	/**
	 * カードレイアウトのパネル
	 */
	private JPanel cardPanel;

	/**
	 * ドラッグパネル
	 */
	private JPanel dragPanel;

	/**
	 * ドロップパネル
	 */
	private JPanel dropPanel;

	/**
	 * 処理中パネル
	 */
	private JPanel checkPanel;

	/**
	 * カードレイアウト
	 */
	private CardLayout card;

	/**
	 * コンストラクタ
	 * @param dw DiaryWriterクラスのオブジェクト
	 * @param year 保存する写真の年
	 * @param month 保存する写真の月
	 * @param day 保存する写真の日
	 */
	public DropImageDialog(DiaryWriter dw, int year, int month, int day) {
		super(df, year + "年" + month + "月" + day + "日の写真", true);
		this.dw = dw;
		this.year = year;
		this.month = month;
		this.day = day;
		new DropTarget(this, this);

		firstLayout();
	}

	/**
	 * レイアウト
	 */
	public void firstLayout() {
		label1.setText("画像ファイルをここにドラッグして下さい");
		label2.setText("対応フォーマット：png, jpeg, gif, bmp");

		dragPanel = new JPanel();
		dragPanel.setLayout(new GridLayout(4, 1));
		dragPanel.add(new JLabel(""));
		dragPanel.add(label1);
		dragPanel.add(label2);
		dragPanel.add(new JLabel(""));

		label3.setText("画像ファイルをそのままドロップして下さい");

		dropPanel = new JPanel();
		dropPanel.setLayout(new GridLayout(1, 1));
		dropPanel.add(label3);

		label4.setText("ファイルチェック中");

		checkPanel = new JPanel();
		checkPanel.setLayout(new GridLayout(1, 1));
		checkPanel.add(label4);

		card = new CardLayout();

		cardPanel = new JPanel();
		cardPanel.setLayout(card);
		cardPanel.add("drag", dragPanel);
		cardPanel.add("drop", dropPanel);
		cardPanel.add("check", checkPanel);

		this.setLayout(new GridLayout(1, 1));
		this.add(cardPanel);

		// 適切なフレームサイズ設定
		resize(df, 200, 200);
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		card.show(cardPanel, "check");

		// ドロップを受け取る準備
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
		boolean flg = false;

		try {
			// 転送クラスの取得
			Transferable tr = dtde.getTransferable();

			// 受け取ったものがファイルであるか
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				@SuppressWarnings("unchecked")
				List<File> list = (List<File>)tr.getTransferData(DataFlavor.javaFileListFlavor);
				if(list.size() == 1) {
					File imageFile = list.get(0);

					// 受け取ったものが対応された画像かどうか
					if(TypeCheck.checkImage(imageFile.toString())) {
						flg = true;
						if(fc.getDiaryImageExists(year, month, day)) {
							new ImageDeleteDialog(dw, this, year, month, day, imageFile);
						}
						else {
							fc.saveImage(year, month, day, imageFile);
							dw.changeImage();
							this.dispose();
						}
					}
					else {
						new YesDialog(this, "画像ファイルをドロップしてください");
					}
				}
				else {
					new YesDialog(this, "1つのファイルをドロップしてください");
				}

			}
			else {
				new YesDialog(this, "画像ファイルをドロップしてください");
			}

		}
		catch (Exception error) {
			error.printStackTrace();
		}
		finally {
			// 転送完了の通知
			dtde.dropComplete(flg);
			card.show(cardPanel, "drag");
		}

	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		card.show(cardPanel, "drop");
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		card.show(cardPanel, "drag");
	}
}
