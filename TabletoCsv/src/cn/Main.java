package cn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import weka.core.Instances;
import weka.core.converters.DatabaseLoader;





public class Main {

	public static void main(String[] args) throws Exception {
		ResultSet result = null;
		List<String> dataList = new ArrayList<String>();
		DatabaseLoader db = new DatabaseLoader();
		db.setSource("jdbc:mysql://localhost:3306/new?characterEncoding=utf8&useSSL=true","root", "123456");
		db.connectToDatabase();
		db.setQuery("select * from info");
		Instances ins = db.getDataSet();
		ins.setClassIndex(ins.numAttributes()-1);
		 System.out.println(ins);
		/* Connection con = SQLConnection.connection("root", "123456");
		 InstanceQuery query = new InstanceQuery();
		 query.setUsername("nobody");
		 query.setPassword("");
		 query.setQuery("select * from info");
		 // You can declare that your data set is sparse
		 // query.setSparseData(true);
		 Instances data = query.retrieveInstances();
		 System.out.println(data);*/
	/*	while (result.next()) {
			dataList.add(result.getString(1) + "," + result.getString(2) + "," + result.getString(3));
		}*/
		
	//	CSVUtils.writeCsv("/Users/douxu/Desktop/CSV/data1.csv",dataList);
	//	CSVUtils.exportCsv(new File("/Users/douxu/Desktop/CSV/data2.csv"), dataList);
	}
}
