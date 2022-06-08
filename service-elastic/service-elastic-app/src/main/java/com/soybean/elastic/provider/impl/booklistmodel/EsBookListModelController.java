package com.soybean.elastic.provider.impl.booklistmodel;

import com.soybean.elastic.api.provider.booklistmodel.EsBookListModelProvider;
import com.soybean.elastic.api.req.EsKeyWordQueryProviderReq;
import com.soybean.elastic.booklistmodel.service.EsBookListModelService;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    public BaseResponse listKeyWorldEsBookListModel(EsKeyWordQueryProviderReq request) {
        return BaseResponse.success(esBookListModelService.listKeyWorldEsBookListModel(request));
    }
}
