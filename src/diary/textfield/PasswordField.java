package diary.textfield;

import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JPasswordField;

import diary.system.Setting;

/**
 * 独自のパスワードフィールド
 * @author Masato Suzuki
 */
public class PasswordField extends JPasswordField {
	/**
	 * Settingクラスのオブジェクト
	 */
	private Setting setting = Setting.getInstance();

	/**
	 * コンストラクタ
	 * @param columns 文字数
	 */
	public PasswordField(int columns) {
		super(columns);

		processing();
	}

	/**
	 * テキストフィールドの設定を行う
	 */
	public void processing() {
		this.setFont(setting.getSettingFont(-5));
		this.setForeground(setting.getSettingFontColor());
		this.setEchoChar('*');
		this.setMargin(new Insets(setting.getSize(5), setting.getSize(5), setting.getSize(5), setting.getSize(5)));
		this.addFocusListener(new FocusAdapter() {
			@Override public void focusGained(FocusEvent e) {
				((JPasswordField) e.getComponent()).selectAll();
			}
		});
	}
}
