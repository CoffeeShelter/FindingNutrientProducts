package management.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField searchTextField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() {
		getContentPane().setPreferredSize(new Dimension(1280, 760));
		setTitle("건강기능식품 탐지 시스템");
		setBounds(100, 100, 1280, 760);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel frame = new JPanel();
		getContentPane().add(frame, BorderLayout.CENTER);
		frame.setLayout(new BorderLayout(0, 0));

		JPanel frame_main_west = new JPanel();
		frame_main_west.setPreferredSize(new Dimension(760, 10));
		frame_main_west.setBackground(Color.MAGENTA);
		frame.add(frame_main_west, BorderLayout.WEST);
		frame_main_west.setLayout(new BorderLayout(0, 0));
		
		JPanel frame_main_west_top = new JPanel();
		frame_main_west_top.setBackground(Color.GREEN);
		frame_main_west.add(frame_main_west_top, BorderLayout.NORTH);
		
		searchTextField = new JTextField();
		frame_main_west_top.add(searchTextField);
		searchTextField.setColumns(10);
		
		JButton searchButton = new JButton("검색");
		frame_main_west_top.add(searchButton);
		
		JPanel frame_main_east = new JPanel();
		frame_main_east.setPreferredSize(new Dimension(520, 10));
		frame_main_east.setBackground(Color.ORANGE);
		frame.add(frame_main_east, BorderLayout.EAST);
		frame_main_east.setLayout(new BorderLayout(0, 0));

		System.out.println(getWidth() - frame_main_east.getWidth());
		System.out.println(frame_main_east.getWidth());
	}

}
