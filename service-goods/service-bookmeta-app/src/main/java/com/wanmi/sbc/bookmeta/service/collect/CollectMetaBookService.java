package com.wanmi.sbc.bookmeta.service.collect;
import java.math.BigDecimal;
import com.google.common.collect.Lists;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaBookResp;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaAward;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookClump;
import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookGroup;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaDataDict;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import com.wanmi.sbc.bookmeta.enums.BookContentTypeEnum;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.enums.DataDictCateEnum;
import com.wanmi.sbc.bookmeta.enums.FigureTypeEnum;
import com.wanmi.sbc.bookmeta.enums.LabelStatusEnum;
import com.wanmi.sbc.bookmeta.enums.LabelTypeEnum;
import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

        //书组
//        List<Integer> bookGroupIds = metaBooks.stream().map(MetaBook::getBookGroupId).collect(Collectors.toList());
//        Example exampleBookGroup = new Example(MetaBookGroup.class);
//        exampleBookGroup.createCriteria()
//                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
//                .andIn("id", bookGroupIds);
//        List<MetaBookGroup> metaBookGroups = metaBookGroupMapper.selectByExample(exampleBookGroup);
//        Map<Integer, MetaBookGroup> publisherId2MetaBookGroupMap =
//                metaBookGroups.stream().collect(Collectors.toMap(MetaBookGroup::getId, Function.identity(), (k1, k2) -> k1));

        //获取出品方
        List<Integer> producerIds = metaBooks.stream().map(MetaBook::getProducerId).collect(Collectors.toList());
        Example exampleProducer = new Example(MetaPublisher.class);
        exampleProducer.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("id", producerIds);
        List<MetaProducer> metaProducers = metaProducerMapper.selectByExample(exampleProducer);
        Map<Integer, MetaProducer> producerId2MetaProducerMap =
                metaProducers.stream().collect(Collectors.toMap(MetaProducer::getId, Function.identity(), (k1, k2) -> k1));

        //字典信息 装帧
        Example exampleDataDict = new Example(MetaDataDict.class);
        exampleDataDict.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                    .andIn("cate", Arrays.asList(DataDictCateEnum.BOOK_BIND.getCode()));
        List<MetaDataDict> metaDataDicts = metaDataDictMapper.selectByExample(exampleDataDict);
        Map<String, MetaDataDict> dictId2DataDictMap =
                metaDataDicts.stream().collect(Collectors.toMap(MetaDataDict::getValue, Function.identity(), (k1, k2) -> k1));

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
        Map<Integer, List<MetaFigure>> bookId2FiguresMap = this.mapBookId2Figure(bookIds);
        //奖项
        Map<Integer, List<MetaAward>> bookId2AwardMap = this.mapBookId2Award(bookIds);
        Map<Integer, List<MetaAward>> figureId2AwardMap = new HashMap<>();
        if (!bookId2FiguresMap.isEmpty()) {
            List<Integer> figureIds = new ArrayList<>();
            for (Integer bookId : bookId2AwardMap.keySet()) {
                List<MetaFigure> metaFigures = bookId2FiguresMap.get(bookId);
                if (CollectionUtils.isEmpty(metaFigures)) {
                    continue;
                }
                figureIds.addAll(bookId2FiguresMap.get(bookId).stream().map(MetaFigure::getId).collect(Collectors.toList()));
            }
            if (!CollectionUtils.isEmpty(figureIds)) {
                figureId2AwardMap = this.mapFigureId2Award(figureIds);
            }
        }
        //标签
        Map<Integer, List<CollectMetaBookResp.Tag>> bookId2LebelsMap = this.mapBookId2Lebel(bookIds);

        for (MetaBook metaBook : metaBooks) {
            CollectMetaBookResp collectMetaBookResp = new CollectMetaBookResp();
            collectMetaBookResp.setBookId(metaBook.getId());
            collectMetaBookResp.setIsbn(metaBook.getIsbn());
            collectMetaBookResp.setBookName(metaBook.getName());
            collectMetaBookResp.setBookOriginName(metaBook.getOriginName());
            collectMetaBookResp.setBookDesc(bookId2MetaBookContentMap.getOrDefault(metaBook.getId(), new MetaBookContent()).getContent());
            List<MetaFigure> metaFigures = bookId2FiguresMap.getOrDefault(metaBook.getId(), new ArrayList<>());
            collectMetaBookResp.setAuthorNames(metaFigures.stream().map(MetaFigure::getName).collect(Collectors.toList())); //作者信息
            collectMetaBookResp.setPublisher(publisherId2MetaPublisherMap.getOrDefault(metaBook.getPublisherId(), new MetaPublisher()).getName());
            collectMetaBookResp.setFixPrice(metaBook.getPrice());
            collectMetaBookResp.setProducer(producerId2MetaProducerMap.getOrDefault(metaBook.getProducerId(), new MetaProducer()).getName());
            collectMetaBookResp.setClumpName(clumpsId2ModelMap.getOrDefault(metaBook.getBookClumpId(), new MetaBookClump()).getName());

            //奖项
            List<CollectMetaBookResp.Award> awardList = new ArrayList<>();
            //图书奖项
            List<MetaAward> bookMetaAwards = bookId2AwardMap.getOrDefault(metaBook.getId(), new ArrayList<>());
            for (MetaAward metaAwardTmp : bookMetaAwards) {
                CollectMetaBookResp.Award award = new CollectMetaBookResp.Award();
                award.setAwardCategory(1);
                award.setAwardName(metaAwardTmp.getName());
                awardList.add(award);
            }
            //人员奖项
            if (!CollectionUtils.isEmpty(metaFigures)) {
                for (MetaFigure metaFigure : metaFigures) {
                    List<MetaAward> figureIdMetaAwards = figureId2AwardMap.getOrDefault(metaFigure.getId(), new ArrayList<>());
                    for (MetaAward figureIdMetaAward : figureIdMetaAwards) {
                        CollectMetaBookResp.Award award = new CollectMetaBookResp.Award();
                        award.setAwardCategory(2);
                        award.setAwardName(figureIdMetaAward.getName());
                        awardList.add(award);
                    }
                }
            }

            collectMetaBookResp.setAwards(awardList);
//            collectMetaBookResp.setGroupName(publisherId2MetaBookGroupMap.getOrDefault(metaBook.getPublisherId(), new MetaBookGroup()).getName());
            collectMetaBookResp.setGroupName(metaBook.getBookGroupDescr());
            collectMetaBookResp.setBindingName(!StringUtils.isEmpty(metaBook.getBindId()) ? dictId2DataDictMap.getOrDefault(metaBook.getBindId().toString(), new MetaDataDict()).getName() : "");

            collectMetaBookResp.setTags(bookId2LebelsMap.get(metaBook.getId()));
            result.add(collectMetaBookResp);
        }
        return result;
    }

    /**
     * 获取图书作者信息
     * @param bookIds
     * @return
     */
    private Map<Integer, List<MetaFigure>> mapBookId2Figure(List<Integer> bookIds) {
        //作者
        Map<Integer, List<MetaFigure>> bookId2FiguresMap = new HashMap<>();
//         1、根据图书获取作者信息
        Example bookFigureExample = new Example(MetaBookFigure.class);
        bookFigureExample.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("bookId", bookIds)
                .andIn("figureType", Arrays.asList(FigureTypeEnum.AUTHOR.getCode()));
        List<MetaBookFigure> metaBookFigures = metaBookFigureMapper.selectByExample(bookFigureExample);

        if (!CollectionUtils.isEmpty(metaBookFigures)) {
            //根据关联关系获取作者信息
            List<Integer> figureIds = metaBookFigures.stream().map(MetaBookFigure::getFigureId).collect(Collectors.toList());
            Example figureExample = new Example(MetaFigure.class);
            figureExample.createCriteria()
                    .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                    .andIn("id", figureIds)
                    .andIn("type", Arrays.asList(FigureTypeEnum.AUTHOR.getCode()));
            List<MetaFigure> metaFigures = metaFigureMapper.selectByExample(figureExample);
            Map<Integer, MetaFigure> figurId2ModelMap = metaFigures.stream().collect(Collectors.toMap(MetaFigure::getId, Function.identity(), (k1, k2) -> k1));
            for (MetaBookFigure metaBookFigureParam : metaBookFigures) {
                List<MetaFigure> metaFiguresTmp = bookId2FiguresMap.get(metaBookFigureParam.getBookId());
                if (CollectionUtils.isEmpty(metaFiguresTmp)) {
                    metaFiguresTmp = new ArrayList<>();
                    bookId2FiguresMap.put(metaBookFigureParam.getBookId(), metaFiguresTmp);
                }
                //获取对应的作者列表信息
                MetaFigure metaFigureModel = figurId2ModelMap.get(metaBookFigureParam.getFigureId());
                if (metaFigureModel == null) {
                    continue;
                }
                metaFiguresTmp.add(metaFigureModel);
            }
        }
        return bookId2FiguresMap;
    }

    /**
     * 获取人物 奖项信息
     * @param figureIds
     * @return
     */
    private Map<Integer, List<MetaAward>> mapFigureId2Award(List<Integer> figureIds) {
        Map<Integer, List<MetaAward>> figureId2AwardMap = new HashMap<>();
        Example figureAwardExample = new Example(MetaFigureAward.class);
        figureAwardExample.createCriteria()
                        .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                                .andIn("figureId", figureIds);
        List<MetaFigureAward> metaFigureAwards = metaFigureAwardMapper.selectByExample(figureAwardExample);
        //获取奖项信息
        if (!CollectionUtils.isEmpty(metaFigureAwards)) {
            List<Integer> awardIds = metaFigureAwards.stream().map(MetaFigureAward::getAwardId).collect(Collectors.toList());
            Example awardExample = new Example(MetaAward.class);
            awardExample.createCriteria()
                            .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                                    .andIn("id", awardIds);
            List<MetaAward> metaAwards = metaAwardMapper.selectByExample(awardExample);
            Map<Integer, MetaAward> awardId2ModelMap = metaAwards.stream().collect(Collectors.toMap(MetaAward::getId, Function.identity(), (k1, k2) -> k1));
            for (MetaFigureAward metaFigureAward : metaFigureAwards) {
                List<MetaAward> metaAwardsTmp = figureId2AwardMap.get(metaFigureAward.getFigureId());
                if (CollectionUtils.isEmpty(metaAwardsTmp)) {
                    metaAwardsTmp = new ArrayList<>();
                    figureId2AwardMap.put(metaFigureAward.getFigureId(), metaAwardsTmp);
                }

                MetaAward metaAward = awardId2ModelMap.get(metaFigureAward.getAwardId());
                if (metaAward == null) {
                    continue;
                }
                metaAwardsTmp.add(metaAward);
            }
        }
        return figureId2AwardMap;
    }

    /**
     * 获取图书 奖项信息
     * @param bookIds
     * @return
     */
    private Map<Integer, List<MetaAward>> mapBookId2Award(List<Integer> bookIds) {
        Map<Integer, List<MetaAward>> bookId2AwardMap = new HashMap<>();

        Example example = new Example(MetaBookRcmmd.class);
        example.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("bookId", bookIds)
                .andIn("bizType", Arrays.asList(BookRcmmdTypeEnum.AWARD.getCode()));
        List<MetaBookRcmmd> metaBookRcmmds = metaBookRcmmdMapper.selectByExample(example);

        if (!CollectionUtils.isEmpty(metaBookRcmmds)) {
            List<Integer> awardIds = metaBookRcmmds.stream().map(MetaBookRcmmd::getBizId).collect(Collectors.toList());
            Example awardExample = new Example(MetaAward.class);
            awardExample.createCriteria()
                    .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                    .andIn("id", awardIds);
            List<MetaAward> metaAwards = metaAwardMapper.selectByExample(awardExample);
            Map<Integer, MetaAward> awardId2ModelMap = metaAwards.stream().collect(Collectors.toMap(MetaAward::getId, Function.identity(), (k1, k2) -> k1));
            for (MetaBookRcmmd metaBookRcmmd : metaBookRcmmds) {
                List<MetaAward> metaAwardsTmp = bookId2AwardMap.get(metaBookRcmmd.getBookId());
                if (CollectionUtils.isEmpty(metaAwardsTmp)) {
                    metaAwardsTmp = new ArrayList<>();
                    bookId2AwardMap.put(metaBookRcmmd.getBookId(), metaAwardsTmp);
                }

                MetaAward metaAward = awardId2ModelMap.get(metaBookRcmmd.getBizId());
                if (metaAward == null) {
                    continue;
                }
                metaAwardsTmp.add(metaAward);
            }
        }
        return bookId2AwardMap;
    }

    //标签信息
    private Map<Integer, List<CollectMetaBookResp.Tag>> mapBookId2Lebel(List<Integer> bookIds) {
        Map<Integer, List<CollectMetaBookResp.Tag>> bookId2TagsMap = new HashMap<>();
        //获取图书关联标签
        Example example = new Example(MetaBookLabel.class);
        example.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("bookId", bookIds);
        List<MetaBookLabel> metaBookLabels = metaBookLabelMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(metaBookLabels)) {
            return bookId2TagsMap;
        }
        List<Integer> thirdLabelIds = metaBookLabels.stream().map(MetaBookLabel::getLabelId).collect(Collectors.toList());
        List<MetaLabel> thirdMetaLabels = this.listMetaLabel(thirdLabelIds);
        if (CollectionUtils.isEmpty(thirdMetaLabels)) {
            return bookId2TagsMap;
        }

        Map<Integer, MetaLabel> thirdLabelId2ModelMap = thirdMetaLabels.stream().collect(Collectors.toMap(MetaLabel::getId, Function.identity(), (k1, k2) -> k1));

        for (MetaBookLabel metaBookLabel : metaBookLabels) {
            List<CollectMetaBookResp.Tag> tags = bookId2TagsMap.get(metaBookLabel.getBookId());
            if (CollectionUtils.isEmpty(tags)) {
                tags = new ArrayList<>();
                bookId2TagsMap.put(metaBookLabel.getBookId(), tags);
            }
            MetaLabel thirdMetaLabel = thirdLabelId2ModelMap.get(metaBookLabel.getLabelId());
            if (thirdMetaLabel != null) {
                CollectMetaBookResp.Tag tag = new CollectMetaBookResp.Tag();
                tag.setStagId(thirdMetaLabel.getParentId());
                tag.setTagId(thirdMetaLabel.getId());
                tag.setTagName(thirdMetaLabel.getName());
                tag.setIsRun(thirdMetaLabel.getIsRun());
                tag.setIsStatic(thirdMetaLabel.getIsStatic());
                tag.setRunFromTime(thirdMetaLabel.getRunFromTime());
                tag.setRunToTime(thirdMetaLabel.getRunToTime());
                tags.add(tag);
            }
        }

        List<Integer> secondLabelIds = thirdMetaLabels.stream().map(MetaLabel::getParentId).collect(Collectors.toList());
        List<MetaLabel> secondMetaLabels = this.listMetaLabel(secondLabelIds);
        //获取标签的二级分类
        if (CollectionUtils.isEmpty(secondMetaLabels)) {
            return bookId2TagsMap;
        }
        Map<Integer, MetaLabel> secondLabelId2ModelMap = secondMetaLabels.stream().collect(Collectors.toMap(MetaLabel::getId, Function.identity(), (k1, k2) -> k1));
        bookId2TagsMap.forEach((K, V) -> {
            //填充二级名称
            for (CollectMetaBookResp.Tag tag : V) {
                MetaLabel secondMetaLabel = secondLabelId2ModelMap.get(tag.getStagId());
                if (secondMetaLabel != null) {
                    tag.setStagName(secondMetaLabel.getName());
                }
            }
        });
        return bookId2TagsMap;
    }

    /**
     * 获取标签信息
     * @param labelIds
     * @return
     */
    private List<MetaLabel> listMetaLabel(List<Integer> labelIds) {
        //获取标签信息
        Example thirdLabelExample = new Example(MetaLabel.class);
        thirdLabelExample.createCriteria()
                .andEqualTo("delFlag", DeleteFlag.NO.toValue())
                .andIn("id", labelIds)
                .andEqualTo("status", LabelStatusEnum.ENABLE.getCode());
        return metaLabelMapper.selectByExample(thirdLabelExample);
    }
}
