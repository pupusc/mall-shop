package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaBookQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBook;
import com.wanmi.sbc.goods.mapper.MetaBookMapper;
import com.wanmi.sbc.goods.provider.MetaBookProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 书籍(MetaBook)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Validated
@RestController
public class MetaBookProviderImpl implements MetaBookProvider {
    @Resource
    private MetaBookMapper metaBookMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBook> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBook>> queryByPage(@Valid MetaBookQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBook metaBook = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBook.class);
        
        page.setTotalCount((int) this.metaBookMapper.count(metaBook));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookMapper.queryAllByLimit(metaBook, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBook 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBook metaBook) {
        this.metaBookMapper.insertSelective(metaBook);
        return BusinessResponse.success(metaBook.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBook 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBook metaBook) {
        return BusinessResponse.success(this.metaBookMapper.update(metaBook) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookMapper.deleteById(id) > 0);
    }
}
