package com.wanmi.osd;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.KeyAndVersion;
import com.obs.services.model.ObsObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.utils.IOUtils;
import com.wanmi.osd.ExceptionHandle.OSDException;
import com.wanmi.osd.bean.*;
import com.wanmi.osd.cos.TxCosClient;
import com.wanmi.osd.obs.HuaWeiObsClient;
import com.wanmi.osd.oss.AliOssClient;
import lombok.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class OsdClient {

    private OsdClient(){}

    private static OsdClient osdClient = new OsdClient();

    public static OsdClient instance() {
        return osdClient;
    }

    public static final String DEFAULT_SCHEME_NAME ="https";

    /**
     * 文件上传(返回资源访问地址)
     * @param osdClientParam
     * @throws Exception
     */
    public void putObject(@NonNull OsdClientParam osdClientParam, @NonNull OsdResource osdResource) throws IOException {
        OsdConfig osdConfig = buildConfig(osdClientParam);
        OsdType osdType = osdConfig.getOsdType();
        switch (osdType) {
            case ALIYUN:
                OSSClient ossClient = null;
                try {

                    ossClient= AliOssClient.instance().init(osdConfig);
                    ossClient.putObject(osdConfig.getBucketName(),
                            osdResource.getOsdResourceKey(),
                            osdResource.getOsdInputStream());
                }catch (OSSException oss) {
                    throw new OSDException(oss.getErrorCode(), oss.getMessage());
                }catch (ClientException client){
                    throw new OSDException(client.getErrorCode(), client.getMessage());
                }finally {
                    Objects.requireNonNull(ossClient).shutdown();
                }
                break;
            case HUAWEIYUN:
                ObsClient obsClient = null;
                try {
                    obsClient = HuaWeiObsClient.instance().init(osdConfig);
                    obsClient.putObject(osdConfig.getBucketName(), osdResource.getOsdResourceKey(),osdResource.getOsdInputStream());
                }catch (ObsException obs){
                    throw new OSDException(obs.getErrorCode(), obs.getXmlMessage());
                }finally {
                    Objects.requireNonNull(obsClient).close();
                }
                break;
            case TXYUN:
                COSClient cosClient = null;
                InputStream input = null;
                try {
                    InputStream inputStream = osdResource.getOsdInputStream();
                    byte [] bytes = IOUtils.toByteArray(inputStream);
                    input = new ByteArrayInputStream(bytes);
                    cosClient = TxCosClient.instance().init(osdConfig);
                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentLength(bytes.length);
                    PutObjectRequest putObjectRequest = new PutObjectRequest(osdConfig.getBucketName(), osdResource.getOsdResourceKey(),
                            input,objectMetadata);
                    cosClient.putObject(putObjectRequest);
                }catch (CosServiceException cos) {
                    throw new OSDException(cos.getErrorCode(), cos.getErrorResponseXml());
                }finally {
                    Objects.requireNonNull(input).close();
                    Objects.requireNonNull(cosClient).shutdown();
                }
                break;
            default:
                throw new OSDException("参数错误！请检查osdType是否正确！！");
        }
    }

    /**
     * 删除文件
     * @param osdClientParam,resourceKeys
     * @throws Exception
     */
    public void deleteObject(@NonNull OsdClientParam osdClientParam, @NonNull List<String> resourceKeys) throws IOException {
        OsdConfig osdConfig = buildConfig(osdClientParam);
        OsdType osdType = osdConfig.getOsdType();
        switch (osdType) {
            case ALIYUN:
                OSSClient ossClient = null;
                try {
                    ossClient = AliOssClient.instance().init(osdConfig);
                    ossClient.deleteObjects(new DeleteObjectsRequest(osdConfig.getBucketName()).withKeys(resourceKeys));
                }catch (OSSException oss) {
                    throw new OSDException(oss.getErrorCode(), oss.getMessage());
                }catch (ClientException client){
                    throw new OSDException(client.getErrorCode(), client.getMessage());
                }finally {
                    Objects.requireNonNull(ossClient).shutdown();
                }
                break;
            case HUAWEIYUN:
                ObsClient obsClient = null;
                try {
                    obsClient = HuaWeiObsClient.instance().init(osdConfig);
                    KeyAndVersion[] keyAndVersions =
                            resourceKeys.stream().map(KeyAndVersion::new).toArray(KeyAndVersion[]::new);
                    obsClient.deleteObjects(new com.obs.services.model.DeleteObjectsRequest(osdConfig.getBucketName(),
                            true,keyAndVersions));
                }catch (ObsException obs){
                    throw new OSDException(obs.getErrorCode(), obs.getMessage());
                }finally {
                    Objects.requireNonNull(obsClient).close();
                }
                break;
            case TXYUN:
                COSClient cosClient = null;
                try {
                    cosClient = TxCosClient.instance().init(osdConfig);
                    List<com.qcloud.cos.model.DeleteObjectsRequest.KeyVersion> keyVersions =
                            resourceKeys.stream().map(com.qcloud.cos.model.DeleteObjectsRequest.KeyVersion::new).collect(Collectors.toList());
                    com.qcloud.cos.model.DeleteObjectsRequest deleteObjectsRequest =
                            new  com.qcloud.cos.model.DeleteObjectsRequest(osdConfig.getBucketName());
                    deleteObjectsRequest.withKeys(keyVersions);
                    cosClient.deleteObjects(deleteObjectsRequest);
                }catch (CosServiceException cos) {
                    throw new OSDException(cos.getErrorCode(), cos.getMessage());
                }finally {
                    Objects.requireNonNull(cosClient).shutdown();
                }
                break;
            default:
                throw new OSDException("参数错误！请检查osdType是否正确！！");
        }
    }

    /**
     * 检查是否存在该资源
     * @param osdClientParam
     * @param osdResourceKey
     * @return
     * @throws IOException
     */
    public  Boolean doesObjectExist(@NonNull OsdClientParam osdClientParam,@NonNull String osdResourceKey) throws IOException {
        OsdConfig osdConfig = buildConfig(osdClientParam);
        OsdType osdType = osdConfig.getOsdType();
        Boolean isExists;
        switch (osdType) {
            case ALIYUN:
                OSSClient ossClient = null;
                try {
                    ossClient = AliOssClient.instance().init(osdConfig);
                    isExists = ossClient.doesObjectExist(osdConfig.getBucketName(),osdResourceKey);
                }catch (OSSException oss) {
                    throw new OSDException(oss.getErrorCode(), oss.getMessage());
                }catch (ClientException client){
                    throw new OSDException(client.getErrorCode(), client.getMessage());
                }finally {
                    Objects.requireNonNull(ossClient).shutdown();
                }
                break;
            case HUAWEIYUN:
                ObsClient obsClient = null;
                try {
                    obsClient = HuaWeiObsClient.instance().init(osdConfig);
                    isExists = obsClient.doesObjectExist(osdConfig.getBucketName(),osdResourceKey);
                }catch (ObsException obs){
                    throw new OSDException(obs.getErrorCode(), obs.getMessage());
                }finally {
                    Objects.requireNonNull(obsClient).close();
                }
                break;
            case TXYUN:
                COSClient cosClient = null;
                try {
                    cosClient = TxCosClient.instance().init(osdConfig);
                    isExists = cosClient.doesObjectExist(osdConfig.getBucketName(),osdResourceKey);
                }catch (CosServiceException cos) {
                    throw new OSDException(cos.getErrorCode(), cos.getMessage());
                }finally {
                    Objects.requireNonNull(cosClient).shutdown();
                }
                break;
            default:
                throw new OSDException("参数错误！请检查osdType是否正确！！");
        }
        return isExists;
    }

    /**
     * 获取文件IO流，需要调用者手动关闭
     * @param osdClientParam
     * @param osdResourceKey
     * @return
     * @throws IOException
     */
    public byte[] getObject(@NonNull OsdClientParam osdClientParam,@NonNull String osdResourceKey) throws IOException {
        OsdConfig osdConfig = buildConfig(osdClientParam);
        OsdType osdType = osdConfig.getOsdType();
        switch (osdType) {
            case ALIYUN:
                OSSClient ossClient = null;
                OSSObject ossObject = null;
                try {
                    ossClient = AliOssClient.instance().init(osdConfig);
                    ossObject = ossClient.getObject(osdConfig.getBucketName(), osdResourceKey);
                    return IOUtils.toByteArray(ossObject.getObjectContent());
                }catch (OSSException oss) {
                    throw new OSDException(oss.getErrorCode(), oss.getMessage());
                }catch (ClientException client){
                    throw new OSDException(client.getErrorCode(), client.getMessage());
                }finally {
                    Objects.requireNonNull(ossObject).close();
                    Objects.requireNonNull(ossClient).shutdown();
                }
            case HUAWEIYUN:
                ObsClient obsClient = null;
                try {
                    obsClient = HuaWeiObsClient.instance().init(osdConfig);
                    ObsObject obsObject = obsClient.getObject(osdConfig.getBucketName(), osdResourceKey);
                    return IOUtils.toByteArray(obsObject.getObjectContent());
                }catch (ObsException obs){
                    throw new OSDException(obs.getErrorCode(), obs.getMessage());
                }finally {
                    Objects.requireNonNull(obsClient).close();
                }
            case TXYUN:
                COSClient cosClient = null;
                COSObject cosObject = null;
                try {
                    cosClient = TxCosClient.instance().init(osdConfig);
                    GetObjectRequest getObjectRequest = new GetObjectRequest(osdConfig.getBucketName(), osdResourceKey);
                    cosObject = cosClient.getObject(getObjectRequest);
                    return IOUtils.toByteArray(cosObject.getObjectContent());
                }catch (CosServiceException cos) {
                    throw new OSDException(cos.getErrorCode(), cos.getMessage());
                }finally {
                    Objects.requireNonNull(cosObject).close();
                    Objects.requireNonNull(cosClient).shutdown();
                }
            default:
                throw new OSDException("参数错误！请检查osdType是否正确！！");
        }
    }

    /**
     * 构建配置类
     * @param osdClientParam
     * @return
     * @throws Exception
     */
    public OsdConfig buildConfig(@NonNull OsdClientParam osdClientParam) {
        String configType = osdClientParam.getConfigType();
        OsdType osdType = OsdType.getOsdType(configType);
        String context = osdClientParam.getContext();
        OsdConfig osdConfig =  JSONObject.toJavaObject(JSON.parseObject(context),OsdConfig.class);
        osdConfig.setOsdType(osdType);
        return osdConfig;
    }

    /**
     * 构建上传成功的访问地址
     * @param osdClientParam
     * @param osdResourceKey
     * @return
     */
    public String getResourceUrl(@NonNull OsdClientParam osdClientParam,@NonNull String osdResourceKey) {
        StringBuilder stringBuilder  = new StringBuilder();
        stringBuilder.append(getPrefix(osdClientParam));
        stringBuilder.append(osdResourceKey);
        return stringBuilder.toString();
    }

    /**
     * 获取前缀
     * @param osdClientParam
     * @return
     */
    public String getPrefix(@NonNull OsdClientParam osdClientParam){
        OsdConfig osdConfig = buildConfig(osdClientParam);
        StringBuilder stringBuilder  = new StringBuilder();
        stringBuilder.append(DEFAULT_SCHEME_NAME);
        stringBuilder.append("://");
        stringBuilder.append(osdConfig.getBucketName());
        stringBuilder.append(".");
        stringBuilder.append(osdConfig.getEndPoint());
        stringBuilder.append("/");
        return stringBuilder.toString();
    }
}
