package com.soybean.elastic.collect.factory.realizer;

import com.alibaba.fastjson.JSON;
import com.soybean.elastic.api.enums.SearchBookListCategoryEnum;
import com.soybean.elastic.api.req.collect.CollectJobReq;
import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.booklistmodel.repository.EsBookListModelRepository;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import com.soybean.elastic.collect.service.booklistmodel.service.BookListModelPublishSpuCollect;
import com.soybean.elastic.redis.RedisConstant;
import com.soybean.elastic.redis.RedisService;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/31 5:45 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class CollectBookListModelFactory extends AbstractCollectFactory {



    @Autowired
    private Map<String, AbstractBookListModelCollect> bookListModelCollectMap;

    @Autowired
    private EsBookListModelRepository esBookListModelRepository;

    @Autowired
    private RedisService redisService;


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


//    @PostConstruct
    public void init(CollectJobReq collectJobReq) {
        log.info("CollectBookListModelFactory collect begin param {}", JSON.toJSONString(collectJobReq));
        long beginTime = System.currentTimeMillis();
        int minSize = 50; //最小采集数量
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (collectJobReq.getStartTime() != null && collectJobReq.getEndTime() != null) {
            startTime = collectJobReq.getStartTime();
            endTime = collectJobReq.getEndTime();
            super.load(bookListModelCollectMap, startTime, endTime, minSize, false);
        } else {
            String lastCollectTimeStr = redisService.getString(RedisConstant.ELASTIC_COLLECT_BOOK_LIST_LAST_TIME_KEY);

            if (!StringUtils.isBlank(lastCollectTimeStr)) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                startTime = LocalDateTime.parse(lastCollectTimeStr, df);
            } else {
                startTime = LocalDateTime.of(2022,01,01,00,00,00,00);
            }
            endTime = LocalDateTime.now();
            super.load(bookListModelCollectMap, startTime, endTime, minSize);
        }
        log.info("CollectBookListModelFactory collect complete cost: {}s", (System.currentTimeMillis() - beginTime));
    }


    @Override
    protected <S> void after(List<S> modelList, LocalDateTime now) {
        if (CollectionUtils.isEmpty(modelList)) {
            return;
        }
        List<EsBookListModel> result = new ArrayList<>();
        for (S s : modelList) {
            EsBookListModel esBookListModel = (EsBookListModel) s;
            if (!Arrays.asList(BusinessTypeEnum.RANKING_LIST.getCode(), BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode(), BusinessTypeEnum.FAMOUS_RECOMMEND.getCode())
                    .contains(esBookListModel.getBookListBusinessType())) {
                continue;
            }
            if (Objects.equals(BusinessTypeEnum.RANKING_LIST.getCode(), esBookListModel.getBookListBusinessType())) {
                esBookListModel.setBookListCategory(SearchBookListCategoryEnum.RANKING_LIST.getCode());
            } else {
                esBookListModel.setBookListCategory(SearchBookListCategoryEnum.BOOK_LIST.getCode());

            }
            esBookListModel.setIndexTime(now);
            result.add(esBookListModel);
        }
        esBookListModelRepository.saveAll(result);
        result.clear(); //帮助gc回收
    }

    @Override
    protected void finalized(LocalDateTime beginTime, LocalDateTime endTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        redisService.setString(RedisConstant.ELASTIC_COLLECT_BOOK_LIST_LAST_TIME_KEY, df.format(endTime));
    }

}
