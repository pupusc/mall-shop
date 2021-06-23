package com.wanmi.sbc.goods.provider.impl.virtualcoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponQueryProvider;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponByIdRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponListRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponPageRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponQueryRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponByIdResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponListResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponPageResponse;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponVO;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCoupon;
import com.wanmi.sbc.goods.virtualcoupon.service.VirtualCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>卡券查询服务接口实现</p>
 *
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@RestController
@Validated
public class VirtualCouponQueryController implements VirtualCouponQueryProvider {

    @Autowired
    private VirtualCouponService virtualCouponService;

    @Override
    public BaseResponse<VirtualCouponPageResponse> page(@RequestBody @Valid VirtualCouponPageRequest virtualCouponPageReq) {
        VirtualCouponQueryRequest queryReq = KsBeanUtil.convert(virtualCouponPageReq, VirtualCouponQueryRequest.class);
        Page<VirtualCoupon> virtualCouponPage = virtualCouponService.page(queryReq);

        Page<VirtualCouponVO> newPage = virtualCouponPage.map(entity -> {
            VirtualCouponVO virtualCouponVO = virtualCouponService.wrapperVo(entity);
            virtualCouponVO.setAbleNumber(virtualCouponService.getAbleStock(virtualCouponVO.getId()));
            return virtualCouponVO;
        });

        MicroServicePage<VirtualCouponVO> microPage = new MicroServicePage<>(newPage, virtualCouponPageReq.getPageable());
        VirtualCouponPageResponse finalRes = new VirtualCouponPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<VirtualCouponListResponse> list(@RequestBody @Valid VirtualCouponListRequest virtualCouponListReq) {
        VirtualCouponQueryRequest queryReq = KsBeanUtil.convert(virtualCouponListReq, VirtualCouponQueryRequest.class);
        List<VirtualCoupon> virtualCouponList = virtualCouponService.list(queryReq);
        List<VirtualCouponVO> newList = virtualCouponList.stream().map(entity -> virtualCouponService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new VirtualCouponListResponse(newList));
    }

    @Override
    public BaseResponse<VirtualCouponByIdResponse> getById(@RequestBody @Valid VirtualCouponByIdRequest virtualCouponByIdRequest) {
        VirtualCoupon virtualCoupon = virtualCouponService.getOne(virtualCouponByIdRequest.getId(), virtualCouponByIdRequest.getStoreId());
        return BaseResponse.success(new VirtualCouponByIdResponse(virtualCouponService.wrapperVo(virtualCoupon)));
    }

}

