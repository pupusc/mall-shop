package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaPublisherQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.mapper.MetaPublisherMapper;
import com.wanmi.sbc.bookmeta.provider.MetaPublisherProvider;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
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
    public BusinessResponse<MetaPublisher> queryById(Integer id) {
        return BusinessResponse.success(this.metaPublisherMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaPublisher>> queryByPage(@Valid MetaPublisherQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaPublisher metaPublisher = JSON.parseObject(JSON.toJSONString(pageRequest), MetaPublisher.class);
        
        page.setTotalCount((int) this.metaPublisherMapper.count(metaPublisher));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaPublisherMapper.queryAllByLimit(metaPublisher, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaPublisher 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaPublisher metaPublisher) {
        this.metaPublisherMapper.insertSelective(metaPublisher);
        return BusinessResponse.success(metaPublisher.getId());
    }

    /**
     * 修改数据
     *
     * @param metaPublisher 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaPublisher metaPublisher) {
        return BusinessResponse.success(this.metaPublisherMapper.update(metaPublisher) > 0);
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
