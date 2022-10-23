package com.wanmi.sbc.setting.search.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.constant.SearchAggsConstant;
import com.wanmi.sbc.setting.api.constant.SearchWeightConstant;
import com.wanmi.sbc.setting.api.response.search.SearchAggsResp;
import com.wanmi.sbc.setting.redis.RedisService;
import com.wanmi.sbc.setting.search.model.SearchAggsModel;
import com.wanmi.sbc.setting.search.repository.SearchAggsRepository;
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

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/23 2:44 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SearchAggsService {

    @Autowired
    private SearchAggsRepository searchAggsRepository;

    @Autowired
    private RedisService redisService;


    /**
     * 获取聚合列表
     * @param key
     * @return
     */
    public List<SearchAggsResp> list(String key) {

        List<SearchAggsResp> result = new ArrayList<>();
        Map<Object, Object> hashValue = redisService.getHashValue(key);
        if (!hashValue.isEmpty()) {
            hashValue.forEach((K, V) -> {
                SearchAggsResp searchAggsResp = new SearchAggsResp();
                searchAggsResp.setAggsKey((String) K);
                searchAggsResp.setAggsValue((String) V);
                result.add(searchAggsResp);
            });
            return result;
        }

        List<SearchAggsModel> searchAggsModelList = searchAggsRepository.findAll(this.packageWhere());
        for (SearchAggsModel searchAggsModel : searchAggsModelList) {
            SearchAggsResp searchAggsResp = new SearchAggsResp();
            searchAggsResp.setAggsKey(searchAggsModel.getAggsKey());
            searchAggsResp.setAggsValue(searchAggsModel.getAggsValue());
            redisService.putHash(SearchAggsConstant.SPU_SEARCH_AGGS_KEY, searchAggsModel.getAggsKey(), searchAggsModel.getAggsValue(), 4 * 60 * 60);
            result.add(searchAggsResp);
        }
        return result;
    }

    /**
     * 删除key
     * @param key
     */
    public void delete(String key) {
        redisService.delete(key);
    }

    private Specification<SearchAggsModel> packageWhere() {
        return new Specification<SearchAggsModel>() {
            @Override
            public Predicate toPredicate(Root<SearchAggsModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();
                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO.toValue()));
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
