import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//change password
public class ChangePwd extends JFrame implements ActionListener{
	private String host;
	private String stuID;
	private connectDB conn=new connectDB();
	private JLabel[] jlArray={new JLabel("Original Password"),new JLabel("New Password"),new JLabel("Confirm New Password")};
	private JPasswordField[] jpfArray={new JPasswordField(),new JPasswordField(),new JPasswordField()};
	private JButton[] jbArray={new JButton("Confirm"),new JButton("Reset")};
	
	public ChangePwd(String stuID){
		this.stuID=stuID;
		this.initialFrame();
		this.addListener();
	}
	
	public void addListener() {
		jpfArray[0].addActionListener(this);
		jpfArray[1].addActionListener(this);
		jpfArray[2].addActionListener(this);
		jbArray[0].addActionListener(this);
		jbArray[1].addActionListener(this);
		
	}

	public void initialFrame() {
		this.setLayout(null);
		this.setTitle("Change Password");
		this.setSize(450, 350);
		this.setVisible(true);
		for(int i=0;i<3;i++)
		{
			jlArray[i].setBounds(30,20+50*i,150,30);
			this.add(jlArray[i]);
			jpfArray[i].setBounds(200,20+50*i,150,30);
			this.add(jpfArray[i]);
		}
		jbArray[0].setBounds(40,180,100,30);
		this.add(jbArray[0]);
		jbArray[1].setBounds(220,180,100,30);
		this.add(jbArray[1]);
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jpfArray[0]){
			jpfArray[1].requestFocus(true);
		}
		else if(e.getSource()==jpfArray[1])
		{
			jpfArray[2].requestFocus(true);
		}
		else if(e.getSource()==jpfArray[2])
		{
			jbArray[0].requestFocus(true);
		}
		else if(e.getSource()==jbArray[1])
		{
			for(int i=0;i<jpfArray.length;i++){
				jpfArray[i].setText("");
			}
		}
		else if(e.getSource()==jbArray[0])
		{
			String patternStr="[0-9a-zA-Z]{6,12}";
			String oldPwd=jpfArray[0].getText();
			if(oldPwd.equals(""))
			{
				JOptionPane.showMessageDialog(this,"Input the original password plz","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			String newPwd=jpfArray[1].getText();
			if(newPwd.equals(""))
			{
				JOptionPane.showMessageDialog(this,"Input the new password plz","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(!newPwd.matches(patternStr))
			{
				JOptionPane.showMessageDialog(this,"Your password must be at least 6 characters.","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			String newPwd1=jpfArray[2].getText();
			if(!newPwd.equals(newPwd1))
			{//
				JOptionPane.showMessageDialog(this,"Confirm pwd and new pwd are different","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			try
			{
				String sql="update user_stu set PWD='"+newPwd+"' where stuID='"+stuID+"'"+
						   " and PWD='"+oldPwd+"'";
				System.out.println(sql);
				int i=conn.stmt.executeUpdate(sql);
				if(i==0){
					JOptionPane.showMessageDialog(this,"Failed，verify your password plz","Error",JOptionPane.ERROR_MESSAGE);
				}else if(i==1)
				{
					JOptionPane.showMessageDialog(this,"Password changed","!",JOptionPane.INFORMATION_MESSAGE);
					conn.close();
				}
				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
	}
	//public void setFocus()
	//{
		//jpfArray[0].requestFocus(true);
	//}
	public static void main(String[] arg){
		new ChangePwd("10411379");
	}

}
