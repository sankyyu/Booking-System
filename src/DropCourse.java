import java.awt.*;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;



//drop course
public class DropCourse extends JPanel implements ActionListener{
	
	private String host="127.0.0.1:3306";
	private String stuID;
	private GetStuInfo getsi;
	
	
	private JLabel currentCourses = new JLabel("Your Current Course:");
	private JTable course = new JTable();
	private JButton drop=new JButton("drop");
	private connectDB conn = new connectDB();
	//private JTable jt;
	private JScrollPane jsp;
	private Vector<String> v_head=new Vector<String>();
	private Vector<Vector> v_data=new Vector<Vector>();
	private Vector<String> v_couid=new Vector<String>();
	private SpringLayout jspring;
	//private connectDB conn=new connectDB();
	public DropCourse(String stuID){
		this.stuID=stuID;
		getsi=new GetStuInfo(host);
		initialTable();
		makePanel();
	}
	public class MyTableModel extends DefaultTableModel{ 
		public MyTableModel(Vector<Vector> v_data, Vector<String> v_head)
		{
			super(v_data, v_head);
		}
	    public boolean isCellEditable(int row, int column) { 
		   return false;
	    }
   }
	
	public void makePanel(){
		drop.addActionListener(this);
		//currentCourses.setBounds(40,40,20,10);
		jspring = new SpringLayout();
		this.setLayout(jspring);
		this.setVisible(true);
		this.setBackground(new Color(255,250,240));
		this.add(currentCourses);
		jspring.putConstraint(SpringLayout.NORTH, currentCourses, 20, SpringLayout.NORTH, this);
		jspring.putConstraint(SpringLayout.WEST, currentCourses, 20, SpringLayout.WEST, this);
		
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		course=new JTable(dtm);
		jsp=new JScrollPane(course);
		jsp.setBackground(Color.WHITE);
		jsp.setPreferredSize(new Dimension(760,300));
		this.add(jsp);
		jspring.putConstraint(SpringLayout.NORTH, jsp, 20, SpringLayout.SOUTH, currentCourses);
		jspring.putConstraint(SpringLayout.WEST, jsp, 20, SpringLayout.WEST, this);
		jspring.putConstraint(SpringLayout.EAST, jsp, -20, SpringLayout.EAST, this);
		jspring.putConstraint(SpringLayout.SOUTH, jsp, -200, SpringLayout.SOUTH, this);
		
		drop.setPreferredSize(new Dimension(100,30));
		this.add(drop);
		drop.setVisible(true);
		jspring.putConstraint(SpringLayout.NORTH, drop, 20, SpringLayout.SOUTH, jsp);
		jspring.putConstraint(SpringLayout.WEST, drop, 370, SpringLayout.WEST, this);
		//this.setVisible(true);
	}
	//initialize table data
	public void initialTable(){
		v_head.add("College");v_head.add("Major");v_head.add("Course ID");v_head.add("Course Name");
		v_head.add("Weekday");v_head.add("Time");v_head.add("Location");v_head.add("Instructor");
		v_head.add("Credits");v_head.add("Available");
		try{
			String sql="select college.collName, major.majorName, grade.courseID, course.courseName, "+
			           "courseinfo.courseDay, courseinfo.courseTime, courseinfo.location, courseinfo.teacher, "+
			           "course.credit, courseinfo.restCapacity from grade,course,courseinfo,college,major where "+
			           "grade.courseID=courseinfo.courseID and grade.courseID=course.courseID and course.majorID=major.majorID and "+
			           "course.collID=college.collID and stuID='"+stuID+"'";
			System.out.println(sql);
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void updateTable(){
		try{//initialize table
			String sql="select college.collName, major.majorName, grade.courseID, course.courseName, "+
			           "courseinfo.courseDay, courseinfo.courseTime, courseinfo.location, courseinfo.teacher, "+
			           "course.credit, courseinfo.restCapacity from grade,course,courseinfo,college,major where "+
			           "grade.courseID=courseinfo.courseID and grade.courseID=course.courseID and course.majorID=major.majorID and "+
			           "course.collID=college.collID and stuID='"+stuID+"'";
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
			course.setModel(tm);
			conn.rs.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.drop){
			if(course.getSelectedRow()==-1){
				JOptionPane.showMessageDialog(this,"Please select a course.","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			String courseID=(String)course.getValueAt(course.getSelectedRow() , 2);
			try
			{
				String sql="delete from grade where courseID='"+courseID+"'";
				conn.stmt.execute(sql);
				sql = "update courseinfo set restCapacity=restCapacity+1 where courseID = '"+courseID+"'";
				conn.stmt.executeUpdate(sql);
				updateTable();
				conn.rs.close();
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] arg){
		DropCourse cc=new DropCourse("10399614");
		JFrame jf=new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBounds(10,10,900,600);
		jf.add(cc);
		jf.setVisible(true);
	}
}
