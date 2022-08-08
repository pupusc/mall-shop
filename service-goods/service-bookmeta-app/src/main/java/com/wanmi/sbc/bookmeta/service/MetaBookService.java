package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookClump;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import com.wanmi.sbc.bookmeta.enums.BookFigureTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaBookClumpMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaProducerMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaPublisherMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-05-27 23:11:00
 */
@Service
public class MetaBookService {
    @Autowired
    private MetaBookMapper metaBookMapper;
    @Autowired
    private MetaBookLabelMapper metaBookLabelMapper;
    @Autowired
    private MetaBookFigureMapper metaBookFigureMapper;
    @Autowired
    private MetaBookClumpMapper metaBookClumpMapper;
    @Autowired
    private MetaFigureMapper metaFigureMapper;
    @Autowired
    private MetaPublisherMapper metaPublisherMapper;
    @Autowired
    private MetaProducerMapper metaProducerMapper;

    public List<Integer> listBookLabelId(Integer id) {
        if (id == null) {
            return Collections.EMPTY_LIST;
        }
        MetaBookLabel query = new MetaBookLabel();
        query.setBookId(id);
        query.setDelFlag(0);
        List<MetaBookLabel> labels = this.metaBookLabelMapper.select(query);
        return labels.stream().map(MetaBookLabel::getLabelId).collect(Collectors.toList());
    }

    public List<MetaBookFigure> listBookFigureByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Example example = new Example(MetaBookFigure.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("bookId", ids.stream().distinct().collect(Collectors.toList()));
        return metaBookFigureMapper.selectByExample(example);
    }

    public List<MetaBook> listEntityByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        Example example = new Example(MetaBook.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", ids.stream().distinct().collect(Collectors.toList()));
        return metaBookMapper.selectByExample(example);
    }

    /**
     * 查询图书的扩展信息
     */
    public MetaBookInfoResult getPackInfoById(@Valid MetaBookInfoParam param) {
        MetaBook metaBook = this.metaBookMapper.queryById(param.getId());
        if (metaBook == null) {
            return null;
        }

        MetaBookInfoResult result = new MetaBookInfoResult();
        BeanUtils.copyProperties(metaBook, result);

        //填充丛书信息
        if (param.isQueryBookClump() && result.getBookClumpId() != null) {
            MetaBookClump queryClump = new MetaBookClump();
            queryClump.setDelFlag(0);
            queryClump.setId(result.getBookClumpId());
            result.setMetaBookClump(this.metaBookClumpMapper.selectOne(queryClump));
        }
        //填充作者信息
        if (param.isQueryAuthor()) {
            result.setAuthors(new ArrayList<>());
            MetaBookFigure queryAuthor = new MetaBookFigure();;
            queryAuthor.setBookId(metaBook.getId());
            queryAuthor.setFigureType(BookFigureTypeEnum.AUTHOR.getCode());
            queryAuthor.setDelFlag(0);
            List<MetaBookFigure> bookFigures = this.metaBookFigureMapper.select(queryAuthor);
            List<Integer> authorIds = bookFigures.stream().map(MetaBookFigure::getFigureId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(authorIds)) {
                Example exampleFigure = new Example(MetaFigure.class);
                exampleFigure.createCriteria().andEqualTo("delFlag", 0).andIn("id", authorIds);
                List<MetaFigure> figures = metaFigureMapper.selectByExample(exampleFigure);
                Map<Integer, MetaFigure> figureM = figures.stream().collect(Collectors.toMap(MetaFigure::getId, i->i, (a, b)->a));
                for (Integer authorId : authorIds) {
                    if (figureM.get(authorId) != null) {
                        result.getAuthors().add(figureM.get(authorId));
                    }
                }
            }
        }
        //填充出版社信息
        if (param.isQueryPublisher() && result.getPublisherId() != null) {
            MetaPublisher queryPublisher = new MetaPublisher();
            queryPublisher.setDelFlag(0);
            queryPublisher.setId(result.getPublisherId());
            result.setPublisher(this.metaPublisherMapper.selectOne(queryPublisher));
        }
        //填充出品方信息
        if (param.isQueryProducer() && result.getProducerId() != null) {
            MetaProducer queryProducer = new MetaProducer();
            queryProducer.setDelFlag(0);
            queryProducer.setId(result.getProducerId());
            result.setProducer(this.metaProducerMapper.selectOne(queryProducer));
        }
        return result;
    }
}
