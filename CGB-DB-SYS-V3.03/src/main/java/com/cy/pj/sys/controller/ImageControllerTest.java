package com.cy.pj.sys.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/")
public class ImageControllerTest {

	
	private static final Logger logger = LoggerFactory.getLogger(ImageControllerTest.class);
	
	/**
	 * https://www.cnblogs.com/xiaoqi/p/spring-valid.html
	 * @Valid（全部验证）与@Validated(可分组验证)
	 * SpringBoot 2.3.0之后放弃了默认对 javax.validation.Valid 的支持。
	 * 
	 * https://blog.csdn.net/weixin_38118016/article/details/80977207/
	 * @param base64Str
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("imageTest")
	@ResponseBody
	public String image(@Validated String base64Str) throws Exception {
		
		String newBase64Str = null;
		// base64加密后的前缀
		if (!StringUtils.isEmpty(base64Str)) {
			newBase64Str = base64Str.replace("data:image/png;base64,", "");
//			newBase64Str = base64Str.substring(base64Str.indexOf(",")+1);
			logger.info("base64String====::{}",newBase64Str);
			// 准备文件夹路径
			Date date = new Date();
			String filePath = new SimpleDateFormat("YYYY").format(date)+"/"+new SimpleDateFormat("MM").format(date)+"/"+new SimpleDateFormat("dd").format(date)+"/";
			logger.info("文件路径===={}",filePath);
			String fileName = "D:/"+filePath+UUID.randomUUID()+".png";
			File file = new File(fileName);
			logger.info("file.getParentFile()=={}",file.getParentFile());
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			byte[] decode = Base64.getDecoder().decode(newBase64Str);
			logger.info("Base64.decode(base64Str)解码结果=={}",decode);
			logger.info("------");
			Files.write(Paths.get(fileName), decode, StandardOpenOption.CREATE);
			return fileName;
		}
		return null;
	}
}
