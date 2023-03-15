package com.cy.pj.sys.dao;

import com.cy.pj.sys.pojo.SysUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 更多测试注解
 * https://www.cnblogs.com/myitnews/p/12330297.html
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class SysUserMapperTest {

    @Autowired
    private SysUserDaoTest sysUserDaoTest;

    /**
     * https://blog.csdn.net/bochuangli/article/details/122898120
     * -XX:+HeapDumpOnXXX
     * 在发生XXX错误时，来dump堆快照
     *
     * 如加入：-XX:+HeapDumpOnOutOfMemoryError 参数
     * 在src目录下生成 dump堆快照文件 如：xxx.hprof
     * 利用Jprofile分析 dump堆快照文件，双击 xxx.hprof
     * 注意：这个过程中不能点skip 不能点skip 不能点skip
     *
     * #生成堆文件地址：
     * -XX:HeapDumpPath=/home/xxx/logs/
     *
     * @throws IOException
     */
    @Test
    public void writeFileByQuerySysUserTest() throws IOException {

        List<SysUser> sysUsers = new ArrayList<>();
        int pageSize = 100000;
        Long startIndex = 0L;

        long start = System.currentTimeMillis();

        do {
            sysUsers = sysUserDaoTest.findPageObjects(null, startIndex, pageSize);

            if (CollectionUtils.isEmpty(sysUsers)) {
                break;
            }

            writeDataFile(sysUsers);

            startIndex += pageSize;
            // 制造内存溢出
//            pageSize += pageSize;

        } while (sysUsers.size() <= pageSize);

        long end = System.currentTimeMillis();
        System.out.println("数据导出文件所用时长：" + (end -start));
    }

    public void writeDataFile(List<SysUser> sysUsers) throws IOException {
        final String split = "#@|";
        final String newLine = "\r\n";
        StringBuffer textContent = new StringBuffer();
        for (SysUser sysUser : sysUsers) {
            StringBuffer fileContent = new StringBuffer();
            Integer id = sysUser.getId();
            String username = sysUser.getUsername();
            String password = sysUser.getPassword();
            String salt = sysUser.getSalt();
            String email = sysUser.getEmail();
            String mobile = sysUser.getMobile();
            Integer valid = sysUser.getValid();
            Integer deptId = sysUser.getDeptId();
            Date createdTime = sysUser.getCreatedTime();
            Date modifiedTime = sysUser.getModifiedTime();
            String createdUser = sysUser.getCreatedUser();
            String modifiedUser = sysUser.getModifiedUser();
            fileContent.append(id).append(split);
            fileContent.append(username).append(split);
            fileContent.append(password).append(split);
            fileContent.append(salt).append(split);
            fileContent.append(email).append(split);
            fileContent.append(mobile).append(split);
            fileContent.append(valid).append(split);
            fileContent.append(deptId).append(split);
            fileContent.append(createdTime).append(split);
            fileContent.append(modifiedTime).append(split);
            fileContent.append(modifiedUser).append(split);
            fileContent.append(createdUser).append(split);
            fileContent.append(username).append(split);
            fileContent.append(password).append(split);
            fileContent.append(salt).append(split);
            fileContent.append(email).append(split);
            fileContent.append(mobile).append(split);
            fileContent.append(valid).append(split);
            fileContent.append(deptId).append(split);
            fileContent.append(createdTime).append(split);
            fileContent.append(modifiedTime).append(split);
            fileContent.append(createdUser).append(split);
            fileContent.append(modifiedUser).append(newLine);
            textContent.append(fileContent);
        }

        String url = "E:\\file\\20230309\\stringformat.txt";
        File file = new File(url);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
            // 判断文件是否存在，不存在则创建
            if (!file.exists()){
                file.createNewFile();
            }
        }

        // 写出数据到文件
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file,true),Charset.forName("UTF-8")))){
            String content = textContent.toString();
            bufferedWriter.write(content,0,content.length());
        }

    }

}
