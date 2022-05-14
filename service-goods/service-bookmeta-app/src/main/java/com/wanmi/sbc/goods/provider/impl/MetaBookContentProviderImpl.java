package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaBookContentQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBookContent;
import com.wanmi.sbc.goods.mapper.MetaBookContentMapper;
import com.wanmi.sbc.goods.provider.MetaBookContentProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 书籍内容描述(MetaBookContent)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Validated
@RestController
public class MetaBookContentProviderImpl implements MetaBookContentProvider {
    @Resource
    private MetaBookContentMapper metaBookContentMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookContent> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookContentMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookContent>> queryByPage(@Valid MetaBookContentQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookContent metaBookContent = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookContent.class);
        
        page.setTotalCount((int) this.metaBookContentMapper.count(metaBookContent));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookContentMapper.queryAllByLimit(metaBookContent, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookContent 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookContent metaBookContent) {
        this.metaBookContentMapper.insertSelective(metaBookContent);
        return BusinessResponse.success(metaBookContent.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookContent 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookContent metaBookContent) {
        return BusinessResponse.success(this.metaBookContentMapper.update(metaBookContent) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookContentMapper.deleteById(id) > 0);
    }
}
