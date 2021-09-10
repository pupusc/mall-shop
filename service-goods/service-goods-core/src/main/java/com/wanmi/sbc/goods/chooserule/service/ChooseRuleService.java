package com.wanmi.sbc.goods.chooserule.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.chooserule.model.root.ChooseRuleDTO;
import com.wanmi.sbc.goods.chooserule.repository.ChooseRuleRepository;
import com.wanmi.sbc.goods.chooserule.request.ChooseRuleRequest;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;

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


    public ChooseRuleDTO add(ChooseRuleRequest chooseRuleRequest) {
        log.info("ChooseRuleService.add param:{}", JSON.toJSONString(chooseRuleRequest));
        ChooseRuleDTO chooseRuleDTO = new ChooseRuleDTO();
        BeanUtils.copyProperties(chooseRuleRequest, chooseRuleDTO);
        chooseRuleDTO.setId(null); //保证是新增
        chooseRuleDTO.setVersion(0);
        chooseRuleDTO.setCreateTime(new Date());
        chooseRuleDTO.setUpdateTime(new Date());
        chooseRuleDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        return chooseRuleRepository.save(chooseRuleDTO);
    }


    public ChooseRuleDTO update(ChooseRuleRequest chooseRuleRequest) {
        ChooseRuleDTO chooseRuleDTORaw = this.findByCondition(chooseRuleRequest);
        if (chooseRuleDTORaw == null) {
            throw new SbcRuntimeException(String.format("用户: %s 订单：%s 不存在", chooseRuleRequest.getId()));
        }

        chooseRuleDTORaw.setUpdateTime(new Date());
        chooseRuleDTORaw.setId(chooseRuleRequest.getId());
        chooseRuleDTORaw.setBookListId(chooseRuleRequest.getBookListId());
        chooseRuleDTORaw.setCategory(chooseRuleRequest.getCategory());
        if (chooseRuleRequest.getFilterRule() != null) {
            chooseRuleDTORaw.setFilterRule(chooseRuleRequest.getFilterRule());
        }

        if (chooseRuleRequest.getChooseType() != null) {
            chooseRuleDTORaw.setChooseType(chooseRuleRequest.getChooseType());
        }
        if (!StringUtils.isEmpty(chooseRuleRequest.getChooseCondition())) {
            chooseRuleDTORaw.setChooseCondition(chooseRuleRequest.getChooseCondition());
        }
        return chooseRuleRepository.save(chooseRuleDTORaw);
    }

    /**
     * 获取单个对象
     * @param chooseRuleRequest
     * @return
     */
    public ChooseRuleDTO findByCondition(ChooseRuleRequest chooseRuleRequest) {
        List<ChooseRuleDTO> chooseRuleDTOList =
                chooseRuleRepository.findAll(this.packageWhere(chooseRuleRequest));
        if (CollectionUtils.isEmpty(chooseRuleDTOList)) {
            return null;
        }
        if (chooseRuleDTOList.size() > 1) {
            throw new SbcRuntimeException("ChooseRuleService.findByCondition 请求参数: {} 返回的数据多条有误",
                    JSON.toJSONString(chooseRuleRequest));
        }
        return chooseRuleDTOList.get(0);
    }

    /**
     * 获取对象列表
     * @param chooseRuleRequest
     * @return
     */
    public List<ChooseRuleDTO> findByConditionCollection(ChooseRuleRequest chooseRuleRequest) {
        return chooseRuleRepository.findAll(this.packageWhere(chooseRuleRequest));
    }




    private Specification<ChooseRuleDTO> packageWhere(ChooseRuleRequest chooseRuleRequest) {
        return new Specification<ChooseRuleDTO>() {
            final List<Predicate> predicateList = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<ChooseRuleDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                predicateList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL));
                if (chooseRuleRequest.getBookListId() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("bookListId"), chooseRuleRequest.getBookListId()));
                }

                if (!CollectionUtils.isEmpty(chooseRuleRequest.getBookListIdCollection())) {
                    predicateList.add(root.get("bookListId").in(chooseRuleRequest.getBookListIdCollection()));
                }

                if (chooseRuleRequest.getCategory() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), chooseRuleRequest.getCategory()));
                }

                if (chooseRuleRequest.getId() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("id"), chooseRuleRequest.getId()));
                }


                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

}
