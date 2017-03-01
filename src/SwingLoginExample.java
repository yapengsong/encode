import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PrivateKey;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField; 
public class SwingLoginExample {
	public JTextField userText;
	public JTextArea passwordText;
    public static void main(String[] args) {    
    	SwingLoginExample se=new SwingLoginExample();
    	se.placeComponents();
    }

    public void placeComponents() {
    	
    	// 创建 JFrame 实例
        JFrame frame = new JFrame("License");
        // Setting the width and height of frame
        frame.setSize(350, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        
        JPanel panel = new JPanel();    
        // 添加面板
        frame.add(panel);
        //frame.add(bg);
        /* 
         * 调用用户定义的方法并添加组件到面板
         */
        //placeComponents(panel);

        // 设置界面可见
        frame.setVisible(true);
    	
    	

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        //panel.setLayout(new GridLayout(0,1));
    	panel.setLayout(null);
        ButtonGroup bg=new ButtonGroup();
        
        JRadioButton rbutt1=new JRadioButton("111");
        rbutt1.setBounds(10, 0, 40, 25);
        
        JRadioButton rbutt2=new JRadioButton("222");
        rbutt2.setBounds(120, 0, 40, 25);
       
        bg.add(rbutt1);
        bg.add(rbutt2);
        panel.add(rbutt1);
        panel.add(rbutt2);
        //panel.add(bg);

        // 创建 JLabel
        JLabel userLabel = new JLabel("注册号：");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(10,30,80,25);
        panel.add(userLabel);

        /* 
         * 创建文本域用于用户输入
         */
        userText = new JTextField(20);
        userText.setBounds(10,60,300,25);
        panel.add(userText);
        
        
        // 创建登录按钮
        JButton loginButton = new JButton("生成");
        loginButton.setBounds(10, 90, 80, 25);
        loginButton.setActionCommand("OK");
        loginButton.addActionListener(new ButtonClickListener()); 
     
        
        panel.add(loginButton);

        // 输入密码的文本域
        JLabel passwordLabel = new JLabel("激活号：");
        passwordLabel.setBounds(10,120,80,25);
        panel.add(passwordLabel);

        /* 
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        passwordText = new JTextArea(10,10);
        passwordText.setBounds(10,150,300,150);
        passwordText.setLineWrap(true);        //激活自动换行功能 
        passwordText.setWrapStyleWord(true);            // 激活断行不断字功能
        JScrollPane sp=new JScrollPane(passwordText);
        sp.setBounds(10, 150, 300, 150);
        panel.add(sp);

        
    }
    
    private class ButtonClickListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
           String command = e.getActionCommand();  
           if( command.equals( "OK" ))  {
        	 //弹出对话框
               JOptionPane.showMessageDialog(null, "弹出对话框");
               //获得userText文本
               String codeTxt=userText.getText();
               //私钥加密
               try {
					String privatePath = "/opt/key/HeartyPri.key";
					PrivateKey privateKey=(PrivateKey)KeyStoreHelper.getKey(privatePath);
					byte [] encode=KeyStoreHelper.encryptByKey(codeTxt, privateKey);
					//加密结果放入passwordText
					passwordText.setText(new String(encode));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
           }
           
        }		
     }

}