package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookLabelQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookLabelProvider;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 标签(MetaBookLabel)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaBookLabelProviderImpl implements MetaBookLabelProvider {
    @Resource
    private MetaBookLabelMapper metaBookLabelMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookLabel> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookLabelMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookLabel>> queryByPage(@Valid MetaBookLabelQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookLabel metaBookLabel = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookLabel.class);
        
        page.setTotalCount((int) this.metaBookLabelMapper.count(metaBookLabel));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookLabelMapper.queryAllByLimit(metaBookLabel, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookLabel 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookLabel metaBookLabel) {
        this.metaBookLabelMapper.insertSelective(metaBookLabel);
        return BusinessResponse.success(metaBookLabel.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookLabel 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookLabel metaBookLabel) {
        return BusinessResponse.success(this.metaBookLabelMapper.update(metaBookLabel) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookLabelMapper.deleteById(id) > 0);
    }
}
