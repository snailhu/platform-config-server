package com.digitalchina.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yangzhec on 2016/12/12.
 * 这个类是从配置中心拉取配置的工具类
 */
public class ConfigUtil {

    public static Properties loadFromPaasConfigCenter(){
        //下面的环境变量在部署的时候都写在容器的环境变量中，可以直接使用，
        // 如果是debug环境，那么就从命令行参数中读取这些配置
        String host,user,password,appName,appVersion,type,projectName,secretKey,env;
        boolean debug =false;
//        if(System.getenv("Config_host")!=null){
//            debug=true;
//        }
//        boolean  debug = System.getenv("Config_host")==null;
        if(debug){
            //如果是在调试环境下
            System.out.print("************"+debug);
            host = System.getProperty("Config_host");
            user = System.getProperty("Config_user");
            password = System.getProperty("Config_password");
            appName = System.getProperty("Config_app");
            appVersion = System.getProperty("Config_version");
            projectName = System.getProperty("Config_projectName");
            secretKey = System.getProperty("Config_secretKey");
            env = System.getProperty("Config_env");
            System.out.print("************"+host+user+password);
            type = "password";
        }else{
            host = System.getenv("Config_host");
            user = System.getenv("Config_user");
            password = System.getenv("Config_password");
            appName = System.getenv("Config_app");
            projectName = System.getenv("Config_projectName");
            secretKey = System.getenv("Config_secretKey");
            env = System.getenv("Config_env");
            appVersion = System.getenv("Config_version");
            System.out.print("******pass******"+host+user+password);
            //写在环境变量的是md5加密的password
            type = "md5";
        }
        Map<String,Object> map = new HashMap<String,Object>();
        ConfigClient configClient = new ConfigClient(host,user,password,projectName,secretKey,env,appName,appVersion);
        configClient.setType(type);
        Properties properties = configClient.getPropertyConfig();
        for(Map.Entry<Object,Object> entry:properties.entrySet()){
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            map.put(key,value);
            System.out.println(key+"--->"+value);
        }
        return properties;
    }
}
