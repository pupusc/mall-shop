package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.bo.CollectMetaReq;
import com.wanmi.sbc.bookmeta.entity.MetaAward;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookClump;
import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import com.wanmi.sbc.bookmeta.mapper.MetaAwardMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookClumpMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookContentMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookGroupMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaProducerMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaPublisherMapper;
import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 采集数据
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 1:24 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectMetaService {


    @Autowired
    private MetaBookMapper metaBookMapper;

    @Autowired
    private MetaPublisherMapper metaPublisherMapper;

    @Autowired
    private MetaProducerMapper metaProducerMapper;

    @Autowired
    private MetaBookGroupMapper metaBookGroupMapper;

    @Autowired
    private MetaBookClumpMapper metaBookClumpMapper;

    @Autowired
    private MetaBookContentMapper metaBookContentMapper;

    @Autowired
    private MetaBookFigureMapper metaBookFigureMapper;

    @Autowired
    private MetaFigureMapper metaFigureMapper;

    @Autowired
    private MetaAwardMapper metaAwardMapper;


    public void collectMetaByTime(CollectMetaReq collectMetaReq) {

        //丛书
        List<MetaBookClump> metaBookClumps = metaBookClumpMapper.collectMetaBookClumpByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        //获取出版社
        if (CollectionUtils.isEmpty(metaBookClumps)) {
            List<Integer> publisherIds = metaBookClumps.stream().map(MetaBookClump::getPublisherId).collect(Collectors.toList());
            //批量获取 出版社信息
            List<MetaPublisher> metaPublishers = metaPublisherMapper.collectMetaPublisherByIds(publisherIds, null);
            //获取商品信息
        }
        //获取商品信息

        //采集出版社
        List<Integer> metaPublishers = metaPublisherMapper.collectMetaPublisherByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        //获取图书商品

        //采集出品方
        List<Integer> metaProducers = metaProducerMapper.collectMetaProducerByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        //获取图书商品

        //书组名称
        List<Integer> metaBookGroup = metaBookGroupMapper.collectMetaBookGroupByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        //获取图书商品

        //简介
        List<MetaBookContent> metaBookContents = metaBookContentMapper.collectMetaBookContentByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        //获取图书信息

        //作者
        List<MetaFigure> metaFigures = metaFigureMapper.collectMetaFigureByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (!CollectionUtils.isEmpty(metaFigures)) {
            //获取作者图书的关联关系
            List<Integer> figureIds = metaFigures.stream().map(MetaFigure::getId).collect(Collectors.toList());
            List<MetaBookFigure> metaBookFigures = metaBookFigureMapper.collectMetaBookFigureByIds(figureIds, null);
            //获取图书信息
        }

        //作者图书关联表
        List<MetaBookFigure> metaBookFigures = metaBookFigureMapper.collectMetaBookFigureByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        //获取图书信息


        //奖项
        List<MetaAward> metaAwards = metaAwardMapper.collectMetaAwardByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
        if (!CollectionUtils.isEmpty(metaAwards)) {
            //人物奖项

            //人物商品
        }
        //商品奖项


        //获取作者；
        //获取推荐；
        //获取图书信息


        //套系
        //或图图书信息

        //装帧
        //获取图书信息

        //标签
        //获取图书标签
        //获取图书信息

        //采集图书信息
        List<MetaBook> metaBooks =
                metaBookMapper.collectMetaBookByTime(collectMetaReq.getBeginTime(), collectMetaReq.getEndTime(), collectMetaReq.getFromId(), collectMetaReq.getPageSize());
    }


    //根据isbn获取对应的图书属性信息
}
