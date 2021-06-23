package com.wanmi.sbc.message.utils.aliyun;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 阿里云图片服务器服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Data
@Slf4j
public class AliYunService {

    /**
     * 请求路径
     */
    @Value("${aliyun.endPoint}")
    private String endPoint;

    /**
     * 用户名
     */
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    /**
     * 密钥
     */
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 存储空间名
     */
    @Value("${aliyun.bucketName}")
    private String bucketName;

    /**
     * 连接超时时间（单位：毫秒）
     */
    private int connectionTimeout = 3 * 1000;

    /**
     * 连接传输数据的超时时间（单位：毫秒）
     */
    private int socketTimeout = 3 * 1000;

    /**
     * 上传阿里云
     * @param baos 缓存数据流
     * @param fileName 指定文件名
     * @return
     */
    public void uploadExcel(ByteArrayOutputStream baos, String fileName) throws SbcRuntimeException {
        if(StringUtils.isBlank(endPoint) || StringUtils.isBlank(accessKeyId) || StringUtils.isBlank(accessKeySecret) || StringUtils.isBlank(bucketName)){
            throw new SbcRuntimeException("R-000104");
        }

        //创建阿里云实例
        OSSClient ossClient = this.buildClient();
        ByteArrayInputStream bais = null;
        try {
            ObjectMetadata meta = new ObjectMetadata();
            // 设置上传内容类型
            meta.setContentType("application/vnd.ms-excel");
            bais = new ByteArrayInputStream(baos.toByteArray());
            ossClient.putObject(bucketName, fileName, bais, meta);
        } catch (OSSException oe) {
            log.error("上传文件-阿里云服务器端错误->错误码:{},描述:{}", oe.getErrorCode(), oe.getMessage());
            throw new SbcRuntimeException("R-000102");
        } catch (ClientException ce) {
            log.error("上传文件-连接阿里云服务器端错误->", ce);
            throw new SbcRuntimeException("R-000101");
        } finally {
            if(bais != null){
                try {
                    bais.close();
                } catch (IOException e) {
                    log.error("缓存关闭异常->", e);
                }
            }
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 删除阿里云文件
     * @param files 文件
     * @return
     */
    public void deleteFiles(List<String> files) throws SbcRuntimeException {
        if(StringUtils.isBlank(endPoint) || StringUtils.isBlank(accessKeyId) || StringUtils.isBlank(accessKeySecret) || StringUtils.isBlank(bucketName)){
            throw new SbcRuntimeException("R-000104");
        }

        //创建阿里云实例
        OSSClient ossClient = this.buildClient();
        try {
            ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(files)).getResponse();
        } catch (OSSException oe) {
            log.error("删降文件->阿里云服务错误->错误码:{},描述:{}", oe.getErrorCode(), oe.getMessage());
            throw new SbcRuntimeException("R-000102");
        } catch (ClientException ce) {
            log.error("删降文件->连接阿里云服务器端错误->", ce);
            throw new SbcRuntimeException("R-000101");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 是否存在阿里云文件
     * @param file 文件
     * @return
     */
    public boolean existsFiles(String file) throws SbcRuntimeException {
        if(StringUtils.isBlank(endPoint) || StringUtils.isBlank(accessKeyId) || StringUtils.isBlank(accessKeySecret) || StringUtils.isBlank(bucketName)){
            throw new SbcRuntimeException("R-000104");
        }

        //创建阿里云实例
        OSSClient ossClient = this.buildClient();
        try {
            return ossClient.doesObjectExist(bucketName, file);
        } catch (OSSException oe) {
            log.error("判断文件存在->阿里云服务错误->错误码:{},描述:{}", oe.getErrorCode(), oe.getMessage());
            throw new SbcRuntimeException("R-000102");
        } catch (ClientException ce) {
            log.error("判断文件存在->连接阿里云服务器端错误->", ce);
            throw new SbcRuntimeException("R-000101");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 公共性创建阿里云实例
     * @return
     */
    private OSSClient buildClient(){
        ClientConfiguration config = new ClientConfiguration();
        config.setConnectionTimeout(connectionTimeout);
        config.setSocketTimeout(socketTimeout);
        return new OSSClient(endPoint, accessKeyId, accessKeySecret, config);
    }

    /**
     * 获取下载前缀
     * @return 下载地址前缀
     */
    public String getPrefix(){
        return String.format("https://%s.%s/",bucketName,endPoint);
    }
}
