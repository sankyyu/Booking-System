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

//choose courses
public class ChooseCourse extends JPanel implements ActionListener{
	
	private String host;
	private String stuID;
	
	//connect to MySQL database
	private connectDB conn = new connectDB();
	//panels
	//private JSplitPane jsplit;
	private JPanel uppart;
	//uppart
	private JLabel intro = new JLabel("Please choose your courses");
	private JLabel coll = new JLabel("College : ");
	private JLabel dept = new JLabel("Department : ");
	private JLabel major = new JLabel("Major : ");
	private JLabel prof = new JLabel("Professor : ");
	private JLabel weekday = new JLabel("Weekday : ");
	private JLabel timepart = new JLabel("Time : ");
	private JLabel blanklabel = new JLabel("  ");
	
	private JComboBox coll_jcb = new JComboBox();
	private JComboBox dept_jcb = new JComboBox();
	private JComboBox major_jcb = new JComboBox();
	private JComboBox prof_jcb = new JComboBox();
	private JComboBox weekday_jcb = new JComboBox();
	private JComboBox timepart_jcb = new JComboBox();
	private Vector coll_v = new Vector();
	private Vector dept_v = new Vector();
	private Vector major_v = new Vector();
	private Vector prof_v = new Vector();
	private Vector weekday_v = new Vector();
	private Vector timepart_v = new Vector();
	
	private JButton intro_jb = new JButton("Course Intro");// course intro button
	private JButton add_jb = new JButton("Add Course");// add course button
	
	private JTable jt;
	private JScrollPane jsp;
	//Vector for table header and content
	private Vector<String> v_head=new Vector<String>();
	private Vector<Vector> v_data=new Vector<Vector>();
	//Vector for available course
	private Vector<String> v_couid=new Vector<String>();
	
	//define table model to make cells not editable but selectable
	public class MyTableModel extends DefaultTableModel{ 
		public MyTableModel(Vector<Vector> v_data, Vector<String> v_head)
		{
			super(v_data, v_head);
		}
	    public boolean isCellEditable(int row, int column) { 
		   return false;
	    }
   }
	//Constructor
	public ChooseCourse(String stuID){
		//this.host=host;
		this.stuID=stuID;
		this.initialData();
		this.makePanel();//make panel
	}
	
	public void initialData(){
		this.initialCombox_coll();
		this.initialCombox_dept();
		this.initialCombox_major();
		this.initialCombox_prof();
		this.initialCombox_weekday();
		this.initialCombox_timepart();
		this.initialTable();//initialize table data

	}
	
