package com.wanmi.sbc.goods.provider.impl.virtualcoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponCodeQueryProvider;
import com.wanmi.sbc.goods.api.request.virtualcoupon.*;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeByIdResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeExcelResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeListResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodePageResponse;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponCodeVO;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponVO;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCoupon;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCouponCode;
import com.wanmi.sbc.goods.virtualcoupon.service.VirtualCouponCodeService;
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
 * <p>券码查询服务接口实现</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@RestController
@Validated
public class VirtualCouponCodeQueryController implements VirtualCouponCodeQueryProvider {
    @Autowired
    private VirtualCouponCodeService virtualCouponCodeService;

    @Autowired
    private VirtualCouponService virtualCouponService;

    @Override
    public BaseResponse<VirtualCouponCodePageResponse> page(@RequestBody @Valid VirtualCouponCodePageRequest request) {
        VirtualCoupon virtualCoupon = virtualCouponService.getOne(request.getCouponId(), request.getStoreId());
        VirtualCouponCodeQueryRequest queryReq = KsBeanUtil.convert(request, VirtualCouponCodeQueryRequest.class);
        Page<VirtualCouponCode> virtualCouponCodePage = virtualCouponCodeService.page(queryReq);
        Page<VirtualCouponCodeVO> newPage = virtualCouponCodePage.map(entity -> virtualCouponCodeService.wrapperVo(entity));
        MicroServicePage<VirtualCouponCodeVO> microPage = new MicroServicePage<>(newPage, request.getPageable());
        VirtualCouponVO virtualCouponVO = KsBeanUtil.convert(virtualCoupon, VirtualCouponVO.class);

        virtualCouponVO.setAbleNumber(virtualCouponService.getAbleStock(virtualCouponVO.getId()));
        virtualCouponVO.setExpireNumber(virtualCouponService.getExpireStock(virtualCoupon.getId()));
        VirtualCouponCodePageResponse finalRes = VirtualCouponCodePageResponse.builder().virtualCouponCodeVOPage(microPage).virtualCoupon(virtualCouponVO).build();
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<VirtualCouponCodeListResponse> list(@RequestBody @Valid VirtualCouponCodeListRequest virtualCouponCodeListReq) {
        VirtualCouponCodeQueryRequest queryReq = KsBeanUtil.convert(virtualCouponCodeListReq, VirtualCouponCodeQueryRequest.class);
        List<VirtualCouponCode> virtualCouponCodeList = virtualCouponCodeService.list(queryReq);
        List<VirtualCouponCodeVO> newList = virtualCouponCodeList.stream().map(entity -> virtualCouponCodeService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new VirtualCouponCodeListResponse(newList));
    }

    @Override
    public BaseResponse<VirtualCouponCodeByIdResponse> getById(@RequestBody @Valid VirtualCouponCodeByIdRequest virtualCouponCodeByIdRequest) {
        VirtualCouponCode virtualCouponCode = virtualCouponCodeService.getOne(virtualCouponCodeByIdRequest.getId());
        return BaseResponse.success(new VirtualCouponCodeByIdResponse(virtualCouponCodeService.wrapperVo(virtualCouponCode)));
    }

    /**
     * 获取卡券的可用库存
     *
     * @param couponId
     * @return 券码库存
     * @author 梁善
     */
    @Override
    public BaseResponse<Integer> getStockByCouponId(Long couponId) {
        return null;
    }


    /**
     * 获取excel导入模板
     */
    @Override
    public BaseResponse<VirtualCouponCodeExcelResponse> virtualCouponCodeExcelExportTemplate(@RequestBody @Valid VirtualCouponCodePageRequest request) {
        VirtualCoupon virtualCoupon = virtualCouponService.getOne(request.getCouponId(), request.getStoreId());
        return BaseResponse.success(VirtualCouponCodeExcelResponse.builder()
                .file(virtualCouponCodeService.exportTemplate(virtualCoupon.getProvideType(), virtualCoupon.getId())).build());
    }
}

