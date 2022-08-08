package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.MetaBookContentBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookContentByBookIdReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.mapper.MetaBookContentMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookContentProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    @Autowired
    private MetaFigureMapper metaFigureMapper;

//    /**
//     * 通过ID查询单条数据
//     *
//     * @param id 主键
//     * @return 实例对象
//     */
//    @Override
//    public BusinessResponse<MetaBookContentBO> queryById(Integer id) {
//        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaBookContentMapper.queryById(id), MetaBookContentBO.class));
//    }
//
//    /**
//     * 分页查询
//     *
//     * @param pageRequest 分页对象
//     * @return 查询结果
//     */
//    @Override
//    public BusinessResponse<List<MetaBookContentBO>> queryByPage(@Valid MetaBookContentQueryByPageReqBO pageRequest) {
//        Page page = pageRequest.getPage();
//        MetaBookContent metaBookContent = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookContent.class);
//
//        page.setTotalCount((int) this.metaBookContentMapper.count(metaBookContent));
//        if (page.getTotalCount() <= 0) {
//            return BusinessResponse.success(Collections.EMPTY_LIST, page);
//        }
//        List<MetaBookContent> metaBookContents = this.metaBookContentMapper.queryAllByLimit(metaBookContent, page.getOffset(), page.getPageSize());
//        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaBookContents, MetaBookContentBO.class), page);
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param metaBookContent 实例对象
//     * @return 实例对象
//     */
//    @Override
//    public BusinessResponse<Integer> insert(@Valid MetaBookContentBO metaBookContent) {
//        this.metaBookContentMapper.insertSelective(DO2BOUtils.objA2objB(metaBookContent, MetaBookContent.class));
//        return BusinessResponse.success(metaBookContent.getId());
//    }
//
//    /**
//     * 修改数据
//     *
//     * @param metaBookContent 实例对象
//     * @return 实例对象
//     */
//    @Override
//    public BusinessResponse<Boolean> update(@Valid MetaBookContentBO metaBookContent) {
//        return BusinessResponse.success(this.metaBookContentMapper.update(DO2BOUtils.objA2objB(metaBookContent, MetaBookContent.class)) > 0);
//    }
//
//    /**
//     * 通过主键删除数据
//     *
//     * @param id 主键
//     * @return 是否成功
//     */
//    @Override
//    public BusinessResponse<Boolean> deleteById(Integer id) {
//        return BusinessResponse.success(this.metaBookContentMapper.deleteById(id) > 0);
//    }

    @Override
    public BusinessResponse<List<MetaBookContentBO>> queryByBookId(Integer bookId) {
        MetaBookContent query = new MetaBookContent();
        query.setBookId(bookId);
        query.setDelFlag(0);
        List<MetaBookContent> contentDTOs = this.metaBookContentMapper.select(query);

        if (CollectionUtils.isEmpty(contentDTOs)) {
            return BusinessResponse.success(Collections.EMPTY_LIST);
        }

        Map<Integer, MetaFigure> figureM = new HashMap<>();
        List<Integer> figureIds = contentDTOs.stream().map(MetaBookContent::getFigureId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(figureIds)) {
            Example exampleFigure = new Example(MetaFigure.class);
            exampleFigure.createCriteria().andEqualTo("delFlag", 0).andIn("id", figureIds);
            List<MetaFigure> figures = this.metaFigureMapper.selectByExample(exampleFigure);
            figureM = figures.stream().collect(Collectors.toMap(MetaFigure::getId, i->i, (a,b)->a));
        }
        //转换BO
        List<MetaBookContentBO> contentBOs = new ArrayList<>();
        for (MetaBookContent item : contentDTOs) {
            MetaBookContentBO contentBO = new MetaBookContentBO();
            BeanUtils.copyProperties(item, contentBO);
            if (figureM.get(contentBO.getFigureId()) != null) {
                contentBO.setFigureName(figureM.get(contentBO.getFigureId()).getName());
            }
            contentBOs.add(contentBO);
        }
        return BusinessResponse.success(contentBOs);
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

        if (CollectionUtils.isEmpty(editReqBO)) {
            return BusinessResponse.success(true);
        }
        Date curr = new Date();
        List<MetaBookContent> insertList = editReqBO.stream().filter(
                item-> StringUtils.isNotBlank(item.getContent())).map(item -> {
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
        if (CollectionUtils.isNotEmpty(insertList)) {
            this.metaBookContentMapper.insertBatch(insertList);
        }
        return BusinessResponse.success(true);
    }
}
