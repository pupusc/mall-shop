package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookContentBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookContentByBookIdReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookContentQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.mapper.MetaBookContentMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookContentProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 书籍内容描述(MetaBookContent)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Validated
@RestController
public class MetaBookContentProviderImpl implements MetaBookContentProvider {
    @Resource
    private MetaBookContentMapper metaBookContentMapper;
    @Resource
    private MetaBookMapper metaBookMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookContentBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaBookContentMapper.queryById(id), MetaBookContentBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookContentBO>> queryByPage(@Valid MetaBookContentQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookContent metaBookContent = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookContent.class);
        
        page.setTotalCount((int) this.metaBookContentMapper.count(metaBookContent));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBookContent> metaBookContents = this.metaBookContentMapper.queryAllByLimit(metaBookContent, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaBookContents, MetaBookContentBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookContent 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookContentBO metaBookContent) {
        this.metaBookContentMapper.insertSelective(DO2BOUtils.objA2objB(metaBookContent, MetaBookContent.class));
        return BusinessResponse.success(metaBookContent.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookContent 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookContentBO metaBookContent) {
        return BusinessResponse.success(this.metaBookContentMapper.update(DO2BOUtils.objA2objB(metaBookContent, MetaBookContent.class)) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookContentMapper.deleteById(id) > 0);
    }

    @Override
    public BusinessResponse<List<MetaBookContentBO>> queryByBookId(Integer bookId) {
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(selectByBookId(bookId), MetaBookContentBO.class));
    }

    @Transactional
    @Override
    public BusinessResponse<Boolean> editByBookId(Integer bookId, List<MetaBookContentByBookIdReqBO> editReqBO) {
        MetaBook metaBook = metaBookMapper.queryById(bookId);
        if (metaBook == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        MetaBookContent delete = new MetaBookContent();
        delete.setBookId(bookId);
        this.metaBookContentMapper.delete(delete);

        if (CollectionUtils.isNotEmpty(editReqBO)) {
            Date curr = new Date();
            List<MetaBookContent> insertList = editReqBO.stream().map(item -> {
                MetaBookContent insert = new MetaBookContent();
                insert.setBookId(bookId);
                insert.setFigureId(item.getFigureId());
                insert.setType(item.getType());
                insert.setContent(item.getContent());
                insert.setCreateTime(curr);
                insert.setUpdateTime(curr);
                insert.setDelFlag(0);
                return insert;
            }).collect(Collectors.toList());
            this.metaBookContentMapper.insertBatch(insertList);
        }
        return BusinessResponse.success(true);
    }

    private List<MetaBookContent> selectByBookId(Integer bookId) {
        if (bookId == null) {
            return Collections.EMPTY_LIST;
        }
        MetaBookContent query = new MetaBookContent();
        query.setBookId(bookId);
        query.setDelFlag(0);
        return this.metaBookContentMapper.select(query);
    }
}
