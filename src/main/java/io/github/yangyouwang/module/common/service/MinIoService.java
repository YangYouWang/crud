package io.github.yangyouwang.module.common.service;

import io.github.yangyouwang.framework.config.properties.MinioProperties;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Description:  Minio工具类 <br/>
 * date: 2022/3/20 0:59<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
@Slf4j
@Component
public class MinIoService {

    @Resource
    private MinioProperties minioProperties;

    /**
     * 初始化连接
     */
    public MinioClient getMinioClient(){
        return MinioClient.builder().endpoint(minioProperties.getEndpoint()).credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()).build();
    }

    /**
     * 根据存储桶的名称判断桶是否存在，不存在就创建
     * @param bucketName 桶名称
     */
    public void isExistBucketName(String bucketName) throws Exception {
        boolean isExist =  getMinioClient().bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if(!isExist) {
            getMinioClient().makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info(bucketName+"创建成功");
        }
    }

    /**
     * Minio文本地文件上传
     * @param filePath 完整路径文件名
     * @param fileName 修饰过的文件名 非源文件名
     * @return 上传是否成功
     */
    public String localMinioUpload(String filePath, String fileName, String bucketName) {
        try {
            //判断存储桶是否存在，不存在则创建
            isExistBucketName(bucketName);
            // minio仓库名
            File file = new File(filePath);
            InputStream is = new FileInputStream(file);
            String contentType = new MimetypesFileTypeMap().getContentType(file);
            System.out.println(contentType);
            getMinioClient().putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(is,
                                    file.length(),
                                    -1)
                            .contentType(contentType)
                            .build());
            log.info("成功上传文件 " + fileName  + " 至 " + bucketName);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传存储服务器出错异常");
        }
    }


    /**
     * Minio远程文件上传 上传对象是MultipartFile
     * @param file 文件实体
     * @param fileName 修饰过的文件名 非源文件名
     * @return 上传是否成功
     */
    public String minioUpload(MultipartFile file, String fileName, String bucketName) {
        try {
            //判断存储桶是否存在，不存在则创建
            isExistBucketName(bucketName);
            // fileName为空，说明要使用源文件名上传
            if (fileName == null) {
                fileName = file.getOriginalFilename();
                fileName = fileName.replaceAll(" ", "_");
            }
            getMinioClient().putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(),
                                    file.getSize(),
                                    -1)
                            .contentType(file.getContentType())
                            .build());
            log.info("成功上传文件 " + fileName + " 至 " + bucketName);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传存储服务器出错异常");
        }
    }

    /**
     * 判断文件是否存在
     * @param fileName   文件全路径
     * @param bucketName   存储桶名称
     * @return 文件是否存储
     */
    public boolean isFileExisted(String fileName,String bucketName) {
        InputStream inputStream = null;
        try {
            inputStream = getMinioClient().getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
            if (inputStream != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 删除文件
     * @param bucketName 桶名（文件夹）
     * @param fileName 文件名
     * @return 是否删除成功
     */
    public boolean delete(String bucketName,String fileName) {
        try {
            getMinioClient().removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }


    /**
     * 获取文件的外链访问URL
     * @param fileName 文件名称
     * @return 文件地址
     */
    public String getShowUtrl (String fileName,String bucketName) throws Exception{
        return getMinioClient().presignedGetObject(bucketName, fileName);
    }
    /**
     * 下载文件
     * @param objectName 文件名
     * @return 是否响应地址
     */
    public void downloadFile(String objectName, HttpServletResponse response, String bucketName) {
        try {
            InputStream stream = getMinioClient().getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
            String filename = new String(objectName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while((len=stream.read(buffer)) > 0){
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            stream.close();
            servletOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("下载报错异常！");
        }
    }

}
