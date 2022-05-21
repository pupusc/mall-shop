package com.wanmi.sbc.goods.provider.impl.info;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.info.VideoChannelSetFilterControllerProvider;
import com.wanmi.sbc.goods.info.service.VideoChannelSetFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/22 1:59 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class VideoChannelSetFilterController implements VideoChannelSetFilterControllerProvider {

    @Autowired
    private VideoChannelSetFilterService videoChannelSetFilterService;

    /**
     * 根据skuId获取 视频号渠道对应的商品 goodsId true/false
     * @param skuIdList
     * @return
     */
    @Override
    public BaseResponse<Map<String, Boolean>> filterGoodsIdHasVideoChannelMap(List<String> skuIdList){
        return BaseResponse.success(videoChannelSetFilterService.filterGoodsIdHasVideoChannelMap(skuIdList));
    }
}
