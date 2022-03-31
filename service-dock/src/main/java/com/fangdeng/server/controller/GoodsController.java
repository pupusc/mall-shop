package com.fangdeng.server.controller;

import com.fangdeng.server.dto.GoodsCostPriceDTO;
import com.fangdeng.server.dto.GoodsStockDTO;
import com.fangdeng.server.service.GoodsService;
import com.fangdeng.server.vo.GoodsCostPriceVO;
import com.fangdeng.server.vo.GoodsStockVO;
import com.wanmi.sbc.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/31 1:35 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@RequestMapping(value = "/bk/goods")
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 获取商品库存
     * @param goodsStock
     * @return
     */
    @PostMapping("/get-goods-stock")
    public BaseResponse<List<GoodsStockVO>> getGoodsStock(@RequestBody GoodsStockDTO goodsStock) {
        return BaseResponse.success(goodsService.getGoodsStock(goodsStock.getGoodsIdList()));
    }


    /**
     * 获取商品成本价
     * @param goodsCostPrice
     * @return
     */
    @PostMapping("/get-goods-cost-price")
    public BaseResponse<List<GoodsCostPriceVO>> getGoodsCostPrice(@RequestBody GoodsCostPriceDTO goodsCostPrice) {
        return BaseResponse.success(goodsService.getGoodsCostPrice(goodsCostPrice.getGoodsIdList()));
    }

}
