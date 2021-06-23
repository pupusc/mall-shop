package com.wanmi.sbc.goods.provider.impl.storecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateGoodsRelaQueryProvider;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateGoodsRelaListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateGoodsRelaListByGoodsIdsResponse;
import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.service.StoreCateGoodsRelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/11/1 9:52
 * @version: 1.0
 */
@RestController
@Validated
public class StoreCateGoodsRelaQueryController implements StoreCateGoodsRelaQueryProvider {

    @Autowired
    private StoreCateGoodsRelaService storeCateGoodsRelaService;


    /**
     * 根据商品ID查询
     *
     * @param storeCateGoodsRelaListByGoodsIdsRequest 包含：商品ID {@link StoreCateGoodsRelaListByGoodsIdsRequest }
     * @return  {@link StoreCateGoodsRelaListByGoodsIdsResponse }
     */
    
    @Override
    public BaseResponse<StoreCateGoodsRelaListByGoodsIdsResponse> listByGoodsIds(@RequestBody @Valid StoreCateGoodsRelaListByGoodsIdsRequest storeCateGoodsRelaListByGoodsIdsRequest) {
        List<StoreCateGoodsRela> storeCateGoodsRelaList =  storeCateGoodsRelaService.selectByGoodsId(storeCateGoodsRelaListByGoodsIdsRequest.getGoodsIds());
        if (CollectionUtils.isEmpty(storeCateGoodsRelaList)){
            return BaseResponse.success(new StoreCateGoodsRelaListByGoodsIdsResponse(Collections.emptyList()));
        }
        List<StoreCateGoodsRelaVO> goodsLevelPriceVOList = KsBeanUtil.convert(storeCateGoodsRelaList,StoreCateGoodsRelaVO.class);
        return BaseResponse.success(new StoreCateGoodsRelaListByGoodsIdsResponse(goodsLevelPriceVOList));
    }
}
