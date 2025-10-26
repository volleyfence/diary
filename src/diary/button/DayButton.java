package diary.button;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import diary.frame.DiaryFrame;
import diary.system.Setting;

/**
 * カレンダーの日付ボタン
 * @author Masato Suzuki
 */
public class DayButton extends JButton implements ActionListener, MouseListener, MouseMotionListener{
	/**
	 * DiaryFrameクラスのオブジェクト
	 */
	private DiaryFrame df;

	/**
	 * フレームサイズや文字サイズなどの設定
	 */
	private Setting setting = Setting.getInstance();

	/**
	 * ボタンの日付
	 */
	private int year, month, day;

	/**
	 * ボタンを押しているか
	 */
	private boolean press = false;

	/**
	 *コンストラクタ
	 *@param df DiaryFrameクラスのオブジェクト
	 * @param day ボタンに表示する日付
	 */
	public DayButton(DiaryFrame df, int day) {
		super(Integer.toString(day));

		this.df = df;
		this.day = day;

		this.setBorder(null);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.addActionListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/**
	 * ボタンの年月を設定する<br>
	 * なお、日は変化しないため初期化時に行う
	 * @param year 年
	 * @param month 月
	 */
	private void setDate(int year, int month) {
		this.year = year;
		this.month = month;
		if(setting.getShowToolTip()) {
			this.setToolTipText(setting.getDefaultToolTipText(year + "年" + month + "月" + (day) + "日の『" + setting.getTitle() + "』を表示します"));
		}
		else {
			this.setToolTipText(null);
		}
	}

	/**
	 * ボタンのリセット
	 * @param year 年
	 * @param month 月
	 */
	public void reset(int year, int month) {
		this.setFont(setting.getFont(5));
		this.setForeground(setting.getDefaultFontColor());
		this.setBorder(null);
		setDate(year, month);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		df.changeDiaryWriter(year, month, day);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(press) {
			this.setBorder(new LineBorder(new Color(176, 196, 222), 2, false));
			this.setFont(setting.getFont(3));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.setBorder(null);
		this.setFont(setting.getFont(5));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			press = true;
			this.setFont(setting.getFont(3));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			press = false;
			this.setBorder(null);
			this.setFont(setting.getFont(5));
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.setBorder(new LineBorder(new Color(176, 196, 222), 2, false));
	}
}
