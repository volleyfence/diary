package diary.panel.setting.common;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import diary.dialog.ChangeSettingDialog;
import diary.dialog.YesDialog;
import diary.label.DiaryLabel;
import diary.panel.setting.SettingPanel;
import diary.textfield.PasswordField;

/**
 * パスワードロックを変更するパネル
 * @author Masato Suzuki
 */
public class ChangePasswordPanel extends SettingPanel {
	/**
	 * パスワード入力用のパネル
	 */
	private JPanel passwordPanel;

	/**
	 * カードレイアウト
	 */
	private CardLayout card;

	/**
	 * 選択肢の表示
	 */
	private JComboBox<String> passCombo;

	/**
	 * パスワードテキスト
	 */
	private PasswordField passText1, passText2, passText3, passText4, passText5, passText6;

	/**
	 * コンストラクタ
	 * @param csd ChangeSettingDialogクラスのオブジェクト
	 * @param message 設定の説明
	 */
	public ChangePasswordPanel(ChangeSettingDialog csd, String message) {
		super(message);
		this.csd = csd;

		label1.setText("パスワードには半角英数字のみ使用できます");
		messagePanel.add(label1);

		String[] tmp = {"ロックする", "ロックしない"};
		passCombo = new JComboBox<String>(tmp);
		passCombo.setPreferredSize(null);
		passCombo.setFont(setting.getSettingFont(0));
		passCombo.setForeground(setting.getSettingFontColor());
		passCombo.addActionListener(this);

		// パスワードロックの有無の選択
		JPanel tmp1 = new JPanel();
		tmp1.add(nowSet);
		tmp1.add(passCombo);

		// パスワード入力画面(新規か変更かで画面を変える)
		passwordPanel = new JPanel();
		card = new CardLayout();
		passwordPanel.setLayout(card);

		// ラベル
		DiaryLabel passLabel1 = new DiaryLabel("新しいパスワード：", 0, JLabel.RIGHT, false);
		DiaryLabel passLabel2 = new DiaryLabel("新しいパスワード(再)：", 0, JLabel.RIGHT, false);
		DiaryLabel passLabel3 = new DiaryLabel("現在のパスワード：", 0, JLabel.RIGHT, false);
		DiaryLabel passLabel4 = new DiaryLabel("新しいパスワード：", 0, JLabel.RIGHT, false);
		DiaryLabel passLabel5 = new DiaryLabel("新しいパスワード(再)：", 0, JLabel.RIGHT, false);
		DiaryLabel passLabel6 = new DiaryLabel("パスワード：", 0, JLabel.RIGHT, false);

		// テキストフィールド
		passText1 = new PasswordField(20);
		passText1.addKeyListener(this);
		passText2 = new PasswordField(20);
		passText2.addKeyListener(this);
		passText3 = new PasswordField(20);
		passText3.addKeyListener(this);
		passText4 = new PasswordField(20);
		passText4.addKeyListener(this);
		passText5 = new PasswordField(20);
		passText5.addKeyListener(this);
		passText6 = new PasswordField(20);
		passText6.addKeyListener(this);

		if(setting.getPasswordLock() == false) {
			// 新規画面の新しいパスワード
			JPanel tmp2 = new JPanel();
			tmp2.setLayout(new GridLayout(1, 2));
			tmp2.add(passLabel1);
			tmp2.add(passText1);

			// 新規画面の新しいパスワード(再)
			JPanel tmp3 = new JPanel();
			tmp3.setLayout(new GridLayout(1, 2));
			tmp3.add(passLabel2);
			tmp3.add(passText2);

			// 新規画面
			JPanel tmp4 = new JPanel();
			tmp4.setLayout(new GridLayout(0, 1));
			tmp4.add(tmp2);
			tmp4.add(tmp3);

			passwordPanel.add("new", tmp4);
		}
		else {
			// 変更画面の現在のパスワード
			JPanel tmp5 = new JPanel();
			tmp5.setLayout(new GridLayout(1, 2));
			tmp5.add(passLabel3);
			tmp5.add(passText3);

			// 変更画面の新しいパスワード
			JPanel tmp6 = new JPanel();
			tmp6.setLayout(new GridLayout(1, 2));
			tmp6.add(passLabel4);
			tmp6.add(passText4);

			// 変更画面の新しいパスワード(再)
			JPanel tmp7 = new JPanel();
			tmp7.setLayout(new GridLayout(1, 2));
			tmp7.add(passLabel5);
			tmp7.add(passText5);

			// 変更画面
			JPanel tmp8 = new JPanel();
			tmp8.setLayout(new GridLayout(0, 1));
			tmp8.add(tmp5);
			tmp8.add(tmp6);
			tmp8.add(tmp7);

			// パスワードロック削除
			JPanel tmp9 = new JPanel();
			tmp9.setLayout(new GridLayout(1, 2));
			tmp9.add(passLabel6);
			tmp9.add(passText6);

			// パスワードロック削除画面
			JPanel tmp10 = new JPanel();
			tmp10.setLayout(new GridLayout(0, 1));
			JLabel msg = new JLabel("ロック削除にはパスワードが必要です", JLabel.CENTER);
			msg.setFont(setting.getSettingFont(0));
			msg.setForeground(setting.getSettingFontColor());
			tmp10.add(msg);
			tmp10.add(tmp9);
			tmp10.add(new JLabel(""));

			passwordPanel.add("change", tmp8);
			passwordPanel.add("delete", tmp10);
		}

		JPanel tmpPanel = new JPanel();
		tmpPanel.setLayout(new BorderLayout());
		tmpPanel.add(tmp1, BorderLayout.NORTH);
		tmpPanel.add(passwordPanel, BorderLayout.CENTER);
		tmpPanel.add(new JLabel("　　"), BorderLayout.WEST);
		tmpPanel.add(new JLabel("　　"), BorderLayout.EAST);

		inputPanel.add(tmpPanel);

		if(setting.getPasswordLock() == true) {
			passCombo.setSelectedIndex(0);
			card.show(passwordPanel, "change");
		}
		else {
			passCombo.setSelectedIndex(1);
			passText1.setEditable(false);
			passText2.setEditable(false);
			card.show(passwordPanel, "new");
		}
	}

