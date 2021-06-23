package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ImageUtils;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunConfigByIdRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunConfigListRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunConfigModifyRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.api.response.yunservice.YunConfigListResponse;
import com.wanmi.sbc.setting.api.response.yunservice.YunConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片服务
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "ImageServerController", description = "图片服务")
@RestController
public class ImageServerController {


    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @ApiOperation(value = "上传图片")
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadFile(@RequestParam("uploadFile")List<MultipartFile> multipartFiles, Long
            cateId) {

        //验证上传参数
        if (CollectionUtils.isEmpty(multipartFiles)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<String> resourceUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            if (file == null || file.getSize() == 0 || file.getOriginalFilename() == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            if (ImageUtils.checkImageSuffix(file.getOriginalFilename())) {
                try {
                    // 上传
                    String resourceUrl = yunServiceProvider.uploadFile(YunUploadResourceRequest.builder()
                            .cateId(cateId)
                            .resourceType(ResourceType.IMAGE)
                            .content(file.getBytes())
                            .resourceName(file.getOriginalFilename())
                            .build()).getContext().getResourceUrl();
                    resourceUrls.add(resourceUrl);
                } catch (Exception e) {
                    throw new SbcRuntimeException(e);
                }
            } else {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
        return ResponseEntity.ok(resourceUrls);
    }

    /**
     * 查询图片服务器
     *
     * @return 图片服务器
     */
    @ApiOperation(value = "查询图片服务器")
    @RequestMapping(value = "/system/imageServers", method = RequestMethod.GET)
    public BaseResponse<YunConfigListResponse> page() {
        YunConfigListResponse yunConfigListResponse = yunServiceProvider.list(YunConfigListRequest.builder()
                .configKey(ConfigKey.RESOURCESERVER.toString())
                .delFlag(DeleteFlag.NO)
                .build()).getContext();
        return BaseResponse.success(yunConfigListResponse);
    }

    /**
     * 获取图片服务器详情信息
     *
     * @param imageServerId 图片编号
     * @return 商品详情
     */
    @ApiOperation(value = "获取图片服务器详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "imageServerId", value = "图片服务器编号",
            required = true)
    @RequestMapping(value = "/system/imageServer/{imageServerId}", method = RequestMethod.GET)
    public BaseResponse<YunConfigResponse> list(@PathVariable Long imageServerId) {
        YunConfigResponse yunConfigResponse = yunServiceProvider.getById(YunConfigByIdRequest.builder()
                .id(imageServerId)
                .build()).getContext();
        return BaseResponse.success(yunConfigResponse);
    }

    /**
     * 编辑图片服务器
     */
    @ApiOperation(value = "编辑图片服务器")
    @RequestMapping(value = "/system/imageServer", method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody YunConfigModifyRequest request) {
        if (request.getId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        yunServiceProvider.modify(request);
        operateLogMQUtil.convertAndSend("设置", "编辑素材服务器接口", "编辑素材服务器接口");
        return BaseResponse.SUCCESSFUL();
    }

}
