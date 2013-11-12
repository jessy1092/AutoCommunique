import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JTextArea;

import org.g0v.ac.AutoCommunique;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class CommWindow extends JFrame {

	private JPanel contentPane;
	private JTextField clientID_txt;
	private JTextField secret_txt;
	private JTextField title_txt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CommWindow frame = new CommWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CommWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 422, 308);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		
		
		JLabel clientID_lbl = new JLabel("Client ID:");
		clientID_lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		clientID_lbl.setBounds(32, 35, 46, 15);
		contentPane.add(clientID_lbl);
		
		JLabel secret_lbl = new JLabel("Secret:");
		secret_lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		secret_lbl.setBounds(32, 60, 46, 15);
		contentPane.add(secret_lbl);
		
		JLabel ApiKey_lbl = new JLabel("HackPad API Key");
		ApiKey_lbl.setBounds(10, 10, 282, 15);
		contentPane.add(ApiKey_lbl);
		
		JLabel Padids_lbl = new JLabel("PadIDs:");
		Padids_lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		Padids_lbl.setBounds(32, 93, 46, 15);
		contentPane.add(Padids_lbl);
		
		JLabel title_lbl = new JLabel("Title:");
		title_lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		title_lbl.setBounds(32, 233, 46, 15);
		contentPane.add(title_lbl);

		clientID_txt = new JTextField();
		clientID_txt.setBounds(86, 32, 206, 21);
		contentPane.add(clientID_txt);
		clientID_txt.setColumns(10);
		
		secret_txt = new JTextField();
		secret_txt.setBounds(86, 57, 206, 21);
		contentPane.add(secret_txt);
		secret_txt.setColumns(10);
		
		final JTextArea padids_txt = new JTextArea();
		padids_txt.setBounds(86, 88, 206, 112);
		contentPane.add(padids_txt);
		
		title_txt = new JTextField();
		title_txt.setBounds(86, 230, 206, 21);
		contentPane.add(title_txt);
		title_txt.setColumns(10);
		
		JButton Combine_btn = new JButton("Combine");
		Combine_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AutoCommunique ac = 
						new AutoCommunique(clientID_txt.getText(),secret_txt.getText(),padids_txt.getText(),title_txt.getText());
				ac.run();
			}
		});
		Combine_btn.setBounds(302, 229, 87, 23);
		contentPane.add(Combine_btn);		
	}
}
