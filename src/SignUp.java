import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;

public class SignUp extends JPanel implements ActionListener{
	
	private int w=290;//LoginWin width
	private int h=320;//LoginWin Height
	private JLabel myTitle = new JLabel("Create your account");
	private JLabel jl_user=new JLabel("Username:");
	private JLabel jl_pwd=new JLabel("Password:");
	private JLabel jl_pwdConf=new JLabel("Confirm Password:");
	private JTextField jtf_user=new JTextField();
	private JPasswordField jpwf=new JPasswordField();
	private JPasswordField jpwf_conf=new JPasswordField();
	private JButton jb_su=new JButton("Sign Up");
	private JLabel jl_blank = new JLabel("");
	private Statement stmt;
	private ResultSet rs;
	
	SignUp(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBackground(new Color(255,255,255));   //set background color
		//add(jl_blank,BorderLayout.SOUTH);
		this.setLayout(new GridLayout(9,1,10,0));
		//this.myTitle.setSize(280, 40);
		myTitle.setForeground(Color.DARK_GRAY);
		myTitle.setFont(new Font("Serif", Font.ITALIC, 20));
		add(myTitle);
		//jl_user.setSize(280,40);
		add(jl_user);
		//jtf_user.setSize(110,30);
		add(jtf_user);
		//this.add(jb_su, BorderLayout.SOUTH);
		add(jl_pwd);
		add(jpwf);
		add(jl_pwdConf);
		add(jpwf_conf);
		add(jl_blank);
		add(jb_su);
		jb_su.addActionListener(this);
	}
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == jb_su){
			String name=this.jtf_user.getText().trim();
			if(name.equals("")){
				JOptionPane.showMessageDialog(this,"Please type your username!","Error",
				                               JOptionPane.ERROR_MESSAGE);
				return;
			}
			String pwd=this.jpwf.getText().trim();
			if(pwd.equals("")){
				JOptionPane.showMessageDialog(this,"Please type your password!","Error",
				                           JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(jpwf_conf.getText().trim().equals("")){
				JOptionPane.showMessageDialog(this,"Please confirm your password!","Error",
				                           JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(!pwd.equals(jpwf_conf.getText().trim())){
				JOptionPane.showMessageDialog(this,"Passwords Don't Match!","Error",
                        JOptionPane.ERROR_MESSAGE);
				return;
			}
			try{
				connectDB cdb = new connectDB();
				    //create SQL to check info in database
					String sql="select * from user_stu where "+
					"stu_id='"+name;
					rs=stmt.executeQuery(sql);
					if(rs.next()){   //username has been used
						JOptionPane.showMessageDialog(this,"This Username already exists!","Error",
		                        JOptionPane.ERROR_MESSAGE);
						return;
					}
					else{
						JOptionPane.showMessageDialog(this,"The login and/or password you specified are not correct.","Error",
						                           JOptionPane.ERROR_MESSAGE);
					}
					cdb.close();//close connection
			}catch(SQLException ea){
			ea.printStackTrace();  
			}
		}
	}
}
