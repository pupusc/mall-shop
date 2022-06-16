package com.wanmi.sbc.bookmeta.service.collect;
import java.math.BigDecimal;
import com.google.common.collect.Lists;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaBookResp;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookClump;
import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookGroup;
import com.wanmi.sbc.bookmeta.entity.MetaDataDict;
import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import com.wanmi.sbc.bookmeta.enums.BookContentTypeEnum;
import com.wanmi.sbc.bookmeta.enums.DataDictCateEnum;
import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 采集图书
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaBookService extends AbstractCollectBookService{

    /**
     * 采集图书
     * @param collectMetaReq
     */
    public CollectMetaResp collectMetaBookByTime(CollectMetaReq collectMetaReq){
        CollectMetaResp collectMetaResp = new CollectMetaResp();
        List<MetaBook> metaBooks =
                metaBookMapper.collectMetaBookByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (CollectionUtils.isEmpty(metaBooks)) {
            return collectMetaResp;
        }
        collectMetaResp.setLastBizId(metaBooks.get(metaBooks.size() -1).getId());
        collectMetaResp.setHasMore(metaBooks.size() > collectMetaReq.getPageSize());
        List<CollectMetaBookResp> result = new ArrayList<>();
        for (MetaBook metaBook : metaBooks) {
            result.add(this.changeMetaBook2Resp(metaBook));
        }
        collectMetaResp.setMetaBookResps(result);
        return collectMetaResp;
    }

    /**
     * 采集图书信息
     * @param collectMetaReq
     * @return
     */
    public List<CollectMetaBookResp> collectMetaBook(CollectMetaReq collectMetaReq) {
        List<CollectMetaBookResp> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(collectMetaReq.getIsbns())) {
            return result;
        }

        //根据isbn获取图书信息
        List<MetaBook> metaBooks = metaBookMapper.collectMetaBookByCondition(collectMetaReq.getIsbns());
        if (CollectionUtils.isEmpty(metaBooks)) {
            return result;
        }
        List<Integer> bookIds = metaBooks.stream().map(MetaBook::getId).collect(Collectors.toList());
        //获取简介信息
        Example example = new Example(MetaBookContent.class);
        example.createCriteria()
                .andEqualTo("type", BookContentTypeEnum.INTRODUCE.getCode())
                .andIn("bookId", bookIds)
                .andEqualTo("delFlag", DeleteFlag.NO.toValue());
        List<MetaBookContent> metaBookContents = metaBookContentMapper.selectByExample(example);
        Map<Integer, MetaBookContent> bookId2MetaBookContentMap =
                metaBookContents.stream().collect(Collectors.toMap(MetaBookContent::getBookId, Function.identity(), (k1, k2) -> k1));

        //获取出版社
        List<Integer> publisherIds = metaBooks.stream().map(MetaBook::getPublisherId).collect(Collectors.toList());
        Example examplePublisher = new Example(MetaPublisher.class);
        examplePublisher.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("id", publisherIds);
        List<MetaPublisher> metaPublishers = metaPublisherMapper.selectByExample(examplePublisher);
        Map<Integer, MetaPublisher> publisherId2MetaPublisherMap =
                metaPublishers.stream().collect(Collectors.toMap(MetaPublisher::getId, Function.identity(), (k1, k2) -> k1));

        //群组
        Example exampleBookGroup = new Example(MetaBookGroup.class);
        exampleBookGroup.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("id", publisherIds);
        List<MetaBookGroup> metaBookGroups = metaBookGroupMapper.selectByExample(exampleBookGroup);
        Map<Integer, MetaBookGroup> publisherId2MetaBookGroupMap =
                metaBookGroups.stream().collect(Collectors.toMap(MetaBookGroup::getId, Function.identity(), (k1, k2) -> k1));

        //获取出品方
        List<Integer> producerIds = metaBooks.stream().map(MetaBook::getProducerId).collect(Collectors.toList());
        Example exampleProducer = new Example(MetaPublisher.class);
        exampleProducer.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("id", producerIds);
        List<MetaProducer> metaProducers = metaProducerMapper.selectByExample(exampleProducer);
        Map<Integer, MetaProducer> producerId2MetaProducerMap =
                metaProducers.stream().collect(Collectors.toMap(MetaProducer::getId, Function.identity(), (k1, k2) -> k1));

        //字典信息
        Example exampleDataDict = new Example(MetaDataDict.class);
        exampleDataDict.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("cate", Arrays.asList(DataDictCateEnum.BOOK_BIND.getCode()));
        List<MetaDataDict> metaDataDicts = metaDataDictMapper.selectByExample(exampleDataDict);
        Map<Integer, MetaDataDict> dictId2DataDictMap =
                metaDataDicts.stream().collect(Collectors.toMap(MetaDataDict::getId, Function.identity(), (k1, k2) -> k1));

        //丛书
        List<Integer> bookClumpIds = metaBooks.stream().map(MetaBook::getBookClumpId).collect(Collectors.toList());
        Example exampleMetaBookClump = new Example(MetaBookClump.class);
        exampleMetaBookClump.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("id", bookClumpIds);
        List<MetaBookClump> metaBookClumps = metaBookClumpMapper.selectByExample(exampleMetaBookClump);
        Map<Integer, MetaBookClump> clumpsId2ModelMap =
                metaBookClumps.stream().collect(Collectors.toMap(MetaBookClump::getId, Function.identity(), (k1, k2) -> k1));

        //作者

        //奖项
        
        //标签


        for (MetaBook metaBook : metaBooks) {
            CollectMetaBookResp collectMetaBookResp = new CollectMetaBookResp();
            collectMetaBookResp.setBookId(metaBook.getId());
            collectMetaBookResp.setIsbn(metaBook.getIsbn());
            collectMetaBookResp.setBookName(metaBook.getName());
            collectMetaBookResp.setBookOriginName(metaBook.getOriginName());
            collectMetaBookResp.setBookDesc(bookId2MetaBookContentMap.getOrDefault(metaBook.getId(), new MetaBookContent()).getContent());
            collectMetaBookResp.setAuthorNames(Lists.newArrayList());
            collectMetaBookResp.setPublisher(publisherId2MetaPublisherMap.getOrDefault(metaBook.getPublisherId(), new MetaPublisher()).getName());
            collectMetaBookResp.setFixPrice(metaBook.getPrice());
            collectMetaBookResp.setProducer(producerId2MetaProducerMap.getOrDefault(metaBook.getProducerId(), new MetaProducer()).getName());
            collectMetaBookResp.setClumpName(clumpsId2ModelMap.getOrDefault(metaBook.getBookClumpId(), new MetaBookClump()).getName());
            collectMetaBookResp.setAwards(Lists.newArrayList());
            collectMetaBookResp.setGroupName(publisherId2MetaBookGroupMap.getOrDefault(metaBook.getPublisherId(), new MetaBookGroup()).getName());
            collectMetaBookResp.setSeriesName("");
            collectMetaBookResp.setBindingName(dictId2DataDictMap.getOrDefault(metaBook.getBindId(), new MetaDataDict()).getName());
            collectMetaBookResp.setTags(Lists.newArrayList());
            result.add(collectMetaBookResp);
        }
        return result;
    }
}
