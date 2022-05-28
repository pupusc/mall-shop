package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaLabelBO;
import com.wanmi.sbc.bookmeta.bo.MetaLabelQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.entity.MetaLabelExt;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import com.wanmi.sbc.bookmeta.provider.MetaLabelProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * 标签(MetaLabel)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-17 11:38:03
 */
@Validated
@RestController
public class MetaLabelProviderImpl implements MetaLabelProvider {
    @Resource
    private MetaLabelMapper metaLabelMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaLabelBO> queryById(Integer id) {
        return BusinessResponse.success(DO2BOUtils.objA2objB(this.metaLabelMapper.queryById(id), MetaLabelBO.class));
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaLabelBO>> queryByPage(@Valid MetaLabelQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaLabel metaLabel = JSON.parseObject(JSON.toJSONString(pageRequest), MetaLabel.class);
        
        page.setTotalCount((int) this.metaLabelMapper.countExt(metaLabel));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }

        //List<MetaLabel> labels = this.metaLabelMapper.queryAllByLimit(metaLabel, page.getOffset(), page.getPageSize());
        List<MetaLabelExt> labels = this.metaLabelMapper.queryAllByLimitExt(metaLabel, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(labels, MetaLabelBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaLabelBO 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaLabelBO metaLabelBO) {
        MetaLabel metaLabel = DO2BOUtils.objA2objB(metaLabelBO, MetaLabel.class);
        validate(metaLabel, true);
        this.metaLabelMapper.insertSelective(metaLabel);
        return BusinessResponse.success(metaLabel.getId());
    }

    /**
     * 修改数据
     *
     * @param metaLabelBO 实例对象
     * @return 实例对象
     */
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaLabelBO metaLabelBO) {
        MetaLabel metaLabel = DO2BOUtils.objA2objB(metaLabelBO, MetaLabel.class);
        validate(metaLabel, false);

        MetaLabel entity = this.metaLabelMapper.selectByPrimaryKey(metaLabelBO.getId());
        if (entity == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        entity.setParentId(metaLabelBO.getParentId());
        entity.setPath(metaLabelBO.getPath());
        entity.setName(metaLabelBO.getName());
        entity.setSeq(metaLabel.getSeq());
        entity.setScene(metaLabel.getScene());
        entity.setDescr(metaLabel.getDescr());
        entity.setStatus(metaLabel.getStatus());
        this.metaLabelMapper.updateByPrimaryKey(metaLabel);
        return BusinessResponse.success(true);
    }

    private void validate(MetaLabel metaLabel, boolean newly) {
        Example example = new Example(MetaLabel.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("name", metaLabel.getName())
                .andEqualTo("parentId", metaLabel.getParentId());
        if (!newly) {
            criteria.andNotEqualTo("id", metaLabel.getId());
        }

        if (this.metaLabelMapper.selectCountByExample(example) > 0) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "相同名称已存在");
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        return BusinessResponse.success(this.metaLabelMapper.deleteById(id) > 0);
    }
}
