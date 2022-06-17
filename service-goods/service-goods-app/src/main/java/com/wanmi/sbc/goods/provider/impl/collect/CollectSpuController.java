package com.wanmi.sbc.goods.provider.impl.collect;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuImageProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuImageResp;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import com.wanmi.sbc.goods.collect.CollectSpuImageService;
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
public class CollectSpuController implements CollectSpuProvider {

    @Autowired
    private CollectSpuService collectSpuService;
    @Autowired
    private CollectSpuImageService collectSpuImageService;


    @Override
    public BaseResponse<List<CollectSpuVO>> collectSpuIdByTime(@RequestBody CollectSpuProviderReq req) {
        return BaseResponse.success(collectSpuService.collectSpuIdByTime(req));
    }


    @Override
    public BaseResponse<List<CollectSpuVO>> collectSpuBySpuIds(@RequestBody CollectSpuProviderReq req) {
        return BaseResponse.success(collectSpuService.collectSpuBySpuIds(req));
    }

    @Override
    public BaseResponse<List<CollectSpuImageResp>> collectSpuIdImageByTime(@RequestBody CollectSpuImageProviderReq req) {
        return BaseResponse.success(collectSpuImageService.collectSpuIdImageByTime(req));
    }
}
