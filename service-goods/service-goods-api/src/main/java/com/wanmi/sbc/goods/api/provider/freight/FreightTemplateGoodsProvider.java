package com.wanmi.sbc.goods.api.provider.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.PageRequestParam;
import com.wanmi.sbc.goods.api.request.freight.*;
import com.wanmi.sbc.goods.api.request.supplier.SecondLevelSupplierCreateUpdateRequest;
import com.wanmi.sbc.goods.bean.vo.ExpressNotSupportVo;
import com.wanmi.sbc.goods.bean.vo.SupplierSecondVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>对单品运费模板操作接口</p>
 * Created by daiyitian on 2018-10-31-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "FreightTemplateGoodsProvider")
public interface FreightTemplateGoodsProvider {

    /**
     * 新增/更新单品运费模板
     *
     * @param request 保存单品运费模板数据结构 {@link FreightTemplateGoodsSaveRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/save")
    BaseResponse save(@RequestBody @Valid FreightTemplateGoodsSaveRequest request);

    /**
     * 保存或更新不支持配送地区
     */
    @PostMapping("/goods/${application.goods.version}/not-support-area/save-update")
    BaseResponse saveOrUpdateNotSupportArea(@RequestBody ExpressNotSupportCreateUpdateRequest request);

    /**
     * 导入更新不支持配送地区
     */
    @PostMapping("/goods/${application.goods.version}/not-support-area/import")
    BaseResponse<String> importNotSupportArea(@RequestParam("areas") String areas, @RequestParam("supplierId") Long supplierId);

    /**
     * 删除不支持配送地区
     */
    @PostMapping("/goods/${application.goods.version}/not-support-area/delete")
    BaseResponse deleteNotSupportArea(@RequestParam("id") Long id);

    /**
     * 查询不支持配送地区
     */
    @PostMapping("/goods/${application.goods.version}/not-support-area/find")
    BaseResponse<ExpressNotSupportVo> findNotSupportArea(@RequestParam("id") Long id);

    /**
     * 创建或更新二级供应商
     */
    @PostMapping("/goods/${application.goods.version}/second-level-supplier/save-update")
    BaseResponse saveOrUpdateSecondLevelSupplier(@RequestBody SecondLevelSupplierCreateUpdateRequest request);

    /**
     * 查询二级供应商
     */
    @PostMapping("/goods/${application.goods.version}/second-level-supplier/find")
    BaseResponse<MicroServicePage<SupplierSecondVo>> findSecondLevelSupplier(@RequestBody PageRequestParam pageRequestParam);

    /**
     * 删除二级供应商
     */
    @PostMapping("/goods/${application.goods.version}/second-level-supplier/delete")
    BaseResponse deleteSecondLevelSupplier(@RequestParam("id") Long id);

    /**
     * 根据单品运费模板id和店铺id删除单品运费模板
     *
     * @param request 删除单品运费模板数据结构 {@link FreightTemplateGoodsDeleteByIdAndStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/delete-by-id-and-store-id")
    BaseResponse deleteByIdAndStoreId(@RequestBody @Valid FreightTemplateGoodsDeleteByIdAndStoreIdRequest request);

    /**
     * 根据单品运费模板id和店铺id复制单品运费模板
     *
     * @param request 复制单品运费模板数据结构 {@link FreightTemplateGoodsCopyByIdAndStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/copy-by-id-and-store-id")
    BaseResponse copyByIdAndStoreId(@RequestBody @Valid FreightTemplateGoodsCopyByIdAndStoreIdRequest request);

    /**
     * 初始单品运费模板
     *
     * @param request 初始单品运费模板数据结构 {@link FreightTemplateGoodsInitByStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/init-by-store-id")
    BaseResponse initByStoreId(@RequestBody @Valid FreightTemplateGoodsInitByStoreIdRequest request);

    /**
     * 更新运费模版
     *
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight49/goods/changeFreeDelivery49")
    BaseResponse changeFreeDelivery49(@RequestBody @Valid FreightTemplate49ChangeReq request);
}
