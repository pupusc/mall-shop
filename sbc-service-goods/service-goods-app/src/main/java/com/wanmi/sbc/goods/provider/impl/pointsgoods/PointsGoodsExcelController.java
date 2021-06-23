package com.wanmi.sbc.goods.provider.impl.pointsgoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsExcelProvider;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsExcelResponse;
import com.wanmi.sbc.goods.pointsgoods.service.PointsGoodsExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yang
 * @since 2019/5/22
 */
@Validated
@RestController
public class PointsGoodsExcelController implements PointsGoodsExcelProvider {

    @Autowired
    private PointsGoodsExcelService pointsGoodsExcelService;

    @Override
    public BaseResponse<PointsGoodsExcelResponse> exportTemplate() {
        return BaseResponse.success(PointsGoodsExcelResponse.builder()
                .file(pointsGoodsExcelService.exportTemlate())
                .build());
    }
}
