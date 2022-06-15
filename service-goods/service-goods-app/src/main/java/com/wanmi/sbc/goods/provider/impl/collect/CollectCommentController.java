package com.wanmi.sbc.goods.provider.impl.collect;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.collect.CollectCommentProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectCommentProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectCommentRelSpuDetailResp;
import com.wanmi.sbc.goods.api.response.collect.CollectCommentRelSpuResp;
import com.wanmi.sbc.goods.collect.CollectCommentService;
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
public class CollectCommentController implements CollectCommentProvider {

    @Autowired
    private CollectCommentService collectCommentService;


    @Override
    public BaseResponse<List<CollectCommentRelSpuResp>> collectCommentSpuIdByTime(CollectCommentProviderReq req) {
        return BaseResponse.success(collectCommentService.collectCommentSpuIdByTime(req));
    }

    @Override
    public BaseResponse<List<CollectCommentRelSpuDetailResp>> collectCommentBySpuIds(CollectCommentProviderReq req) {
        return BaseResponse.success(collectCommentService.collectCommentBySpuIds(req));
    }
}
