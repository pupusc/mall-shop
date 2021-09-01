package com.wanmi.sbc.goods.chooserule.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.request.chooserule.ChooseRuleProviderRequest;
import com.wanmi.sbc.goods.chooserule.model.root.ChooseRuleDTO;
import com.wanmi.sbc.goods.chooserule.repository.ChooseRuleRepository;
import com.wanmi.sbc.goods.util.GoodsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 8:00 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class ChooseRuleService {

    @Resource
    private ChooseRuleRepository chooseRuleRepository;


    public void add(ChooseRuleProviderRequest chooseRuleProviderRequest) {
        log.info("ChooseRuleService.add param:{}", JSON.toJSONString(chooseRuleProviderRequest));
        ChooseRuleDTO chooseRuleDTO = new ChooseRuleDTO();
        BeanUtils.copyProperties(chooseRuleProviderRequest, chooseRuleDTO);
        chooseRuleDTO.setId(null); //保证是新增
        chooseRuleDTO.setVersion(0);
        chooseRuleDTO.setCreateTime(new Date());
        chooseRuleDTO.setUpdateTime(new Date());
        chooseRuleDTO.setDelFlag(DeleteFlagEnum.DELETE.getCode());
        chooseRuleRepository.save(chooseRuleDTO);
    }


    public ChooseRuleDTO update(ChooseRuleProviderRequest chooseRuleProviderRequest) {
        ChooseRuleDTO chooseRuleDTORaw = this.findByCondition(chooseRuleProviderRequest);
        if (chooseRuleDTORaw == null) {
            throw new SbcRuntimeException(String.format("用户: %s 订单：%s 不存在", chooseRuleProviderRequest.getId()));
        }

        chooseRuleDTORaw.setUpdateTime(new Date());
        chooseRuleDTORaw.setId(chooseRuleProviderRequest.getId());
        chooseRuleDTORaw.setBookListId(chooseRuleProviderRequest.getBookListId());
        chooseRuleDTORaw.setCategory(chooseRuleProviderRequest.getCategory());
        if (chooseRuleProviderRequest.getFilterRule() != null) {
            chooseRuleDTORaw.setFilterRule(chooseRuleProviderRequest.getFilterRule());
        }

        if (chooseRuleProviderRequest.getChooseType() != null) {
            chooseRuleDTORaw.setChooseType(chooseRuleProviderRequest.getChooseType());
        }
        if (!StringUtils.isEmpty(chooseRuleProviderRequest.getChooseCondition())) {
            chooseRuleDTORaw.setChooseCondition(chooseRuleProviderRequest.getChooseCondition());
        }
        return chooseRuleRepository.save(chooseRuleDTORaw);
    }


    public ChooseRuleDTO findByCondition(ChooseRuleProviderRequest chooseRuleProviderRequest) {
        List<ChooseRuleDTO> chooseRuleDTOList =
                chooseRuleRepository.findAll(this.packageWhere(chooseRuleProviderRequest));
        if (CollectionUtils.isEmpty(chooseRuleDTOList)) {
            return null;
        }
        return chooseRuleDTOList.get(0);
    }


    private Specification<ChooseRuleDTO> packageWhere(ChooseRuleProviderRequest chooseRuleProviderRequest) {
        return new Specification<ChooseRuleDTO>() {
            final List<Predicate> predicateList = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<ChooseRuleDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                predicateList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL));
                if (chooseRuleProviderRequest.getBookListId() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("bookListId"), chooseRuleProviderRequest.getBookListId()));
                }

                if (chooseRuleProviderRequest.getCategory() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), chooseRuleProviderRequest.getCategory()));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

}
