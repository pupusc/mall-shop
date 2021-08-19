package com.wanmi.sbc.goods.storegoodstab.model.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.storegoodstab.model.root.StoreGoodsTab;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品模板配置请求实体类
 * Created by xiemengnan on 2018/10/12.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreGoodsTabQueryRequest extends BaseQueryRequest {

    /**
     * 模板标识
     */
    private Long tabId;

    /**
     * 批量模板标识
     */
    private List<Long> tabIds;

    /**
     * 店铺标识
     */
    private Long storeId;

    /**
     * 模板名称
     */
    private String tabName;


    /**
     * 删除标记
     */
    private DeleteFlag delFlag;


    /**
     * 创建人
     */
    private String createPerson;


    /**
     * 封装公共的查询条件
     *
     * @return
     */
    public Specification<StoreGoodsTab> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //批量模板id
            if (CollectionUtils.isNotEmpty(tabIds)) {
                predicates.add(root.get("tabId").in(tabIds));
            }
            //店铺id
            if (storeId != null) {
                predicates.add(cbuild.equal(root.get("storeId"), storeId));
            }
            //模板名称
            if (tabName != null) {
                predicates.add(cbuild.equal(root.get("tabName"), tabName));
            }
            //删除标记
            if (delFlag != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag.toValue()));
            }
            if (predicates.isEmpty()) {
                return null;
            } else if (predicates.size() == 1) {
                return predicates.get(0);
            } else {
                return cbuild.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
