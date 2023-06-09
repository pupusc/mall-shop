package com.soybean.elastic.provider.impl.spu;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.req.EsSpuNewQueryProviderReq;
import com.soybean.elastic.api.req.collect.CollectJobReq;
import com.soybean.elastic.api.resp.EsSpuNewAggResp;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.elastic.collect.factory.realizer.CollectSpuFactory;
import com.soybean.elastic.spu.service.EsNormalSpuNewService;
import com.soybean.elastic.spu.service.EsSpuNewService;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 5:11 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class EsSpuNewController implements EsSpuNewProvider {


    @Autowired
    private EsSpuNewService esSpuNewService;

    @Autowired
    private CollectSpuFactory collectSpuFactory;

    @Autowired
    private EsNormalSpuNewService esNormalSpuNewService;

    @Override
    public BaseResponse<EsSpuNewAggResp<List<EsSpuNewResp>>> listKeyWorldEsSpu(EsKeyWordSpuNewQueryProviderReq req) {
        return BaseResponse.success(esSpuNewService.listKeyWorldEsSpu(req));
    }

    @Override
    public BaseResponse<EsSpuNewAggResp<List<EsSpuNewResp>>> listKeyWorldEsSpuBySpuId(EsKeyWordSpuNewQueryProviderReq req) {
        return BaseResponse.success(esSpuNewService.listKeyWorldEsSpuId(req));
    }

    @Override
    public BaseResponse init(CollectJobReq collectJobReq) {
        collectSpuFactory.init(collectJobReq);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<CommonPageResp<List<EsSpuNewResp>>> listNormalEsSpuNew(EsSpuNewQueryProviderReq req) {
        return BaseResponse.success(esNormalSpuNewService.listNormalEsSpuNew(req));
    }
}
