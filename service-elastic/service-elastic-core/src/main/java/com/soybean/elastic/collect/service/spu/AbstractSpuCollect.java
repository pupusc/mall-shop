package com.soybean.elastic.collect.service.spu;



import com.soybean.elastic.collect.service.AbstractCollect;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaBookResp;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.provider.collect.CollectMetaBookProvider;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuPropProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuPropProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuPropResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 2:51 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
public abstract class AbstractSpuCollect extends AbstractCollect {


    @Autowired
    protected CollectSpuPropProvider collectSpuPropProvider;


    /**
     * 采集商品id信息
     * @return
     */
    protected Set<String> collectSpuPropByIsbn(CollectMetaResp collectMetaResp) {
        List<String> isbns = collectMetaResp.getMetaBookResps().stream().map(CollectMetaBookResp::getIsbn).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(isbns)) {
            return new HashSet<>();
        }
        CollectSpuPropProviderReq collectSpuPropProviderReq = new CollectSpuPropProviderReq();
        collectSpuPropProviderReq.setIsbn(isbns);
        List<CollectSpuPropResp> context = collectSpuPropProvider.collectSpuPropByISBN(collectSpuPropProviderReq).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return new HashSet<>();
        }
        return context.stream().map(CollectSpuPropResp::getSpuId).collect(Collectors.toSet());
    }


}
