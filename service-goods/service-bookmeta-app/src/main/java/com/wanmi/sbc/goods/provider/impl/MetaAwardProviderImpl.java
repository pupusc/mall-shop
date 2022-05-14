package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaAwardQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaAward;
import com.wanmi.sbc.goods.mapper.MetaAwardMapper;
import com.wanmi.sbc.goods.provider.MetaAwardProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 奖项(MetaAward)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Validated
@RestController
public class MetaAwardProviderImpl implements MetaAwardProvider {
    @Resource
    private MetaAwardMapper metaAwardMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaAward> queryById(Integer id) {
        return BusinessResponse.success(this.metaAwardMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaAward>> queryByPage(@Valid MetaAwardQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaAward metaAward = JSON.parseObject(JSON.toJSONString(pageRequest), MetaAward.class);
        
        page.setTotalCount((int) this.metaAwardMapper.count(metaAward));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaAwardMapper.queryAllByLimit(metaAward, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaAward 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaAward metaAward) {
        this.metaAwardMapper.insertSelective(metaAward);
        return BusinessResponse.success(metaAward.getId());
    }

    /**
     * 修改数据
     *
     * @param metaAward 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaAward metaAward) {
        return BusinessResponse.success(this.metaAwardMapper.update(metaAward) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaAwardMapper.deleteById(id) > 0);
    }
}
