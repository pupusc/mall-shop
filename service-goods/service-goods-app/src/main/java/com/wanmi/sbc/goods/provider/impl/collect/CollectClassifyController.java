package com.wanmi.sbc.goods.provider.impl.collect;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.collect.CollectClassifyProvider;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectClassifyProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuDetailResp;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuResp;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import com.wanmi.sbc.goods.collect.CollectClassifyRelSpuService;
import com.wanmi.sbc.goods.collect.CollectClassifyService;
import com.wanmi.sbc.goods.collect.CollectSpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/4 1:14 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@Slf4j
public class CollectClassifyController implements CollectClassifyProvider {

    @Autowired
    private CollectClassifyService collectClassifyService;

    @Autowired
    private CollectClassifyRelSpuService collectClassifyRelSpuService;


    @Override
    public BaseResponse<List<CollectClassifyRelSpuResp>> collectClassifySpuIdByTime(CollectClassifyProviderReq req) {
        return BaseResponse.success(collectClassifyService.collectClassifySpuIdByTime(req));
    }

    @Override
    public BaseResponse<List<CollectClassifyRelSpuDetailResp>> collectClassifyBySpuIds(CollectClassifyProviderReq req) {
        return BaseResponse.success(collectClassifyService.collectClassifyBySpuIds(req));
    }

    @Override
    public BaseResponse<List<CollectClassifyRelSpuResp>> collectClassifySpuIdRelByTime(CollectClassifyProviderReq req) {
        return BaseResponse.success(collectClassifyRelSpuService.collectClassifySpuIdByTime(req));
    }
}
