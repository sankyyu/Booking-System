import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

//log-in window
public class loginWindow extends JFrame implements ActionListener{
	
	//define login window
	private String host = "localhost";
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	//private JLabel imgl = new JLabel(new ImageIcon("C:\\Users\\86\\Documents\\GitHub\\learningJava\\courseSelection\\res\\Stevens_Statue.png"));
	private JPanel jp=new JPanel();
	private CardLayout cardLayout = new CardLayout();
	private JPanel cardPanel = new JPanel(cardLayout);
	private Container c = getContentPane();
	private JLabel myTitle = new JLabel("SIT Course Selection System");
	private JLabel jl1=new JLabel("Username");
	private JLabel jl2=new JLabel("Password");
	private JTextField jtf=new JTextField();
	private JPasswordField jpwf=new JPasswordField();
	private JRadioButton[] jrbArray=// selection buttons
	        {
	        	new JRadioButton("Student",true),
	        	new JRadioButton("Admin")
	        };
	//button group
	private ButtonGroup bg=new ButtonGroup();
	//Buttons
	private JButton jb1=new JButton("Sign in");
	private JButton jb2=new JButton("Reset");
	private JButton jb3=new JButton("ForgotPWD");

	public loginWindow()
	{ 
	    this.addListener();
		initialFrame();//initialize frame
	}
	public void addListener(){
		this.jb1.addActionListener(this);//SignIn ActionListener
		this.jb2.addActionListener(this);//Reset ActionListener
		this.jb3.addActionListener(this);//SignUp ActionListener
		this.jtf.addActionListener(this);//User text field ActionListener
		this.jpwf.addActionListener(this);//password text field ActionListener
	}
	public void initialFrame()
	{
		//Set title, size, Location of LoginWin
		this.setTitle("WELCOME");
		//Image image=new ImageIcon("Stevens_Statue.png").getImage(); 
		//this.getContentPane().add(imgl);
 		//this.setIconImage(image);
		this.setResizable(false);    //can not be resized
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=290;//LoginWin width
		int h=320;//LoginWin Height
		this.setBounds(centerX-w/2,centerY-h/2-100,w,h);//set Window in the center of screen
		this.setVisible(true);
		//jp.setBackground(Color.CYAN);    //set background color
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//set LoginWin's buttons and labels
		jp.setLayout(null);
		jp.setBackground(Color.WHITE);
		this.myTitle.setBounds(25, 5, 240, 40);  //title
		myTitle.setForeground(Color.DARK_GRAY);
		myTitle.setFont(new Font("Serif", Font.ITALIC, 20));
		this.jp.add(myTitle);
		this.jl1.setBounds(30,60,110,25);  //user
		this.jp.add(jl1);
		this.jtf.setBounds(120,60,130,25);
		this.jp.add(jtf);
		this.jl2.setBounds(30,100,110,25);  //password
		this.jp.add(jl2);
		this.jpwf.setBounds(120,100,130,25);
		this.jpwf.setEchoChar('*');
		this.jp.add(jpwf);
		this.bg.add(jrbArray[0]);           //ButtonGroup
		this.bg.add(jrbArray[1]);
		this.jrbArray[0].setBounds(35,210,100,25);
		this.jrbArray[0].setBackground(Color.WHITE);
		this.jp.add(jrbArray[0]);
		this.jrbArray[1].setBounds(150,210,100,25);
		this.jrbArray[1].setBackground(Color.WHITE);
		this.jp.add(jrbArray[1]);
		this.jb1.setBounds(40,140,200,30);   //Login
		this.jp.add(jb1);
		this.jb2.setBounds(142,180,98,25);  //Reset
		this.jp.add(jb2);
		this.jb3.setBounds(40,180,98,25);   //ForgotPWD
		this.jp.add(jb3);
		this.add(jp);		
		//this.imgl.setBounds(15,40,250,140); //image
		//this.jp.add(imgl);  
		cardPanel.add(new SignUp(), "SignUp");
		cardPanel.add(jp,"Login");
		c.add(cardPanel,BorderLayout.CENTER);
		cardLayout.show(cardPanel, "Login");
	}
	//define ActionListener
	public void actionPerformed(ActionEvent e)
	{
		// press "Sign in"
		if(e.getSource()==this.jb1)
		{
			this.setTitle("Logging...");
			String userID=this.jtf.getText().trim();
			if(userID.equals("")){
				JOptionPane.showMessageDialog(this,"Please type your username!","Error",
				                               JOptionPane.ERROR_MESSAGE);
				this.setTitle("WELCOME");return;
			}
			String pwd=this.jpwf.getText().trim();
			if(pwd.equals("")){
				JOptionPane.showMessageDialog(this,"Please type your password!","Error",
				                           JOptionPane.ERROR_MESSAGE);
				this.setTitle("WELCOME");return;
			}
			int type=this.jrbArray[0].isSelected()?0:1;// get the jrbArray number
			try{   
	            this.initialConnection();
				if(type==0){//Student
				    //create SQL to check info in database
					String sql="select * from user_stu where "+
					"stuID='"+userID+"' and PWD='"+pwd+"'";
					rs=stmt.executeQuery(sql);
					if(rs.next()){
						new StuClient(userID);//create Student Client window
						this.dispose();//close Login window
					}
					else{//error warning
						JOptionPane.showMessageDialog(this,"The login and/or password you specified are not correct.","Error",
						                           JOptionPane.ERROR_MESSAGE);
						this.setTitle("WELCOME");
					}
					this.closeConn();//close connection
				}
				else{//Admin
					//create SQL to check info in database
					String sql="select * from user_admin where "+
					             "adminID='"+userID+"' and PWD='"+pwd+"'";
					rs=stmt.executeQuery(sql);
					if(rs.next()){
						new adminClient(userID);//create Admin Client window
						this.dispose();//close Login window
					}
					else{//error warning
						JOptionPane.showMessageDialog(this,"The login and/or password you specified are not correct.","Error",
						                           JOptionPane.ERROR_MESSAGE);
						this.setTitle("WELCOME");
					}
					this.closeConn();	//close connection
				}
			}
			catch(SQLException ea){ea.printStackTrace();}
		}
		else if(e.getSource()==this.jb2){// Reset: clean up text field
			this.jtf.setText("");
			this.jpwf.setText("");
		}
		else if(e.getSource()==this.jb3){// forgotPWD action
			cardLayout.show(cardPanel, "Forgot PWD");//create forgotPWD window
			//this.dispose();//close Login window
		}
		else if(e.getSource()==jtf){// type in User name and press "Enter"
			this.jpwf.requestFocus(true);
		}
		else if(e.getSource()==jpwf){//type in Password and press "Enter"
			this.jb1.requestFocus(true);
		}
	}

	public void  initialConnection()
	{
		try
		{//load JDBC driver, make Connection and Statement
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost"+"/courseBooking","root","");
			stmt=conn.createStatement();
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(this,"Failed to connect, please check the Hostname and port","Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	//close connection
	public void closeConn()
	{
		try
		{
			if(rs!=null)
			{
				rs.close();
			}
			if(stmt!=null)
			{
				stmt.close();
			}
			if(conn!=null)
			{
				conn.close();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		    loginWindow login = new loginWindow();
	}
}
