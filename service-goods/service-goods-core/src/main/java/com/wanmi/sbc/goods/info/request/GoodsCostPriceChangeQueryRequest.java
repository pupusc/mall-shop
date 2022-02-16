package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsInfoSelectStatus;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.util.XssUtils;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class GoodsCostPriceChangeQueryRequest  extends BaseQueryRequest implements Serializable {
    private static final long serialVersionUID = -3531770095624708167L;

    /**
     * ids
     */
    private List<String> goodsInfoIds;

    private Long providerId;

    private List<String> goodsInfoNos;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<GoodsInfo> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //批量SKU编号
            if (CollectionUtils.isNotEmpty(goodsInfoIds)) {
                predicates.add(root.get("goodsInfoId").in(goodsInfoIds));
            }
            if (CollectionUtils.isNotEmpty(goodsInfoNos)) {
                predicates.add(root.get("goodsInfoNo").in(goodsInfoNos));
            }
            if (providerId != null) {
                predicates.add(cbuild.equal(root.get("providerId"), providerId));
            }
            predicates.add(cbuild.isNotNull(root.get("erpGoodsNo")));
            predicates.add(cbuild.equal(root.get("costPriceSyncFlag"), 1));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
