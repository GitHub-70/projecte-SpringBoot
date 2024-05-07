package com.cy.pj.common.excel;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import java.io.*;


public class WrodToPDF {


    public static void main(String[] args) {

        long start = System.currentTimeMillis();

//        File inputWord = new File("E:\\个人资料\\吴槐_Java开发_15685114493 - 副本.doc");
        File inputWord = new File("E:\\个人资料\\吴槐_Java开发_15685114493.docx");
        File outputFile = new File("E:\\个人资料\\吴槐.pdf");

        try (InputStream docxInputStream = new FileInputStream(inputWord);
             OutputStream outputStream = new FileOutputStream(outputFile)){
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            // 关闭转换器
            converter.shutDown();
            System.out.println("success");
            long end = System.currentTimeMillis();
            System.out.println("耗时：" + (end-start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
