package cn.power;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class PowerDialog extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 退出开关
	private boolean quit_flag = false;
	// 关机开关
	private boolean off_flag = false;
	// 关机时间
	private Date off_time = null;

	public PowerDialog(String title) {
		super(title);
		// this.lock = lock;
		initUI();

		Thread power_thread = new Thread(new PowerThread());
		power_thread.start();

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				quit_flag = true;
			}
		});
	}
	
	private JRadioButton timing = null;
	private JTextField power_date_field = null;
	private DateChooser dateChooser1 = null;
//	private JTextField time_hour_field = null;
//	private JTextField time_min_field = null;
	private JFormattedTextField time_hour_field = null;
	private JFormattedTextField time_min_field = null;
	
	private JRadioButton delay = null;
//	private JTextField hour_field = null;
//	private JTextField min_field = null;
	private JFormattedTextField hour_field = null;
	private JFormattedTextField min_field = null;
	
	private JButton set = null;
	private JButton cancel = null;
	
	
	
	private Date getPowerOffTime(){
		Date off_time = new Date();
		
		if(timing.isSelected()){
			String date = power_date_field.getText();

			String shour = time_hour_field.getText();
			String smin = time_min_field.getText();
			
			if(shour.isEmpty() || smin.isEmpty()){
				JOptionPane.showMessageDialog(null, "请设置关机时间！", "错误", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			int ihour = Integer.valueOf(shour);
			int imin = Integer.valueOf(smin);
			
			if(ihour < 0 || ihour > 24){
				JOptionPane.showMessageDialog(null, "时间格式错误", "错误", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			if(imin < 0 || imin > 59){
				JOptionPane.showMessageDialog(null, "时间格式错误", "错误", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			String off_time_str = date + " " + 
					String.format("%02d", ihour) + ":" + 
					String.format("%02d", imin) + ":00";
			
//			System.out.println(off_time_str);
			Date set_time = DateUtil.getTime(off_time_str);
			
			if(set_time.before(off_time)){
				JOptionPane.showMessageDialog(null, "必须设置未来时间！", "错误", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			off_time = set_time;
			
		}else if(delay.isSelected()){
			String shour = hour_field.getText();
			String smin = min_field.getText();
			
			if(shour.isEmpty() || smin.isEmpty()){
				JOptionPane.showMessageDialog(null, "请设置关机时间！", "错误", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			int ihour = Integer.valueOf(shour);
			int imin = Integer.valueOf(smin);
			
			if(ihour < 0 || ihour > 24){
				JOptionPane.showMessageDialog(null, "时间格式错误", "错误", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			if(imin < 0 || imin > 59){
				JOptionPane.showMessageDialog(null, "时间格式错误", "错误", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(off_time);
			calendar.add(Calendar.HOUR, ihour);
			calendar.add(Calendar.MINUTE, imin);

			off_time = calendar.getTime();
		}else{
			JOptionPane.showMessageDialog(null, "请设置关机时间！", "错误", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		System.out.println(DateUtil.getTimeString(off_time));
		return off_time;
	}
	

	private void initUI() {
		timing = new JRadioButton("定时", true);
		timing.setBounds(20, 20, 60, 25);

		Date now = new Date();
		dateChooser1 = DateChooser.getInstance(now, "yyyy-MM-dd");
		power_date_field = new JTextField("关机日期");
		String dividend_date = DateUtil.getDateString(now);
		power_date_field.setText(dividend_date);
		power_date_field.setBounds(40, 50, 80, 25);
		dateChooser1.register(power_date_field);

		//time_hour_field = new JTextField();
		time_hour_field = new JFormattedTextField(NumberFormat.getIntegerInstance());
		time_hour_field.setColumns(2);
		time_hour_field.setBounds(125, 50, 30, 25);
		JLabel time_hour_label = new JLabel(" : ");
		time_hour_label.setBounds(155, 50, 20, 25);
		time_min_field = new JFormattedTextField(NumberFormat.getIntegerInstance());
		time_min_field.setColumns(2);
		time_min_field.setBounds(175, 50, 30, 25);
		// JLabel time_min_label = new JLabel("分");

		delay = new JRadioButton("延时");
		delay.setBounds(20, 80, 60, 25);
		hour_field = new JFormattedTextField(NumberFormat.getIntegerInstance());
		hour_field.setColumns(2);
		hour_field.setBounds(40, 110, 30, 25);
		JLabel hour_label = new JLabel("小时");
		hour_label.setBounds(70, 110, 30, 25);
		min_field = new JFormattedTextField(NumberFormat.getIntegerInstance());
		min_field.setColumns(2);
		min_field.setBounds(105, 110, 30, 25);
		JLabel min_label = new JLabel("分");
		min_label.setBounds(135, 110, 30, 25);

		ButtonGroup group = new ButtonGroup();
		group.add(timing);
		group.add(delay);

		JPanel offtime_panel = new JPanel();
		offtime_panel.setBorder(BorderFactory.createTitledBorder("关机时间"));
		offtime_panel.add(timing);
		offtime_panel.add(delay);

		set = new JButton("设置");
		set.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("设置关机时间");

				off_time = getPowerOffTime();
				if(off_time == null){
					return;
				}
				
				set.setEnabled(false);
				cancel.setEnabled(true);
				
				off_flag = true;
			}
		});
		set.setEnabled(true);
		set.setBounds(40, 150, 60, 25);

		cancel = new JButton("取消");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("关机取消");

				set.setEnabled(true);
				cancel.setEnabled(false);
				
				off_flag = false;
			}
		});
		cancel.setEnabled(false);
		cancel.setBounds(120, 150, 60, 25);

		JPanel main_panel = new JPanel(null);
		// main_panel.add(offtime_panel);
		main_panel.add(timing);
		main_panel.add(power_date_field);
		main_panel.add(time_hour_field);
		main_panel.add(time_hour_label);
		main_panel.add(time_min_field);
		main_panel.add(delay);
		main_panel.add(hour_field);
		main_panel.add(hour_label);
		main_panel.add(min_field);
		main_panel.add(min_label);

		main_panel.add(set);
		main_panel.add(cancel);
		setContentPane(main_panel);

		this.setSize(250, 230);
		// this.setTitle("股息工具");
		// URL icon_url = this.getClass().getResource(Global.icon);
		// ImageIcon img = new ImageIcon(icon_url);
		// ImageIcon img = new ImageIcon(Global.icon);
		// this.setIconImage(img.getImage());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 点击关闭按钮时退出
		this.setLocationRelativeTo(null); // 设置窗口在屏幕的中央
		// 设置大小不可调
		this.setResizable(false);

	}

	// 关机子线程
	class PowerThread implements Runnable {

		private long DEFAULT_SLEEP_TIME = 60 * 1000;
		private long sleep_time = DEFAULT_SLEEP_TIME; // 1s

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(quit_flag){
					break;
				}

				if (off_flag) {
					sleep_time = DEFAULT_SLEEP_TIME;
					//
					Date now = new Date();
					if(!now.before(off_time)){
						System.out.println("Power off now!");
						try {
							Runtime.getRuntime().exec("shutdown /s /t 1");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}else{
						long ltime = off_time.getTime() - now.getTime();
						long mins = (ltime/1000)/60;
						if(mins < 1){
							long seconds = (ltime/1000)%60;
							if(seconds == 0){
								seconds = 1;
							}
							sleep_time = seconds * 1000;
							System.out.println("less than 1 min. sleep " + seconds);
							System.out.println(DateUtil.getTimeString(now));
						}
					}
				}
			}
		}

	}

}
