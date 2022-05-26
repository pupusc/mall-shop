package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookIndustryInfoBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookIndustryInfoQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookIndustryInfo;
import com.wanmi.sbc.bookmeta.mapper.MetaBookIndustryInfoMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookIndustryInfoProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    public BusinessResponse<MetaBookIndustryInfoBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaBookIndustryInfoMapper.queryById(id), MetaBookIndustryInfoBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookIndustryInfoBO>> queryByPage(@Valid MetaBookIndustryInfoQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookIndustryInfo metaBookIndustryInfo = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookIndustryInfo.class);
        
        page.setTotalCount((int) this.metaBookIndustryInfoMapper.count(metaBookIndustryInfo));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBookIndustryInfo> metaBookIndustryInfos = this.metaBookIndustryInfoMapper.queryAllByLimit(metaBookIndustryInfo, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaBookIndustryInfos, MetaBookIndustryInfoBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookIndustryInfo 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookIndustryInfoBO metaBookIndustryInfo) {
        this.metaBookIndustryInfoMapper.insertSelective(DO2BOUtils.objA2objB(metaBookIndustryInfo, MetaBookIndustryInfo.class));
        return BusinessResponse.success(metaBookIndustryInfo.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookIndustryInfo 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookIndustryInfoBO metaBookIndustryInfo) {
        return BusinessResponse.success(this.metaBookIndustryInfoMapper.update(DO2BOUtils.objA2objB(metaBookIndustryInfo, MetaBookIndustryInfo.class)) > 0);
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
