package com.wanmi.sbc.goods.provider.impl.hotgoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.hotgoods.HotGoodsProvider;
import com.wanmi.sbc.goods.api.request.goods.HotGoodsTypeRequest;
import com.wanmi.sbc.goods.bean.dto.HotGoodsDto;
import com.wanmi.sbc.goods.hotgoods.service.HotGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * com.wanmi.sbc.goods.provider.impl.goods.GoodsController
 *
 * @author lipeng
 * @dateTime 2018/11/7 下午3:20
 */
@RestController
@Validated
@Slf4j
public class HotGoodsController implements HotGoodsProvider {

    @Autowired
    private HotGoodsService hotGoodsService;

    @Override
    public BaseResponse<Boolean> updateSort() {
        Integer count =  hotGoodsService.updateSort();
        return BaseResponse.success(count > 0);
    }

    @Override
    public BaseResponse<List<HotGoodsDto>> selectAllBySort() {
        List<HotGoodsDto> hotGoodsDtos =  hotGoodsService.selectAllBySort();
        return BaseResponse.success(hotGoodsDtos);

    }

    @Override
    public BaseResponse<List<HotGoodsDto>> selectAllByTypes(HotGoodsTypeRequest hotGoodsTypeRequest) {
        List<HotGoodsDto> hotGoodsDtos =  hotGoodsService.selectAllByTypes(hotGoodsTypeRequest);
        return BaseResponse.success(hotGoodsDtos);
    }
}
