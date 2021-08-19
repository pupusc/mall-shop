package com.wanmi.sbc.goods.provider.impl.customergoodsevaluatepraise;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseSaveProvider;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.*;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseAddResponse;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseModifyResponse;
import com.wanmi.sbc.goods.customergoodsevaluatepraise.model.root.CustomerGoodsEvaluatePraise;
import com.wanmi.sbc.goods.customergoodsevaluatepraise.service.CustomerGoodsEvaluatePraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>会员商品评价点赞关联表保存服务接口实现</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@RestController
@Validated
public class CustomerGoodsEvaluatePraiseSaveController implements CustomerGoodsEvaluatePraiseSaveProvider {
    @Autowired
    private CustomerGoodsEvaluatePraiseService customerGoodsEvaluatePraiseService;

    @Override
    public BaseResponse<CustomerGoodsEvaluatePraiseAddResponse> add(@RequestBody @Valid CustomerGoodsEvaluatePraiseAddRequest customerGoodsEvaluatePraiseAddRequest) {
        CustomerGoodsEvaluatePraise customerGoodsEvaluatePraise = new CustomerGoodsEvaluatePraise();
        KsBeanUtil.copyPropertiesThird(customerGoodsEvaluatePraiseAddRequest, customerGoodsEvaluatePraise);
        CustomerGoodsEvaluatePraise customerGoodsEvaluatePraise1 = new CustomerGoodsEvaluatePraise();
        //查询是否已存在
        CustomerGoodsEvaluatePraise customerGoodsEvaluatePraise2 =
                customerGoodsEvaluatePraiseService.getCustomerGoodsEvaluatePraise(CustomerGoodsEvaluatePraiseQueryRequest.builder()
                        .customerId(customerGoodsEvaluatePraise.getCustomerId())
                        .goodsEvaluateId(customerGoodsEvaluatePraise.getGoodsEvaluateId()).build());
        //不存在新增，存在取消点赞
        if (Objects.isNull(customerGoodsEvaluatePraise2)) {
            customerGoodsEvaluatePraise1 = customerGoodsEvaluatePraiseService.add(customerGoodsEvaluatePraise);
        } else {
            customerGoodsEvaluatePraiseService.cancel(customerGoodsEvaluatePraise);
            customerGoodsEvaluatePraise1 = null;
        }
        return BaseResponse.success(new CustomerGoodsEvaluatePraiseAddResponse(
                customerGoodsEvaluatePraiseService.wrapperVo(customerGoodsEvaluatePraise1)));
    }

    @Override
    public BaseResponse<CustomerGoodsEvaluatePraiseModifyResponse> modify(@RequestBody @Valid CustomerGoodsEvaluatePraiseModifyRequest customerGoodsEvaluatePraiseModifyRequest) {
        CustomerGoodsEvaluatePraise customerGoodsEvaluatePraise = new CustomerGoodsEvaluatePraise();
        KsBeanUtil.copyPropertiesThird(customerGoodsEvaluatePraiseModifyRequest, customerGoodsEvaluatePraise);
        return BaseResponse.success(new CustomerGoodsEvaluatePraiseModifyResponse(
                customerGoodsEvaluatePraiseService.wrapperVo(customerGoodsEvaluatePraiseService.modify(customerGoodsEvaluatePraise))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid CustomerGoodsEvaluatePraiseDelByIdRequest customerGoodsEvaluatePraiseDelByIdRequest) {
        customerGoodsEvaluatePraiseService.deleteById(customerGoodsEvaluatePraiseDelByIdRequest.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid CustomerGoodsEvaluatePraiseDelByIdListRequest customerGoodsEvaluatePraiseDelByIdListRequest) {
        customerGoodsEvaluatePraiseService.deleteByIdList(customerGoodsEvaluatePraiseDelByIdListRequest.getIdList());
        return BaseResponse.SUCCESSFUL();
    }

}

