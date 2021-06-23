package com.wanmi.sbc.order.trade.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>订单查询参数结构</p>
 * Created by of628-wenzhi on 2017-07-18-下午3:25.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrouponInstanceQueryRequest extends BaseQueryRequest {


    private static final long serialVersionUID = 1708934975753489437L;
    /**
     * 团号
     */
    private String grouponNo;

    /**
     * 拼团活动id
     */
    private String grouponActivityId;

    /**
     * 拼团状态
     */
    private GrouponOrderStatus grouponStatus;


    /**
     * 团长用户id
     */
    private String customerId;

    /**
     * 公共条件
     * @return
     */
    public Criteria getWhereCriteria(){
        List<Criteria> criteriaList = this.getCommonCriteria();
        if(CollectionUtils.isEmpty(criteriaList)){
            return new Criteria();
        }
        return new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
    }
    /**
     * 封装公共条件
     *
     * @return
     */
    private List<Criteria> getCommonCriteria() {
        List<Criteria> criterias = new ArrayList<>();
        // 活动ID
        if (StringUtils.isNotEmpty(grouponActivityId)) {
            criterias.add(Criteria.where("grouponActivityId").is(grouponActivityId));
        }
        //团编号
        if (StringUtils.isNotEmpty(grouponNo)) {
            criterias.add(Criteria.where("grouponNo").is(grouponNo));
        }
        //团状态
        if (Objects.nonNull(grouponStatus)) {
            criterias.add(Criteria.where("grouponStatus").is(grouponStatus));
        }

        if (Objects.nonNull(grouponStatus) && GrouponOrderStatus.WAIT == grouponStatus){
            criterias.add(Criteria.where("endTime").gt(LocalDateTime.now()));
        }
        return criterias;
    }
}
