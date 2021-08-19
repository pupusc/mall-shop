package com.wanmi.sbc.goods.ruleinfo.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.RuleCacheType;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoQueryRequest;
import com.wanmi.sbc.goods.bean.vo.RuleInfoVO;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.ruleinfo.model.root.RuleInfo;
import com.wanmi.sbc.goods.ruleinfo.repository.RuleInfoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>规则说明业务逻辑</p>
 *
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@Service("RuleInfoService")
public class RuleInfoService {
    @Autowired
    private RuleInfoRepository ruleInfoRepository;


    /**
     * 新增规则说明
     *
     * @author zxd
     */
    @Transactional
    public RuleInfo add(RuleInfo entity) {
        entity.setCreateTime(LocalDateTime.now());
        ruleInfoRepository.save(entity);
        return entity;
    }

    @Autowired
    private RedisService redisService;

    /**
     * 修改规则说明
     *
     * @author zxd
     */
    @Transactional
    public RuleInfo modify(RuleInfo entity) {
        entity.setUpdateTime(LocalDateTime.now());
        ruleInfoRepository.save(entity);
        String ruleContent = entity.getRuleContent();
        if (StringUtils.isBlank(ruleContent)) {
            ruleContent = RuleCacheType.RULE_KEY_VALUE;
        }
        redisService.setString(RuleCacheType.RULE_KEY + entity.getRuleType().toValue(), ruleContent);
        return entity;
    }

    /**
     * 单个删除规则说明
     *
     * @author zxd
     */
    @Transactional
    public void deleteById(RuleInfo entity) {
        ruleInfoRepository.save(entity);
    }

    /**
     * 批量删除规则说明
     *
     * @author zxd
     */
    @Transactional
    public void deleteByIdList(List<RuleInfo> infos) {
        ruleInfoRepository.saveAll(infos);
    }

    /**
     * 单个查询规则说明
     *
     * @author zxd
     */
    public RuleInfo getOne(Long id) {
        return ruleInfoRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "规则说明不存在"));
    }

    /**
     * 分页查询规则说明
     *
     * @author zxd
     */
    public Page<RuleInfo> page(RuleInfoQueryRequest queryReq) {
        return ruleInfoRepository.findAll(
                RuleInfoWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询规则说明
     *
     * @author zxd
     */
    public List<RuleInfo> list(RuleInfoQueryRequest queryReq) {
        return ruleInfoRepository.findAll(RuleInfoWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author zxd
     */
    public RuleInfoVO wrapperVo(RuleInfo ruleInfo) {
        if (ruleInfo != null) {
            RuleInfoVO ruleInfoVO = KsBeanUtil.convert(ruleInfo, RuleInfoVO.class);
            return ruleInfoVO;
        }
        return null;
    }
}

