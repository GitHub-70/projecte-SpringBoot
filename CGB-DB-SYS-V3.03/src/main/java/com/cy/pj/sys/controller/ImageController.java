package com.cy.pj.sys.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import sun.misc.BASE64Decoder;

/**
 * import sun.misc.BASE64Decoder无法找到
 * JDK升级从1.8升级到9.0.1,发现JDK中的lib\tools.jar和JRE中的lib\rt.jar已从Java SE 9中删除
 */
@RestController
@RequestMapping("/")
public class ImageController {

	
	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@RequestMapping("image")
	public String image(String base64Str) throws IOException {
		String base64String = null;
		if (!StringUtils.isEmpty(base64Str)) {
			// base64加密后的前缀
			base64String = base64Str.replace("data:image/png;base64,", "");
//			String base64String = base64Str.substring(base64Str.indexOf(",")+1);
			logger.info("base64String====::{}",base64String);
			BASE64Decoder base64Decoder = new BASE64Decoder();
			// 图片解码
			byte[] decodeBuffer = base64Decoder.decodeBuffer(base64String);
			logger.info("图片解码长度={}\n图片解码结果::{}",decodeBuffer.length, decodeBuffer);
			for (int i = 0; i < decodeBuffer.length; i++) {
				if (decodeBuffer[i] < 0) {//调整异常数据
					decodeBuffer[i] += 256;
//					logger.info("decodeBuffer[i] < 0=={}---i=={}",decodeBuffer[i] += 256,i);
				}
			}
			logger.info("decodeBuffer=={}",decodeBuffer);
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
			FileOutputStream fileOutputStream = null;
			try {
				String imageName = UUID.randomUUID()+".png";
				logger.info("图片缩略图名字==={}",imageName);
//				byteArrayOutputStream(decodeBuffer, file);
				// 与某路径的文件建立流通道
				fileOutputStream = new FileOutputStream(file);
				// 写进流通道中
				fileOutputStream.write(decodeBuffer);
				// 将流通道中的数据刷新到文件(该流是--字节流，直接操作文件，所以不用flush也能输出到文件)
				fileOutputStream.flush();
				String thumbnailName = new SimpleDateFormat("HHmmss").format(date)+".png";
				Thumbnails.of(file).size(10, 10).toFile(thumbnailName);
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if (null != fileOutputStream) {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return fileName;
		}
		return null;
	}

	/**
	 * 字节数组流--byteArrayOutputStream
	 * @param decodeBuffer
	 * @param file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void byteArrayOutputStream(byte[] decodeBuffer, File file) throws IOException, FileNotFoundException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byteArrayOutputStream.write(decodeBuffer);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		FileOutputStream fileOutputStreams = new FileOutputStream(file);
		fileOutputStreams.write(byteArray);
		fileOutputStreams.flush();
		fileOutputStreams.close();
		byteArrayOutputStream.flush();
		byteArrayOutputStream.close();
		System.out.println(byteArrayOutputStream.toString());
	}
}
