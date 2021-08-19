package com.wanmi.sbc.marketing.provider.impl.grouponcate;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateSortRequest;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.marketing.grouponcate.model.entity.GrouponCateSort;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.grouponcate.GrouponCateSaveProvider;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateAddRequest;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateModifyRequest;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateDelByIdRequest;
import com.wanmi.sbc.marketing.grouponcate.service.GrouponCateService;
import com.wanmi.sbc.marketing.grouponcate.model.root.GrouponCate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>拼团活动信息表保存服务接口实现</p>
 *
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@RestController
@Validated
public class GrouponCateSaveController implements GrouponCateSaveProvider {
    @Autowired
    private GrouponCateService grouponCateService;

    @Override
    public BaseResponse add(@RequestBody @Valid GrouponCateAddRequest grouponCateAddRequest) {
        GrouponCate grouponCate = new GrouponCate();
        KsBeanUtil.copyPropertiesThird(grouponCateAddRequest, grouponCate);
        grouponCateService.add(grouponCate);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modify(@RequestBody @Valid GrouponCateModifyRequest grouponCateModifyRequest) {
        GrouponCate grouponCate = new GrouponCate();
        KsBeanUtil.copyPropertiesThird(grouponCateModifyRequest, grouponCate);
        grouponCateService.modify(grouponCate);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid GrouponCateDelByIdRequest grouponCateDelByIdRequest) {
        GrouponCate grouponCate = new GrouponCate();
        KsBeanUtil.copyPropertiesThird(grouponCateDelByIdRequest, grouponCate);
        grouponCateService.deleteById(grouponCate);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse dragSort(@RequestBody @Valid GrouponCateSortRequest grouponCateSortRequest) {
        if (Objects.isNull(grouponCateSortRequest) || CollectionUtils.isEmpty(grouponCateSortRequest.getGrouponCateSortVOList())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<GrouponCateSort> grouponCateSorts = grouponCateSortRequest.getGrouponCateSortVOList().stream().map(cateSort ->
                grouponCateService.wrapperSortVo(cateSort)).collect(Collectors.toList());
        grouponCateService.dragSort(grouponCateSorts);
        return BaseResponse.SUCCESSFUL();
    }

}

