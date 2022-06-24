package com.soybean.elastic.provider.impl.booklistmodel;

import com.soybean.elastic.api.provider.booklistmodel.EsBookListModelProvider;
import com.soybean.elastic.api.req.EsBookListQueryProviderReq;
import com.soybean.elastic.api.req.EsKeyWordBookListQueryProviderReq;
import com.soybean.elastic.api.req.collect.CollectJobReq;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.booklistmodel.service.EsBookListModelService;
import com.soybean.elastic.collect.factory.realizer.CollectBookListModelFactory;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 2:15 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class EsBookListModelController implements EsBookListModelProvider {

    @Autowired
    private EsBookListModelService esBookListModelService;

    @Autowired
    private CollectBookListModelFactory collectBookListModelFactory;

    @Override
    public BaseResponse<CommonPageResp<List<EsBookListModelResp>>> listKeyWorldEsBookListModel(EsKeyWordBookListQueryProviderReq request) {
        return BaseResponse.success(esBookListModelService.listKeyWorldEsBookListModel(request));
    }


    @Override
    public BaseResponse<CommonPageResp<List<EsBookListModelResp>>> listEsBookListModel(EsBookListQueryProviderReq request) {
        return BaseResponse.success(esBookListModelService.listEsBookListModel(request));
    }

    @Override
    public BaseResponse init(CollectJobReq collectJobReq) {
        collectBookListModelFactory.init(collectJobReq);
        return BaseResponse.SUCCESSFUL();
    }
}
