import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class form extends JFrame
{
 public form()
 {
  setSize(400,300);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setVisible(true);
  ButtonGroup bg=new ButtonGroup();
  JRadioButton rb1=new JRadioButton();
  JRadioButton rb2=new JRadioButton();
  bg.add(rb1);
  bg.add(rb2);
  setLayout(new FlowLayout());//-------布局方式
  add(rb1);     //----------添加rb1
  add(rb2);    //---------------添加rb2
 }
 
 public static void main(String[] args) {
	 form frame = new form();
}
} 
