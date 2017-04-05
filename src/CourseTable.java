import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import layout.SpringUtilities;

//student courses table
public class CourseTable extends JPanel{
	
	private String host="127.0.0.1:3306";
	private String stuID;
	private GetStuInfo getsi;
	private connectDB conn = new connectDB();
	//table rows and cols
	private int rows = 5;
	private int cols = 8;
	public CourseTable(String stuID){
		this.stuID = stuID;
		getsi=new GetStuInfo(host);
		makePanel();
		updateCourseTable();
	}
	public void makePanel(){
	//course table
		SpringLayout table_jsl = new SpringLayout();
		this.setLayout(table_jsl);
		this.setBackground(Color.WHITE);
		
		
		for (int r = 0; r < rows; r++) {
		    for (int c = 0; c < cols; c++) {
		        JTextField textField =
		                new JTextField(" ");
		        textField.setEditable(false);
		        textField.setBackground(new Color(255,250,240));
		        textField.setHorizontalAlignment(JTextField.CENTER );
		        this.add(textField);
		    }
		}
		String weekdays[] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
		String timeSection[] = {"9:00-11:30", "12:00-14:30", "15:00-17:30", "18:15-20:45"};
		for(int c = 1; c < cols; c++){
			JTextField j = (JTextField) getComponent(c);
	        j.setFont(new Font("Helvetica", Font.TRUETYPE_FONT, 18));
			j.setText(weekdays[c-1]);
		}
		for(int r = 1; r < rows; r++){
			JTextField j = (JTextField) getComponent(cols*r);
	        j.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
			j.setText(timeSection[r-1]);
		}
		
		//Lay out the panel.
		SpringUtilities.makeCompactGrid(this, //parent
		                                rows, cols,
		                                10, 60,  //initX, initY
		                                3, 3); //xPad, yPad

		//basic info
		String[] baseinfo=getsi.getBaseInfo(this.stuID);
		JLabel StuName_jl = new JLabel("Student Name: ");           //JLabel Student Name
		JLabel stuName_jl = new JLabel(baseinfo[1]);
		StuName_jl.setFont(new Font("Serif", Font.BOLD, 14));
		stuName_jl.setFont(new Font("Serif", Font.ROMAN_BASELINE, 14));
		JLabel StuID_jl = new JLabel("Student ID: ");				//JLabel Student ID
		JLabel stuID_jl = new JLabel(baseinfo[0]);
		StuID_jl.setFont(new Font("Serif", Font.BOLD, 14));
		stuID_jl.setFont(new Font("Serif", Font.ROMAN_BASELINE, 14));
		JLabel CurrentTerm_jl = new JLabel("Current Term: ");          //JLabel Current Term
		JLabel currentTerm_jl = new JLabel("2016 Fall");
		CurrentTerm_jl.setFont(new Font("Serif", Font.BOLD, 14));
		currentTerm_jl.setFont(new Font("Serif", Font.ROMAN_BASELINE, 14));
		
		
		//set gap size between basic info labels
		this.add(StuName_jl);
		table_jsl.putConstraint(SpringLayout.NORTH, StuName_jl, 10, SpringLayout.NORTH, this);
		table_jsl.putConstraint(SpringLayout.WEST, StuName_jl, 20, SpringLayout.WEST, this);
		this.add(stuName_jl);
		table_jsl.putConstraint(SpringLayout.NORTH, stuName_jl, 10, SpringLayout.NORTH, this);
		table_jsl.putConstraint(SpringLayout.WEST, stuName_jl, 10, SpringLayout.EAST, StuName_jl);
		this.add(StuID_jl);
		Spring name_id = Spring.constant(10,50,100);
		table_jsl.putConstraint(SpringLayout.NORTH, StuID_jl, 10, SpringLayout.NORTH, this);
		table_jsl.putConstraint(SpringLayout.WEST, StuID_jl, name_id, SpringLayout.EAST, stuName_jl);
		this.add(stuID_jl);
		table_jsl.putConstraint(SpringLayout.NORTH, stuID_jl, 10, SpringLayout.NORTH, this);
		table_jsl.putConstraint(SpringLayout.WEST, stuID_jl, 10, SpringLayout.EAST, StuID_jl);
		this.add(CurrentTerm_jl);
		Spring id_term = Spring.constant(10,50,100);
		table_jsl.putConstraint(SpringLayout.NORTH, CurrentTerm_jl, 10, SpringLayout.NORTH, this);
		table_jsl.putConstraint(SpringLayout.WEST, CurrentTerm_jl, id_term, SpringLayout.EAST, stuID_jl);
		this.add(currentTerm_jl);
		Spring termMargin = Spring.constant(10,50,1000);
		table_jsl.putConstraint(SpringLayout.NORTH, currentTerm_jl, 10, SpringLayout.NORTH, this);
		table_jsl.putConstraint(SpringLayout.WEST, currentTerm_jl, 10, SpringLayout.EAST, CurrentTerm_jl);
		table_jsl.putConstraint(SpringLayout.EAST, currentTerm_jl, termMargin, SpringLayout.EAST, this);
					
	}
	public void updateCourseTable(){
		try{
			String sql = "select course.courseName,courseDay, courseTime from course,courseinfo,grade "
					+ " where course.courseID=courseinfo.courseID and courseinfo.courseID=grade.courseID and grade.score=0 and grade.StuID='"
					+ stuID +"'";
			conn.rs = conn.stmt.executeQuery(sql);
			for (int r = 1; r < rows; r++) {
			    for (int c = 1; c < cols; c++) {
			    	JTextField j = (JTextField)(this.getComponent(r*cols+c));
					j.setText("");
			    }
			}
			while(conn.rs.next()){
				String courseName = conn.rs.getString(1);
				int courseDay = conn.rs.getInt(2);
				int courseTime = conn.rs.getInt(3);
				JTextField j = (JTextField)(this.getComponent(courseTime*cols+courseDay));
				j.setText(courseName);
			}
		}catch(Exception e){e.printStackTrace();}
	}
	public static void main(String[] arg){
		CourseTable cc=new CourseTable("10399614");
		JFrame jf=new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBounds(10,10,900,600);
		jf.add(cc);
		jf.setVisible(true);
//		JTextField j = (JTextField)(cc.getComponent( 9));
//		j.setText("I'm a course with long long name!");
//		j = (JTextField)(cc.getComponent( 18));
//		j.setText("I'm a course with long name!");
//		j = (JTextField)(cc.getComponent( 36));
//		j.setText("I'm a course!");
	}
}