	private SpringLayout jspring;
	//make panel
	public void makePanel(){
		
		jspring = new SpringLayout();
		this.setLayout(jspring);
		this.setVisible(true);
		//this.setSize(800,600);
		this.setBackground(new Color(255,250,240));
		uppart = new JPanel(new GridLayout(5,1,5,5));
//		jsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,uppart,downpart);
//		jsplit.setDividerLocation(100);
//		jsplit.setDividerSize(1);
		uppart.setBackground(Color.WHITE);
//		this.add(jsplit);
		
		blanklabel.setBackground(Color.WHITE);
		//uppart
		uppart.add(intro);
		JPanel coll_dept = new JPanel(new GridLayout(1,4,10,0));
		coll_dept.add(coll);
		coll_dept.add(coll_jcb);
		coll_dept.add(dept);
		coll_dept.add(dept_jcb);
		uppart.add(coll_dept);
		JPanel major_prof = new JPanel(new GridLayout(1,4,10,0));
		major_prof.add(major);
		major_prof.add(major_jcb);
		major_prof.add(prof);
		major_prof.add(prof_jcb);
		uppart.add(major_prof);
		JPanel weekday_timepart = new JPanel(new GridLayout(1,4,10,0));
		weekday_timepart.add(weekday);
		weekday_timepart.add(weekday_jcb);
		weekday_timepart.add(timepart);
		weekday_timepart.add(timepart_jcb);
		uppart.add(weekday_timepart);
		uppart.add(blanklabel);
		//set the uppart location
		this.add(uppart);
		jspring.putConstraint(SpringLayout.NORTH, uppart, 10, SpringLayout.NORTH, this);
		jspring.putConstraint(SpringLayout.WEST, uppart, 20, SpringLayout.WEST, this);
		jspring.putConstraint(SpringLayout.EAST, uppart, 756, SpringLayout.WEST, this);
		
		//table 
		//DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		MyTableModel tm = new MyTableModel(v_data,v_head);
		jt = new JTable(tm);// create JTable for course info
		jsp = new JScrollPane(jt);//put JTable in JScrollPane
		jsp.setBackground(Color.WHITE);
		
		//set the table location
		this.add(jsp);
		jspring.putConstraint(SpringLayout.NORTH, jsp, 10, SpringLayout.SOUTH, uppart);
		jspring.putConstraint(SpringLayout.WEST, jsp, 20, SpringLayout.WEST, this);
		jspring.putConstraint(SpringLayout.EAST, this, 20, SpringLayout.EAST, jsp);
		jspring.putConstraint(SpringLayout.SOUTH, jsp, 200, SpringLayout.NORTH, jsp);
		//set the intro button location
		intro_jb.setSize(50, 30);
		this.add(intro_jb);
		jspring.putConstraint(SpringLayout.NORTH, intro_jb, 10, SpringLayout.SOUTH, jsp);
		jspring.putConstraint(SpringLayout.WEST, intro_jb, 200, SpringLayout.WEST, this);
		//set the add-course button location
		add_jb.setSize(50, 30);
		this.add(add_jb);
		Spring addspring = Spring.constant(10,200,1000);
		jspring.putConstraint(SpringLayout.NORTH, add_jb, 10, SpringLayout.SOUTH, jsp);
		jspring.putConstraint(SpringLayout.EAST,add_jb, 300 , SpringLayout.EAST, intro_jb);
		
		//add actionListeners
		intro_jb.addActionListener(this);	   
		add_jb.addActionListener(this);		
		coll_jcb.addItemListener(new itemListener());
		dept_jcb.addItemListener(new itemListener());
		major_jcb.addItemListener(new itemListener());
		prof_jcb.addItemListener(new itemListener());
		weekday_jcb.addItemListener(new itemListener());
		timepart_jcb.addItemListener(new itemListener());

	}
	
	public void initialCombox_coll(){
		try{//initialize college items
			String sql="select distinct collName from college";
			conn.rs=conn.stmt.executeQuery(sql);
			coll_jcb.removeAllItems();
			coll_jcb.addItem("All");
			while(conn.rs.next()){
				coll_jcb.addItem(conn.rs.getString(1));
			}
			conn.rs.close();
			}
			catch(Exception e){e.printStackTrace();}
			
	}
	
	public void initialCombox_dept(){
		//initialize combox_dept according to coll_jcb selected item
		if(coll_jcb.getSelectedItem().equals("All")){
			try{
				dept_jcb.removeAllItems();
				dept_jcb.addItem("All");
				String sql = "select distinct deptName from dept";
				conn.rs = conn.stmt.executeQuery(sql);
				while(conn.rs.next()){
					dept_jcb.addItem(conn.rs.getString(1));
				}
				conn.rs.close();
			}catch(Exception e){e.printStackTrace();}
		}else{
			try{
				dept_jcb.removeAllItems();
				dept_jcb.addItem("All");
				String sql = "select collID from college where collName='"+coll_jcb.getSelectedItem()+"'";
				conn.rs = conn.stmt.executeQuery(sql);
				conn.rs.next();
				String collID = conn.rs.getString(1);
				sql = "select distinct deptName from dept where collID='"+collID+"'";
				conn.rs = conn.stmt.executeQuery(sql);
				while(conn.rs.next()){
					dept_jcb.addItem(conn.rs.getString(1));
				}
			}catch(Exception e){e.printStackTrace();}
		}
	}

