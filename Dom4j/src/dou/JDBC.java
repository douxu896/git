package dou;
import java.sql.*;
public class JDBC {
	public  void getConn() throws Exception {
		  Class.forName("com.mysql.jdbc.Driver");
		  Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mldn","root","123456");
		  Statement sta = con.createStatement();
		  ResultSet rs = sta.executeQuery("select *" + "from emp");
		  while(rs.next()){
		   System.out.println(rs.getInt(1)+"\t" + 
		     rs.getString(2)+"\t"+
		     rs.getString(3)+"\t"+
		     rs.getDate(4)+"\t"+
		     rs.getFloat(5));
		  }
		 }

}
