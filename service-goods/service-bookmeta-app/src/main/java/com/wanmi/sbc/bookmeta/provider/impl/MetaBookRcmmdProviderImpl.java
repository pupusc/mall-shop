package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdByBookIdReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.enums.LabelStatusEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookRcmmdProvider;
import com.wanmi.sbc.bookmeta.service.MetaBookService;
import com.wanmi.sbc.bookmeta.service.MetaFigureService;
import com.wanmi.sbc.bookmeta.service.MetaLabelService;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 书籍推荐(MetaBookRcmmd)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Slf4j
@Validated
@RestController
public class MetaBookRcmmdProviderImpl implements MetaBookRcmmdProvider {
    @Resource
    private MetaBookRcmmdMapper metaBookRcmmdMapper;
    @Resource
    private MetaBookLabelMapper metaBookLabelMapper;
    @Resource
    private MetaLabelMapper metaLabelMapper;
    @Resource
    private MetaBookMapper metaBookMapper;
    @Resource
    private MetaFigureService metaFigureService;
    @Resource
    private MetaBookService metaBookService;
    @Resource
    private MetaLabelService metaLabelService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookRcmmdBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaBookRcmmdMapper.queryById(id), MetaBookRcmmdBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookRcmmdBO>> queryByPage(@Valid MetaBookRcmmdQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookRcmmd metaBookRcmmd = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookRcmmd.class);
        
