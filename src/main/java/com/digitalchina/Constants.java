package com.digitalchina;


public interface Constants {
	public enum AdapterConfigPlace {
		PROPERTIES, /**本地properties文件或yaml文件*/
		BJPAAS,     /**北京paas配置中心*/
		PLATFROM    /**东区微服务平台*/
	}
	
	public static final String ADAPTER_CONFIG_PLACE_NAME="ADAPTER_CONFIG_PLACE";
	public static final String DEFAULT_ENCODING="utf-8";
}
