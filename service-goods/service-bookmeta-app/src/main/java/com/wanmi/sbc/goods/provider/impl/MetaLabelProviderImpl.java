package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaLabelQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaLabel;
import com.wanmi.sbc.goods.mapper.MetaLabelMapper;
import com.wanmi.sbc.goods.provider.MetaLabelProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 标签(MetaLabel)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-16 17:15:50
 */
@Validated
@RestController
public class MetaLabelProviderImpl implements MetaLabelProvider {
    @Resource
    private MetaLabelMapper metaLabelMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaLabel> queryById(Integer id) {
        return BusinessResponse.success(this.metaLabelMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaLabel>> queryByPage(@Valid MetaLabelQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaLabel metaLabel = JSON.parseObject(JSON.toJSONString(pageRequest), MetaLabel.class);
        
        page.setTotalCount((int) this.metaLabelMapper.count(metaLabel));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaLabelMapper.queryAllByLimit(metaLabel, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaLabel 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaLabel metaLabel) {
        this.metaLabelMapper.insertSelective(metaLabel);
        return BusinessResponse.success(metaLabel.getId());
    }

    /**
     * 修改数据
     *
     * @param metaLabel 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaLabel metaLabel) {
        return BusinessResponse.success(this.metaLabelMapper.update(metaLabel) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaLabelMapper.deleteById(id) > 0);
    }
}
