package com.soybean.mall.miniapp.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsDataUpdateRequest;
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
 * Date       : 2022/6/25 1:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/load/goodsdata")
@RestController
public class LoadGoodsDataController {

    @Autowired
    private GoodsProvider goodsProvider;

    @PostMapping("/update-goods-by-condition")
    public BaseResponse updateGoodsUnBackGroundImage(@RequestBody List<GoodsDataUpdateRequest> goodsDetas) {
        goodsProvider.updateGoodsByCondition(goodsDetas);
        return BaseResponse.SUCCESSFUL();
    }
}
