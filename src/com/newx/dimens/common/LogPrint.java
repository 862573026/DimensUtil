package com.newx.dimens.common;

import java.awt.Font;

import javax.swing.JLabel;

public class LogPrint {
	
	public static Font f=new Font("宋体",Font.PLAIN,13);
	
	public static void logWarn(JLabel label,String str){
		
		label.setText(str);
	}
	
	public static void logError(JLabel label,String str){
		label.setText(str);
	}
}