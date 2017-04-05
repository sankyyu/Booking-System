import java.sql.*;
import java.util.*;

//connect MySQL
//define actionMethod to MySQL
public class connectDB {
	String host = "localhost";
	public Connection conn;
	public Statement stmt;
	public ResultSet rs;
	public Statement stmt2;
	public ResultSet rs2;
		
	//connect to local MySQL database
	public connectDB(){
			try
			{//load JDBC driver, make Connection and Statement
				Class.forName("com.mysql.jdbc.Driver");
				conn=DriverManager.getConnection("jdbc:mysql://"+host+"/courseBooking","root","");
				stmt=conn.createStatement();
				stmt2=conn.createStatement();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	public void close()
		{
			try
			{
				if(rs!=null)
					rs.close();
				if(rs2!=null)
					rs2.close();
				if(stmt!=null)
					stmt.close();
				if(stmt2!=null)
					stmt2.close();
				if(conn!=null)
					conn.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
}
