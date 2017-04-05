
public class test {
	public static void main(String[] arg){
		String sql="update student SET Email='newport',address='ysk1@163.com',phone='2103346645' where stuID='10411379'";
		connectDB conn = new connectDB();
		try{
			conn.stmt.execute(sql);
			System.out.println("i am here");
			//while(conn.rs.next()){
				//for(int i=0;i<15;i++){
					//System.out.println(conn.rs.getString(i+1));
				//}
			//}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
