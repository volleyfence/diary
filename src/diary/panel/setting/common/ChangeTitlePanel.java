package diary.panel.setting.common;

import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import diary.dialog.ChangeSettingDialog;
import diary.dialog.YesDialog;
import diary.panel.setting.SettingPanel;

/**
 * タイトルを変更するパネル
 * @author Masato Suzuki
 */
public class ChangeTitlePanel extends SettingPanel {
	/**
	 * タイトル入力用
	 */
	private JTextField titleText;

	/**
	 * コンストラクタ
	 * @param csd ChangeSettingDialogクラスのオブジェクト
	 * @param message 設定の説明
	 */
	public ChangeTitlePanel(ChangeSettingDialog csd, String message) {
		super(message);
		this.csd = csd;

		titleText = new JTextField(setting.getTitle(), 20);
		titleText.setMargin(new Insets(setting.getSize(5), setting.getSize(5), setting.getSize(5), setting.getSize(5)));
		titleText.setFont(setting.getSettingFont(0));
		titleText.setForeground(setting.getSettingFontColor());
		titleText.addActionListener(this);
		titleText.addKeyListener(this);
		titleText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)e.getComponent()).selectAll();
			}
		});
		inputPanel.add(nowSet);
		inputPanel.add(titleText);
	}

	@Override
	protected boolean addProcessing(Object source) {
		if(source == titleText) {
			// YesDialogから返ってきたときにEnterキーが押しっぱなしのときは処理しない
			if(!press) {
				press = true;
				this.processing(source);
			}
		}
		return true;
	}

	@Override
	protected boolean changeProcessing() {
		out = titleText.getText().replaceAll("\t", "").replaceAll("　", " ").replaceAll("\\\\", "￥").trim();
		if(out.replaceAll(" ", "").length() != 0) {
			if(!out.equals(setting.getTitle())) {
				csd.changeSetting(out);
				df.updateComponents();
			}
		}
		else {
			new YesDialog(csd, "何も入力されていません", setting.getSettingFont(0)) {
				@Override
				public void keyPressed(KeyEvent e) {
					if(!press) {
						press = true;
						if(e.getKeyCode() == KeyEvent.VK_ENTER) {
							processing(e.getSource());
						}
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					press = false;
				}
			};
			titleText.setText("");
			out = "";
			return false;
		}

		return true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(KeyEvent.VK_ENTER == e.getKeyCode()) {
			if(e.getSource() == titleText) {
				press = false;
			}
			else {
				methodControl(e.getSource());
			}
		}
	}
}