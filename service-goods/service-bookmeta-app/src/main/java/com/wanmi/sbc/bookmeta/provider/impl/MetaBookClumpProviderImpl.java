package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookClumpBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookClumpQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookClump;
import com.wanmi.sbc.bookmeta.mapper.MetaBookClumpMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookClumpProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * 丛书(MetaBookClump)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Validated
@RestController
public class MetaBookClumpProviderImpl implements MetaBookClumpProvider {
    @Resource
    private MetaBookClumpMapper metaBookClumpMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookClumpBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaBookClumpMapper.queryById(id), MetaBookClumpBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookClumpBO>> queryByPage(@Valid MetaBookClumpQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookClump metaBookClump = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookClump.class);
        
        page.setTotalCount((int) this.metaBookClumpMapper.count(metaBookClump));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBookClump> metaBookClumps = this.metaBookClumpMapper.queryAllByLimit(metaBookClump, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaBookClumps, MetaBookClumpBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookClump 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookClumpBO metaBookClump) {
        this.metaBookClumpMapper.insertSelective(DO2BOUtils.objA2objB(metaBookClump, MetaBookClump.class));
        return BusinessResponse.success(metaBookClump.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookClump 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookClumpBO metaBookClump) {
        return BusinessResponse.success(this.metaBookClumpMapper.update(DO2BOUtils.objA2objB(metaBookClump, MetaBookClump.class)) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookClumpMapper.deleteById(id) > 0);
    }
}
