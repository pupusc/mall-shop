package com.wanmi.sbc.goods.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.bo.MetaBookIndustryInfoQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBookIndustryInfo;
import com.wanmi.sbc.goods.mapper.MetaBookIndustryInfoMapper;
import com.wanmi.sbc.goods.provider.MetaBookIndustryInfoProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 行业数据(MetaBookIndustryInfo)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaBookIndustryInfoProviderImpl implements MetaBookIndustryInfoProvider {
    @Resource
    private MetaBookIndustryInfoMapper metaBookIndustryInfoMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookIndustryInfo> queryById(Integer id) {
        return BusinessResponse.success(this.metaBookIndustryInfoMapper.queryById(id));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookIndustryInfo>> queryByPage(@Valid MetaBookIndustryInfoQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookIndustryInfo metaBookIndustryInfo = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookIndustryInfo.class);
        
        page.setTotalCount((int) this.metaBookIndustryInfoMapper.count(metaBookIndustryInfo));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        return BusinessResponse.success(this.metaBookIndustryInfoMapper.queryAllByLimit(metaBookIndustryInfo, page.getOffset(), page.getPageSize()), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookIndustryInfo 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookIndustryInfo metaBookIndustryInfo) {
        this.metaBookIndustryInfoMapper.insertSelective(metaBookIndustryInfo);
        return BusinessResponse.success(metaBookIndustryInfo.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookIndustryInfo 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookIndustryInfo metaBookIndustryInfo) {
        return BusinessResponse.success(this.metaBookIndustryInfoMapper.update(metaBookIndustryInfo) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaBookIndustryInfoMapper.deleteById(id) > 0);
    }
}
