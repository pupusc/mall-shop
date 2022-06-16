package com.soybean.elastic.collect.service.spu.service;

import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.model.sub.SubBookNew;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 采集图书信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 2:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookCollect extends AbstractSpuCollect {


    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        Set<String> result = new HashSet<>();
        return result;
    }



    @Override
    public <T> List<T> collect(List<T> list) {
//        for (T t : list) {
//            EsSpuNew esSpuNew = (EsSpuNew) t;
//            SubBookNew subBookNew = new SubBookNew();
//            subBookNew.setBookName("图书信息");
//            esSpuNew.setBook(subBookNew);
//        }
        return list;
    }
}
