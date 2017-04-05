import java.util.*;

//import com.mysql.jdbc.*;

public class GetStuInfo {
	private String host;
	connectDB conn = new connectDB();;
	//private Statement stmt;
	//private ResultSet rs;
	
	public GetStuInfo(String host)
	{
		this.host=host;
	}
	
	public String[] getBaseInfo(String stu_id){
		String[] message =new String[15];
		try
		{
			//this.initialConnection();
			/*String sql=
			"select stuID,firstame,middlename,lastname,gender,stuBirth,nationality,"+
			"collName,deptName,majorName,enrollTime,graduateTime,Email,"+
			"address,phone from student,college,dept,major where stuID='"+stu_id+"' and student.collID=college.collID"+
			"and student.deptID=dept.deptID and student.majorID=major.majorID";*/
			String sql="select stuID,firstname,middlename,lastname,gender,stuBirth,nationality,"+
			"collName,deptName,majorName,enrollTime,graduateTime,Email,"+
			"address,phone from student,college,dept,major where stuID='"+stu_id+"' and student.collID=college.collID"+
			" and student.deptID=dept.deptID and student.majorID=major.majorID";
			System.out.println(sql);
			//conn.rs = conn.stmt.executeQuery(sql);
			conn.rs=conn.stmt.executeQuery(sql);
			if(conn.rs.next())
			{
				for(int i=0;i<15;i++){
					message[i]=conn.rs.getString(i+1);
					//System.out.println(message[i]);
				}
			}
			conn.rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return message;
	}
	
	
		
}
