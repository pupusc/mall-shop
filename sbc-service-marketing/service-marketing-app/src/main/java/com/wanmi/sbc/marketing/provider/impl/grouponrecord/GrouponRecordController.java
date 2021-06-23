package com.wanmi.sbc.marketing.provider.impl.grouponrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.grouponrecord.GrouponRecordProvider;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordDecrBuyNumRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordIncrBuyNumRequest;
import com.wanmi.sbc.marketing.grouponrecord.model.entity.IncrBuyNumEntity;
import com.wanmi.sbc.marketing.grouponrecord.service.GrouponRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>拼团记录</p>
 *
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@RestController
@Validated
public class GrouponRecordController implements GrouponRecordProvider {
    @Autowired
    private GrouponRecordService grouponRecordService;

    @Override
    public BaseResponse incrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(@RequestBody @Valid GrouponRecordIncrBuyNumRequest request){

        grouponRecordService.incrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(KsBeanUtil.convert(request, IncrBuyNumEntity.class));
        return BaseResponse.SUCCESSFUL();

    }

    @Override
    public BaseResponse decrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(@RequestBody @Valid GrouponRecordDecrBuyNumRequest
                                                                                         request){
        Integer result =  grouponRecordService.decrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(request.getGrouponActivityId(),request.getCustomerId(),
                request.getGoodsInfoId(),request.getBuyNum());
        return BaseResponse.success(result);
    }

}

