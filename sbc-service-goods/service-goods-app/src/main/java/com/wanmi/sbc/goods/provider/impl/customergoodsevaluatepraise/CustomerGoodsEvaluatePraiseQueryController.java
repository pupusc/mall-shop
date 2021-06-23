package com.wanmi.sbc.goods.provider.impl.customergoodsevaluatepraise;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseQueryProvider;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseByIdRequest;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseListRequest;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraisePageRequest;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseQueryRequest;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseByIdResponse;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseListResponse;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraisePageResponse;
import com.wanmi.sbc.goods.bean.vo.CustomerGoodsEvaluatePraiseVO;
import com.wanmi.sbc.goods.customergoodsevaluatepraise.model.root.CustomerGoodsEvaluatePraise;
import com.wanmi.sbc.goods.customergoodsevaluatepraise.service.CustomerGoodsEvaluatePraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员商品评价点赞关联表查询服务接口实现</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@RestController
@Validated
public class CustomerGoodsEvaluatePraiseQueryController implements CustomerGoodsEvaluatePraiseQueryProvider {
    @Autowired
    private CustomerGoodsEvaluatePraiseService customerGoodsEvaluatePraiseService;

    @Override
    public BaseResponse<CustomerGoodsEvaluatePraisePageResponse> page(@RequestBody @Valid CustomerGoodsEvaluatePraisePageRequest customerGoodsEvaluatePraisePageReq) {
        CustomerGoodsEvaluatePraiseQueryRequest queryReq = new CustomerGoodsEvaluatePraiseQueryRequest();
        KsBeanUtil.copyPropertiesThird(customerGoodsEvaluatePraisePageReq, queryReq);
        Page<CustomerGoodsEvaluatePraise> customerGoodsEvaluatePraisePage = customerGoodsEvaluatePraiseService.page(queryReq);
        Page<CustomerGoodsEvaluatePraiseVO> newPage = customerGoodsEvaluatePraisePage.map(entity -> customerGoodsEvaluatePraiseService.wrapperVo(entity));
        MicroServicePage<CustomerGoodsEvaluatePraiseVO> microPage = new MicroServicePage<>(newPage, customerGoodsEvaluatePraisePageReq.getPageable());
        CustomerGoodsEvaluatePraisePageResponse finalRes = new CustomerGoodsEvaluatePraisePageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<CustomerGoodsEvaluatePraiseListResponse> list(@RequestBody @Valid CustomerGoodsEvaluatePraiseListRequest customerGoodsEvaluatePraiseListReq) {
        CustomerGoodsEvaluatePraiseQueryRequest queryReq = new CustomerGoodsEvaluatePraiseQueryRequest();
        KsBeanUtil.copyPropertiesThird(customerGoodsEvaluatePraiseListReq, queryReq);
        List<CustomerGoodsEvaluatePraise> customerGoodsEvaluatePraiseList = customerGoodsEvaluatePraiseService.list(queryReq);
        List<CustomerGoodsEvaluatePraiseVO> newList = customerGoodsEvaluatePraiseList.stream().map(entity -> customerGoodsEvaluatePraiseService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new CustomerGoodsEvaluatePraiseListResponse(newList));
    }

    @Override
    public BaseResponse<CustomerGoodsEvaluatePraiseByIdResponse> getById(@RequestBody @Valid CustomerGoodsEvaluatePraiseByIdRequest customerGoodsEvaluatePraiseByIdRequest) {
        CustomerGoodsEvaluatePraise customerGoodsEvaluatePraise = customerGoodsEvaluatePraiseService.getById(customerGoodsEvaluatePraiseByIdRequest.getId());
        return BaseResponse.success(new CustomerGoodsEvaluatePraiseByIdResponse(customerGoodsEvaluatePraiseService.wrapperVo(customerGoodsEvaluatePraise)));
    }

    @Override
    public BaseResponse<CustomerGoodsEvaluatePraiseByIdResponse> getCustomerGoodsEvaluatePraise(@RequestBody CustomerGoodsEvaluatePraiseQueryRequest customerGoodsEvaluatePraiseQueryRequest) {
        CustomerGoodsEvaluatePraise customerGoodsEvaluatePraise = customerGoodsEvaluatePraiseService.getCustomerGoodsEvaluatePraise(customerGoodsEvaluatePraiseQueryRequest);
        return BaseResponse.success(new CustomerGoodsEvaluatePraiseByIdResponse(customerGoodsEvaluatePraiseService.wrapperVo(customerGoodsEvaluatePraise)));
    }

}

