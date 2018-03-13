package com.newx.dimens.xmlutil;

import java.io.File;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import org.dom4j.DocumentException;

import com.newx.dimens.common.ComFun;

public class DimensOperateTool{
	
	final static String DEFAUT_FOR_FILLGAP="DEFAUT_FOR_FILLGAP";
	List<ItemsFilter> mFilters;
	File mSrcFile;
	List<File> mTargetFiles;
	List<Double> scales;
	
	public DimensOperateTool(File src,List<File> files
			,List<Double> scales){
		this(null, src, files, scales);
	}
	
	public DimensOperateTool(List<ItemsFilter> filters,File src,List<File> files
			,List<Double> scales){
		this.mFilters=filters;
		this.mSrcFile=src;
		this.mTargetFiles=files;
		this.scales=scales;
	}
	
	//执行克隆操作
	public void dimensClone() throws DocumentException{
		//根据过滤规则从源文件获取数据
		LinkedHashMap<String,String> mapFromSrcFile;
		if(mFilters.size()!=mTargetFiles.size()||scales.size()!=mTargetFiles.size()){//若
			System.out.println("过滤器个数和目标文件个数不相等");
			return;
		}
		int i=0;
		for(File file:mTargetFiles){
			ItemsFilter filter=mFilters.get(i);
			double scale=scales.get(i);
			if(filter!=null){
				mapFromSrcFile=filter.filter(DomXmlUtil.parSerSrcXml(mSrcFile),scale);
				DomXmlUtil.writeXml(mapFromSrcFile, file);
			}
			i++;
		}	
	}
	
	//执行新增操作
	public void dimensAdd(String newDimensName,String newDimensValue) throws DocumentException{
		if(scales.size()!=mTargetFiles.size()){
			System.out.println("过滤器个数和目标文件个数不相等");
			return;
		}
		int i=0;
		for(File file:mTargetFiles){
			double scale=scales.get(i++);
			LinkedHashMap<String, String> targetMap=new LinkedHashMap<String, String>();   
			String unit=ComFun.getUnit(newDimensValue);
			double value=ComFun.getNumFromDimStr(newDimensValue);
			value=ComFun.round(value*scale, 2, BigDecimal.ROUND_HALF_UP);
			targetMap.put(newDimensName,String.valueOf(value)+unit);
			DomXmlUtil.writeXml(targetMap, file);
		}
	}
	
	//修改/删除节点操作
	public void deleteItem(String name) throws DocumentException{
		modItem(name,null);
	}
	
	
	public void modItem(String name,String newValue) throws DocumentException{
		if(scales.size()!=mTargetFiles.size()){
			System.out.println("过滤器个数和目标文件个数不相等");
			return;
		}
		int i=0;
		for(File file:mTargetFiles){
			if(newValue!=null){//修改
				double scale=scales.get(i++);
				String unit=ComFun.getUnit(newValue);
				double value=ComFun.getNumFromDimStr(newValue);
				value=ComFun.round(value*scale, 2, BigDecimal.ROUND_HALF_UP);
				DomXmlUtil.modifyOrDeleteItems(name, String.valueOf(value)+unit, file);
			}
			else{//删除
				DomXmlUtil.modifyOrDeleteItems(name, DomXmlUtil.INVALID_VALUE, file);
			}
		}
	}
}