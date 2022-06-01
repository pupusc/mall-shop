//package com.soybean.elastic.collect.service.spu.service;
//import com.soybean.elastic.collect.service.spu.model.sub.SubBookNew;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//import com.soybean.elastic.collect.service.spu.model.EsSpuNew;
//import com.soybean.elastic.collect.service.spu.model.sub.SubClassifyNew;
//import com.soybean.elastic.collect.service.booklistmodel.model.EsBookListModel;
//import com.soybean.elastic.collect.service.spu.repository.EsSpuRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Description:
// * Company    : 上海黄豆网络科技有限公司
// * Author     : duanlongshan@dushu365.com
// * Date       : 2022/5/19 2:05 上午
// * Modify     : 修改日期          修改人员        修改说明          JIRA编号
// ********************************************************************/
//@Service
//@Slf4j
//public class EsSpuNewService {
//
//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    @Autowired
//    private EsSpuRepository esSpuRepository;
//
//    @PostConstruct
//    public void initEsGoods() {
//        log.info("-------------init es goods ----------");
//
//        log.info("elasticsearchTemplate >>>>>>>>>> " + elasticsearchTemplate);
//        log.info("esSpuRepository >>>>>>>>>> " + esSpuRepository);
//        List<EsSpuNew> result = new ArrayList<>();
//        EsSpuNew esSpu = new EsSpuNew();
//        esSpu.setSpuId(UUID.randomUUID().toString().replace("-", ""));
//        esSpu.setSpuName("红楼梦");
//        esSpu.setSpuSubName("石头记");
//        esSpu.setSpuCategory(1);
//        esSpu.setSpuChannels(Arrays.asList(1,2,3));
////        esSpu.setSpuAuditType(1);
//        esSpu.setCommentNum(10L);
//        esSpu.setFavorCommentNum(8L);
//        esSpu.setSalesNum(15L);
//        esSpu.setSalesPrice(new BigDecimal("53.44"));
//        esSpu.setAddedFlag(1);
//        esSpu.setAddedTime(LocalDateTime.now());
//        esSpu.setCreateTime(LocalDateTime.now());
//        esSpu.setIndexTime(LocalDateTime.now());
//        esSpu.setDelFlag(0);
//
//        List<SubClassifyNew> classifyNewList = new ArrayList<>();
//        SubClassifyNew classifyNew = new SubClassifyNew();
////        classifyNew.setId(123L);
//        classifyNew.setClassifyName("四大名著");
//        classifyNewList.add(classifyNew);
//
//        classifyNew = new SubClassifyNew();
////        classifyNew.setId(1234L);
//        classifyNew.setClassifyName("国学");
//        classifyNewList.add(classifyNew);
//
//        classifyNew = new SubClassifyNew();
////        classifyNew.setId(1235L);
//        classifyNew.setClassifyName("国学");
//        classifyNewList.add(classifyNew);
//
//
//        esSpu.setClassifys(classifyNewList);
//
//
////        esSpu.setClassifySeconds(Lists.newArrayList());
//
//        List<EsBookListModel> bookListModelNews = new ArrayList<>();
//        EsBookListModel bookListModelNew = new EsBookListModel();
//        bookListModelNew.setBookListName("国学");
//        bookListModelNew.setBookListCategory(1);
//        bookListModelNew.setBookListId(2L);
//        bookListModelNews.add(bookListModelNew);
//
//        bookListModelNew = new EsBookListModel();
//        bookListModelNew.setBookListName("推荐");
//        bookListModelNew.setBookListCategory(2);
//        bookListModelNew.setBookListId(3L);
//        bookListModelNews.add(bookListModelNew);
//
//        bookListModelNew = new EsBookListModel();
//        bookListModelNew.setBookListName("必看书籍");
//        bookListModelNew.setBookListCategory(2);
//        bookListModelNew.setBookListId(3L);
//        bookListModelNews.add(bookListModelNew);
//
//        bookListModelNews.add(bookListModelNew);
//
//        SubBookNew subBookNew = new SubBookNew();
//        subBookNew.setIsbn("123123");
//        subBookNew.setBookName("红楼梦");
//        subBookNew.setBookOriginName("红楼学");
//        subBookNew.setBriefIntroduce("红楼梦中阿斯顿发阿迪舒服阿斯顿发送到瓦尔克尔拉屎的");
//        subBookNew.setAuthorNames(Arrays.asList("高鹗", "曹雪芹"));
//        subBookNew.setPublisher("中国人民出版社");
//        subBookNew.setProducer("中国中信出版社");
//        subBookNew.setClumpName("精装");
////        subBookNew.setAwardNames(Arrays.asList("一等奖", "二等奖"));
//        subBookNew.setGroupName("");
//        subBookNew.setSeriesName("");
//        subBookNew.setBindingName("");
////        subBookNew.setTagNames(Lists.newArrayList());
////        subBookNew.setTagSecondNames(Lists.newArrayList());
//
//        result.add(esSpu);
//        esSpuRepository.saveAll(result);
//    }
//
//}
