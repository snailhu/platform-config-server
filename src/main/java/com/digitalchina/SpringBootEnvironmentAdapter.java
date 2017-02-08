package com.digitalchina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 环境适配器，此类适合spring boot
 * 默认读取本地配置文件，如果环境变量中ADAPTER_CONFIG_PLACE=BJ_PAAS
 * @author zhang
 *
 */
public class SpringBootEnvironmentAdapter implements EnvironmentPostProcessor {
	
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment, SpringApplication springApplication) {
        String str = "PAASConfigs";
        
        CommonConfigAdapter commonConfigAdapter=new CommonConfigAdapter();
        Properties prop=commonConfigAdapter.readPropertiesByEnvironment();
        
        Map<String,Object> map = new HashMap<String,Object>();
        for(Map.Entry<Object,Object> entry:prop.entrySet()){
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            map.put(key,value);
        }
        
        MapPropertySource mps =  new MapPropertySource(str, map);
        MutablePropertySources sources = configurableEnvironment.getPropertySources();
        String name = findPropertySource(sources);
        if (sources.contains(name)) {
            sources.addBefore(name, mps);
        }
        else {
            sources.addFirst(mps);
        }
        
    }

    private String findPropertySource(MutablePropertySources sources) {
        return "PAAS.CONFIG";
    }

}