        page.setTotalCount((int) this.metaBookRcmmdMapper.count(metaBookRcmmd));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBookRcmmd> rcmmds = this.metaBookRcmmdMapper.queryAllByLimit(metaBookRcmmd, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(rcmmds, MetaBookRcmmdBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookRcmmdBO metaBookRcmmd) {
        this.metaBookRcmmdMapper.insertSelective(DO2BOUtils.objA2objB(metaBookRcmmd, MetaBookRcmmd.class));
        return BusinessResponse.success(metaBookRcmmd.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookRcmmdBO metaBookRcmmd) {
        return BusinessResponse.success(this.metaBookRcmmdMapper.update(DO2BOUtils.objA2objB(metaBookRcmmd, MetaBookRcmmd.class)) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookRcmmdMapper.deleteById(id) > 0);
    }

    @Override
    public BusinessResponse<MetaBookRcmmdByBookIdReqBO> queryByBookId(Integer bookId) {
        MetaBook metaBook = metaBookMapper.queryById(bookId);
        if (metaBook == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }

        MetaBookRcmmdByBookIdReqBO result = new MetaBookRcmmdByBookIdReqBO();
        result.setBookId(bookId);
        //适读年龄
        result.setFitAgeMax(metaBook.getFitAgeMax());
        result.setFitAgeMin(metaBook.getFitAgeMin());
        //适读对象
        result.setFitTargetIds(getEnableFitIdsByBookId(bookId));
        //推荐信息
        MetaBookRcmmd queryBookRcmmd = new MetaBookRcmmd();
        queryBookRcmmd.setBookId(bookId);
        queryBookRcmmd.setDelFlag(0);
        List<MetaBookRcmmd> rcmmdDOs = this.metaBookRcmmdMapper.select(queryBookRcmmd);
        List<MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO> rcmmdBOs = rcmmdDOs.stream().map(item -> {
            MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO rcmmdBO = new MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO();
            BeanUtils.copyProperties(item, rcmmdBO);
            return rcmmdBO;
        }).collect(Collectors.toList());
        result.setBookRcmmds(rcmmdBOs);
        //填充名称
        fillRcmmdName(rcmmdBOs);

        return BusinessResponse.success(result);
    }

    @Transactional
    @Override
    public BusinessResponse<Boolean> editByBookId(MetaBookRcmmdByBookIdReqBO editBO) {
        MetaBook metaBook = metaBookMapper.queryById(editBO.getBookId());
        if (metaBook == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        //更新适读年龄
        metaBook.setFitAgeMax(editBO.getFitAgeMax());
        metaBook.setFitAgeMin(editBO.getFitAgeMin());
        this.metaBookMapper.update(metaBook);
        //更新适读对象
        updateFitIdsByBookId(editBO.getBookId(), editBO.getFitTargetIds());
        //更新推荐信息
        MetaBookRcmmd deleteBookRcmmd = new MetaBookRcmmd();
        deleteBookRcmmd.setBookId(editBO.getBookId());
        this.metaBookRcmmdMapper.delete(deleteBookRcmmd);

        if (!CollectionUtils.isEmpty(editBO.getBookRcmmds())) {
            Date now = new Date();
            List<MetaBookRcmmd> insertBookRcmmds = editBO.getBookRcmmds().stream().map(item -> {
                MetaBookRcmmd bookRcmmd = new MetaBookRcmmd();
                bookRcmmd.setBookId(editBO.getBookId());
                bookRcmmd.setBizId(item.getBizId());
                bookRcmmd.setBizType(item.getBizType());
                bookRcmmd.setBizTime(item.getBizTime());
                bookRcmmd.setDescr(item.getDescr());
                bookRcmmd.setCreateTime(now);
                bookRcmmd.setUpdateTime(now);
                bookRcmmd.setDelFlag(0);
                return bookRcmmd;
            }).collect(Collectors.toList());
            this.metaBookRcmmdMapper.insertBatch(insertBookRcmmds);
        }
        return BusinessResponse.success(true);
    }

    private List<Integer> getEnableFitIdsByBookId(Integer bookId) {
        List<MetaBookLabel> bookLabels = getBookLabels(bookId);
        if (CollectionUtils.isEmpty(bookLabels)) {
            return Collections.EMPTY_LIST;
        }
        //适读对象标签
        List<Integer> labelIds = metaLabelService.getFitTargetLabels().stream()
                .filter(item-> LabelStatusEnum.ENABLE.getCode().equals(item.getStatus()))
                .map(MetaLabel::getId).collect(Collectors.toList());
        return bookLabels.stream().map(MetaBookLabel::getLabelId).filter(item->labelIds.contains(item)).collect(Collectors.toList());
    }

    private void updateFitIdsByBookId(Integer bookId, List<Integer> fitTargetIds) {
        List<Integer> editFitTargetIds = fitTargetIds != null ? fitTargetIds : new ArrayList<>();
        List<MetaBookLabel> bookLabels = getBookLabels(bookId);
        List<Integer> allFitLabelIds = metaLabelService.getFitTargetLabelIds();
        List<Integer> bookFitLabelIds = bookLabels.stream().map(MetaBookLabel::getLabelId).filter(item->allFitLabelIds.contains(item)).collect(Collectors.toList());
        //没变化不更新
        if (editFitTargetIds.containsAll(bookFitLabelIds) && bookFitLabelIds.containsAll(editFitTargetIds)) {
            return;
        }
        //删除标签
        List<Integer> deleteBookLabels = bookFitLabelIds.stream().filter(item -> !editFitTargetIds.contains(item)).collect(Collectors.toList());
        //新增标签
        List<Integer> insertBookLabels = editFitTargetIds.stream().filter(item -> !bookFitLabelIds.contains(item)).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(deleteBookLabels)) {
            Example deleteExample = new Example(MetaBookLabel.class);
            deleteExample.createCriteria().andEqualTo("bookId", bookId).andIn("labelId", deleteBookLabels);
            this.metaBookLabelMapper.deleteByExample(deleteExample);
        }
        if (!CollectionUtils.isEmpty(insertBookLabels)) {
            Date now = new Date();
            List<MetaBookLabel> insertList = insertBookLabels.stream().map(item -> {
                MetaBookLabel bookLabel = new MetaBookLabel();
                bookLabel.setBookId(bookId);
                bookLabel.setLabelId(item);
                bookLabel.setCreateTime(now);
                bookLabel.setUpdateTime(now);
                bookLabel.setDelFlag(0);
                return bookLabel;
            }).collect(Collectors.toList());
            this.metaBookLabelMapper.insertBatch(insertList);
        }
    }

    /**
     * 书籍关联标签
     */
    private List<MetaBookLabel> getBookLabels(Integer bookId) {
        MetaBookLabel query = new MetaBookLabel();
        query.setBookId(bookId);
        query.setDelFlag(0);
        return metaBookLabelMapper.select(query);
    }

    private void fillRcmmdName(List<MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO> rcmmdBOs) {
        if (CollectionUtils.isEmpty(rcmmdBOs)) {
            return;
        }
        List<MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO> figureList = new ArrayList<>();
        List<MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO> bookList = new ArrayList<>();
        for (MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO item : rcmmdBOs) {
            if (BookRcmmdTypeEnum.AWARD.getCode().equals(item.getBizType())) {
                figureList.add(item);
            } else if (BookRcmmdTypeEnum.EDITOR.getCode().equals(item.getBizType())) {
                figureList.add(item);
            } else if (BookRcmmdTypeEnum.MEDIA.getCode().equals(item.getBizType())) {
                figureList.add(item);
            } else if (BookRcmmdTypeEnum.ORGAN.getCode().equals(item.getBizType())) {
                figureList.add(item);
            } else if (BookRcmmdTypeEnum.EXPERT.getCode().equals(item.getBizType())) {
                figureList.add(item);
            } else if (BookRcmmdTypeEnum.QUOTE.getCode().equals(item.getBizType())) {
                bookList.add(item);
            } else if (BookRcmmdTypeEnum.DRAFT.getCode().equals(item.getBizType())) {
                bookList.add(item);
            } else {
                log.error("书籍错误的推荐类型，type={}", item.getBizType());
            }
        }
        if (!figureList.isEmpty()) {
            List<Integer> figureIds = figureList.stream().map(item -> item.getBizId()).distinct().collect(Collectors.toList());
            Map<Integer, MetaFigure> figureMap = this.metaFigureService.listFigureByIds(figureIds).stream().collect(Collectors.toMap(MetaFigure::getId, item -> item, (a, b) -> a));
            for (MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO rcmmdBO : figureList) {
                if (figureMap.get(rcmmdBO.getBizId()) != null) {
                    rcmmdBO.setName(figureMap.get(rcmmdBO.getBizId()).getName());
                }
            }
        }
        if (!bookList.isEmpty()) {
            List<Integer> bookIds = bookList.stream().map(item -> item.getBizId()).collect(Collectors.toList());
            Map<Integer, MetaBook> bookMap = this.metaBookService.listEntityByIds(bookIds).stream().collect(Collectors.toMap(MetaBook::getId, item -> item, (a, b) -> a));
            for (MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO rcmmdBO : bookList) {
                if (bookMap.get(rcmmdBO.getBizId()) != null) {
                    rcmmdBO.setName(bookMap.get(rcmmdBO.getBizId()).getName());
                }
            }
        }
    }
}
