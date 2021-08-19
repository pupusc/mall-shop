package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.systemresource.SystemResourceQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemresource.SystemResourceSaveProvider;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceDelByIdListRequest;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceModifyRequest;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourceMoveRequest;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourcePageRequest;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourceModifyResponse;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourcePageResponse;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 图片服务
 * Created by daiyitian on 17/4/12.
 *
 * 废弃--后期用素材库代替
 *
 */
@Api(tags = "ImageController", description = "图片服务 Api")
@RestController
@RequestMapping("/system")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private SystemResourceQueryProvider systemResourceQueryProvider;

    @Autowired
    private SystemResourceSaveProvider systemResourceSaveProvider;


    /**
     * 分页图片
     *
     * @param pageReq 图片参数
     * @return
     */
    @ApiOperation(value = "分页图片")
    @RequestMapping(value = "/images", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<SystemResourceVO>> page(@RequestBody @Valid SystemResourcePageRequest pageReq) {
        pageReq.setResourceType(ResourceType.IMAGE);
        BaseResponse<SystemResourcePageResponse> response= systemResourceQueryProvider.page(pageReq);
        return BaseResponse.success(response.getContext().getSystemResourceVOPage());

    }


    /**
     * 编辑图片
     */
    @ApiOperation(value = "编辑图片")
    @RequestMapping(value = "/image", method = RequestMethod.PUT)
    public BaseResponse<SystemResourceModifyResponse> modify(@RequestBody @Valid SystemResourceModifyRequest
                                                                     modifyReq) {
        modifyReq.setResourceType(ResourceType.IMAGE);
        modifyReq.setUpdateTime(LocalDateTime.now());
        return systemResourceSaveProvider.modify(modifyReq);

    }

    /**
     * 删除图片
     */
    @ApiOperation(value = "删除图片")
    @RequestMapping(value = "/image", method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody @Valid SystemResourceDelByIdListRequest delByIdListReq) {

        return systemResourceSaveProvider.deleteByIdList(delByIdListReq);
    }

    /**
     * 批量修改图片的分类
     */
    @ApiOperation(value = "批量修改图片的分类")
    @RequestMapping(value = "/image/imageCate", method = RequestMethod.PUT)
    public BaseResponse updateCate(@RequestBody @Valid SystemResourceMoveRequest
                                           moveRequest) {

        if (moveRequest.getCateId() == null || CollectionUtils.isEmpty(moveRequest.getResourceIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return systemResourceSaveProvider.move(moveRequest);
    }
}
