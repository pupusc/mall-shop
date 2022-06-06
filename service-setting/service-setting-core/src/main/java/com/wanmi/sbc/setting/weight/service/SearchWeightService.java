package com.wanmi.sbc.setting.weight.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.weight.model.SearchWeightModel;
import com.wanmi.sbc.setting.weight.repository.SearchWeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/6 3:11 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SearchWeightService {

    @Autowired
    private SearchWeightRepository searchWeightRepository;

    public List<SearchWeightModel> list() {

        List<SearchWeightModel> searchWeightModelList = searchWeightRepository.findAll(this.packageWhere());
        return searchWeightModelList;
    }



    private Specification<SearchWeightModel> packageWhere() {
        return new Specification<SearchWeightModel>() {
            @Override
            public Predicate toPredicate(Root<SearchWeightModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();
                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
