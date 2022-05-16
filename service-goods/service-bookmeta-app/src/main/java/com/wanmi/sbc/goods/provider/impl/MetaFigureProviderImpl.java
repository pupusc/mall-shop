package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaFigureQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaFigure;
import com.wanmi.sbc.goods.mapper.MetaFigureMapper;
import com.wanmi.sbc.goods.provider.MetaFigureProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 人物(MetaFigure)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:27:01
 */
@Validated
@RestController
public class MetaFigureProviderImpl implements MetaFigureProvider {
    @Resource
    private MetaFigureMapper metaFigureMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaFigure> queryById(Integer id) {
        return BusinessResponse.success(this.metaFigureMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaFigure>> queryByPage(@Valid MetaFigureQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaFigure metaFigure = JSON.parseObject(JSON.toJSONString(pageRequest), MetaFigure.class);
        
        page.setTotalCount((int) this.metaFigureMapper.count(metaFigure));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaFigureMapper.queryAllByLimit(metaFigure, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaFigure 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaFigure metaFigure) {
        this.metaFigureMapper.insertSelective(metaFigure);
        return BusinessResponse.success(metaFigure.getId());
    }

    /**
     * 修改数据
     *
     * @param metaFigure 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaFigure metaFigure) {
        return BusinessResponse.success(this.metaFigureMapper.update(metaFigure) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaFigureMapper.deleteById(id) > 0);
    }
}
