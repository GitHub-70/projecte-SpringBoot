package com.cy.pj.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@SpringBootTest
public class SnakeYamlTest {

	@Value("${swagger.enable}")
	private String enable;
	
	@Test
	public void testLoad() {
	    String yamlStr = "key: hello yaml";
	    Yaml yaml = new Yaml();
	    Object ret = yaml.load(yamlStr);
	    System.out.println(ret);
	    System.out.println(ret.getClass().getSimpleName());
	}
	
	@Test
	public void test2() throws Exception {
	    Yaml yaml = new Yaml();
	    Map<String, Object> ret = (Map<String, Object>) yaml.load(this
	            .getClass().getClassLoader().getResourceAsStream("application.yml"));
	    System.out.println(ret);
	}

	/**
	 * 修改yml中 swagger.enable属性
	 * TODO 多次执行结果会不同 why？
	 */
	@Test
    public void updateYaml(){
        try {
            URL url = this.getClass().getClassLoader().getResource("application.yml");
            // 设置yaml格式
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
            dumperOptions.setPrettyFlow(false);
            Yaml yaml = new Yaml(dumperOptions);
            // 加载yaml中内容
            Map map =(Map)yaml.load(new FileInputStream(url.getFile()));
            Object object = map.get("swagger");
			boolean enable = (boolean)((Map)object).get("enable");
            System.out.println("这是修改前："+enable);
            Map linkedHashMap = (Map)map.get("swagger");
            linkedHashMap.put("enable", false);
            // 输出到内存中的yaml中
            yaml.dump(map, new OutputStreamWriter(new FileOutputStream(url.getFile())));
            System.out.println("这是修改后："+map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	
	@Test
	public void test3() throws Exception {
		
	    System.out.println("enable="+enable);
	}
}
