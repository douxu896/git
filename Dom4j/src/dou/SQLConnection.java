package dou;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLConnection {
	static ResultSet RESULT = null;
	public static Connection connection(String username, String password){
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/new?characterEncoding=utf8&useSSL=true",username,password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	
	public static void closeConnection(Connection con){
		try {
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	public static boolean findByAuthorAndYear(String str, Connection con, String authorAndYear){
		String sql = str;
		try {
			PreparedStatement pre = con.prepareStatement(sql);
			pre.setString(1, authorAndYear);
			ResultSet res = pre.executeQuery();
			if(!res.next()){
				return false;
			}else{
				RESULT = res;
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
