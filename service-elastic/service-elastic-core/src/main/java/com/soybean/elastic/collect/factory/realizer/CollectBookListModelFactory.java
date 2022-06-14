package com.soybean.elastic.collect.factory.realizer;

import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.booklistmodel.repository.EsBookListModelRepository;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import com.soybean.elastic.collect.service.booklistmodel.service.BookListModelPublishSpuCollect;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/31 5:45 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectBookListModelFactory extends AbstractCollectFactory {



    @Autowired
    private Map<String, AbstractBookListModelCollect> bookListModelCollectMap;

    @Autowired
    private EsBookListModelRepository esBookListModelRepository;


    @Override
    protected EsBookListModel packModelId(Object k) {
        EsBookListModel esBookListModel = new EsBookListModel();
        esBookListModel.setBookListId((long)k);
        return esBookListModel;
    }


    @Override
    protected Class<?> firstLoadCls() {
        return BookListModelPublishSpuCollect.class;
    }


    @PostConstruct
    public void init() {
        LocalDateTime now = LocalDateTime.now();
        int minSize = 50;
        LocalDateTime lastCollectTime = LocalDateTime.of(2021,12,12,12,12,12,12);
//        super.load(bookListModelCollectMap, lastCollectTime, now, minSize);
    }


    @Override
    protected <S> void after(List<S> modelList, LocalDateTime now) {
        if (CollectionUtils.isEmpty(modelList)) {
            return;
        }
        List<EsBookListModel> result = new ArrayList<>();
        for (S s : modelList) {
            EsBookListModel esBookListModel = (EsBookListModel) s;
//            esBookListModel.setBookListId(12313123L);
//            esBookListModel.setBookListName("红楼梦；四大名著");
            if (!Arrays.asList(BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.RANKING_LIST.getCode())
                    .contains(esBookListModel.getBookListBusinessType())) {
                continue;
            }
            esBookListModel.setIndexTime(now);
            result.add(esBookListModel);
        }
        esBookListModelRepository.saveAll(result);
        result.clear(); //帮助gc回收
    }

}
