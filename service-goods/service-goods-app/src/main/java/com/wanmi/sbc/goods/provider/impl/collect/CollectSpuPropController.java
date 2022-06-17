package com.wanmi.sbc.goods.provider.impl.collect;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuPropProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuPropProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuPropResp;
import com.wanmi.sbc.goods.bean.vo.CollectSpuVO;
import com.wanmi.sbc.goods.collect.CollectSpuPropService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CollectSpuPropController implements CollectSpuPropProvider {

    @Autowired
    private CollectSpuPropService collectSpuPropService;

    @Override
    public BaseResponse<List<CollectSpuPropResp>> collectSpuIdPropByTime(CollectSpuPropProviderReq req) {
        return BaseResponse.success(collectSpuPropService.collectSpuIdPropByTime(req));
    }

    @Override
    public BaseResponse<List<CollectSpuPropResp>> collectSpuPropBySpuIds(CollectSpuPropProviderReq req) {
        return BaseResponse.success(collectSpuPropService.collectSpuPropBySpuIds(req));
    }


    @Override
    public BaseResponse<List<CollectSpuPropResp>> collectSpuPropByISBN(CollectSpuPropProviderReq req) {
        return BaseResponse.success(collectSpuPropService.collectSpuPropByISBN(req));
    }
}
