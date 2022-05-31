package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaPublisherBO;
import com.wanmi.sbc.bookmeta.bo.MetaPublisherQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import com.wanmi.sbc.bookmeta.mapper.MetaPublisherMapper;
import com.wanmi.sbc.bookmeta.provider.MetaPublisherProvider;
import com.wanmi.sbc.bookmeta.service.ParamValidator;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * 出版社(MetaPublisher)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:03
 */
@Validated
@RestController
public class MetaPublisherProviderImpl implements MetaPublisherProvider {
    @Resource
    private MetaPublisherMapper metaPublisherMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaPublisherBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaPublisherMapper.queryById(id), MetaPublisherBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaPublisherBO>> queryByPage(@Valid MetaPublisherQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaPublisher metaPublisher = JSON.parseObject(JSON.toJSONString(pageRequest), MetaPublisher.class);
        
        page.setTotalCount((int) this.metaPublisherMapper.count(metaPublisher));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }

        List<MetaPublisher> metaPublishers = this.metaPublisherMapper.queryAllByLimit(metaPublisher, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaPublishers, MetaPublisherBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaPublisher 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaPublisherBO metaPublisher) {
        ParamValidator.validPropValueExist("name", metaPublisher.getName(), "id", metaPublisher.getId(), this.metaPublisherMapper, MetaPublisher.class);
        this.metaPublisherMapper.insertSelective(DO2BOUtils.objA2objB(metaPublisher, MetaPublisher.class));
        return BusinessResponse.success(metaPublisher.getId());
    }

    /**
     * 修改数据
     *
     * @param metaPublisher 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaPublisherBO metaPublisher) {
        ParamValidator.validPropValueExist("name", metaPublisher.getName(), "id", metaPublisher.getId(), this.metaPublisherMapper, MetaPublisher.class);
        MetaPublisher entity = this.metaPublisherMapper.selectByPrimaryKey(metaPublisher.getId());
        if (entity == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        entity.setName(metaPublisher.getName());
        entity.setImage(metaPublisher.getImage());
        entity.setDescr(metaPublisher.getDescr());
        entity.setBuildTime(metaPublisher.getBuildTime());
        this.metaPublisherMapper.updateByPrimaryKey(entity);
        return BusinessResponse.success(true);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaPublisherMapper.deleteById(id) > 0);
    }
}
