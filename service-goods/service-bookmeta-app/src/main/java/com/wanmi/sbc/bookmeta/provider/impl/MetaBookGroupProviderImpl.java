package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookGroupBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookGroupQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookGroup;
import com.wanmi.sbc.bookmeta.mapper.MetaBookGroupMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookGroupProvider;
import com.wanmi.sbc.bookmeta.service.MetaBookGroupService;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * 书组(MetaBookGroup)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Validated
@RestController
public class MetaBookGroupProviderImpl implements MetaBookGroupProvider {
    @Resource
    private MetaBookGroupMapper metaBookGroupMapper;
    @Resource
    private MetaBookGroupService metaBookGroupService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaBookGroupBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaBookGroupMapper.queryById(id), MetaBookGroupBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaBookGroupBO>> queryByPage(@Valid MetaBookGroupQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaBookGroup metaBookGroup = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookGroup.class);
        
        page.setTotalCount((int) this.metaBookGroupMapper.count(metaBookGroup));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<MetaBookGroup> metaBookGroups = this.metaBookGroupMapper.queryAllByLimit(metaBookGroup, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaBookGroups, MetaBookGroupBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaBookGroup 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaBookGroupBO metaBookGroup) {
        this.metaBookGroupMapper.insertSelective(DO2BOUtils.objA2objB(metaBookGroup, MetaBookGroup.class));
        return BusinessResponse.success(metaBookGroup.getId());
    }

    /**
     * 修改数据
     *
     * @param metaBookGroup 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaBookGroupBO metaBookGroup) {
        MetaBookGroup entity = this.metaBookGroupMapper.selectByPrimaryKey(metaBookGroup.getId());
        if (entity == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        entity.setName(metaBookGroup.getName());
        entity.setImage(metaBookGroup.getImage());
        entity.setDescr(metaBookGroup.getDescr());
        entity.setType(metaBookGroup.getType());
        this.metaBookGroupMapper.updateByPrimaryKey(entity);
        return BusinessResponse.success(true);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Transactional
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        metaBookGroupService.deleteById(id);
        return BusinessResponse.success(true);
    }
}
