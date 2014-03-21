package com.skycloud.tools.sql.join;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class SqlJoinWin extends JFrame {

	private JTextField textField;
	private JTextArea textArea_1;
	private JTextArea textArea;
	/** serialVersionUID [long] */
	private static final long serialVersionUID = 8474363470818761427L;

	/**
	 * Launch the application
	 * 
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
					SqlJoinWin frame = new SqlJoinWin();
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
	public SqlJoinWin() {

		super();
		setTitle("SQL StringBuffer拼装 - author:xiwc@skycloudtech.com");
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		final JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		final JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);

		textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);

		final JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		final JLabel label = new JLabel();
		label.setText("变量名:");
		panel.add(label);

		textField = new JTextField();
		panel.add(textField);
		textField.setText("sqlSb");

		final JButton button = new JButton();
		panel.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				exec();
			}
		});
		button.setText("执行拼装");

		final JButton button_1 = new JButton();
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				try {
					textArea.setText(getClipboardText());
					exec();
					setClipboardText(textArea_1.getText());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		button_1.setText("一键转换");
		panel.add(button_1);
		//
	}

	protected static String getClipboardText() throws Exception {

		// 获取系统剪贴板
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

		// 获取剪切板中的内容
		Transferable clipT = clip.getContents(null);

		if (clipT != null) {

			// 检查内容是否是文本类型
			if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return (String) clipT.getTransferData(DataFlavor.stringFlavor);
			}

		}

		return null;

	}

	// 往剪切板写文本数据

	protected static void setClipboardText(String writeMe) {
		// 获取系统剪贴板
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(writeMe);
		clip.setContents(tText, null);
	}

	/**
	 * 
	 */
	private void exec() {
		String sql = textArea.getText();
		String varName = textField.getText();
		if (sql != null && !"".equals(sql)) {
			String[] arr = sql.split("\n");
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("StringBuffer " + varName + " = new StringBuffer();").append("\r\n");
			for (String line : arr) {
				sBuffer.append(varName + ".append(\"" + line).append("\\n\");\r\n");
			}
			textArea_1.setText(sBuffer.toString());
		}
	}

}
