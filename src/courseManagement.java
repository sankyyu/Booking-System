import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class courseManagement extends JPanel{
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = screenSize.width/2;
	private int height = screenSize.height/2;
	private connectDB conn = new connectDB();
	//JPanels
	private ChooseCourse addCourse_jp;
	//Layout
	private SpringLayout jspring;
	//JButtons
	private JButton delete_jb = new JButton("Delete Course");// delete-course button
	private JButton add_jb = new JButton("New Course");// add-course button
	private JButton edit_jb = new JButton("Edit Course");// edit-course button
	public courseManagement(){
		jspring = new SpringLayout();
		this.setLayout(jspring);
		this.setVisible(true);
		this.setBackground(new Color(255,250,240));
		makePanel();
		addListener();
	}
	
	public void makePanel(){
		//add course selection table 
		addCourse_jp = new ChooseCourse(""); 
		addCourse_jp.removeButtons();
		addCourse_jp.setTitle("The current courses are listed below :");
		addCourse_jp.setPreferredSize(new Dimension(this.getWidth(),400));
		this.add(addCourse_jp);
		jspring.putConstraint(SpringLayout.NORTH, addCourse_jp, 10, SpringLayout.NORTH, this);
		jspring.putConstraint(SpringLayout.WEST, addCourse_jp, 20, SpringLayout.WEST, this);
		jspring.putConstraint(SpringLayout.EAST, addCourse_jp, -20, SpringLayout.EAST, this);
		
		//add buttons
		edit_jb.setSize(50, 30);
		this.add(edit_jb);
		jspring.putConstraint(SpringLayout.NORTH, edit_jb, 0, SpringLayout.SOUTH, addCourse_jp);
		jspring.putConstraint(SpringLayout.WEST, edit_jb, 150, SpringLayout.WEST, this);
		add_jb.setSize(50,30);
		this.add(add_jb);
		jspring.putConstraint(SpringLayout.NORTH, add_jb, 0, SpringLayout.SOUTH, addCourse_jp);
		jspring.putConstraint(SpringLayout.WEST, add_jb, 100, SpringLayout.EAST, edit_jb);
		delete_jb.setSize(50,30);
		this.add(delete_jb);
		jspring.putConstraint(SpringLayout.NORTH, delete_jb, 0, SpringLayout.SOUTH, addCourse_jp);
		jspring.putConstraint(SpringLayout.WEST, delete_jb, 100, SpringLayout.EAST, add_jb);
	}
	
	public class makeCourseinfoWin extends JFrame{
		private Container c;
		private JPanel jp;
		private JPanel grid_jp;
		private int centerX=screenSize.width/2;
		private int centerY=screenSize.height/2;

		private JLabel[] label = {  new JLabel("Course Name:"),  new JLabel("Course ID:"),
					                new JLabel("College ID:"),	 new JLabel("Department ID:"),
					                new JLabel("Major ID:"),     new JLabel("Instructor:"),
					                new JLabel("Credit:"),		 new JLabel("Location:"),
					                new JLabel("Weekday:"), 	 new JLabel("Time:"),
					                new JLabel("Capacity:"),	 new JLabel("Rest Capacity:"),
					                new JLabel("Introduction:"), new JLabel(),
					                new JLabel(),new JLabel()
									};
		private JTextField[] textField = {  new JTextField(),new JTextField(),
							                new JTextField(),new JTextField(),
							                new JTextField(),new JTextField(),
							                new JTextField(),new JTextField(),
							                new JTextField(),new JTextField(),
							                new JTextField(),new JTextField()
											};
		private JTextArea intro_ta = new JTextArea(20,50);
		
		private JButton confm = new JButton("Confirm");
		
		public makeCourseinfoWin(String title){
			super(title);
			c = this.getContentPane();
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLocation(centerX-width/2,centerY-height/2-100);
			this.setSize(new Dimension(800,400));
			//main panel
			jp = new JPanel(null);
			jp.setBackground(new Color(255,250,240));
			c.add(jp);
			//add course info
			grid_jp = new JPanel(new GridLayout(7,4,5,5));
			grid_jp.setVisible(true);
			grid_jp.setSize(740, 200);
			grid_jp.setLocation(20, 10);
			//grid_jp.setBackground(Color.WHITE);
			
			for(int i = 0;i<12;i++){
				grid_jp.add(label[i]);
				grid_jp.add(textField[i]);
			}
			for(int i = 12; i<16;i++)
				grid_jp.add(label[i]);
			jp.add(grid_jp);
			//add course introduction text area
			intro_ta.setBounds(20,210,740,80);
			intro_ta.setLineWrap(true);
			intro_ta.setWrapStyleWord(true);
			jp.add(intro_ta);
			//add confirm button
			confm.setBounds(340, 300, 120, 30);
			jp.add(confm);	
			for(int i=0;i<10;i++){
				textField[i].setText("REQUIRED");
				textField[i].addFocusListener(new myFocusListener());
			}
			
		}
		//check text field to enable confirm button or not
		public void setConfmStatus(){
			for(int i =0; i<10;i++){
				if(textField[i].getText().equals("REQUIRED")){
					confm.setEnabled(false);
					return;
				}
			}
			confm.setEnabled(true);	
		}
		
		//set text field editable option
		public void setTextEditable(boolean[] b){
			for(int i = 0; i<12; i++)
				textField[i].setEditable(b[i]);
		}
		//set text field content
		public void setTextContent(String[] s){
			for(int i = 0;i<12;i++)
				textField[i].setText(s[i]);
			intro_ta.setText(s[12]);
		}
		//get text input
		public String[] getTextContent(){
			String[] s = {  textField[0].getText(),textField[1].getText(),
							textField[2].getText(),textField[3].getText(),
							textField[4].getText(),textField[5].getText(),
							textField[6].getText(),textField[7].getText(),
							textField[8].getText(),textField[9].getText(),
							textField[10].getText(),textField[11].getText(),
							intro_ta.getText()	
			};
			return s;
		}
		//add actionListener
		public void setActionListener(ActionListener a){
			confm.addActionListener(a);
		}
		//set focusListener for text field
		private class myFocusListener implements FocusListener{

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				JTextField j = (JTextField)(e.getComponent());
				if("REQUIRED".equalsIgnoreCase(j.getText())){
			        j.setText("");
			    }
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				JTextField j = (JTextField)(e.getComponent());
				if("".equals(j.getText().trim())){
			        j.setText("REQUIRED");
			    }
				setConfmStatus();
			}
			
		}
	}
	
	//make course editor window
	public class makeEditWin{
		makeCourseinfoWin editwin = new makeCourseinfoWin("Course Editor");
		boolean[] b = { false, false,
						false,false,
						false,true,
						true,true,
						true,true,
						true,true};
		
		public makeEditWin(){
			
			editwin.setTextEditable(b);
			try{
				String courseID = (String)(addCourse_jp.getSelectedCourseID());
				String sql = "select courseName,course.courseID,collID,deptID,majorID,teacher,"
						+ "credit,location,courseDay,courseTime,capacity,restCapacity,description from course,courseinfo "
						+ " where course.courseID='"+courseID+"' and courseinfo.courseID='"+courseID+"'";
				conn.rs = conn.stmt.executeQuery(sql);
				conn.rs.next();
				String[] s = {  conn.rs.getString(1),conn.rs.getString(2),
								conn.rs.getString(3),conn.rs.getString(4),
								conn.rs.getString(5),conn.rs.getString(6),
								conn.rs.getString(7),conn.rs.getString(8),
								conn.rs.getString(9),conn.rs.getString(10),
								conn.rs.getString(11),conn.rs.getString(12),
								conn.rs.getString(13)
				};
				editwin.setTextContent(s);
			}catch(Exception e){e.printStackTrace();}
			editwin.setActionListener(new editConfirmActionListener());
		}
		
		//actionListener for edit-confirm
		public class editConfirmActionListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try{
					String courseID = (String)(addCourse_jp.getSelectedCourseID());
					String []data = editwin.getTextContent();
					if(data[10].trim().equals(""))
						data[10] = "30";
					else
						data[10] = editwin.textField[10].getText();
					if(data[11].trim().equals(""))
						data[11] = "30";
					else
						data[11] = editwin.textField[11].getText();
					if(data[12].trim().equals(""))
						data[12] = "Sorry, there is no introdution this moment.";
					else
						data[12] = (String)(editwin.intro_ta.getText());
					String sql = "update course,courseinfo set teacher='"+data[5]+"', credit='"+data[6]
							+"', location='"+data[7]+"', courseDay='"+data[8]+"', courseTime='"+data[9]
							+"', capacity='"+data[10]+"', restCapacity='"+data[11]+"',description='"+data[12]
							+ "' where course.courseID='"+courseID+"' and courseinfo.courseID='"+courseID+"'";
					System.out.println(sql);
					int i=conn.stmt.executeUpdate(sql);
					if(i!=0)
					{//successful
						JOptionPane.showMessageDialog(editwin,"Updated!","Successful",
						                            JOptionPane.INFORMATION_MESSAGE);
						addCourse_jp.updateTable();
						editwin.dispose();
					}
					else
					{//failed
						JOptionPane.showMessageDialog(editwin,"Failed to update! Please try again.","Error",
						                                 JOptionPane.ERROR_MESSAGE);
					}
				}catch(Exception E){E.printStackTrace();}
			}
			
		}
	}
	
	
	public class makeAddWin{
		makeCourseinfoWin addwin = new makeCourseinfoWin("New Course");
		boolean[] b = { true,true,
						true,true,
						true,true,
						true,true,
						true,true,
						true,true};
		
		public makeAddWin(){
			
			addwin.setTextEditable(b);
			addwin.setConfmStatus();
			addwin.setActionListener(new addConfirmActionListener());
		}
		
		//actionListener for edit-confirm
		public class addConfirmActionListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try{
					String []data = addwin.getTextContent();
					if(data[10].trim().equals(""))
						data[10] = "30";
					if(data[11].trim().equals(""))
						data[11] = "30";
					if(data[12].trim().equals(""))
						data[12] = "Sorry, there is no introdution this moment.";
					String sql = "select * from course where courseID='"+data[1]+"'";
					System.out.println(sql);
					conn.rs = conn.stmt.executeQuery(sql);
					if(conn.rs.next()){
						JOptionPane.showMessageDialog(addwin,"The course already exists.","Error",
		                           JOptionPane.ERROR_MESSAGE);
						return;
					}else{
						sql = "insert into course value('"+data[1]+"', '"+data[0]+"', '"+data[6]+"', '"+data[2]+"', '"+data[3]+"', '"+data[4]+"')";
						System.out.println(sql);
						int i = conn.stmt.executeUpdate(sql);
						if(i==0)
						{//failed
							JOptionPane.showMessageDialog(addwin,"Failed to add! Please try again.","Error",
							                                 JOptionPane.ERROR_MESSAGE);
							return;
						}
						sql = "insert into courseinfo value('"+data[1]+"', '"+data[8]+"', '"+data[9]+"', '"
								+data[7]+"', '"+data[5]+"', '"+data[10]+"', '"+data[11]+"', '"+data[12]+"')";
						System.out.println(sql);
						//conn.stmt.executeUpdate(sql);
						i=conn.stmt.executeUpdate(sql);
						if(i!=0)
						{//successful
							JOptionPane.showMessageDialog(addwin,"Course added!","Successful",
							                            JOptionPane.INFORMATION_MESSAGE);
							addCourse_jp.updateTable();
							addwin.dispose();
						}
						else
						{//failed
							JOptionPane.showMessageDialog(addwin,"Failed to add! Please try again.","Error",
							                                 JOptionPane.ERROR_MESSAGE);
						}
					}
				}catch(Exception E){E.printStackTrace();}
			}
			
		}
	}
	
	public void addListener(){
		edit_jb.addActionListener(new myActionListener());
		add_jb.addActionListener(new myActionListener());
		delete_jb.addActionListener(new myActionListener());
	}
	
	//actionListener for edit, add, delete buttons
	public class myActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == edit_jb){
				if(addCourse_jp.isSelected())
					new makeEditWin();
			}
			if(e.getSource() == add_jb){
				new makeAddWin();
			}
			if(e.getSource() == delete_jb){
				if(addCourse_jp.isSelected()){
					try{
						String courseID = (String)(addCourse_jp.getSelectedCourseID());
						String sql = "delete courseinfo.* from courseinfo where courseinfo.courseID='"+courseID+"'";
						System.out.println(sql);
						conn.stmt.executeUpdate(sql);
						sql = "delete course.* from course where course.courseID='"+courseID+"'";
						System.out.println(sql);
						int i=conn.stmt.executeUpdate(sql);
						if(i!=0)
						{//successful
							JOptionPane.showMessageDialog(getParent(),"Deleted!","Successful",
							                            JOptionPane.INFORMATION_MESSAGE);
							addCourse_jp.updateTable();
						}
						else
						{//failed
							JOptionPane.showMessageDialog(getParent(),"Failed to deleted! Please try again.","Error",
							                                 JOptionPane.ERROR_MESSAGE);
							addCourse_jp.updateTable();
						}
					}catch(Exception E){E.printStackTrace();}
				}
			}
		}
		
	}
	
			
	public static void main(String[] arg){
		courseManagement cc=new courseManagement();
		JFrame jf=new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBounds(10,10,900,600);
		jf.add(cc);
		jf.setVisible(true);
	}
}
