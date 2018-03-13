package com.newx.dimens.common;

import java.io.File;
import java.math.BigDecimal;

public class ComFun{
	
	public final static int INVALID=-1;
	//从dimes字符创数值中中解析数值
	public static double getNumFromDimStr(String str){
		double values=INVALID;
		StringBuffer sbuf=new StringBuffer();
		for(int i=0;i<str.length();i++){
			char t=str.charAt(i);
			int asc=(int)t;
			if ((asc > 47 && asc < 58) || (asc == 46) || (asc == 45))
				sbuf.append(t);
		}
		try{
			values=Double.parseDouble(sbuf.toString());
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		return values;
	}
	
	//从values文件夹名字中获取数值：values-sw450dp
	public static int getVluesSw(String str){
		int swValue=INVALID;
		int indexDP=str.indexOf("dp");
		int indexSW=str.indexOf("sw");
		if(indexDP-indexSW==5){
			try{
			swValue=Integer.parseInt(str.substring(indexSW+2, indexDP));
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		return swValue;
	}
	
	//获取数值单位
	public static String getUnit(String value){
		String unit="";
		if(value.contains("dp"))
			unit="dp";
		else if(value.contains("dip"))
			unit="dip";
		else if(value.contains("sp"))
			unit="sp";
		else if(value.contains("px"))
			unit="px";
		return unit;
	}
	
	//四舍五入保留指定小数位
	public static double round(double value, int scale,int roundingMode)
	{
		BigDecimal bd= new BigDecimal(value);
		bd=bd.setScale(scale,roundingMode);
		double d=bd.doubleValue();
		bd=null;
		return d;
	}
	
	public static boolean createFile(File file) throws Exception {   
		if(! file.exists()) {   
			makeDir(file.getParentFile());   
		}   
		return file.createNewFile();   
	}  

	public static void makeDir(File dir) throws Exception {   
		if(! dir.getParentFile().exists()) {   
			makeDir(dir.getParentFile());   
		}
		dir.mkdir(); 
	}
	
	public static double parseFromStr(String str){
		double temp=INVALID;
		if(str==null||str.equals(""))
			return temp;
		try{
			temp=Double.parseDouble(str);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		return temp;
	}

}