package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookAddReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookEditPublishInfoReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookEditReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryByIdResBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryPublishInfoResBO;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookExt;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookProvider;
import com.wanmi.sbc.bookmeta.service.MetaBookService;
import com.wanmi.sbc.bookmeta.service.MetaFigureService;
import com.wanmi.sbc.bookmeta.service.MetaLabelService;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 书籍(MetaBook)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-17 16:03:45
 */
@Validated
@RestController
public class MetaBookProviderImpl implements MetaBookProvider {
    @Resource
    private MetaBookMapper metaBookMapper;
    @Resource
    private MetaBookLabelMapper metaBookLabelMapper;
    @Resource
    private MetaBookFigureMapper metaBookFigureMapper;
    @Resource
    private MetaBookService metaBookService;
    @Resource
    private MetaLabelService metaLabelService;
    @Resource
    private MetaFigureService metaFigureService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookQueryByIdResBO> queryById(Integer id) {
        MetaBook metaBookDO = this.metaBookMapper.queryById(id);
        if (metaBookDO == null) {
            return BusinessResponse.success();
        }
        MetaBookQueryByIdResBO metaBookBO = new MetaBookQueryByIdResBO();
        BeanUtils.copyProperties(metaBookDO, metaBookBO);
        //标签
        List<Integer> bookLabelIds = metaBookService.listBookLabelId(id);
        List<MetaLabel> bookLabels = metaLabelService.listLabelById(bookLabelIds);

        List<Integer> parentLabelIds = new ArrayList<>();
        for (MetaLabel bookLabel : bookLabels) {
            if (StringUtils.hasText(bookLabel.getPath())) {
                for (String pid : bookLabel.getPath().split("_")) {
                    parentLabelIds.add(Integer.valueOf(pid));
                }
            }
        }
        List<MetaLabel> parentLabels = metaLabelService.listLabelById(parentLabelIds);
        Map<String, MetaLabel> parentLabelM = parentLabels.stream().collect(Collectors.toMap(item->item.getId().toString(), item -> item, (a, b) -> a));

        for (MetaLabel bookLabel : bookLabels) {
            MetaBookQueryByIdResBO.BookLabel labelBO = new MetaBookQueryByIdResBO.BookLabel();
            labelBO.setLabelId(bookLabel.getId());
            labelBO.setLabelName(bookLabel.getName());
            labelBO.setLabelPath(bookLabel.getPath());
            labelBO.setLabelCates(new ArrayList<>());
            List<MetaBookQueryByIdResBO.LabelCate> labelCates = labelBO.getLabelCates();
            for (String pid : bookLabel.getPath().split("_")) {
                MetaBookQueryByIdResBO.LabelCate labelCate = new MetaBookQueryByIdResBO.LabelCate();
                labelCate.setId(Integer.valueOf(pid));
                labelCate.setName(parentLabelM.get(pid) == null ? "N/A" : parentLabelM.get(pid).getName());
            }
            metaBookBO.getBookLabels().add(labelBO);
        }

        //人物
        MetaBookFigure queryFigure = new MetaBookFigure();
        queryFigure.setBookId(id);
        queryFigure.setDelFlag(0);
        List<MetaBookFigure> bookFigures = this.metaBookFigureMapper.select(queryFigure);
        List<MetaFigure> figures = metaFigureService.listFigureById(bookFigures.stream().map(MetaBookFigure::getFigureId).collect(Collectors.toList()));
        Map<Integer, MetaFigure> figureM = figures.stream().collect(Collectors.toMap(MetaFigure::getId, item -> item, (a, b) -> b));

        metaBookBO.setBookFigures(bookFigures.stream().map(item -> {
            MetaBookQueryByIdResBO.BookFigure bookFigure = new MetaBookQueryByIdResBO.BookFigure();
            bookFigure.setFigureId(item.getFigureId());
            bookFigure.setFigureType(item.getFigureType());
            bookFigure.setFigureName(figureM.get(item.getFigureId())==null ? null : figureM.get(item.getFigureId()).getName());
            return bookFigure;
        }).collect(Collectors.toList()));

        return BusinessResponse.success(metaBookBO);
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookBO>> queryByPage(@Valid MetaBookQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookExt metaBook = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookExt.class);
        
        page.setTotalCount(this.metaBookMapper.countExt(metaBook));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBook> metaBooks = this.metaBookMapper.queryAllByLimitExt(metaBook, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaBooks, MetaBookBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaBook 实例对象
     * @return 实例对象
     */
    @Transactional
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookAddReqBO metaBook) {
        Date now = new Date();
        MetaBook insert = new MetaBook();
        BeanUtils.copyProperties(metaBook, insert);
        insert.setCreateTime(now);
        insert.setUpdateTime(now);
        if (this.metaBookMapper.insertSelective(insert) != 1) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "新增失败");
        }
        //标签
        insertBookLabel(insert.getId(), metaBook.getLabelIds(), now);
        //人物
        insertBookFigure(insert.getId(), metaBook.getFigures().stream().map(item -> Pair.of(item.getFigureId(), item.getFigureType())).collect(Collectors.toList()), now);
        return BusinessResponse.success(insert.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookBO 实例对象
     * @return 实例对象
     */
    @Transactional
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookEditReqBO metaBookBO) {
        if (metaBookBO.getId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //查询
        MetaBook metaBookDO = this.metaBookMapper.selectByPrimaryKey(metaBookBO.getId());
        if (metaBookDO == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        Date now = new Date();
        MetaBook update = new MetaBook();
        BeanUtils.copyProperties(metaBookDO, update);
        //如果是更新基础信息
        update.setIsbn(metaBookBO.getIsbn());
        update.setName(metaBookBO.getName());
        update.setSubName(metaBookBO.getSubName());
        update.setOriginName(metaBookBO.getOriginName());
        //标签
        insertBookLabel(metaBookBO.getId(), metaBookBO.getLabelIds(), now);
        //人物
        insertBookFigure(metaBookBO.getId(), metaBookBO.getFigures().stream().map(item -> Pair.of(item.getFigureId(), item.getFigureType())).collect(Collectors.toList()), now);

        update.setUpdateTime(now);
        this.metaBookMapper.update(update);
        return BusinessResponse.success(true);
    }

    /**
     * 修改数据
     *
     * @param metaBookBO 实例对象
     * @return 实例对象
     */
    @Transactional
    @Override
    public BusinessResponse<Boolean> updatePublishInfo(@Valid MetaBookEditPublishInfoReqBO metaBookBO) {
        if (metaBookBO.getId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //查询
        MetaBook metaBookDO = this.metaBookMapper.selectByPrimaryKey(metaBookBO.getId());
        if (metaBookDO == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        Date now = new Date();
        MetaBook update = new MetaBook();
        BeanUtils.copyProperties(metaBookDO, update);
        //如果是更新出版信息
        update.setBindId(metaBookBO.getBindId());
        update.setPublisherId(metaBookBO.getPublisherId());
        update.setPublishTime(metaBookBO.getPublishTime());
        update.setPublishBatch(metaBookBO.getPublishBatch());
        update.setPrintTime(metaBookBO.getPrintTime());
        update.setPrintBatch(metaBookBO.getPrintBatch());
        update.setPageCount(metaBookBO.getPageCount());
        update.setWordCount(metaBookBO.getWordCount());
        update.setProducerId(metaBookBO.getProducerId());
        update.setBookGroupId(metaBookBO.getBookGroupId());
        update.setBookGroupDescr(metaBookBO.getBookGroupDescr());
        update.setBookClumpId(metaBookBO.getBookClumpId());
        update.setPrice(metaBookBO.getPrice());
        update.setLanguageId(metaBookBO.getLanguageId());
        update.setPaperId(metaBookBO.getPaperId());
        update.setPrintSheet(metaBookBO.getPrintSheet());
        update.setSizeLength(metaBookBO.getSizeLength());
        update.setSizeWidth(metaBookBO.getSizeWidth());
        //开本属性
        update.setUpdateTime(now);
        this.metaBookMapper.update(update);
        return BusinessResponse.success(true);
    }

    @Override
    public BusinessResponse<MetaBookQueryPublishInfoResBO> queryPublishInfo(Integer id) {
        MetaBook metaBook = this.metaBookMapper.queryById(id);
        if (metaBook == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        MetaBookQueryPublishInfoResBO resultBO = new MetaBookQueryPublishInfoResBO();
        BeanUtils.copyProperties(metaBook, resultBO);
        return BusinessResponse.success(resultBO);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookMapper.deleteById(id) > 0);
    }

    private void insertBookLabel(Integer bookId, List<Integer> labelIds, Date time) {
        MetaBookLabel deleteLabel = new MetaBookLabel();
        deleteLabel.setBookId(bookId);
        this.metaBookLabelMapper.delete(deleteLabel);

        if (CollectionUtils.isNotEmpty(labelIds)) {
            List<MetaBookLabel> bookLabels = labelIds.stream().map(item -> {
                MetaBookLabel bookLabel = new MetaBookLabel();
                bookLabel.setBookId(bookId);
                bookLabel.setLabelId(item);
                bookLabel.setCreateTime(time);
                bookLabel.setUpdateTime(time);
                bookLabel.setDelFlag(0);
                return bookLabel;
            }).collect(Collectors.toList());
            metaBookLabelMapper.insertBatch(bookLabels);
        }
    }

    private void insertBookFigure(Integer bookId, List<Pair<Integer, Integer>> figures, Date time) {
        MetaBookFigure deleteFigure = new MetaBookFigure();
        deleteFigure.setBookId(bookId);
        this.metaBookFigureMapper.delete(deleteFigure);

        if (CollectionUtils.isNotEmpty(figures)) {
            List<MetaBookFigure> bookFigures = figures.stream().map(item -> {
                MetaBookFigure bookFigure = new MetaBookFigure();
                bookFigure.setBookId(bookId);
                bookFigure.setFigureId(item.getKey());
                bookFigure.setFigureType(item.getValue());
                bookFigure.setCreateTime(time);
                bookFigure.setUpdateTime(time);
                bookFigure.setDelFlag(0);
                return bookFigure;
            }).collect(Collectors.toList());
            metaBookFigureMapper.insertBatch(bookFigures);
        }
    }
}
