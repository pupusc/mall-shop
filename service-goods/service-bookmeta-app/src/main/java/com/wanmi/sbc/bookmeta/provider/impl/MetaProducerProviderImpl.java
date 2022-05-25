package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaProducerQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.mapper.MetaProducerMapper;
import com.wanmi.sbc.bookmeta.provider.MetaProducerProvider;
import com.wanmi.sbc.bookmeta.entity.MetaProducer;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
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

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaProducer> queryById(Integer id) {
        return BusinessResponse.success(this.metaProducerMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaProducer>> queryByPage(@Valid MetaProducerQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaProducer metaProducer = JSON.parseObject(JSON.toJSONString(pageRequest), MetaProducer.class);
        
        page.setTotalCount((int) this.metaProducerMapper.count(metaProducer));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaProducerMapper.queryAllByLimit(metaProducer, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaProducer 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaProducer metaProducer) {
        this.metaProducerMapper.insertSelective(metaProducer);
        return BusinessResponse.success(metaProducer.getId());
    }

    /**
     * 修改数据
     *
     * @param metaProducer 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaProducer metaProducer) {
        return BusinessResponse.success(this.metaProducerMapper.update(metaProducer) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaProducerMapper.deleteById(id) > 0);
    }
}
