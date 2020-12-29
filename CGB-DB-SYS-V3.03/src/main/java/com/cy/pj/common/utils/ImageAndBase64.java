package com.cy.pj.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class ImageAndBase64 {

	
	private static final Logger logger = LoggerFactory.getLogger(ImageAndBase64.class);

	private ImageAndBase64() {}
	
	/**
	 * 本地图片转base64String
	 * @param filePath
	 * @return
	 */
	public static String imageToBase64(String filePath) {
		String encode = null;
		if (!StringUtils.isEmpty(filePath)) {
			try {
				byte[] readBytes = Files.readAllBytes(Paths.get(filePath));
				encode = Base64.encode(readBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return encode;
	}
	
	/**
	 * base64String转图片
	 * @param base64Encode
	 * @return
	 * @throws Exception
	 */
	public static String base64ToImage(String base64Encode) throws Exception {
		String fileName = null;
		if (!StringUtils.isEmpty(base64Encode)) {
			Date date = new Date();
			String filePath = new SimpleDateFormat("YYYY").format(date)+"/"+new SimpleDateFormat("MM").format(date)+"/"+new SimpleDateFormat("dd").format(date)+"/";
			logger.info("文件路径===={}",filePath);
			fileName = "D:/"+filePath+UUID.randomUUID()+".png";
			File file = new File(fileName);
			logger.info("file.getParentFile()=={}",file.getParentFile());
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			//com.sun.org.apache.xml.internal.security.utils.Base64
			byte[] decode = Base64.decode(base64Encode);
			// 文件流写入
			Files.write(Paths.get(fileName), decode, StandardOpenOption.CREATE);
//			Files.write(Paths.get(fileName), java.util.Base64.getDecoder().decode(base64Encode), StandardOpenOption.CREATE);
		}
		return fileName;
	}
}
