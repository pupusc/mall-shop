package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaBookExtendQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBookExtend;
import com.wanmi.sbc.goods.mapper.MetaBookExtendMapper;
import com.wanmi.sbc.goods.provider.MetaBookExtendProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 书籍扩展属性(MetaBookExtend)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Validated
@RestController
public class MetaBookExtendProviderImpl implements MetaBookExtendProvider {
    @Resource
    private MetaBookExtendMapper metaBookExtendMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookExtend> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookExtendMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookExtend>> queryByPage(@Valid MetaBookExtendQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookExtend metaBookExtend = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookExtend.class);
        
        page.setTotalCount((int) this.metaBookExtendMapper.count(metaBookExtend));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookExtendMapper.queryAllByLimit(metaBookExtend, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookExtend 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookExtend metaBookExtend) {
        this.metaBookExtendMapper.insertSelective(metaBookExtend);
        return BusinessResponse.success(metaBookExtend.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookExtend 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookExtend metaBookExtend) {
        return BusinessResponse.success(this.metaBookExtendMapper.update(metaBookExtend) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookExtendMapper.deleteById(id) > 0);
    }
}