	public void initialCombox_major(){
		//initialize combox_major according to coll_jcb and combox_dept selected items
				if(dept_jcb.getSelectedItem().equals("All")){//dept is all
					if(coll_jcb.getSelectedItem().equals("All")){ // college is all
						try{
							major_jcb.removeAllItems();
							major_jcb.addItem("All");
							String sql = "select distinct majorName from major";
							conn.rs = conn.stmt.executeQuery(sql);
							while(conn.rs.next()){
								major_jcb.addItem(conn.rs.getString(1));
							}
							conn.rs.close();
						}catch(Exception e){e.printStackTrace();}
					}else{//college is specific
						try{
							major_jcb.removeAllItems();
							major_jcb.addItem("All");
							String sql = "select collID from college where collName='"+coll_jcb.getSelectedItem()+"'";
							conn.rs = conn.stmt.executeQuery(sql);
							conn.rs.next();
							String collID = conn.rs.getString(1);
							sql = "select distinct majorName from major where collID='"+collID+"'";
							conn.rs = conn.stmt.executeQuery(sql);
							while(conn.rs.next()){
								major_jcb.addItem(conn.rs.getString(1));
							}
							conn.rs.close();
						}catch(Exception e){e.printStackTrace();}
					}
				}else{ // dept is specific
					try{
						major_jcb.removeAllItems();
						major_jcb.addItem("All");
						String sql = "select deptID from dept where deptName='"+dept_jcb.getSelectedItem()+"'";
						conn.rs = conn.stmt.executeQuery(sql);
						conn.rs.next();
						String deptID = conn.rs.getString(1);
						if(coll_jcb.getSelectedItem().equals("All")){//college is all
							sql = "select distinct majorName from major where deptID='"+deptID+"'";
							conn.rs = conn.stmt.executeQuery(sql);
							while(conn.rs.next()){
								major_jcb.addItem(conn.rs.getString(1));
							}
						}else{// college is specific
								sql = "select collID from college where collName='"+coll_jcb.getSelectedItem()+"'";
								conn.rs = conn.stmt.executeQuery(sql);
								conn.rs.next();
								String collID = conn.rs.getString(1);
								sql = "select distinct majorName from major where collID='"+collID+"' and deptID='"+deptID+"'";
								conn.rs = conn.stmt.executeQuery(sql);
								while(conn.rs.next()){
									major_jcb.addItem(conn.rs.getString(1));
								}
						}
						conn.rs.close();
					}catch(Exception e){e.printStackTrace();}
				}
	}
	
	public void initialCombox_prof(){
		try{//initialize professor options
			String sql="select distinct teacher from courseinfo";
			conn.rs=conn.stmt.executeQuery(sql);
			prof_jcb.removeAllItems();
			prof_jcb.addItem("All");
			while(conn.rs.next()){
				prof_jcb.addItem(conn.rs.getString(1));
			}
			conn.rs.close();
			}
			catch(Exception e){e.printStackTrace();}
	}
	
	public void initialCombox_weekday(){
		//initialize weekday options
		weekday_jcb.addItem("All");
		weekday_jcb.addItem("Monday");
		weekday_jcb.addItem("Tuesday");
		weekday_jcb.addItem("Wednesday");
		weekday_jcb.addItem("Thursday");
		weekday_jcb.addItem("Friday");
		weekday_jcb.addItem("Saturday");
		weekday_jcb.addItem("Sunday");
	}
	
	public void initialCombox_timepart(){
		timepart_jcb.addItem("All");
		timepart_jcb.addItem("9:00-11:30");
		timepart_jcb.addItem("12:00-14:30");
		timepart_jcb.addItem("15:00-17:30");
		timepart_jcb.addItem("18:15-20:45");
	}
	
