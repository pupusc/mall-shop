package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaAwardBO;
import com.wanmi.sbc.bookmeta.bo.MetaAwardQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaAward;
import com.wanmi.sbc.bookmeta.mapper.MetaAwardMapper;
import com.wanmi.sbc.bookmeta.provider.MetaAwardProvider;
import com.wanmi.sbc.bookmeta.service.MetaAwardService;
import com.wanmi.sbc.bookmeta.service.ParamValidator;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * 奖项(MetaAward)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */

@Intercepts({@Signature(
        type= Executor.class,
        method = "update",
        args = {MappedStatement.class,Object.class})})
@Validated
@RestController
public class MetaAwardProviderImpl implements MetaAwardProvider {
    @Resource
    private MetaAwardMapper metaAwardMapper;
    @Resource
    private MetaAwardService metaAwardService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaAwardBO> queryById(Integer id) {
        MetaAward entity = this.metaAwardMapper.queryById(id);
        return BusinessResponse.success(DO2BOUtils.objA2objB(entity, MetaAwardBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaAwardBO>> queryByPage(@Valid MetaAwardQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaAward metaAward = JSON.parseObject(JSON.toJSONString(pageRequest), MetaAward.class);
        
        page.setTotalCount((int) this.metaAwardMapper.count(metaAward));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }

        List<MetaAward> metaAwards = this.metaAwardMapper.queryAllByLimit(metaAward, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaAwards, MetaAwardBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaAward 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaAwardBO metaAward) {
        ParamValidator.validPropValueExist("name", metaAward.getName(), metaAward.getId(), metaAwardMapper, MetaAward.class);
        this.metaAwardMapper.insertSelective(DO2BOUtils.objA2objB(metaAward, MetaAward.class));
        return BusinessResponse.success(metaAward.getId());
    }

    /**
     * 修改数据
     *
     * @param metaAward 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaAwardBO metaAward) {
        ParamValidator.validPropValueExist("name", metaAward.getName(), metaAward.getId(), metaAwardMapper, MetaAward.class);
        MetaAward entity = this.metaAwardMapper.selectByPrimaryKey(metaAward.getId());
        if (entity == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        entity.setName(metaAward.getName());
        entity.setImage(metaAward.getImage());
        entity.setDescr(metaAward.getDescr());
        this.metaAwardMapper.updateByPrimaryKey(entity);
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
        this.metaAwardService.deleteById(id);
        return BusinessResponse.success(true);
    }

    @Override
    public BusinessResponse<List<MetaAwardBO>> queryBySku(String sku) {
        return metaAwardService.queryBySku(sku);
    }
}
