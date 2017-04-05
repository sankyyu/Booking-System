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


public class gradeManagement extends JPanel{
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = screenSize.width/2;
	private int height = screenSize.height/2;
	
	private connectDB conn = new connectDB(); //connect to database
	//JPanels
	private JPanel search_jp; // for major search
	private JPanel grid_jp;  //for comboxBox
	private JScrollPane jsp;  //for course table
	//Layout
	private SpringLayout jspring;
	//JLabels
	private JLabel coll = new JLabel("College : ");
	private JLabel dept = new JLabel("Department : ");
	private JLabel major = new JLabel("Major : ");
	private JLabel prof = new JLabel("Professor : ");
	private JLabel courseid = new JLabel("Course ID: ");
	//JTextField
	private JTextField search_tf = new JTextField("Please type a course ID");
	//JComboBoxes
	private JComboBox coll_jcb = new JComboBox();
	private JComboBox dept_jcb = new JComboBox();
	private JComboBox major_jcb = new JComboBox();
	private JComboBox prof_jcb = new JComboBox();
	//Vectors for setting JComboBox items
	private Vector coll_v = new Vector();
	private Vector dept_v = new Vector();
	private Vector major_v = new Vector();
	private Vector prof_v = new Vector();
	//JButtons
	private JButton search_jb = new JButton("Search");// delete-course button
	private JButton edit_jb = new JButton("View and Edit");// edit-course button
	//JTable for course list
	private JTable jt = new JTable();
	//Vector for table header and content
	private Vector<String> v_head=new Vector<String>();
	private Vector<Vector> v_data=new Vector<Vector>();
	
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
	
	public gradeManagement(){
		jspring = new SpringLayout();
		this.setLayout(jspring);
		this.setVisible(true);
		this.setBackground(new Color(255,250,240));
		initialData();
		makePanel();
		addListener();
	}
	
	public void initialData(){
		this.initialCombox_coll();
		this.initialCombox_dept();
		this.initialCombox_major();
		this.initialCombox_prof();
		this.initialTable();//initialize table data

	}
	
	public void makePanel(){
		jspring = new SpringLayout();
		this.setLayout(jspring);
		this.setVisible(true);
		//this.setSize(800,600);
		this.setBackground(new Color(255,250,240));
		//search area
		search_jp = new JPanel(null);
		search_jp.setVisible(true);
		search_jp.setPreferredSize(new Dimension(400,30));
		search_jp.setBackground(new Color(255,250,240));
		courseid.setBounds(0,0,65,30);
		search_tf.setBounds(65,0,180,30);
		search_jb.setBounds(280,0,80, 30);
		search_jp.add(courseid);
		search_jp.add(search_tf);
		search_jp.add(search_jb);
		//options area
		grid_jp = new JPanel(new GridLayout(2,1,5,5));
		grid_jp.setBackground(Color.WHITE);
		JPanel coll_dept = new JPanel(new GridLayout(1,4,10,0));
		coll_dept.add(coll);
		coll_dept.add(coll_jcb);
		coll_dept.add(dept);
		coll_dept.add(dept_jcb);
		grid_jp.add(coll_dept);
		JPanel major_prof = new JPanel(new GridLayout(1,4,10,0));
		major_prof.add(major);
		major_prof.add(major_jcb);
		major_prof.add(prof);
		major_prof.add(prof_jcb);
		grid_jp.add(major_prof);
		//set the search area location
		this.add(search_jp);
		jspring.putConstraint(SpringLayout.NORTH, search_jp, 10, SpringLayout.NORTH, this);
		jspring.putConstraint(SpringLayout.WEST, search_jp, 20, SpringLayout.WEST, this);
		//set the options area location
		this.add(grid_jp);
		jspring.putConstraint(SpringLayout.NORTH, grid_jp, 10, SpringLayout.SOUTH, search_jp);
		jspring.putConstraint(SpringLayout.WEST, grid_jp, 20, SpringLayout.WEST, this);
		jspring.putConstraint(SpringLayout.EAST, grid_jp, 756, SpringLayout.WEST, this);
		
		//table 
		//DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		MyTableModel tm = new MyTableModel(v_data,v_head);
		jt.setModel(tm);// create JTable for course info
		jsp = new JScrollPane(jt);//put JTable in JScrollPane
		jsp.setBackground(Color.WHITE);
		
		//set the table location
		this.add(jsp);
		jspring.putConstraint(SpringLayout.NORTH, jsp, 10, SpringLayout.SOUTH, grid_jp);
		jspring.putConstraint(SpringLayout.WEST, jsp, 20, SpringLayout.WEST, this);
		jspring.putConstraint(SpringLayout.EAST, this, 20, SpringLayout.EAST, jsp);
		jspring.putConstraint(SpringLayout.SOUTH, jsp, 200, SpringLayout.NORTH, jsp);
		//set the View-and-Edit button location
		edit_jb.setSize(100, 30);
		this.add(edit_jb);
		jspring.putConstraint(SpringLayout.NORTH, edit_jb, 0, SpringLayout.SOUTH, jsp);
		jspring.putConstraint(SpringLayout.WEST, edit_jb, 370, SpringLayout.WEST, this);
		
	}
	