	@Override
	protected boolean addProcessing(Object source) {
		if(source == passCombo) {
			String out = passCombo.getSelectedItem().toString();
			if(setting.getPasswordLock() && out.equals("ロックしない")) {
				passText6.setText("");
				card.show(passwordPanel, "delete");
				passText6.requestFocusInWindow();
			}
			else if(out.equals("ロックする")) {
				if(setting.getPasswordLock() == true) {
					passText3.setText("");
					passText4.setText("");
					passText5.setText("");
					card.show(passwordPanel, "change");
					passText3.requestFocusInWindow();
				}
				else {
					passText1.setEditable(true);
					passText1.setText("");
					passText2.setEditable(true);
					passText2.setText("");
					card.show(passwordPanel, "new");
					passText1.requestFocusInWindow();
				}
			}
			else {
				passText1.setEditable(false);
				passText1.setText("");
				passText2.setEditable(false);
				passText2.setText("");
			}
		}

		else if(source == passText1 || source == passText2 || source == passText3 || source == passText4 || source == passText5 || source == passText6) {
			this.processing(source);
		}

		return true;
	}

	@Override
	protected boolean changeProcessing() {
		out = passCombo.getSelectedItem().toString();
		if(setting.getPasswordLock()) {
			// パスワード変更
			if(out.equals("ロックする")) {
				// 空欄がないかどうか
				if(new String(passText3.getPassword()).length() != 0 && new String(passText4.getPassword()).length() != 0 && new String(passText5.getPassword()).length() != 0) {
					// 現在のパスワードが正しいか
					if(setting.crypt(new String(passText3.getPassword())).equals(setting.getPassword())) {
						// パスワードに半角英数字以外使用されていないか
						if((new String(passText4.getPassword())).replaceAll("[A-Za-z0-9]", "").length() == 0) {
							// 2つの新しいパスワードが一致しているか
							if((new String(passText4.getPassword())).equals((new String(passText5.getPassword())))) {
								out = setting.crypt(new String(passText4.getPassword()));
							}
							else {
								new YesDialog(csd, "新しいパスワードが一致していません", setting.getSettingFont(0));
								passText4.setText("");
								passText5.setText("");
								passText4.requestFocusInWindow();
								return false;
							}
						}
						else {
							new YesDialog(csd, "パスワードに使用できるのは半角英数字のみです", setting.getSettingFont(0));
							passText4.setText("");
							passText5.setText("");
							passText4.requestFocusInWindow();
							return false;
						}
					}
					else {
						new YesDialog(csd, "現在のパスワードが異なります", setting.getSettingFont(0));
						passText3.setText("");
						passText4.setText("");
						passText5.setText("");
						passText3.requestFocusInWindow();
						return false;
					}
				}
				else {
					new YesDialog(csd, "空欄があります", setting.getSettingFont(0));
					passText3.setText("");
					passText4.setText("");
					passText5.setText("");
					passText3.requestFocusInWindow();
					return false;
				}
			}

			// ロック解除
			else {
				// 空欄がないかどうか
				if(new String(passText6.getPassword()).length() != 0) {
					// 現在のパスワードが正しいか
					if(!(setting.crypt(new String(passText6.getPassword())).equals(setting.getPassword()))) {
						new YesDialog(csd, "現在のパスワードが異なります", setting.getSettingFont(0));
						passText6.setText("");
						passText6.requestFocusInWindow();
						return false;
					}
					else {
						out = "";
					}
				}
				else {
					new YesDialog(csd, "空欄があります", setting.getSettingFont(0));
					passText6.setText("");
					passText6.requestFocusInWindow();
					return false;
				}
			}
		}
		else {
			// 新規
			if(out.equals("ロックする")) {
				// 空欄がないかどうか
				if(new String(passText1.getPassword()).length() != 0 && new String(passText2.getPassword()).length() != 0) {
					// パスワードに半角英数字以外使用されていないか
					if((new String(passText1.getPassword())).replaceAll("[A-Za-z0-9]", "").length() == 0) {
						// 2つの新しいパスワードが一致しているか
						if((new String(passText1.getPassword())).equals((new String(passText2.getPassword())))) {
							out = setting.crypt(new String(passText1.getPassword()));
						}
						else {
							new YesDialog(csd, "新しいパスワードが一致していません", setting.getSettingFont(0));
							passText1.setText("");
							passText2.setText("");
							passText1.requestFocusInWindow();
							return false;
						}
					}
					else {
						new YesDialog(csd, "パスワードに使用できるのは半角英数字のみです", setting.getSettingFont(0));
						passText1.setText("");
						passText2.setText("");
						passText1.requestFocusInWindow();
						return false;
					}
				}
				else {
					new YesDialog(csd, "空欄があります", setting.getSettingFont(0));
					passText1.setText("");
					passText2.setText("");
					passText1.requestFocusInWindow();
					return false;
				}
			}
			else {
				out = "";
			}
		}

		csd.changeSetting(out);
		return true;
	}
}