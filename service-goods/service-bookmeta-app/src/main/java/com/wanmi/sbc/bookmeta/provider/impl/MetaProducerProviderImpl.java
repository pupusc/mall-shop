package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaProducerBO;
import com.wanmi.sbc.bookmeta.bo.MetaProducerQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import com.wanmi.sbc.bookmeta.mapper.MetaProducerMapper;
import com.wanmi.sbc.bookmeta.provider.MetaProducerProvider;
import com.wanmi.sbc.bookmeta.service.MetaProducerService;
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
 * 出品方(MetaProducer)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaProducerProviderImpl implements MetaProducerProvider {
    @Resource
    private MetaProducerMapper metaProducerMapper;
    @Resource
    private MetaProducerService metaProducerService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaProducerBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaProducerMapper.queryById(id), MetaProducerBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaProducerBO>> queryByPage(@Valid MetaProducerQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaProducer metaProducer = JSON.parseObject(JSON.toJSONString(pageRequest), MetaProducer.class);
        
        page.setTotalCount((int) this.metaProducerMapper.count(metaProducer));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }

        List<MetaProducer> metaProducers = this.metaProducerMapper.queryAllByLimit(metaProducer, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaProducers, MetaProducerBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaProducer 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaProducerBO metaProducer) {
        ParamValidator.validPropValueExist("name", metaProducer.getName(), metaProducer.getId(), this.metaProducerMapper, MetaProducer.class);
        this.metaProducerMapper.insertSelective(DO2BOUtils.objA2objB(metaProducer, MetaProducer.class));
        return BusinessResponse.success(metaProducer.getId());
    }

    /**
     * 修改数据
     *
     * @param metaProducer 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaProducerBO metaProducer) {
        ParamValidator.validPropValueExist("name", metaProducer.getName(), metaProducer.getId(), this.metaProducerMapper, MetaProducer.class);
        MetaProducer entity = this.metaProducerMapper.selectByPrimaryKey(metaProducer.getId());
        if (entity == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        entity.setName(metaProducer.getName());
        entity.setImage(metaProducer.getImage());
        entity.setDescr(metaProducer.getDescr());
        this.metaProducerMapper.updateByPrimaryKey(entity);
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
        this.metaProducerService.deleteById(id);
        return BusinessResponse.success(true);
    }
}
