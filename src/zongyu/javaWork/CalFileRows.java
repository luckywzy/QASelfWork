package zongyu.javaWork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CalFileRows {

    private File file;
    private int fileRows;

    public CalFileRows(File file) {
	this.file = file;
	fileRows = 0;
    }

    private void cal() throws IOException {
	if (file == null) {
	    throw new IllegalArgumentException("请选择一个文件");
	}
	if (!file.exists()) {
	    throw new IllegalArgumentException("文件不存在");
	}
	if (!file.isFile()) {
	    throw new IllegalArgumentException("不是一个文件");
	}
	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	String str;
	while ((str = br.readLine()) != null) {
	    if(isEffectiveRow(str) == true) {
		System.out.println(str);
	   	fileRows++; // 这里计算行数
	    }
	}
    }
    private boolean isEffectiveRow(String line) {
	String s = line.replaceAll("\\s*", "");
	//注释行不算有效行
	if(s.startsWith("//")) { 
	    return false;
	}
	//空行不算有效行
	if(s.length() <= 0) {
	    return false;
	}
	return true;
    }
    
    public int getRows() {
	try {
	    cal();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return fileRows;
    }

    public static void main(String[] args) {
	// TODO Auto-generated method stub
	CalFileRows cfr = new CalFileRows(
		new File("F:\\workspace\\自学作业\\src\\com\\zongyu\\CalFileRows.java"));
	System.out.println("this java file rows is :" + cfr.getRows());
    }

}
