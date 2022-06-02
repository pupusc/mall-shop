package com.soybean.elastic.collect.service.booklistmodel.service;

import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.booklistmodel.model.sub.EsBookListSubSpuNew;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 10:44 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookListModelSpuCollect extends AbstractBookListModelCollect {


    @Override
    public Set<Long> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        Set<Long> result = new HashSet<>();
        result.add(new Random().nextLong());
        return result;
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        for (F f : list) {
            EsBookListModel esBookListModel = (EsBookListModel) f;
            EsBookListSubSpuNew esBookListSubSpuNew = new EsBookListSubSpuNew();
            esBookListSubSpuNew.setSpuId(UUID.randomUUID().toString());
            esBookListModel.setSpus(Collections.singletonList(esBookListSubSpuNew));
        }
        return list;
    }
}
