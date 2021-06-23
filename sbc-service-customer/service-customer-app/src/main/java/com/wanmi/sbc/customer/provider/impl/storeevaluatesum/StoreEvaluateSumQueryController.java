package com.wanmi.sbc.customer.provider.impl.storeevaluatesum;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storeevaluatesum.StoreEvaluateSumQueryProvider;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumPageRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumQueryRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumPageResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumListRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumListResponse;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumByIdRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumByIdResponse;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import com.wanmi.sbc.customer.storeevaluatesum.service.StoreEvaluateSumService;
import com.wanmi.sbc.customer.storeevaluatesum.model.root.StoreEvaluateSum;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺评价查询服务接口实现</p>
 *
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@RestController
@Validated
public class StoreEvaluateSumQueryController implements StoreEvaluateSumQueryProvider {
    @Autowired
    private StoreEvaluateSumService storeEvaluateSumService;

    @Override
    public BaseResponse<StoreEvaluateSumPageResponse> page(@RequestBody @Valid StoreEvaluateSumPageRequest storeEvaluateSumPageReq) {
        StoreEvaluateSumQueryRequest queryReq = new StoreEvaluateSumQueryRequest();
        KsBeanUtil.copyPropertiesThird(storeEvaluateSumPageReq, queryReq);
        Page<StoreEvaluateSum> storeEvaluateSumPage = storeEvaluateSumService.page(queryReq);
        Page<StoreEvaluateSumVO> newPage = storeEvaluateSumPage.map(entity -> storeEvaluateSumService.wrapperVo(entity));
        MicroServicePage<StoreEvaluateSumVO> microPage = new MicroServicePage<>(newPage, storeEvaluateSumPageReq.getPageable());
        StoreEvaluateSumPageResponse finalRes = new StoreEvaluateSumPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<StoreEvaluateSumListResponse> list(@RequestBody @Valid StoreEvaluateSumListRequest storeEvaluateSumListReq) {
        StoreEvaluateSumQueryRequest queryReq = new StoreEvaluateSumQueryRequest();
        KsBeanUtil.copyPropertiesThird(storeEvaluateSumListReq, queryReq);
        List<StoreEvaluateSum> storeEvaluateSumList = storeEvaluateSumService.list(queryReq);
        List<StoreEvaluateSumVO> newList = storeEvaluateSumList.stream().map(entity -> storeEvaluateSumService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new StoreEvaluateSumListResponse(newList));
    }

    @Override
    public BaseResponse<StoreEvaluateSumByIdResponse> getById(@RequestBody @Valid StoreEvaluateSumByIdRequest storeEvaluateSumByIdRequest) {
        StoreEvaluateSum storeEvaluateSum = storeEvaluateSumService.getById(storeEvaluateSumByIdRequest.getSumId());
        return BaseResponse.success(new StoreEvaluateSumByIdResponse(storeEvaluateSumService.wrapperVo(storeEvaluateSum)));
    }

    /**
     * 根据店铺id查询店铺评价信息 30 90 180的
     *
     * @param storeEvaluateSumQueryRequest 单个查询店铺评价请求参数 {@link StoreEvaluateSumByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<StoreEvaluateSumByIdResponse> getByStoreId(@RequestBody StoreEvaluateSumQueryRequest storeEvaluateSumQueryRequest) {
        StoreEvaluateSum storeEvaluateSum = storeEvaluateSumService.getByStoreId(storeEvaluateSumQueryRequest);
        return BaseResponse.success(new StoreEvaluateSumByIdResponse(storeEvaluateSumService.wrapperVo(storeEvaluateSum)));
    }
}

