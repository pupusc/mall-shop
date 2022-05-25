package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaFigureAwardQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureAwardMapper;
import com.wanmi.sbc.bookmeta.provider.MetaFigureAwardProvider;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 人物获奖(MetaFigureAward)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaFigureAwardProviderImpl implements MetaFigureAwardProvider {
    @Resource
    private MetaFigureAwardMapper metaFigureAwardMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaFigureAward> queryById(Integer id) {
        return BusinessResponse.success(this.metaFigureAwardMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaFigureAward>> queryByPage(@Valid MetaFigureAwardQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaFigureAward metaFigureAward = JSON.parseObject(JSON.toJSONString(pageRequest), MetaFigureAward.class);
        
        page.setTotalCount((int) this.metaFigureAwardMapper.count(metaFigureAward));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaFigureAwardMapper.queryAllByLimit(metaFigureAward, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaFigureAward 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaFigureAward metaFigureAward) {
        this.metaFigureAwardMapper.insertSelective(metaFigureAward);
        return BusinessResponse.success(metaFigureAward.getId());
    }

    /**
     * 修改数据
     *
     * @param metaFigureAward 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaFigureAward metaFigureAward) {
        return BusinessResponse.success(this.metaFigureAwardMapper.update(metaFigureAward) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaFigureAwardMapper.deleteById(id) > 0);
    }
}
