package zongyu.javaWork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;


public class CalImport
{
	private String dirPath; //源文件路径
	private ArrayList<HashMap.Entry<String, Integer>> infoVals; //import的所有类名和次数
	
	public void setDirPath(String dir) throws NotDirectoryException
	{
		File file = new java.io.File(dir);
		if(file.isDirectory())
		{
			dirPath = dir;
			return ;
		}
		throw new NotDirectoryException(dir+"is not a directory, please check!");
	}
	public void run()
	{
		infoVals =  sortMap(getImportClasses());
	}

	public void printTopN(int n)
	{
		//输出前10行
		int end = infoVals.size() > n ? n : infoVals.size();
		for (int i=0; i<end; i++)
		{
			Entry<String, Integer> entry = infoVals.get(i);
			System.out.println(entry.getKey()+" "+entry.getValue());
		}
	}
	
	
	private String  getDirPath()
	{
		return dirPath;
	}

	private HashMap<String, Integer> getImportClasses()
	{
		File file = new java.io.File(getDirPath());
		File[] files = file.listFiles();
		HashMap<String, Integer> iMap= new HashMap<String, Integer>();
		for (File f : files)
		{
			if(f.isFile()) 
			{
				//System.out.println(f.getName());
				BufferedReader bf=null;
				try
				{
					bf = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
					String line;
					//System.out.println(f.getName());
					while ((line = bf.readLine()) != null)
					{
						//是导入的包的那一行
						if(isImportLine(line))
						{
							//提取类名
							String className = getImportName(line);
							if(className == null) {
								continue;
							}
							if(iMap.containsKey(className))
							{	//此类存在，则将其value+1
								iMap.put(className, iMap.get(className)+1);
								//System.out.println("\t"+className);
							}else 
							{
								//不存在将其加入map中，并设置value为1
								iMap.put(className, 1);
								//System.out.println("\t"+className);
							}
						}
					}
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
		}
		return iMap;
	}
	private  ArrayList<HashMap.Entry<String, Integer>> sortMap(HashMap<String, Integer> iMap)
	{
		//将imap中的类名根据value的大小排序 
		ArrayList<HashMap.Entry<String, Integer>> infoVals = 
				new ArrayList<>(iMap.entrySet());
		Collections.sort(infoVals, new Comparator<HashMap.Entry<String, Integer>>()
		{
			@Override
			public int compare(Entry<String, Integer> m1, Entry<String, Integer> m2)
			{
				
				return m2.getValue()-m1.getValue();
			}
		});;
		return infoVals;
	}

	

	//得到import的类名
	private static String getImportName(String line)
	{
		//import xxx.xx.xx;
		String[] string = line.split(" ");
		if(string.length >= 2)
		{
			String string2 = string[1].substring(0, string[1].length()-1);
			//System.out.println(string2);
			return string2;
		}
		return null;
	}

	//判断是否是import的那一行
	private boolean isImportLine(String line)
	{
		if(line == null)
		{
			return false;
		}
		if(line.startsWith("import"))
		{
			return true;
		}
		return false;
	}
	
	public static void main(String[] args)
	{
		/*
		 * 根据指定项目目录下（可以认为是 Java 源文件目录）中，
		 * 统计被 import 最多的类，前十个是什么
		 */
		String dir = "F:\\workspace\\自学作业\\src\\com\\zongyu";
		//String dir="F:\\workspace\\自学作业\\pa_huilv.py";
		CalImport cal = new CalImport();
		try
		{
			cal.setDirPath(dir);
		} catch (NotDirectoryException e)
		{
			e.printStackTrace();
		}
		cal.run();
		cal.printTopN(10);
	}
}
