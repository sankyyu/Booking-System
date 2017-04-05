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

public class studentManagement  extends JPanel{
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = screenSize.width/2;
	private int height = screenSize.height/2;
	
	private connectDB conn = new connectDB(); //connect to database
	//JPanels
	private JPanel jp; // major
	//Layout
	private SpringLayout jspring;
	private JLabel hint = new JLabel("Future Work");
		
	public studentManagement(){
		jspring = new SpringLayout();
		this.setLayout(jspring);
		this.setVisible(true);
		this.setBackground(new Color(255,250,240));
		initialData();
		makePanel();
		addListener();
	}
	
	public void makePanel(){
		hint.setPreferredSize(new Dimension(200,60));
		hint.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		this.add(hint);
		jspring.putConstraint(SpringLayout.NORTH, hint, 10, SpringLayout.NORTH, this);
		jspring.putConstraint(SpringLayout.WEST, hint, 20, SpringLayout.WEST, this);
		
	}
	
	public void initialData(){
		
	}
	
	public void addListener(){
		
	}
}
