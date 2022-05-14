package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaBookFigureQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBookFigure;
import com.wanmi.sbc.goods.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.goods.provider.MetaBookFigureProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 书籍人物(MetaBookFigure)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Validated
@RestController
public class MetaBookFigureProviderImpl implements MetaBookFigureProvider {
    @Resource
    private MetaBookFigureMapper metaBookFigureMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookFigure> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookFigureMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookFigure>> queryByPage(@Valid MetaBookFigureQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookFigure metaBookFigure = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookFigure.class);
        
        page.setTotalCount((int) this.metaBookFigureMapper.count(metaBookFigure));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookFigureMapper.queryAllByLimit(metaBookFigure, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookFigure 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookFigure metaBookFigure) {
        this.metaBookFigureMapper.insertSelective(metaBookFigure);
        return BusinessResponse.success(metaBookFigure.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookFigure 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookFigure metaBookFigure) {
        return BusinessResponse.success(this.metaBookFigureMapper.update(metaBookFigure) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookFigureMapper.deleteById(id) > 0);
    }
}
