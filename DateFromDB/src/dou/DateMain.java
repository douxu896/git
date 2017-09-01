package dou;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DateMain {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String outfile = File.separator + "Users" + File.separator + "douxu" + File.separator + "Desktop"
				+ File.separator + "CSV";
		String csvname = "data.txt";
		String subfile = outfile + File.separator + csvname;
		 File file = new File(subfile);
		  PrintWriter pw = null;
         if(!file.exists()){
             // 判断文件不存在就new新文件,写数据
        	 System.out.println(subfile+"不存在");
             try {
                 file.createNewFile();
                 // java IO流和文件关联
                 System.out.println(subfile+"创建成功");
             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
         }
         try {
 			Readfrommysql(subfile);
 		} catch (SQLException e) {
 			// TODO 自动生成的 catch 块
 			e.printStackTrace();
 		}
	}

	private static void Readfrommysql(String filename) throws SQLException {
		// TODO 自动生成的方法存根

		// PrintWriter pw = null;
		FileWriter fw = null;
		File file = new File(filename);
		try {
			fw = new FileWriter(filename, true);
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		Connection con = SQLConnection.connection("root", "123456");
		String sql = "select * from info1;";
		PreparedStatement pre = con.prepareStatement(sql);
		ResultSet rs = pre.executeQuery();
		String context;
		while (rs.next()) {
			context = rs.getString("context");
			
			// 关联文件
			try {
				fw.write(context + "\n");
				fw.flush();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

		}
		con.close();
		if (fw != null) {
			try {
				fw.close();
				 System.out.println("读取完成至 "+filename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
