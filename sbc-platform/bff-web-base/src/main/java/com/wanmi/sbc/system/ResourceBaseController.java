package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.common.enums.ResourceType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
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
 * 素材服务
 * Created by yinxianzhi on 18/10/24.
 */
@Api(tags = "ResourceBaseController", description = "素材服务 API")
@RestController
@RequestMapping("/common")
public class ResourceBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ResourceBaseController.class);

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @ApiOperation(value = "上传素材", notes = "resourceTyp: 0图片，1视频")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "List",
                    name = "uploadFile", value = "上传素材", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Long",
                    name = "cateId", value = "素材分类Id", required = true),
            @ApiImplicitParam(paramType = "query",
                    name = "resourceType", value = "素材类型", required = true)
    })
    @RequestMapping(value = "/uploadResource", method = RequestMethod.POST)
    public BaseResponse<Object> uploadFile(@RequestParam("uploadFile") List<MultipartFile> multipartFiles, Long cateId, ResourceType resourceType) {

        //验证上传参数
        if (CollectionUtils.isEmpty(multipartFiles)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        List<String> resourceUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            if (file == null || file.getSize() == 0 || file.getOriginalFilename() == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            if (file.getOriginalFilename().endsWith("jpg") || file.getOriginalFilename().endsWith("jpeg") ||
                    file.getOriginalFilename().endsWith("png") || file.getOriginalFilename().endsWith("gif") ||
                    file.getOriginalFilename().endsWith("heic")) {
                try {
                    // 上传
                    String resourceUrl = yunServiceProvider.uploadFile(YunUploadResourceRequest.builder()
                            .cateId(cateId)
                            .resourceType(resourceType)
                            .resourceName(file.getOriginalFilename())
                            .content(file.getBytes())
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
