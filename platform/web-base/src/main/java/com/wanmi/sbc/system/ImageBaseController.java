package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ImageUtils;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.common.enums.ResourceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片服务
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "ImageBaseController", description = "图片服务 API")
@RestController
@RequestMapping("/common")
public class ImageBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ImageBaseController.class);


    @Autowired
    private YunServiceProvider yunServiceProvider;

    @ApiOperation(value = "上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "List",
                    name = "uploadFile", value = "上传图片", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Long",
                    name = "cateId", value = "图片分类Id", required = true)
    })
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public BaseResponse<Object> uploadFile(@RequestParam("uploadFile")List<MultipartFile> multipartFiles, Long cateId) {

        //验证上传参数
        if (org.apache.commons.collections.CollectionUtils.isEmpty(multipartFiles)) {
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
                            .content(file.getBytes())
                            .resourceType(ResourceType.IMAGE)
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
        return BaseResponse.success(resourceUrls);
    }
}
