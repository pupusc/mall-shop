package com.wanmi.sbc.goods.api.provider.info;

import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/22 2:05 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@FeignClient(value = "${application.goods.name}", contextId = "VideoChannelSetFilterControllerProvider")
public interface VideoChannelSetFilterControllerProvider {


    /**
     * 根据skuId 获取 spuId 对应的渠道是否是 视频号
     * @param skuIdList
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/goods/channel/filter-goodsid-has-video-channnel-map")
    BaseResponse<Map<String, Boolean>> filterGoodsIdHasVideoChannelMap(@RequestBody List<String> skuIdList);
}
