package com.wanmi.sbc.goods.provider.impl.virtualcoupon;

import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponCodeProvider;
import com.wanmi.sbc.goods.api.request.virtualcoupon.*;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeAddResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeByIdResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeListResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeModifyResponse;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponCodeVO;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCoupon;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCouponCode;
import com.wanmi.sbc.goods.virtualcoupon.service.VirtualCouponCodeService;
import com.wanmi.sbc.goods.virtualcoupon.service.VirtualCouponService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>券码保存服务接口实现</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@RestController
@Validated
public class VirtualCouponCodeController implements VirtualCouponCodeProvider {

    @Autowired
    private VirtualCouponCodeService virtualCouponCodeService;

    @Autowired
    private VirtualCouponService virtualCouponService;

    @Override
    public BaseResponse add(@RequestBody @Valid VirtualCouponCodeAddRequest request) {
        List<VirtualCouponCodeVO> virtualCouponCodes = request.getVirtualCouponCodes();
        virtualCouponCodeService.batchAdd(KsBeanUtil.convertList(virtualCouponCodes, VirtualCouponCode.class), request.getStoreId(), request.getCouponId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid VirtualCouponCodeDelByIdRequest request) {
        virtualCouponService.getOne(request.getCouponId(), request.getStoreId());
        virtualCouponCodeService.deleteById(request.getId(), request.getCouponId(), request.getUserId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid VirtualCouponCodeDelByIdListRequest request) {
        virtualCouponService.getOne(request.getCouponId(), request.getStoreId());
        virtualCouponCodeService.deleteByIdList(request.getIdList(), request.getCouponId(), request.getUpdatePerson());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 发券接口(关联订单)-下单时调用
     *
     * @param request {@link VirtualCouponCodeByIdRequest}
     * @return 券码列表 {@link VirtualCouponCodeByIdResponse}
     * @author 梁善
     */
    @Override
    public BaseResponse<VirtualCouponCodeListResponse> linkOrder(@RequestBody @Valid VirtualCouponCodeLinkOrderRequest request) {
        return BaseResponse.success(virtualCouponCodeService.linkOrder(request.getTid(), request.getCouponIds(), request.getUpdatePerson()));
    }

    /**
     * 发券接口(关联订单)-订单失效时调用
     *
     * @param request {@link VirtualCouponCodeByIdRequest}
     * @return 券码详情 {@link VirtualCouponCodeByIdResponse}
     * @author 梁善
     */
    @Override
    public BaseResponse unlinkOrder(@RequestBody @Valid VirtualCouponCodeUnLinkOrderRequest request) {
        virtualCouponCodeService.unlinkOrder(request.getCodeVOList());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 初始化卡券券码的缓存--导入时调用
     *
     * @param couponId
     */
    @Override
    public BaseResponse initCouponCodeNoCache(Long couponId) {
        virtualCouponCodeService.initCouponNo(couponId);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 处理过期的券码
     *
     * @return
     */
    @Override
    public BaseResponse expireVirtualCouponCode() {
        virtualCouponCodeService.expireVirtualCouponCode();
        return BaseResponse.SUCCESSFUL();
    }
}

