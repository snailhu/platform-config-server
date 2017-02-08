package com.digitalchina;

import com.digitalchina.Server.ConfigUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * 普通java读取配置中心
 * @author zhang
 *
 */
public class CommonConfigAdapter {
	
	private Logger log = LoggerFactory.getLogger(CommonConfigAdapter.class);
	
	private String[] loadProperties;
	
	private static final String DEFAULT_PROPERTIES_FILE="application.properties";
	
	public CommonConfigAdapter(){
		
	}
	
	public CommonConfigAdapter(String[] loadProperties){
		this.loadProperties=loadProperties;
	}
	
	/**
	 * 根据环境变量，读取属性配置文件
	 * @return
	 */
	public Properties readPropertiesByEnvironment(){
		String adapterConfigPlace=System.getenv(Constants.ADAPTER_CONFIG_PLACE_NAME);
		String PathPlace=System.getenv("PATH");
        log.info(String.format("The value of environment 'ADAPTER_CONFIG_PLACE' is : %s", adapterConfigPlace));
        System.out.println(String.format("The value of environment 'ADAPTER_CONFIG_PLACE' is : %s", adapterConfigPlace));
        
        /**
         * 如果loadProperties为空，加载classpath下面所有的properties文件
         */
        scannProperties();
        
        
        /**
         * if the environment is null,read config properties
         */
        if(adapterConfigPlace==null || "".equals(adapterConfigPlace.trim())){
        	return readProperties();
        }
        
        /**
         * load properties from paas of beijing
         */
        Properties prop = null;
        if(Constants.AdapterConfigPlace.BJPAAS.toString().equals(adapterConfigPlace)){
        	prop = ConfigUtil.loadFromPaasConfigCenter();
        }
        
        if(prop==null){
        	throw new RuntimeException("The config of beijing paas is not exists!");
        }
        
    	return prop;
	}
	
	/**
	 * 扫描classpath下面所有的properties配置文件
	 */
	private void scannProperties(){
		if(loadProperties!=null && loadProperties.length>0){
			return;
		}
		
		String parentPath=null;
		try {
			parentPath = URLDecoder.decode(this.getClass().getClassLoader().getResource(".").getPath(),Constants.DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		log.info(String.format("Scann dir:%s",parentPath));
		System.out.println(String.format("Scann dir:%s",parentPath));
		
		File file=new File(parentPath);
		
		
		Collection<File> collection=FileUtils.listFiles(file, FileFilterUtils.suffixFileFilter(".properties"), null);
		if(collection==null || collection.size()==0){
			return;
		}
		
		Iterator<File> iterator=collection.iterator();
		loadProperties=new String[collection.size()];
		int i=0;
		while(iterator.hasNext()){
			File tempFile=iterator.next();
			loadProperties[i++]=tempFile.getName();
			System.out.println(String.format("load file:%s",tempFile.getName()));
		}
	}
	
	public static void main(String[] args){
		CommonConfigAdapter ccfa=new CommonConfigAdapter();
		ccfa.scannProperties();
	}
	
	/**
     * 读取bean中配置的properties文件
     * @return
     */
    private Properties readProperties(){
    	Properties properties=new Properties();
    	if(loadProperties!=null && loadProperties.length>0){
    		for(int i=0;i<loadProperties.length;i++){
    			Properties temp=readPropertiesFromFile(loadProperties[i], Constants.DEFAULT_ENCODING);
    			copyProperties(properties, temp);
    		}
    		
    	}else{
    		try{
    			copyProperties(properties, readPropertiesFromFile(DEFAULT_PROPERTIES_FILE, Constants.DEFAULT_ENCODING));
    		}catch(Exception e){
    			log.warn("Read the default config file 'application.properties', but it is not exists!");
    		}
    	}
    	
    	return properties;
    }
    
    /**
     * 从文件中读取属性配置
     * @param fileName
     * @param encoding
     * @return
     */
    private Properties readPropertiesFromFile(String fileName, String encoding) {
		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread()
					            .getContextClassLoader()
					            .getResourceAsStream(fileName);
			
			if (inputStream == null)
				throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
			
			Properties properties = new Properties();
			properties.load(new InputStreamReader(inputStream, encoding));
			return properties;
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		}
		finally {
			if (inputStream != null) try {inputStream.close();} catch (IOException e) {e.printStackTrace();}
		}
	}
    
    /**
     * 复制属性
     * @param src
     * @param dest
     * @return
     */
    public Properties copyProperties(Properties src,Properties dest){
    	if(dest==null || dest.isEmpty()){
    		return src;
    	}
    	
    	if(src==null){
    		src=new Properties();
    	}
    	
    	Iterator<Entry<Object,Object>> iterator=dest.entrySet().iterator();
    	
    	while(iterator.hasNext()){
    		Entry<Object, Object> entry=iterator.next();
    		src.put(entry.getKey(), entry.getValue());
    	}
    	
    	return src;
    	
    }

	public String[] getLoadProperties() {
		return loadProperties;
	}

	public void setLoadProperties(String[] loadProperties) {
		this.loadProperties = loadProperties;
	}
    
}
