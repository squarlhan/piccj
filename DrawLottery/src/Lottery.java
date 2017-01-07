import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// This example demonstrates the use of JButton, JTextField
// and JLabel.
public class Lottery implements ActionListener {

	final static int START_INDEX = 0;

	int REAL_NUM_IMAGES = 0;

	List<ImageIcon> images = new ArrayList<ImageIcon>();

	List<String> imageNames = new ArrayList<String>();
	
	static JFrame phasesFrame = new JFrame("抽奖程序");

	JPanel mainPanel, selectPanel, displayPanel, resultPanel, controlPanel;

	JButton phaseChoice = null;
	JButton phaseSet = null;

	JTextField p1 = null;
	JTextField p2 = null;
	JTextField p3 = null;
	JTextField p4 = null;
	JTextArea result = null;

	JLabel phaseIconLabel = null, phaseResult = null, phaseBlank1 = null,
			phaseBlank2 = null, wait_des = null;

	// Constructor
	public Lottery() {
		// Create the phase selection and display panels.
		selectPanel = new JPanel();
		displayPanel = new JPanel();
		resultPanel = new JPanel();
		controlPanel = new JPanel();

		// Add various widgets to the sub panels.
		createFrame();

		// Create the main panel to contain the two sub panels.
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1, 2, 5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		controlPanel.setLayout(new GridLayout(2, 1, 5, 5));
		controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		// Add the select and display panels to the main panel.
		mainPanel.add(displayPanel);
		mainPanel.add(controlPanel);
		controlPanel.add(selectPanel);
		controlPanel.add(resultPanel);
	}

	private void createFrame() {

		URL path = ClassLoader.getSystemResource("images");
		if (path == null) { // 避免因找不到图片目录而出现空指针异常
			StringBuffer msg = new StringBuffer("没有找到图片目录: ")
					.append(new File(ClassLoader.getSystemResource("")
							.getPath())).append(File.separator)
					.append("images").append(File.separator);
			System.out.println(msg);
			System.exit(0);
			return;
		}
		File dir = new File(path.getPath());
		File[] files = dir.listFiles();

		ImageIcon icon = new ImageIcon();
		try {
			Image src = ImageIO.read(files[0]);
			int imageWidth = 800;
			int imageHeight = src.getHeight(null)*800/src.getWidth(null);
//			System.out.println(imageWidth);
			BufferedImage tag = new BufferedImage((int)imageWidth, (int)imageHeight, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH), 0, 0, null);
//			BufferedImage bi = ImageIO.read(files[0]);
			icon = new ImageIcon(tag);
		} catch (IOException e) {
		}
		images.add(icon);

