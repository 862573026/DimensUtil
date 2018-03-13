package com.newx.dimens.xmlutil;

import java.util.LinkedHashMap;

public abstract class ItemsFilter {
	
	/**根据过滤规则生成新的节点列表**/
	public abstract LinkedHashMap<String, String> filter(LinkedHashMap<String, String> srcItemMap,double scale);	
	
}
