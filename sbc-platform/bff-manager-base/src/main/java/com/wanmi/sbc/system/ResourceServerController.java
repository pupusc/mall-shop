package com.wanmi.sbc.system;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ImageUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.fandeng.MaterialCheckRequest;
import com.wanmi.sbc.customer.api.response.fandeng.MaterialCheckResponse;
import com.wanmi.sbc.elastic.api.provider.systemresource.EsSystemResourceProvider;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourceSaveRequest;
import com.wanmi.sbc.elastic.bean.vo.systemresource.EsSystemResourceVO;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunConfigByIdRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunConfigListRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunConfigModifyRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.api.response.yunservice.YunConfigListResponse;
import com.wanmi.sbc.setting.api.response.yunservice.YunConfigResponse;
import com.wanmi.sbc.setting.api.response.yunservice.YunUploadResourceResponse;
import com.wanmi.sbc.setting.bean.enums.AuditStatus;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 素材服务
 * Created by yinxianzhi on 18/10/15.
 */
@Api(tags = "ResourceServerController", description = "素材服务")
@RestController
public class ResourceServerController {

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private EsSystemResourceProvider esSystemResourceProvider;
    @Autowired
    private ExternalProvider externalProvider;

    @ApiOperation(value = "上传素材")
    @RequestMapping(value = "/uploadResource", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadFile(@RequestParam("uploadFile") List<MultipartFile> multipartFiles, Long
            cateId, ResourceType resourceType,Integer isAudit) {

        //验证上传参数
        if (CollectionUtils.isEmpty(multipartFiles)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<String> resourceUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            if (file == null || file.getSize() == 0 || file.getOriginalFilename() == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            if (ImageUtils.checkImageAndVideoSuffix(file.getOriginalFilename())) {
                try {
                    AuditStatus auditStatus = AuditStatus.CHECKED;
                    if (isAudit !=null && isAudit.equals(NumberUtils.INTEGER_ZERO)){
                    BaseResponse<MaterialCheckResponse> baseResponse =
                            externalProvider.materialCheck(MaterialCheckRequest.builder().
                                    fileName(file.getOriginalFilename())
                                    .build());
                    HttpResponse httpResponse = doPost(baseResponse.getContext().getUrl(), file);
                    if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                        try {
                            String  entity = EntityUtils.toString(httpResponse.getEntity());
                            JSONObject object = JSONObject.parseObject(entity);
                            String code = (String) object.get("status");
                            if (code == null || (code != null && code.equals("0000"))) {
                                auditStatus=AuditStatus.CHECKED;
                            }else {
                                auditStatus=AuditStatus.NOT_PASS;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                 }
            // 上传
            YunUploadResourceResponse response = yunServiceProvider.uploadFile(YunUploadResourceRequest.builder()
                    .cateId(cateId)
                    .resourceType(resourceType)
                    .content(file.getBytes())
                    .resourceName(file.getOriginalFilename())
                    .auditStatus(auditStatus)
                    .build()).getContext();
            SystemResourceVO systemResourceVO = response.getSystemResourceVO();

            if (Objects.nonNull(systemResourceVO)) {
                EsSystemResourceVO esSystemResourceVO = KsBeanUtil.convert(systemResourceVO, EsSystemResourceVO.class);
                EsSystemResourceSaveRequest saveRequest = EsSystemResourceSaveRequest.builder()
                        .systemResourceVOList(Collections.singletonList(esSystemResourceVO))
                        .build();
                //同步es
                esSystemResourceProvider.add(saveRequest);
            }


            resourceUrls.add(response.getResourceUrl());
        } catch (Exception e) {
            throw new SbcRuntimeException(e);
        }
            } else {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
        return ResponseEntity.ok(resourceUrls);
    }
    private static HttpResponse doPost(String url, MultipartFile file) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file",file.getBytes()
                , ContentType.MULTIPART_FORM_DATA,file.getOriginalFilename());
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000).build();
        request.setConfig(requestConfig);
        HttpEntity build = builder.build();
        request.setEntity(build);
        return httpClient.execute(request);
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }

    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", ssf, 443));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     * 商品详情富文本编辑器ueditor需要用到的文件上传方法，返回格式与普通的有区别
     *
     * @param uploadFile
     * @param cateId
     * @return
     */
    @RequestMapping(value = "/uploadImage4UEditor", method = RequestMethod.POST)
    public String uploadFile4UEditor(@RequestParam("uploadFile") MultipartFile uploadFile, Long cateId) {
        //验证上传参数
        if (null == uploadFile || uploadFile.getSize() == 0 || uploadFile.getOriginalFilename() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        String fileName = uploadFile.getOriginalFilename();
        if (ImageUtils.checkImageSuffix(fileName)) {
            try {
                // 上传
                String resourceUrl = yunServiceProvider.uploadFile(YunUploadResourceRequest.builder()
                        .cateId(cateId)
                        .resourceType(ResourceType.IMAGE)
                        .content(uploadFile.getBytes())
                        .resourceName(fileName)
                        .build()).getContext().getResourceUrl();
                return "{original:'" + fileName + "',name:'" + fileName + "',url:'"
                        + resourceUrl + "',size:" + uploadFile.getSize() + ",state:'SUCCESS'}";
            } catch (Exception e) {
                throw new SbcRuntimeException(e);
            }
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }

    /**
     * 查询素材服务器
     *
     * @return 素材服务器
     */
    @ApiOperation(value = "查询素材服务器")
    @RequestMapping(value = "/system/resourceServers", method = RequestMethod.GET)
    public BaseResponse<YunConfigListResponse> page() {
        YunConfigListResponse yunConfigListResponse = yunServiceProvider.list(YunConfigListRequest.builder()
                .configKey(ConfigKey.RESOURCESERVER.toString())
                .delFlag(DeleteFlag.NO)
                .build()).getContext();
        return BaseResponse.success(yunConfigListResponse);
    }

    /**
     * 获取素材服务器详情信息
     *
     * @param resourceServerId 编号
     * @return 配置详情
     */
    @ApiOperation(value = "获取素材服务器详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "resourceServerId", value = "素材编号",
            required = true)
    @RequestMapping(value = "/system/resourceServer/{resourceServerId}", method = RequestMethod.GET)
    public BaseResponse<YunConfigResponse> list(@PathVariable Long resourceServerId) {
        YunConfigResponse yunConfigResponse = yunServiceProvider.getById(YunConfigByIdRequest.builder()
                .id(resourceServerId)
                .build()).getContext();
        return BaseResponse.success(yunConfigResponse);
    }

    /**
     * 编辑素材服务器
     */
    @ApiOperation(value = "编辑素材服务器")
    @RequestMapping(value = "/system/resourceServer", method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody YunConfigModifyRequest request) {
        if (request.getId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        yunServiceProvider.modify(request);
        operateLogMQUtil.convertAndSend("设置", "编辑素材服务器接口", "编辑素材服务器接口");
        return BaseResponse.SUCCESSFUL();
    }

}
