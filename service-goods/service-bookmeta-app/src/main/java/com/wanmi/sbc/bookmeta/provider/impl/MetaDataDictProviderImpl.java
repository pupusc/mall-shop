package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaDataDictBO;
import com.wanmi.sbc.bookmeta.bo.MetaDataDictQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaDataDict;
import com.wanmi.sbc.bookmeta.mapper.MetaDataDictMapper;
import com.wanmi.sbc.bookmeta.provider.MetaDataDictProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * 数据字典(MetaDataDict)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-24 00:37:02
 */
@Validated
@RestController
public class MetaDataDictProviderImpl implements MetaDataDictProvider {
    @Resource
    private MetaDataDictMapper metaDataDictMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaDataDictBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaDataDictMapper.queryById(id), MetaDataDictBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaDataDictBO>> queryByPage(@Valid MetaDataDictQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaDataDict metaDataDict = JSON.parseObject(JSON.toJSONString(pageRequest), MetaDataDict.class);
        
        page.setTotalCount((int) this.metaDataDictMapper.count(metaDataDict));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }

        List<MetaDataDict> metaDataDicts = this.metaDataDictMapper.queryAllByLimit(metaDataDict, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaDataDicts, MetaDataDictBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaDataDict 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaDataDictBO metaDataDict) {
        this.metaDataDictMapper.insertSelective(DO2BOUtils.objA2objB(metaDataDict, MetaDataDict.class));
        return BusinessResponse.success(metaDataDict.getId());
    }

    /**
     * 修改数据
     *
     * @param metaDataDict 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaDataDictBO metaDataDict) {
        return BusinessResponse.success(this.metaDataDictMapper.update(DO2BOUtils.objA2objB(metaDataDict, MetaDataDict.class)) > 0);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaDataDictMapper.deleteById(id) > 0);
    }
}
