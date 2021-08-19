package com.wanmi.sbc.goods.api.provider.virtualcoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponCodeByIdRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponCodeListRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponCodePageRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeByIdResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeExcelResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodeListResponse;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponCodePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>券码查询服务Provider</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@FeignClient(value = "${application.goods.name}", contextId = "VirtualCouponCodeQueryProvider")
public interface VirtualCouponCodeQueryProvider {

    /**
     * 分页查询券码API
     *
     * @param virtualCouponCodePageReq 分页请求参数和筛选对象 {@link VirtualCouponCodePageRequest}
     * @return 券码分页列表信息 {@link VirtualCouponCodePageResponse}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcouponcode/page")
    BaseResponse<VirtualCouponCodePageResponse> page(@RequestBody @Valid VirtualCouponCodePageRequest virtualCouponCodePageReq);

    /**
     * 列表查询券码API
     *
     * @param virtualCouponCodeListReq 列表请求参数和筛选对象 {@link VirtualCouponCodeListRequest}
     * @return 券码的列表信息 {@link VirtualCouponCodeListResponse}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcouponcode/list")
    BaseResponse<VirtualCouponCodeListResponse> list(@RequestBody @Valid VirtualCouponCodeListRequest virtualCouponCodeListReq);

    /**
     * 单个查询券码API
     *
     * @param virtualCouponCodeByIdRequest 单个查询券码请求参数 {@link VirtualCouponCodeByIdRequest}
     * @return 券码详情 {@link VirtualCouponCodeByIdResponse}
     * @author 梁善
     */
    @PostMapping("/goods/${application.goods.version}/virtualcouponcode/get-by-id")
    BaseResponse<VirtualCouponCodeByIdResponse> getById(@RequestBody @Valid VirtualCouponCodeByIdRequest virtualCouponCodeByIdRequest);

    /**
     * 获取卡券的可用库存
     *
     * @return 券码库存
     * @author 梁善
     */
    @GetMapping("/goods/${application.goods.version}/virtualcouponcode/stock/{couponId}")
    BaseResponse<Integer> getStockByCouponId(@PathVariable("couponId") Long couponId);

    /**
     * 获取卡券导入摸板
     *
     */
    @PostMapping("/goods/${application.goods.version}/virtualcouponcode/get-code-excel-template")
    BaseResponse<VirtualCouponCodeExcelResponse> virtualCouponCodeExcelExportTemplate(@RequestBody @Valid VirtualCouponCodePageRequest request);
}

