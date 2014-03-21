package com.skycloud.tools.batch.cr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.skycloud.tools.batch.cr.BatchConfirmAndRepair.ICallBack;

public class BatchCRWin extends JFrame {

	/** serialVersionUID long */
	private static final long serialVersionUID = 4207946169686030877L;
	private JList<String> ltMsg;
	private JPasswordField tbPwd;
	private JTextField tbUsername;
	private JTextField tbDbName;
	private JTextField tbIpAndPort;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BatchCRWin frame = new BatchCRWin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame
	 */
	public BatchCRWin() {
		super();
		setAlwaysOnTop(true);
		setTitle("批量确认&修复工具");
		setResizable(false);
		setBounds(100, 100, 295, 369);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		final JPanel panel = new JPanel();
		panel.setLayout(null);
		scrollPane.setViewportView(panel);

		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC}));
		panel_1.setBorder(new TitledBorder(null, "数据库连接配置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		panel_1.setBounds(15, 10, 256, 134);
		panel.add(panel_1);

		final JLabel urlLabel = new JLabel();
		urlLabel.setText("ip & port:");
		panel_1.add(urlLabel, new CellConstraints());

		tbIpAndPort = new JTextField();
		tbIpAndPort.setText("192.168.2.14:3306");
		tbIpAndPort.setColumns(80);
		tbIpAndPort.setComponentPopupMenu(null);
		panel_1.add(tbIpAndPort, new CellConstraints(3, 1));

		final JLabel dbNameLabel = new JLabel();
		dbNameLabel.setText("database name :");
		panel_1.add(dbNameLabel, new CellConstraints(1, 3));

		tbDbName = new JTextField();
		tbDbName.setText("skycloudnew");
		panel_1.add(tbDbName, new CellConstraints(3, 3));

		final JLabel usernameLabel = new JLabel();
		usernameLabel.setText("username :");
		panel_1.add(usernameLabel, new CellConstraints(1, 5));

		tbUsername = new JTextField();
		tbUsername.setText("skycloud");
		panel_1.add(tbUsername, new CellConstraints(3, 5));

		final JLabel passwordLabel = new JLabel();
		passwordLabel.setText("password :");
		panel_1.add(passwordLabel, new CellConstraints(1, 7));

		tbPwd = new JPasswordField();
		tbPwd.setEchoChar('*');
		tbPwd.setText("skycloud*123");
		panel_1.add(tbPwd, new CellConstraints(3, 7));

		final JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, false));
		panel_2.setBounds(15, 150, 256, 42);
		panel.add(panel_2);

		final JButton btnExec = new JButton();
		btnExec.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				
				btnExec.setEnabled(false);
				
				String ipAndPort = tbIpAndPort.getText().trim();
				String dbName = tbDbName.getText().trim();
				String userName = tbUsername.getText().trim();
				String pwd = String.valueOf(tbPwd.getPassword());
				
				final DefaultListModel<String> model = (DefaultListModel<String>) ltMsg.getModel();
				model.clear();
				
				BatchConfirmAndRepair.Exec(ipAndPort, dbName, userName, pwd, new ICallBack() {
					
					@Override
					public void invoke(String msg) {
						model.addElement(msg);
					}
				});
				
				btnExec.setEnabled(true);
			}
		});
		btnExec.setPreferredSize(new Dimension(100, 30));
		btnExec.setText("执行 >>");
		panel_2.add(btnExec);

		final JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new TitledBorder(null, "执行信息", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		scrollPane_1.setBounds(15, 198, 256, 131);
		panel.add(scrollPane_1);

		ltMsg = new JList<String>();
		ltMsg.setModel(new DefaultListModel<String>());
		scrollPane_1.setViewportView(ltMsg);
		//
	}

}
