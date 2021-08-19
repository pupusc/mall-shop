package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ImageUtils;
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
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺图片服务
 * Created by bail on 17/11/20.
 * 完全参考平台图片管理
 */
@Api(tags = "StoreImageController", description = "店铺图片服务 API")
@RestController
@RequestMapping("/store")
public class StoreImageController {

    private static final Logger logger = LoggerFactory.getLogger(StoreImageController.class);

    @Autowired
    private CommonUtil commonUtil;


    @Autowired
    private StoreResourceSaveProvider storeResourceSaveProvider;

    @Autowired
    private StoreResourceQueryProvider storeResourceQueryProvider;


    @Autowired
    private YunServiceProvider yunServiceProvider;


    /**
     * 分页店铺图片
     * @param pageReq 店铺图片参数
     * @return
     */
    @ApiOperation(value = "分页店铺图片")
    @RequestMapping(value = "/images", method = RequestMethod.POST)
    public BaseResponse page(@RequestBody @Valid StoreResourcePageRequest pageReq) {
        pageReq.setResourceType(ResourceType.IMAGE);
        pageReq.setStoreId(commonUtil.getStoreId());
        BaseResponse<StoreResourcePageResponse> response = storeResourceQueryProvider.page(pageReq);
        return BaseResponse.success(response.getContext().getStoreResourceVOPage());
    }

    /**
     * 上传店铺图片
     * @param multipartFiles
     * @param cateId         分类id
     * @return
     */
    @ApiOperation(value = "上传店铺图片")
    @RequestMapping(value = "/uploadStoreImage", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadFile(@RequestParam("uploadFile") List<MultipartFile> multipartFiles, Long cateId) {
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
                            .storeId(commonUtil.getStoreId())
                            .companyInfoId(commonUtil.getCompanyInfoId())
                            .resourceType(ResourceType.IMAGE)
                            .resourceName(file.getOriginalFilename())
                            .content(file.getBytes())
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

    /**
     * 编辑店铺图片
     */
    @ApiOperation(value = "编辑店铺图片")
    @RequestMapping(value = "/image", method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody StoreResourceModifyRequest
                                     modifyReq) {
        modifyReq.setStoreId(commonUtil.getStoreId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return storeResourceSaveProvider.modify(modifyReq);
    }

    /**
     * 删除店铺图片
     */
    @ApiOperation(value = "删除店铺图片")
    @RequestMapping(value = "/image", method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody StoreResourceDelByIdListRequest delByIdListReq) {

        delByIdListReq.setStoreId(commonUtil.getStoreId());

        return storeResourceSaveProvider.deleteByIdList(delByIdListReq);
    }

    /**
     * 批量修改店铺图片的分类
     */
    @ApiOperation(value = "批量修改店铺图片的分类")
    @RequestMapping(value = "/image/imageCate", method = RequestMethod.PUT)
    public BaseResponse updateCate(@RequestBody StoreResourceMoveRequest
                                           moveRequest) {
        moveRequest.setStoreId(commonUtil.getStoreId());
        return storeResourceSaveProvider.move(moveRequest);
    }
}
