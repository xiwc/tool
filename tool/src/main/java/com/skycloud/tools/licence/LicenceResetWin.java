package com.skycloud.tools.licence;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LicenceResetWin extends JFrame {

	private JTextField tfLicence;
	/** serialVersionUID [long] */
	private static final long serialVersionUID = -1742551798872663664L;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LicenceResetWin frame = new LicenceResetWin();
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
	public LicenceResetWin() {
		super();
		setTitle("Licence Reset - author:xiwc@skycloudtech.com");
		setBounds(100, 100, 319, 288);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);

		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout());
		panel.add(panel_1, BorderLayout.NORTH);

		final JLabel licenceLabel = new JLabel();
		licenceLabel.setText("Licence:");
		panel_1.add(licenceLabel, BorderLayout.WEST);

		tfLicence = new JTextField();
		tfLicence.setText("c60f%2FxTMQc3CTJgVRQ3Cjw%3D%3D");
		panel_1.add(tfLicence);

		final JPanel panel_2 = new JPanel();
		panel_2.setLayout(new BorderLayout());
		panel.add(panel_2, BorderLayout.CENTER);

		final JButton resetButton = new JButton();
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				
				LicenceResetHandler handler = new LicenceResetHandler();
				handler.doReset(tfLicence.getText());
			}
		});
		resetButton.setText("Reset");
		panel_2.add(resetButton);
		//
	}

}