	public void addListener(){
		edit_jb.addActionListener(new myActionListener());
		search_jb.addActionListener(new myActionListener());
		search_tf.addFocusListener(new myFocusListener());
		coll_jcb.addItemListener(new itemListener());
		dept_jcb.addItemListener(new itemListener());
		major_jcb.addItemListener(new itemListener());
		prof_jcb.addItemListener(new itemListener());
	}
	
	public class myActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == edit_jb){
				if(isSelected()){
					try{
						String courseID = (String)(jt.getValueAt(jt.getSelectedRow(), 0));
						String courseName = (String)(jt.getValueAt(jt.getSelectedRow(), 1));
						new makeEditWin(courseID, courseName);
					}catch(Exception E){E.printStackTrace();}
				}
			}
			if(e.getSource() == search_jb){
					try{
						String courseID = search_tf.getText();
						String sql = "select * from grade where courseID='"+courseID+"'";
						conn.rs = conn.stmt.executeQuery(sql);
						if(conn.rs.next()){//successful
							updateTable(courseID);
						}
						else
						{//failed
							JOptionPane.showMessageDialog(getParent(),"Not found! Please make sure the course exists.","Error",
							                                 JOptionPane.ERROR_MESSAGE);
							
						}
					}catch(Exception E){E.printStackTrace();}
			}
		}
	}
	
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
	//set search-textField action
	private class myFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent e) {
		        search_tf.setText("");
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			
		}
	}
	//View-and-Edit window
	public class makeEditWin{
		
		private String courseID, courseName;
		private JFrame jframe;
		private Container c;
		private SpringLayout slayout;
		private JPanel jp;
		private JScrollPane grade_jsp;
		private int centerX=screenSize.width/2;
		private int centerY=screenSize.height/2;

		private JLabel label = new JLabel();
		
		private JButton confm = new JButton("Confirm");
		
		private JTable jt = new JTable();
		//Vector for table header and content
		private Vector<String> head_v=new Vector<String>();
		private Vector<Vector> data_v=new Vector<Vector>();
		
		//define table model to make cells not editable but selectable
		public class myTableModel extends DefaultTableModel{ 
			public myTableModel(Vector<Vector> v_data, Vector<String> v_head)
			{
				super(v_data, v_head);
			}
		    public boolean isCellEditable(int row, int column) { 
		    	if(column == 1)
		    		return true;
			    return false;
		    }
	   }
		
		public makeEditWin(String courseID, String courseName){
			jframe = new JFrame("Student Score");
			this.courseID = courseID;
			this.courseName = courseName;
			c = jframe.getContentPane();
			jframe.setVisible(true);
			jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			jframe.setLocation(centerX-200,centerY-height/2-100);
			jframe.setSize(new Dimension(400,600));
			//main panel
			slayout = new SpringLayout();
			jp = new JPanel(slayout);
			jp.setBackground(new Color(255,250,240));
			c.add(jp);
			//set course-info label
			label.setText(courseID+" - "+courseName);
			label.setSize(200,30);
			jp.add(label);
			slayout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, jp);
			slayout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, jp);
			//set grade table
			initialGradeTable();
			myTableModel tm = new myTableModel(data_v,head_v);
			jt.setModel(tm);// create JTable for course info
			jt.setEditingColumn(1);
			grade_jsp = new JScrollPane(jt);//put JTable in JScrollPane
			grade_jsp.setPreferredSize(new Dimension(380,480));
			grade_jsp.setBackground(Color.WHITE);
			
			jp.add(grade_jsp);
			slayout.putConstraint(SpringLayout.NORTH, grade_jsp, 5, SpringLayout.SOUTH, label);
			slayout.putConstraint(SpringLayout.WEST, grade_jsp, 10, SpringLayout.WEST, jp);
			slayout.putConstraint(SpringLayout.EAST, jp, 10, SpringLayout.EAST, grade_jsp);
			slayout.putConstraint(SpringLayout.SOUTH, grade_jsp, -50, SpringLayout.SOUTH, jp);

			//add confirm button
			confm.setSize(100, 30);
			jp.add(confm);	
			slayout.putConstraint(SpringLayout.NORTH, confm, 20, SpringLayout.SOUTH, grade_jsp);
			slayout.putConstraint(SpringLayout.WEST, confm, 150, SpringLayout.WEST, jp);
			slayout.putConstraint(SpringLayout.SOUTH, confm, -10, SpringLayout.SOUTH, jp);
			confm.addActionListener(new mylistener());
		}
		
		public void initialGradeTable(){
			head_v.add("Student ID");head_v.add("Score");

			try{//initialize table
				String sql="select stuID, score from grade where courseID='"+courseID+"'";
				conn.rs=conn.stmt.executeQuery(sql);
				while(conn.rs.next()){
					Vector v = new Vector();
					String stuID = conn.rs.getString(1);
					String score = conn.rs.getString(2);
					v.add(stuID);v.add(score);
					data_v.add(v);
				}
				MyTableModel tm = new MyTableModel(data_v,head_v);
				jt.setModel(tm);
			}
			catch(Exception e){e.printStackTrace();}
			finally{try{conn.rs.close();}catch(Exception E){}}
		}
		
		class mylistener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(jt.getRowCount());
				int i = 0;
				for(int x=0;x<jt.getRowCount();x++){
					try{
					String stuID = (String)(jt.getValueAt(x, 0));
					String score = (String)(jt.getValueAt(x, 1));
					System.out.println(stuID);
					System.out.println(score);
					String sql = "update grade set score='"+score+"' where stuID='"+stuID
							+"' and courseID='"+courseID+"'";
					System.out.println(sql);
					i = conn.stmt.executeUpdate(sql);
					System.out.println((String)(jt.getValueAt(0, 1)));
					
					}catch(Exception E){E.printStackTrace();}
					finally{try{conn.rs.close();}catch(Exception E){}}
				}
				if(i!=0)
				{//successful
					JOptionPane.showMessageDialog(jframe,"Updated!","Successful",
					                            JOptionPane.INFORMATION_MESSAGE);
					updateTable();
					jframe.dispose();
				}
				else
				{//failed
					JOptionPane.showMessageDialog(jframe,"Failed to update! Please try again.","Error",
					                                 JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}
	}
	
	//initialize college items
	public void initialCombox_coll(){
		try{
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
	
		//initialize table header and data
		public void initialTable()
		{   //initialize table head
			v_head.add("Course ID");v_head.add("Course Name");v_head.add("Instructor");
			v_head.add("Credits");v_head.add("Average Score");
			updateTable();
		}
		
		//update table data
		public void updateTable(){
			try{//initialize table

				String sql="select distinct grade.courseID, grade.courseName, grade.instructor,"+
				           "grade.credit from grade,course,courseinfo "
				           + "where course.courseID=grade.courseID and courseinfo.courseID=grade.courseID "
						+ getSQL_college() + getSQL_dept() + getSQL_major() + getSQL_prof() ;
				conn.rs=conn.stmt.executeQuery(sql);
				v_data.clear();
				while(conn.rs.next()){
					Vector v = new Vector();
					String courseID = conn.rs.getString(1);
					String courseName=conn.rs.getString(2);
					String instructor=conn.rs.getString(3);
					String credit=conn.rs.getString(4);
					sql = "select avg(grade.score) from grade where courseID='"+courseID+"'";
					conn.rs2 = conn.stmt2.executeQuery(sql);
					conn.rs2.next();
					String AvgScore=conn.rs2.getString(1);
					v.add(courseID);v.add(courseName);v.add(instructor);v.add(credit);v.add(AvgScore);
					v_data.add(v);
				}
				//DefaultTableModel tmodel=new DefaultTableModel(v_data,v_head);
				MyTableModel tm = new MyTableModel(v_data,v_head);
				jt.setModel(tm);
			}
			catch(Exception e){e.printStackTrace();}
			finally{try{conn.rs.close();}catch(Exception E){E.printStackTrace();}
			}
		}
		public void updateTable(String courseId){
			try{//initialize table
				String sql="select grade.courseID, grade.courseName, grade.instructor,"+
				           "grade.credit, avg(grade.score) from grade "
				           + " where grade.courseID='"+courseId+"'" ;        								  
				conn.rs=conn.stmt.executeQuery(sql);
				v_data.clear();
				while(conn.rs.next()){
					Vector v = new Vector();
					String courseID = conn.rs.getString(1);
					String courseName=conn.rs.getString(2);
					String instructor=conn.rs.getString(3);
					String credit=conn.rs.getString(4);
					String AvgScore=conn.rs.getString(5);
					
					v.add(courseID);v.add(courseName);v.add(instructor);v.add(credit);v.add(AvgScore);
					v_data.add(v);
				}
				//DefaultTableModel tmodel=new DefaultTableModel(v_data,v_head);
				MyTableModel tm = new MyTableModel(v_data,v_head);
				jt.setModel(tm);
			}
			catch(Exception e){e.printStackTrace();}
			finally{try{conn.rs.close();}catch(Exception E){E.printStackTrace();}
			}
		}
	
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
	//is any table row selected?
	public boolean isSelected(){
		if(jt.getSelectedRow() == -1){//no course selected
			JOptionPane.showMessageDialog(this,"Please select a course.","Error",
			                               JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	public static void main(String[] arg){
		gradeManagement cc=new gradeManagement();
		JFrame jf=new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBounds(10,10,900,600);
		jf.add(cc);
		jf.setVisible(true);
	}
}
