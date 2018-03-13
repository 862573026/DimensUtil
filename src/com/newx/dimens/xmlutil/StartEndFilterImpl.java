package com.newx.dimens.xmlutil;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import com.newx.dimens.common.ComFun;

public class StartEndFilterImpl extends ItemsFilter{

	private String mStartItemName,mEndItemName;
	
	public StartEndFilterImpl(String startName,String endName){
		this.mStartItemName=startName;
		this.mEndItemName=endName;
	}
	
	@Override
	public LinkedHashMap<String, String> filter(
			LinkedHashMap<String, String> srcItemMap,double scale) {
		// TODO Auto-generated method stub
		if(srcItemMap.isEmpty())
			return null;
		LinkedHashMap<String, String> targetMap=new LinkedHashMap<String, String>();
		boolean isUsefullNode=false;
		for(String name:srcItemMap.keySet()){
			if(name.equals(mStartItemName)||mStartItemName.equals(""))
				isUsefullNode=true;			
			if(isUsefullNode){
				String strValue=srcItemMap.get(name);
				String unit=ComFun.getUnit(strValue);
				if(unit!=null){
					double doubleValue=ComFun.round(
							ComFun.getNumFromDimStr(strValue)*scale,2,BigDecimal.ROUND_HALF_UP);
					String newStrValue=String.valueOf(doubleValue)+unit;
					targetMap.put(name, newStrValue);
				}
			}
			if(name.equals(mEndItemName))
				isUsefullNode=false;
		}
		return targetMap;
	}
	
}
