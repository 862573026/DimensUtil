package com.newx.dimens.xmlutil;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import com.newx.dimens.common.ComFun;

public class StartMatchFilterImpl extends ItemsFilter {
	
	String mMatchChars;
	public StartMatchFilterImpl(String matchChars){
		this.mMatchChars=matchChars;
	}

	@Override
	public LinkedHashMap<String, String> filter(
			LinkedHashMap<String, String> srcItemMap,double scale) {
		// TODO Auto-generated method stub
		if(srcItemMap.isEmpty())
			return null;
		LinkedHashMap<String, String> targetMap=new LinkedHashMap<String, String>();
		for(String name:srcItemMap.keySet()){
			if(name.startsWith(mMatchChars)){
				String strValue=srcItemMap.get(name);
				String unit=ComFun.getUnit(strValue);
				if(unit!=null){
					double doubleValue=ComFun.round(
							ComFun.getNumFromDimStr(strValue)*scale,2,BigDecimal.ROUND_HALF_UP);
					String newStrValue=String.valueOf(doubleValue)+unit;
					targetMap.put(name, newStrValue);
				}
			}
		}
		return targetMap;
	}
	
}
