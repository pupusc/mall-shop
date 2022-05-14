package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaLabelCateQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaLabelCate;
import com.wanmi.sbc.goods.mapper.MetaLabelCateMapper;
import com.wanmi.sbc.goods.provider.MetaLabelCateProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 标签目录(MetaLabelCate)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaLabelCateProviderImpl implements MetaLabelCateProvider {
    @Resource
    private MetaLabelCateMapper metaLabelCateMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaLabelCate> queryById(Integer id) {
        return BusinessResponse.success(this.metaLabelCateMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaLabelCate>> queryByPage(@Valid MetaLabelCateQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaLabelCate metaLabelCate = JSON.parseObject(JSON.toJSONString(pageRequest), MetaLabelCate.class);
        
        page.setTotalCount((int) this.metaLabelCateMapper.count(metaLabelCate));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaLabelCateMapper.queryAllByLimit(metaLabelCate, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaLabelCate 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaLabelCate metaLabelCate) {
        this.metaLabelCateMapper.insertSelective(metaLabelCate);
        return BusinessResponse.success(metaLabelCate.getId());
    }

    /**
     * 修改数据
     *
     * @param metaLabelCate 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaLabelCate metaLabelCate) {
        return BusinessResponse.success(this.metaLabelCateMapper.update(metaLabelCate) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaLabelCateMapper.deleteById(id) > 0);
    }
}
