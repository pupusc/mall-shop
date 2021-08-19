package com.wanmi.sbc.goods.api.provider.pointsgoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsExcelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>积分商品导入操作接口</p>
 * @author yang
 * @since 2019/5/22
 */
@FeignClient(value = "${application.goods.name}", contextId = "PointsGoodsExcelProvider")
public interface PointsGoodsExcelProvider {

    /**
     * 导出积分商品模板
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/export-template")
    BaseResponse<PointsGoodsExcelResponse> exportTemplate();
}
