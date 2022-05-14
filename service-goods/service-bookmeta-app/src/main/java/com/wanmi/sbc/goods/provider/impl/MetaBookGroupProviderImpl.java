package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaBookGroupQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBookGroup;
import com.wanmi.sbc.goods.mapper.MetaBookGroupMapper;
import com.wanmi.sbc.goods.provider.MetaBookGroupProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 书组(MetaBookGroup)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaBookGroupProviderImpl implements MetaBookGroupProvider {
    @Resource
    private MetaBookGroupMapper metaBookGroupMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookGroup> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookGroupMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookGroup>> queryByPage(@Valid MetaBookGroupQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookGroup metaBookGroup = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookGroup.class);
        
        page.setTotalCount((int) this.metaBookGroupMapper.count(metaBookGroup));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookGroupMapper.queryAllByLimit(metaBookGroup, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookGroup 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookGroup metaBookGroup) {
        this.metaBookGroupMapper.insertSelective(metaBookGroup);
        return BusinessResponse.success(metaBookGroup.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookGroup 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookGroup metaBookGroup) {
        return BusinessResponse.success(this.metaBookGroupMapper.update(metaBookGroup) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookGroupMapper.deleteById(id) > 0);
    }
}
