package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdByBookIdReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.enums.LabelSceneEnum;
import com.wanmi.sbc.bookmeta.enums.LabelTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookRcmmdProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 书籍推荐(MetaBookRcmmd)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
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

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookRcmmd> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookRcmmdMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookRcmmd>> queryByPage(@Valid MetaBookRcmmdQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookRcmmd metaBookRcmmd = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookRcmmd.class);
        
        page.setTotalCount((int) this.metaBookRcmmdMapper.count(metaBookRcmmd));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookRcmmdMapper.queryAllByLimit(metaBookRcmmd, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookRcmmd metaBookRcmmd) {
        this.metaBookRcmmdMapper.insertSelective(metaBookRcmmd);
        return BusinessResponse.success(metaBookRcmmd.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookRcmmd metaBookRcmmd) {
        return BusinessResponse.success(this.metaBookRcmmdMapper.update(metaBookRcmmd) > 0);
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
        result.setFitTargetIds(getFitIdsByBookId(bookId));
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

    private List<Integer> getFitIdsByBookId(Integer bookId) {
        List<MetaBookLabel> bookLabels = getBookLabels(bookId);
        if (CollectionUtils.isEmpty(bookLabels)) {
            return Collections.EMPTY_LIST;
        }
        //适读对象标签
        List<Integer> labelIds = getFitTargetLabelIds();
        return bookLabels.stream().map(MetaBookLabel::getLabelId).filter(item->labelIds.contains(item)).collect(Collectors.toList());
    }

    private void updateFitIdsByBookId(Integer bookId, List<Integer> editFitTargetIds) {
        List<MetaBookLabel> bookLabels = getBookLabels(bookId);
        List<Integer> allFitLabelIds = getFitTargetLabelIds();
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
    /**
     * 适读对象标签
     */
    private List<Integer> getFitTargetLabelIds() {
        MetaLabel queryLabel = new MetaLabel();
        queryLabel.setType(LabelTypeEnum.LABEL.getCode());
        queryLabel.setScene(LabelSceneEnum.FIT_TARGET.getCode());
        queryLabel.setDelFlag(0);
        return metaLabelMapper.select(queryLabel).stream().map(MetaLabel::getId).collect(Collectors.toList());
    }
}
