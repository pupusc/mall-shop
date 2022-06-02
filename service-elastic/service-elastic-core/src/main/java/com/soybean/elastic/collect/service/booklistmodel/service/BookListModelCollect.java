package com.soybean.elastic.collect.service.booklistmodel.service;

import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class BookListModelCollect extends AbstractBookListModelCollect {


    @Override
    public Set<Long> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        Set<Long> result = new HashSet<>();

        return result;
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        for (F f : list) {
            EsBookListModel esBookListModel = (EsBookListModel) f;
            esBookListModel.setBookListName("这个是一个书单" + new Random().nextInt());
        }
        return list;
    }
}
