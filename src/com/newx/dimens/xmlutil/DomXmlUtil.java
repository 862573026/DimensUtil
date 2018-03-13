package com.newx.dimens.xmlutil;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List; 
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author ms
 * Dom4j 生成与解析XML文档 
 * Dom4j使用详解  http://blog.csdn.net/redarmy_chen/article/details/12969219
 */ 
public class DomXmlUtil{ 
	 
	private final static String RES_TAG_NAME="resources";
	private final static String DIMEN_TAG_NAME="dimen";
	private final static String STRING_TAG_NAME="string";
	public final static String INVALID_VALUE="-123654987";
	
	/***解析xml文件，返回LinkedHashMap 类型数据
	 * @throws DocumentException **/
	public static  LinkedHashMap<String, String> parSerSrcXml(File src) throws DocumentException{
		LinkedHashMap<String, String> srcMap=new LinkedHashMap<String, String>();
		 SAXReader reader = new SAXReader();    
		 Document   document;			
		 document = reader.read(src);
			Element resources = document.getRootElement();
			 List nodes = resources.elements(DIMEN_TAG_NAME);
			 for (Iterator it = nodes.iterator(); it.hasNext();) {   
			      Element dimen = (Element) it.next();
			      Attribute attr=dimen.attribute("name");
			      String name=attr.getText();
			      String value=dimen.getText();	
			      srcMap.put(name, value);
			 }		
		return srcMap;
		
	}
	
	/***根据itemlist内容 写xml文件
	 * @throws DocumentException **/
	@SuppressWarnings("deprecation")
	public static void writeXml(LinkedHashMap<String, String> itemMap,File target) throws DocumentException{

        Document document;
        Element root;
       if(target.length()>0){
        	SAXReader sax=new SAXReader();	
        	document = sax.read(target);
			root=document.getRootElement();//获取根节点
        }else{
        	document = DocumentHelper.createDocument();
        	root = document.addElement(RES_TAG_NAME);
        }
      
        for(String name:itemMap.keySet()){
        	 Element elm = root.addElement(DIMEN_TAG_NAME);
        	 elm.setAttributeValue("name", name);
        	 elm.setText(itemMap.get(name));
        }
        witerXml(document,target);
	}
	
	/**修改/删除某个节点 返回-1表示操作目标节点不存在
	 * @throws DocumentException **/
	public static int modifyOrDeleteItems(String name,String value,File file) throws DocumentException{
		int returnValue=-1;
		Document document;
		Element root;
		if(file.length()>0){
			SAXReader sax=new SAXReader();	
			document = sax.read(file);
			root=document.getRootElement();//获取根节点
			List nodes = root.elements(DIMEN_TAG_NAME);
			for (Iterator it = nodes.iterator(); it.hasNext();) { 
				Element dimen = (Element) it.next();
				Attribute attr=dimen.attribute("name");
				String tempName=attr.getText();
				if(tempName.equals(name)){
					if(value.equals(INVALID_VALUE)){//删除操作
						root.remove(dimen);
					}else{//修改操作
						dimen.setText(value);
					}
					returnValue=1;
				}			       
			}
			witerXml(document,file);
		}
		return returnValue;
	}
	
	public static void witerXml(Document document,File file){
		XMLWriter writer;        
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			writer = new XMLWriter(new FileWriter(file),format);
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
} 