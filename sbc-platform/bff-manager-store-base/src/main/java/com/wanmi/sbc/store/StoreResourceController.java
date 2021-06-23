package com.wanmi.sbc.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ImageUtils;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.fandeng.MaterialCheckRequest;
import com.wanmi.sbc.customer.api.response.fandeng.MaterialCheckResponse;
import com.wanmi.sbc.setting.api.provider.storeresource.StoreResourceQueryProvider;
import com.wanmi.sbc.setting.api.provider.storeresource.StoreResourceSaveProvider;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceDelByIdListRequest;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceModifyRequest;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceMoveRequest;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourcePageRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourcePageResponse;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.setting.bean.enums.AuditStatus;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import sun.misc.BASE64Encoder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 店铺素材服务
 * Created by yinxianzhi on 18/10/18.
 * 完全参考平台素材管理
 */
@Api(tags = "StoreResourceController", description = "店铺素材服务 API")
@RestController
@RequestMapping("/store")
public class StoreResourceController {

    private static final Logger logger = LoggerFactory.getLogger(StoreResourceController.class);

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private StoreResourceSaveProvider storeResourceSaveProvider;

    @Autowired
    private StoreResourceQueryProvider storeResourceQueryProvider;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private ExternalProvider externalProvider;

    /**
     * 分页店铺素材
     *
     * @param pageReq 店铺素材参数
     * @return
     */
    @ApiOperation(value = "分页店铺素材")
    @RequestMapping(value = "/resources", method = RequestMethod.POST)
    public BaseResponse page(@RequestBody @Valid StoreResourcePageRequest pageReq) {
        pageReq.setStoreId(commonUtil.getStoreId());
        BaseResponse<StoreResourcePageResponse> response = storeResourceQueryProvider.page(pageReq);
        return BaseResponse.success(response.getContext().getStoreResourceVOPage());
    }

    /**
     * 上传店铺素材
     *
     * @param multipartFiles
     * @param cateId         分类id
     * @return
     */
    @ApiOperation(value = "上传店铺素材", notes = "resourceType-->0: 图片, 1: 视频,isAudit: 0 需要走审核。不传或者 1 则无需走审核接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "List",
                    name = "uploadFile", value = "上传素材", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Long",
                    name = "cateId", value = "素材分类Id", required = true),
            @ApiImplicitParam(paramType = "query",
                    name = "resourceType", value = "素材类型", required = true),
            @ApiImplicitParam(paramType = "query",
                    name = "isAudit", value = "是否需要审核", required = false)
    })
    @RequestMapping(value = "/uploadStoreResource", method = RequestMethod.POST)
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
                    //默认审核通过
                    Integer auditStatus =1;
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
                                auditStatus=1;
                            }else {
                                auditStatus=2;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       }
                    }
                    // 上传
                    String resourceUrl = yunServiceProvider.uploadFile(YunUploadResourceRequest.builder()
                            .cateId(cateId)
                            .storeId(commonUtil.getStoreId())
                            .companyInfoId(commonUtil.getCompanyInfoId())
                            .resourceType(resourceType)
                            .resourceName(file.getOriginalFilename())
                            .content(file.getBytes())
                            .auditStatus(AuditStatus.fromValue(auditStatus))
                            .build()).getContext().getResourceUrl();
                    resourceUrls.add(resourceUrl);
                } catch (Exception e) {
                    logger.error("uploadStoreResource error: {}", e.getMessage());
                    return ResponseEntity.ok(BaseResponse.FAILED());
                }
            } else {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
        return ResponseEntity.ok(resourceUrls);
    }

    private static HttpResponse doPost(String url,  MultipartFile file) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file",file.getBytes()
                    ,ContentType.MULTIPART_FORM_DATA,file.getOriginalFilename());
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
     * 编辑店铺素材
     */
    @ApiOperation(value = "编辑店铺素材")
    @RequestMapping(value = "/resource", method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody @Valid StoreResourceModifyRequest
                                     modifyReq) {
        modifyReq.setStoreId(commonUtil.getStoreId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return storeResourceSaveProvider.modify(modifyReq);
    }

    /**
     * 删除店铺素材
     */
    @ApiOperation(value = "删除店铺素材")
    @RequestMapping(value = "/resource", method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody @Valid StoreResourceDelByIdListRequest delByIdListReq) {

        delByIdListReq.setStoreId(commonUtil.getStoreId());

        return storeResourceSaveProvider.deleteByIdList(delByIdListReq);
    }


    /**
     * 批量修改店铺素材的分类
     */
    @ApiOperation(value = "批量修改店铺素材的分类")
    @RequestMapping(value = "/resource/resourceCate", method = RequestMethod.PUT)
    public BaseResponse updateCate(@RequestBody @Valid StoreResourceMoveRequest
                                           moveRequest) {
        moveRequest.setStoreId(commonUtil.getStoreId());
        return storeResourceSaveProvider.move(moveRequest);
    }
}