		phaseIconLabel = new JLabel();
		phaseIconLabel.setHorizontalAlignment(JLabel.CENTER);
		phaseIconLabel.setVerticalAlignment(JLabel.CENTER);
		phaseIconLabel.setVerticalTextPosition(JLabel.CENTER);
		phaseIconLabel.setHorizontalTextPosition(JLabel.CENTER);
		phaseIconLabel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(6, 6, 6, 6)));

		phaseIconLabel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(0, 0, 10, 0),
				phaseIconLabel.getBorder()));

		phaseResult = new JLabel();
		phaseBlank1 = new JLabel();
		phaseBlank2 = new JLabel();

		phaseSet = new JButton("把照片裝進來");
		phaseChoice = new JButton("开始/停止");

		// Display the first image.
		phaseIconLabel.setIcon(images.get(START_INDEX));
		phaseIconLabel.setText("");

		// Add border around the select panel.
		selectPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Select Phase"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		resultPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Result Phase"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// Add border around the display panel.
		displayPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Display Phase"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		selectPanel.add(phaseSet);
		displayPanel.add(phaseIconLabel);

		phaseSet.addActionListener(this);

	}

	// Create and the widgets to select and display the phases of the moon.
	private void addWidgets(String path) {

		selectPanel.remove(phaseChoice);
		selectPanel.remove(phaseSet);
		displayPanel.remove(phaseIconLabel);
		resultPanel.remove(phaseBlank1);
		resultPanel.remove(phaseResult);
		resultPanel.remove(phaseBlank2);

		// Get the images and put them into an list of ImageIcon.
		if (path == null) { // 避免因找不到图片目录而出现空指针异常
			StringBuffer msg = new StringBuffer("没有找到图片目录: ")
					.append(new File(ClassLoader.getSystemResource("")
							.getPath())).append(File.separator)
					.append("images").append(File.separator);
			System.out.println(msg);
			System.exit(0);
			return;
		}
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				continue; // 目录则不处理
			}
			// 文件名
			String fileName = files[i].getName();
			// 文件名后缀(转换为小写,否则不能兼容大写后缀名)
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();

			if (!suffix.equals("gif") && !suffix.equals("jpg")
					&& !suffix.equals("png") && !suffix.equals("bmp")) {
				continue;
			}
			ImageIcon icon;
			try {
				Image src = ImageIO.read(files[i]);
				int imageWidth = 800;
				int imageHeight = src.getHeight(null)*800/src.getWidth(null);
				BufferedImage tag = new BufferedImage((int)imageWidth, (int)imageHeight, BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(src.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH), 0, 0, null);
//				BufferedImage bi = ImageIO.read(files[i]);
//				if (bi == null)
//					continue;
				icon = new ImageIcon(tag);
			} catch (IOException e) {
				continue;
			}
			// if (suffix.equals("bmp")) {
			// images.add(icon);
			// continue;
			// }
			images.add(icon);
			imageNames.add(fileName.substring(0, fileName.lastIndexOf(".")));
			REAL_NUM_IMAGES++;
		}

		phaseSet = new JButton("把照片裝進來");
		phaseChoice = new JButton("开始/停止");

		final JLabel p1_des = new JLabel("紀念品數量：");
		p1 = new JTextField(20);
		final JLabel p2_des = new JLabel("三等獎數量：");
		p2 = new JTextField(20);
		final JLabel p3_des = new JLabel("二等獎數量：");
		p3 = new JTextField(20);
		final JLabel p4_des = new JLabel("一等獎數量：");
		p4 = new JTextField(20);
		wait_des = new JLabel();
		
		// Add moon phases combo box to select panel and image label to
		// displayPanel.
		JPanel choicePanel = new JPanel();
		JPanel p1Panel = new JPanel();
		JPanel p2Panel = new JPanel();
		JPanel p3Panel = new JPanel();
		JPanel p4Panel = new JPanel();
		JPanel waitPanel = new JPanel();
		
		selectPanel.setLayout(new GridLayout(7, 1, 5, 5));
		selectPanel.add(choicePanel);
		selectPanel.add(p1Panel);
		selectPanel.add(p2Panel);
		selectPanel.add(p3Panel);
		selectPanel.add(p4Panel);
		selectPanel.add(waitPanel);
		
		choicePanel.add(phaseChoice);
		p1Panel.add(p1_des);
		p1Panel.add(p1);
		p2Panel.add(p2_des);
		p2Panel.add(p2);
		p3Panel.add(p3_des);
		p3Panel.add(p3);
		p4Panel.add(p4_des);
		p4Panel.add(p4);
		waitPanel.add(wait_des);

		displayPanel.add(phaseIconLabel);
		resultPanel.add(phaseBlank1);
		resultPanel.add(phaseResult);
		resultPanel.add(phaseBlank2);

		// Listen to events from combo box.
		phaseChoice.addActionListener(this);
		phaseSet.addActionListener(this);
	}

	boolean run = false;

	int b = 1;
	int i = 0;
	StringBuffer s = new StringBuffer("獲獎結果如下：\r\n");
	// Implementation of ActionListener interface.
	public void actionPerformed(ActionEvent event) {
        if("开始/停止".endsWith(event.getActionCommand())) {
        	if (run) {
        		run = false;
        		phaseBlank1.setText("恭喜");
        		if (Integer.parseInt(p1.getText()) > 0) {
        			p1.setText(String.valueOf(Integer.parseInt(p1.getText()) - 1));
            		phaseBlank2.setText("獲得紀念獎！");
            		s.append(imageNames.get(b) + "獲得紀念獎！\r\n");
        		} else if (Integer.parseInt(p2.getText())>0 && Integer.parseInt(p1.getText()) == 0) {
        			p2.setText(String.valueOf(Integer.parseInt(p2.getText()) - 1));
        			phaseBlank2.setText("獲得三等獎！");
        			s.append(imageNames.get(b) + "獲得三等獎！\r\n");
        		} else if (Integer.parseInt(p3.getText())>0 && Integer.parseInt(p2.getText()) == 0 && Integer.parseInt(p1.getText()) == 0) {
        			p3.setText(String.valueOf(Integer.parseInt(p3.getText()) - 1));
        			phaseBlank2.setText("獲得二等獎！");
        			s.append(imageNames.get(b) + "獲得二等獎！\r\n");
        		} else if (Integer.parseInt(p4.getText())>0 && Integer.parseInt(p3.getText()) == 0 && Integer.parseInt(p2.getText()) == 0 && Integer.parseInt(p1.getText()) == 0) {
        			p4.setText(String.valueOf(Integer.parseInt(p4.getText()) - 1));
        			phaseBlank2.setText("獲得一等獎！");
        			s.append(imageNames.get(b) + "獲得一等獎！\r\n");
        			if (Integer.parseInt(p4.getText()) == 0) {
        				phaseChoice.setEnabled(false);
        				wait_des.setText("獎品已全部抽完，5秒鈡后生成獲獎名單！");
        				new Timer().schedule(new TimerTask() {
        					public void run () {
        						phaseResult.setText("");
        						phaseBlank1.setText("");
        						phaseBlank2.setText("");
        						result = new JTextArea(28, 82);
        						result.setLineWrap(true);
        						JScrollPane jsp = new JScrollPane();
        						jsp.getViewport().add(result);
        						result.setText(s.toString());
        						resultPanel.add(result);
        					}
        				}, 5000);
            		}
        		} 
        		images.remove(b);
        		imageNames.remove(b);
        		REAL_NUM_IMAGES --;
        		
        	} else {
        		run = true;
        		wait_des.setText("還有"+images.size()+"人有機會哦！");
                if(images.size() == 0) { // 避免因找不到图片目录而出现空指针异常
                    StringBuffer msg = new StringBuffer("没有找到图片目录: ")
                        .append(new File(ClassLoader.getSystemResource("").getPath()))
                        .append(File.separator).append("images").append(File.separator);
                    System.out.println(msg);
                    System.exit(0);
                    return;
                }
                
        		new Thread() {
        			public void run() {
        				while (run) {
        					int a = (int) (Math.random() * REAL_NUM_IMAGES);
        					b = a;
        					System.out.println(a);
        					phaseIconLabel.setIcon(images.get(a));
        					phaseBlank1.setText("");
        					phaseResult.setText(imageNames.get(a));
        					phaseBlank2.setText("");
        					try {
        						Thread.sleep(10);
        					} catch (Exception e) {
        					}
        				}
        			}
        		}.start();
        	}
        }
        
        if("把照片裝進來".endsWith(event.getActionCommand())) {
        
	        JFileChooser jfc=new JFileChooser();  
	        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
	        jfc.showDialog(new JLabel(), "请选择");  
	        File file=jfc.getSelectedFile();  
	        images.clear();
	        imageNames.clear();
	        addWidgets(file.getAbsolutePath());
	        phasesFrame.validate();
	  
        }
    }

	// main method
	public static void main(String[] args) {
		// create a new instance
		Lottery lottery = new Lottery();

//		// Create a frame and container for the panels.
//		JFrame phasesFrame = new JFrame("抽奖程序");

		// Set the look and feel.
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//		phasesFrame.setBounds((d.width - 500)/2, (d.height - 500)/2, 500, 500);
		phasesFrame.setPreferredSize(d);
		phasesFrame.setResizable(false);

		phasesFrame.setContentPane(lottery.mainPanel);
		
		// Exit when the window is closed.
		phasesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Show the converter.
		phasesFrame.pack();
		phasesFrame.setVisible(true);
	}
}
