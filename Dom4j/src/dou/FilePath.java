package dou;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FilePath  {
	
	private volatile boolean isRunning = true;
    private BlockingQueue<String> queue;// 内存缓冲区
    private static AtomicInteger count = new AtomicInteger();// 总数 原子操作
    private static final int SLEEPTIME = 1000;

    public FilePath(BlockingQueue<String> queue) {
        this.queue = queue;
    }
	
	 static boolean createDir(String file) {
		// TODO Auto-generated method stub
			File dir = new File(file);
			if (dir.exists()) {// 判断目录是否存在
				System.out.println("创建CSV目录失败，目标目录已存在！ ");
				return false;
			}
			if (!file.endsWith(File.separator)) {// 结尾是否以"/"结束
				file = file + File.separator;
			}
			if (dir.mkdirs()) {// 创建目标目录
				System.out.println("创建数据目录成功！" + file);
				return true;
			} else {
				System.out.println("创建数据目录失败！");
				return false;
			}
	}
	 //suffix为文件后缀名
	  public static List<String> getListFiles(String path, String suffix, boolean isdepth) {  
    	  List<String> lstFileNames = new ArrayList<String>();  
    	  File file = new File(path);  
    	  return listFile(lstFileNames, file, suffix, isdepth);  
    	 }  
    //显示目录的方法
    private static List<String> listFile(List<String> lstFileNames, File f, String suffix, boolean isdepth) {  
    	  // 若是目录, 采用递归的方法遍历子目录     
    	  if (f.isDirectory()) {  
    	   File[] t = f.listFiles();  
    	     
    	   for (int i = 0; i < t.length; i++) {  
    	    if (isdepth || t[i].isFile()) {  
    	     listFile(lstFileNames, t[i], suffix, isdepth);  
    	    }  
    	   }     
    	  } else {  
    	   String filePath = f.getAbsolutePath();     
    	   if (!suffix.equals("")) {  
    	    int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引   
    	    String tempsuffix = "";  
    	  
    	    if (begIndex != -1) {  
    	     tempsuffix = filePath.substring(begIndex + 1, filePath.length());  
    	     if (tempsuffix.equals(suffix)) {  
    	      lstFileNames.add(filePath);  
    	     }  
    	    }  
    	   } else {  
    	    lstFileNames.add(filePath);  
    	   }  
    	  }  
    	  return lstFileNames;  
    	 }
}
