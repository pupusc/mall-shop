package com.wanmi.sbc.setting.weight.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.constant.SearchWeightConstant;
import com.wanmi.sbc.setting.api.response.weight.SearchWeightResp;
import com.wanmi.sbc.setting.redis.RedisService;
import com.wanmi.sbc.setting.weight.model.SearchWeightModel;
import com.wanmi.sbc.setting.weight.repository.SearchWeightRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Autowired
    private RedisService redisService;

    /**
     * 获取权重
     * @param key
     * @return
     */
    public List<SearchWeightResp> list(String key) {

        List<SearchWeightResp> result = new ArrayList<>();
        Map<Object, Object> hashValue = redisService.getHashValue(key);
        if (!hashValue.isEmpty()) {
            hashValue.forEach((K, V) -> {
                SearchWeightResp searchWeightResp = new SearchWeightResp();
                searchWeightResp.setWeightKey((String) K);
                searchWeightResp.setWeightValue((String) V);
                result.add(searchWeightResp);
            });
            return result;
        }

        List<SearchWeightModel> searchWeightModelList = searchWeightRepository.findAll(this.packageWhere());
        for (SearchWeightModel searchWeightModel : searchWeightModelList) {
            SearchWeightResp searchWeightResp = new SearchWeightResp();
            if (Objects.equals(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_KEY, key) && searchWeightModel.getWeightCategory() == 1) {
                searchWeightResp.setWeightKey(searchWeightModel.getWeightKey());
                searchWeightResp.setWeightValue(searchWeightModel.getWeightValue());
                redisService.putHash(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_KEY, searchWeightModel.getWeightKey(), searchWeightModel.getWeightValue(), 4 * 60 * 60);
            } else if (Objects.equals(SearchWeightConstant.SPU_SEARCH_WEIGHT_KEY, key) && searchWeightModel.getWeightCategory() == 2) {
                searchWeightResp.setWeightKey(searchWeightModel.getWeightKey());
                searchWeightResp.setWeightValue(searchWeightModel.getWeightValue());
                redisService.putHash(SearchWeightConstant.SPU_SEARCH_WEIGHT_KEY, searchWeightModel.getWeightKey(), searchWeightModel.getWeightValue(), 4 * 60 * 60);
            }
            result.add(searchWeightResp);
        }
        return result;
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
