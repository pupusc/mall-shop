package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.bookmeta.entity.MetaAward;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.bookmeta.mapper.MetaAwardMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureAwardMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureMapper;
import com.wanmi.sbc.bookmeta.provider.MetaFigureProvider;
import com.wanmi.sbc.bookmeta.service.MetaFigureService;
import com.wanmi.sbc.bookmeta.service.ParamValidator;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 人物(MetaFigure)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:27:01
 */
@Validated
@RestController
public class MetaFigureProviderImpl implements MetaFigureProvider {
    @Resource
    private MetaFigureMapper metaFigureMapper;
    @Resource
    private MetaFigureAwardMapper metaFigureAwardMapper;
    @Resource
    private MetaFigureService metaFigureService;
    @Autowired
    private MetaAwardMapper metaAwardMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaFigureQueryByIdResBO> queryById(Integer id) {
        MetaFigure metaFigureDO = this.metaFigureMapper.queryById(id);
        if (metaFigureDO == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }

        MetaFigureQueryByIdResBO resultBO = new MetaFigureQueryByIdResBO();
        BeanUtils.copyProperties(metaFigureDO, resultBO);
        //获奖信息
        MetaFigureAward figureAwardQuery = new MetaFigureAward();
        figureAwardQuery.setFigureId(id);
        figureAwardQuery.setDelFlag(0);
        List<MetaFigureAward> metaFigureAwards = this.metaFigureAwardMapper.select(figureAwardQuery);
        if (CollectionUtils.isEmpty(metaFigureAwards)) {
            return BusinessResponse.success(resultBO);
        }

        //奖项ids
        List<Integer> awardIds = metaFigureAwards.stream().map(MetaFigureAward::getAwardId).collect(Collectors.toList());

        Example exampleAward = new Example(MetaAward.class);
        exampleAward.createCriteria().andEqualTo("delFlag", 0).andIn("id", awardIds);
        List<MetaAward> metaAwards = metaAwardMapper.selectByExample(exampleAward);
        Map<Integer, MetaAward> metaAwardM = metaAwards.stream().collect(Collectors.toMap(MetaAward::getId, i -> i, (a, b) -> a));

        for (MetaFigureAward item : metaFigureAwards) {
            MetaFigureQueryByIdResBO.FigureAward figureAwardBO = new MetaFigureQueryByIdResBO.FigureAward();
            figureAwardBO.setAwardId(item.getAwardId());
            figureAwardBO.setAwardTime(item.getAwardTime());
            if (metaAwardM.containsKey(item.getAwardId())) {
                figureAwardBO.setAwardName(metaAwardM.get(item.getAwardId()).getName());
            }
            resultBO.getAwardList().add(figureAwardBO);
        }
        return BusinessResponse.success(resultBO);
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaFigureBO>> queryByPage(@Valid MetaFigureQueryByPageReqBO pageRequest) {
        Example example = new Example(MetaFigure.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("delFlag", 0);
        if (pageRequest.getType() != null) {
            criteria.andEqualTo("type", pageRequest.getType());
        }
        if (!CollectionUtils.isEmpty(pageRequest.getTypeIn())) {
            criteria.andIn("type", pageRequest.getTypeIn());
        }
        if (StringUtils.isNotBlank(pageRequest.getName())) {
            criteria.andLike("name", "%" + pageRequest.getName() + "%");
        }

        Page page = pageRequest.getPage();
        page.setTotalCount(this.metaFigureMapper.selectCountByExample(example));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }

        example.setOrderByClause("create_time desc limit " + page.getOffset() + "," + page.getPageSize());
        List<MetaFigure> metaFigures = this.metaFigureMapper.selectByExample(example);
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(metaFigures, MetaFigureBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param addReqBO 实例对象
     * @return 实例对象
     */
    @Transactional
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaFigureAddReqBO addReqBO) {
        //验证名称
        checkName(addReqBO.isCheckName(), addReqBO.getName(), null);
        //人物
        MetaFigure insertFigure = new MetaFigure();
        BeanUtils.copyProperties(addReqBO, insertFigure);
        this.metaFigureMapper.insertSelective(insertFigure);
        //获奖
        if (!CollectionUtils.isEmpty(addReqBO.getAwardList())) {
            Date now = new Date();
            List<MetaFigureAward> figureAwards = addReqBO.getAwardList().stream().map(item -> {
                MetaFigureAward figureAward = new MetaFigureAward();
                figureAward.setFigureId(insertFigure.getId());
                figureAward.setAwardId(item.getAwardId());
                figureAward.setAwardTime(item.getAwardTime());
                figureAward.setCreateTime(now);
                figureAward.setUpdateTime(now);
                figureAward.setDelFlag(0);
                return figureAward;
            }).collect(Collectors.toList());
            metaFigureAwardMapper.insertBatch(figureAwards);
        }
        return BusinessResponse.success(insertFigure.getId());
    }

    /**
     * 修改数据
     *
     * @param editReqBO 实例对象
     * @return 实例对象
     */
    @Transactional
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaFigureEditReqBO editReqBO) {
        //验证名称
        checkName(editReqBO.isCheckName(), editReqBO.getName(), editReqBO.getId());

        MetaFigure metaFigureDO = this.metaFigureMapper.queryById(editReqBO.getId());
        if (metaFigureDO == null) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        //人物信息
        BeanUtils.copyProperties(editReqBO, metaFigureDO);
        this.metaFigureMapper.update(metaFigureDO);
        //获奖信息
        MetaFigureAward figureAwardDelete = new MetaFigureAward();
        figureAwardDelete.setFigureId(editReqBO.getId());
        this.metaFigureAwardMapper.delete(figureAwardDelete);

        if (!CollectionUtils.isEmpty(editReqBO.getAwardList())) {
            Date now = new Date();
            List<MetaFigureAward> figureAwards = editReqBO.getAwardList().stream().map(item -> {
                MetaFigureAward figureAward = new MetaFigureAward();
                figureAward.setFigureId(editReqBO.getId());
                figureAward.setAwardId(item.getAwardId());
                figureAward.setAwardTime(item.getAwardTime());
                figureAward.setCreateTime(now);
                figureAward.setUpdateTime(now);
                figureAward.setDelFlag(0);
                return figureAward;
            }).collect(Collectors.toList());
            metaFigureAwardMapper.insertBatch(figureAwards);
        }

        return BusinessResponse.success(true);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        this.metaFigureService.deleteById(id);
        return BusinessResponse.success(true);
    }

    @Override
    public BusinessResponse<List<MetaBookRcmmdFigureBO>> listFigureByskuId(String skuId) {
        return BusinessResponse.success(metaFigureService.listFigureByskuId(skuId));
    }

    private void checkName(Boolean check, String figureName, Integer figureId) {
        if (Boolean.TRUE.equals(check)) {
            ParamValidator.validPropValueExist("name", figureName, figureId, this.metaFigureMapper, MetaFigure.class);
        }
    }
}