	//initialize table data
	public void initialTable()
	{   //initialize table head
		v_head.add("College");v_head.add("Major");v_head.add("Course ID");v_head.add("Course Name");
		v_head.add("Weekday");v_head.add("Time");v_head.add("Location");v_head.add("Instructor");
		v_head.add("Credits");v_head.add("Available");
		try{//initialize table
			String sql="select college.collName, major.majorName, courseinfo.courseID, course.courseName, "+
			           "courseinfo.courseDay, courseinfo.courseTime, courseinfo.location, courseinfo.teacher, "+
			           "course.credit, courseinfo.restCapacity from course,courseinfo,college,major where "+
			           "courseinfo.courseID=course.courseID and course.majorID=major.majorID and "+
			           "course.collID=college.collID";
			conn.rs=conn.stmt.executeQuery(sql);
			while(conn.rs.next()){
				Vector v = new Vector();
				String collName = conn.rs.getString(1);
				String majorName=conn.rs.getString(2);
				String courseID=conn.rs.getString(3);
				String courseName=conn.rs.getString(4);
				String courseDay=conn.rs.getString(5);
				String courseTime=conn.rs.getString(6);
				String location=conn.rs.getString(7);
				String teacher=conn.rs.getString(8);
				String credit=conn.rs.getDouble(9)+"";
				String restCapacity=conn.rs.getInt(10)+"";
				String weekday, time;
				//translate number into weekday
				if(courseDay.equals("1"))
					weekday = "Mon";
				else if(courseDay.equals("2"))
					weekday = "Tue";
				else if(courseDay.equals("3"))
					weekday = "Wed";
				else if(courseDay.equals("4"))
					weekday = "Thu";
				else if(courseDay.equals("5"))
					weekday = "Fri";
				else if(courseDay.equals("6"))
					weekday = "Sat";
				else
					weekday = "Sun";
				//translate number into time
				if(courseTime.equals("1"))
					time = "9:00-11:30";
				else if(courseTime.equals("2"))
					time = "12:00-14:30";
				else if(courseTime.equals("3"))
					time = "15:00-17:30";
				else
					time = "18:15-20:45";
				v.add(collName);v.add(majorName);v.add(courseID);v.add(courseName);v.add(weekday);
				v.add(time);v.add(location);v.add(teacher);v.add(credit);v.add(restCapacity);
				v_data.add(v);
			}
			conn.rs.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void updateTable(){
		try{//update table
			String sql="select college.collName, major.majorName, courseinfo.courseID, course.courseName, "+
			           "courseinfo.courseDay, courseinfo.courseTime, courseinfo.location, courseinfo.teacher, "+
			           "course.credit, courseinfo.restCapacity from course,courseinfo,college,major where "+
			           "courseinfo.courseID=course.courseID and course.majorID=major.majorID and "+
			           "course.collID=college.collID " + getSQL_college() + getSQL_dept() + getSQL_major() 
			          								  + getSQL_prof() + getSQL_weekday() + getSQL_timepart();
			conn.rs=conn.stmt.executeQuery(sql);
			v_data.clear();
			while(conn.rs.next()){
				Vector v = new Vector();
				String collName = conn.rs.getString(1);
				String majorName=conn.rs.getString(2);
				String courseID=conn.rs.getString(3);
				String courseName=conn.rs.getString(4);
				String courseDay=conn.rs.getString(5);
				String courseTime=conn.rs.getString(6);
				String location=conn.rs.getString(7);
				String teacher=conn.rs.getString(8);
				String credit=conn.rs.getDouble(9)+"";
				String restCapacity=conn.rs.getInt(10)+"";
				String weekday, time;
				//translate number into weekday
				if(courseDay.equals("1"))
					weekday = "Mon";
				else if(courseDay.equals("2"))
					weekday = "Tue";
				else if(courseDay.equals("3"))
					weekday = "Wed";
				else if(courseDay.equals("4"))
					weekday = "Thu";
				else if(courseDay.equals("5"))
					weekday = "Fri";
				else if(courseDay.equals("6"))
					weekday = "Sat";
				else
					weekday = "Sun";
				//translate number into time
				if(courseTime.equals("1"))
					time = "9:00-11:30";
				else if(courseTime.equals("2"))
					time = "12:00-14:30";
				else if(courseTime.equals("3"))
					time = "15:00-17:30";
				else
					time = "18:15-20:45";
				v.add(collName);v.add(majorName);v.add(courseID);v.add(courseName);v.add(weekday);
				v.add(time);v.add(location);v.add(teacher);v.add(credit);v.add(restCapacity);
				v_data.add(v);
			}
			//DefaultTableModel tmodel=new DefaultTableModel(v_data,v_head);
			MyTableModel tm = new MyTableModel(v_data,v_head);
			jt.setModel(tm);
			conn.rs.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	//get SQL from comboBox
	public String getSQL_college(){
		String coll = (String)(coll_jcb.getSelectedItem());
		if(coll.equals("All"))
			coll = "";
		else{
			try{
				String sql = "select collID from college where collName='"+coll+"' ";
				conn.rs = conn.stmt.executeQuery(sql);
				conn.rs.next();
				String collID = conn.rs.getString(1);
				coll = " and course.collID = '"+collID+"'";
				conn.rs.close();
			}catch(Exception e){e.printStackTrace();}
		}
		return coll;
	}
	public String getSQL_dept(){
		String dept = (String)(dept_jcb.getSelectedItem());
		if(dept.equals("All"))
			dept = "";
		else{
			try{
				String sql = "select deptID from dept where deptName='"+dept+"'";
				conn.rs = conn.stmt.executeQuery(sql);
				conn.rs.next();
				String deptID = conn.rs.getString(1);
				dept = " and course.deptID = '"+deptID+"' ";
				conn.rs.close();
			}catch(Exception e){e.printStackTrace();}
		}
		return dept;
	}
	public String getSQL_major(){
		String major = (String)(major_jcb.getSelectedItem());
		if(major.equals("All"))
			major = "";
		else{
			try{
				String sql = "select majorID from major where majorName='"+major+"'";
				conn.rs = conn.stmt.executeQuery(sql);
				conn.rs.next();
				String majorID = conn.rs.getString(1);
				major = " and course.majorID = '"+majorID+"' ";
				conn.rs.close();
			}catch(Exception e){e.printStackTrace();}
		}
		return major;
	}
	public String getSQL_prof(){
		String prof = (String)(prof_jcb.getSelectedItem());
		if(prof.equals("All"))
			prof = "";
		else{
			prof = " and courseinfo.teacher = '"+prof+"' ";
		}
		return prof;
	}
	public String getSQL_weekday(){
		String weekday = (String)(weekday_jcb.getSelectedItem());
		if(weekday.equals("All"))
			weekday = "";
		else{
			if(weekday.equals("Monday"))
				weekday = "1";
			else if(weekday.equals("Tuesday"))
				weekday = "2";
			else if(weekday.equals("Wednesday"))
				weekday = "3";
			else if(weekday.equals("Thursday"))
				weekday = "4";
			else if(weekday.equals("Friday"))
				weekday = "5";
			else if(weekday.equals("Saturday"))
				weekday = "6";
			else
				weekday = "7";
			weekday = " and courseinfo.courseDay = '"+weekday+"' ";
		}
		return weekday;
	}
	public String getSQL_timepart(){
		String timepart = (String)(timepart_jcb.getSelectedItem());
		if(timepart.equals("All"))
			timepart = "";
		else{
			if(timepart.equals("9:00-11:30"))
				timepart = "1";
			else if(timepart.equals("12:00-14:30"))
				timepart = "2";
			else if(timepart.equals("15:00-17:30"))
				timepart = "3";
			else
				timepart = "4";
			timepart = " and courseinfo.courseTime = '"+timepart+"' ";
		}
		return timepart;
	}
	
	// comboBox actionListener
	class itemListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
		   if (e.getStateChange() == ItemEvent.SELECTED) { //update table when options are changed
			   if(e.getSource() == coll_jcb){
				   initialCombox_dept();
				   initialCombox_major();
			   }
			   if(e.getSource() == dept_jcb){
				   initialCombox_major();
			   }

			   updateTable();
		   } 
		}
	}
	
	public Object getSelectedCourseID(){
		return jt.getValueAt(jt.getSelectedRow(), 2);
	}
	
	public boolean isSelected(){
		if(jt.getSelectedRow() == -1){//no course selected
			JOptionPane.showMessageDialog(this,"Please select a course.","Error",
			                               JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	//Buttons ActionListener
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == intro_jb){
			if(jt.getSelectedRow() == -1){//no course selected
				JOptionPane.showMessageDialog(this,"Please select a course.","Error",
				                               JOptionPane.ERROR_MESSAGE);
				return;
			}
			String courseId = (String)(jt.getValueAt(jt.getSelectedRow(), 2));
			
			//make introduction window
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int w = screenSize.width/2;
			int h = screenSize.height/2;
			JFrame jf = new JFrame("Introduction");
			jf.setVisible(true);
			jf.setLocation(w-200,h-200);
			jf.setSize(new Dimension(400,400));
			JTextArea ta = new JTextArea(20,50);
			ta.setLineWrap(true);
			ta.setWrapStyleWord(true);
			JScrollPane js = new JScrollPane(ta);
			jf.getContentPane().add(js);
			try{
				String sql = "select description from courseinfo where courseID = '"+courseId+"'";
				conn.rs = conn.stmt.executeQuery(sql);
				conn.rs.next();
				String intro = conn.rs.getString(1);
				ta.setText(intro);
			}catch(Exception E){ta.setText("Failed to get the introduction, Please try again...");}
		}
		if(e.getSource()==add_jb)
		{	// click "add course" button
			
			if(jt.getSelectedRow() == -1){//no course selected
				JOptionPane.showMessageDialog(this,"Please select a course.","Error",
				                               JOptionPane.ERROR_MESSAGE);
				return;
			}
			String courseID = (String)jt.getValueAt(jt.getSelectedRow() , 2);//get the selected courseID
			try{
				String sql = "select restCapacity from courseinfo where restCapacity>0 and courseID = '"+courseID+"'";
				conn.rs = conn.stmt.executeQuery(sql);
				if(!conn.rs.next()) {//out of capacity                        
					JOptionPane.showMessageDialog(this,"This course is out of capacity.","Failed",
					                                     JOptionPane.ERROR_MESSAGE);
					conn.rs.close();
					return;
				}
			}catch(Exception E){E.printStackTrace();}
			
			try{
				String sql1="select * from grade where stuID='"+stuID+"' and "+
				             "courseID='"+courseID+"'";
				conn.rs=conn.stmt.executeQuery(sql1);
				if(conn.rs.next()){//already learned this course
					JOptionPane.showMessageDialog(this,"You already took this course.","Failed",
					                                       JOptionPane.ERROR_MESSAGE);	
				    conn.rs.close();
				}
				else{   //check course table to make sure there is no time conflict
				    String sql2 = "select count(courseID) from grade where score = 0 and stuID='"+stuID+"'" ;
				    System.out.println(sql2);
				    conn.rs=conn.stmt.executeQuery(sql2);
				    conn.rs.next();
				    if(conn.rs.getDouble(1) > 3){
				    	JOptionPane.showMessageDialog(this,"You can not take more than 4 courses each semester.","Failed",
                                JOptionPane.ERROR_MESSAGE);
				    	conn.rs.close();
				    	return;
				    }
					String sql3="select course.courseName from course,courseinfo,grade "+
					             "where grade.courseID=course.courseID "+
					            "and grade.courseID=courseinfo.courseID "+
					            "and grade.stuID='"+stuID+"' "+
					            "and (courseinfo.courseDay,courseinfo.courseTime) in "+
					            "(select courseDay,courseTime from courseinfo where "+
					            "courseID='"+courseID+"')";
					 conn.rs=conn.stmt.executeQuery(sql3);
					 if(conn.rs.next())
					 {//time conflict
					 	String cou_name=new String(conn.rs.getString(1));
					 	JOptionPane.showMessageDialog(this,"The time is in conflict with "+cou_name+".","Failed",
					 	                                        JOptionPane.ERROR_MESSAGE);
					 }
					 else
					 {//no time conflict - add course
						String sql = "select course.courseName, courseinfo.teacher, course.credit from course, courseinfo "
								+ "where course.courseID='"+courseID+"' and courseinfo.courseID='"+courseID+"'"; 
						System.out.println(sql);
						conn.rs = conn.stmt.executeQuery(sql);
						conn.rs.next();
						String courseName = conn.rs.getString(1);
						String instructor = conn.rs.getString(2);
						String credit = conn.rs.getString(3);
					 	sql="insert into grade values"+
					 	            "('"+stuID+"','"+courseID+"','"+courseName+"','"+instructor+"','0','"+credit+"')";
					 	System.out.println(sql);
						int i=conn.stmt.executeUpdate(sql);
						if(i==1)
						{//successful
							JOptionPane.showMessageDialog(this,"Course added!","Done",
							                            JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{//failed
							JOptionPane.showMessageDialog(this,"Failed to add! Please try again.","Error",
							                                 JOptionPane.ERROR_MESSAGE);
						}
						sql = "update courseinfo set restCapacity=restCapacity-1 where courseID = '"+courseID+"'";
						conn.stmt.executeUpdate(sql);
						updateTable();
					 }
				}
				conn.rs.close();
			}
			catch(Exception ea){ea.printStackTrace();}
		}
	}
	//DIY methods for admin client
	public void setTitle(String title){
		intro.setText(title);
	}
	public void removeButtons(){
		this.remove(add_jb);
		this.remove(intro_jb);
	}
	
	public static void main(String args[])
	{
		ChooseCourse cc=new ChooseCourse("10399614");
		JFrame jf=new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBounds(10,10,900,600);
		jf.add(cc);
		jf.setVisible(true);
	}
}
