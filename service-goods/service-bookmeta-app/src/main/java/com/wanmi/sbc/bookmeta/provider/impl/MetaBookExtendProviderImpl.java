package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookExtendBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookExtendQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookExtend;
import com.wanmi.sbc.bookmeta.mapper.MetaBookExtendMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookExtendProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    public BusinessResponse<MetaBookExtendBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaBookExtendMapper.queryById(id), MetaBookExtendBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookExtendBO>> queryByPage(@Valid MetaBookExtendQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookExtend metaBookExtend = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookExtend.class);
        
        page.setTotalCount((int) this.metaBookExtendMapper.count(metaBookExtend));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBookExtend> metaBookExtends = this.metaBookExtendMapper.queryAllByLimit(metaBookExtend, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaBookExtends, MetaBookExtendBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookExtend 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookExtendBO metaBookExtend) {
        this.metaBookExtendMapper.insertSelective(DO2BOUtils.objA2objB(metaBookExtend, MetaBookExtend.class));
        return BusinessResponse.success(metaBookExtend.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookExtend 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookExtendBO metaBookExtend) {
        return BusinessResponse.success(this.metaBookExtendMapper.update(DO2BOUtils.objA2objB(metaBookExtend, MetaBookExtend.class)) > 0);
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
