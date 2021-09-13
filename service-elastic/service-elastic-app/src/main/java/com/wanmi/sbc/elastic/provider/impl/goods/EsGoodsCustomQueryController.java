package com.wanmi.sbc.elastic.provider.impl.goods;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.goods.service.EsGoodsCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/13 3:53 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@Validated
public class EsGoodsCustomQueryController implements EsGoodsCustomQueryProvider {

    @Autowired
    private EsGoodsCustomService esGoodsCustomService;

    @Override
    public MicroServicePage<EsGoodsVO> listEsGoodsNormal(EsGoodsCustomProviderRequest request) {
        return esGoodsCustomService.listEsGoodsNormal(request);
    }
    
}
