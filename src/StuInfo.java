import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;

public class StuInfo extends JPanel implements ActionListener{
	private String host;
	//private JPanel jp=new JPanel();
	private String stu_id;
	//private Connection conn;
	//private Statement stmt;
	private GetStuInfo getsi;
	
	private JLabel[] jlArray={new JLabel("StudentID:"),new JLabel("FirstName:"),
							  new JLabel("MiddleName:"),new JLabel("LastName:"),
			                  new JLabel("Gender:"), new JLabel("Birthday:"),new JLabel("Nationality:"),
			                  new JLabel("College:"), new JLabel("Department:"),
			                  new JLabel("Major:"), new JLabel("Enrolltime:"),
			                  new JLabel("Graduatetime:"), new JLabel("Address:"),
			                  new JLabel("E-mail:"), new JLabel("Phone number:")
	};
	private JLabel[] jlinfo=new JLabel[12];
	private JTextField[] jtArray=new JTextField[3];
	private JButton[] jbArray={new JButton("Change"),new JButton("Change"),
							   new JButton("Change"),new JButton("Apply")};
	
	//@Override
	public StuInfo(String stu_id,String host){
		this.host=host;
		this.stu_id=stu_id;
		getsi=new GetStuInfo(host);
		this.initialFrame();
		this.addListener();
	}
	public void addListener(){
		for(int i=0;i<4;i++){
			jbArray[i].addActionListener(this);
		}
	}
	public void initialFrame()
	{	String[] baseinfo=getsi.getBaseInfo(this.stu_id);
		//System.out.println("The stuid in StuInfo is "+this.stu_id);
		this.setLayout(null);
		this.setBackground(new Color(255,250,240));
		for(int i=0;i<15;i++){
			jlArray[i].setBounds(60,20+i*35,120,20);
			jlArray[i].setFont(new Font("Serif",Font.BOLD,14));
			this.add(jlArray[i]);
		}
		for(int i=0;i<12;i++){
			jlinfo[i]=new JLabel(baseinfo[i]);
			jlinfo[i].setBounds(210, 20+i*35, 120, 20);
			jlinfo[i].setFont(new Font("Serif",Font.ROMAN_BASELINE,14));
			this.add(jlinfo[i]);
		}
		for(int i=0;i<3;i++){
			jtArray[i]=new JTextField(baseinfo[i+12]);
			jtArray[i].setBounds(210,440+i*35,120,20);
			jtArray[i].setEditable(false);
			jtArray[i].setFont(new Font("Serif",Font.ROMAN_BASELINE,14));
			this.add(jtArray[i]);
		}
		for(int i=0;i<3;i++){
			jbArray[i].setBounds(360, 440+i*35, 100, 20);
			this.add(jbArray[i]);
		}
		jbArray[3].setBounds(360, 560, 100, 30);
		this.add(jbArray[3]);
		jbArray[3].setVisible(false);
		//this.setTitle("Student Information");
		this.setVisible(true);
		this.setBounds(0,0,600,2000);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.jbArray[0]){
			jtArray[0].setEditable(true);
			jbArray[3].setVisible(true);
		}
		if(e.getSource()==this.jbArray[1]){
			jtArray[1].setEditable(true);
			jbArray[3].setVisible(true);
		}
		if(e.getSource()==this.jbArray[2]){
			jtArray[2].setEditable(true);
			jbArray[3].setVisible(true);
		}
		if(e.getSource()==this.jbArray[3]){
			String sql="update student SET Email='"+jtArray[0].getText()+"',address='"+jtArray[1].getText()+"',phone='"+jtArray[2].getText()+"' where stuID='"+stu_id+"'";
			System.out.println(sql);
			connectDB conn=new connectDB();
			jtArray[0].setEditable(false);
			jtArray[1].setEditable(false);
			jtArray[2].setEditable(false);
			jbArray[3].setVisible(false);
			try
			{
				conn.stmt.execute(sql);
				JOptionPane.showMessageDialog(null, "Changed");
				//System.out.println("i am here");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
	}
	/*public static void main(String[] arg){
		StuInfo a=new StuInfo("a","a");
	}*/

}

