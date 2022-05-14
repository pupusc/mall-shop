package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaBookRcmmdQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBookRcmmd;
import com.wanmi.sbc.goods.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.goods.provider.MetaBookRcmmdProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 书籍推荐(MetaBookRcmmd)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaBookRcmmdProviderImpl implements MetaBookRcmmdProvider {
    @Resource
    private MetaBookRcmmdMapper metaBookRcmmdMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookRcmmd> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookRcmmdMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookRcmmd>> queryByPage(@Valid MetaBookRcmmdQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookRcmmd metaBookRcmmd = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookRcmmd.class);
        
        page.setTotalCount((int) this.metaBookRcmmdMapper.count(metaBookRcmmd));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookRcmmdMapper.queryAllByLimit(metaBookRcmmd, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookRcmmd metaBookRcmmd) {
        this.metaBookRcmmdMapper.insertSelective(metaBookRcmmd);
        return BusinessResponse.success(metaBookRcmmd.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookRcmmd 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookRcmmd metaBookRcmmd) {
        return BusinessResponse.success(this.metaBookRcmmdMapper.update(metaBookRcmmd) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookRcmmdMapper.deleteById(id) > 0);
    }
}
