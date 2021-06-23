package com.wanmi.sbc.order.appointmentrecord.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class AppointmentQueryRequest extends BaseQueryRequest {


    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private String buyerId;

    /**
     * 抢购开始时间到达
     */
    @ApiModelProperty(value = "抢购开始时间到达")
    private LocalDateTime snapUpStartTimeBegin;


    /**
     * 封装公共条件
     *
     * @return
     */
    private List<Criteria> getCommonCriteria() {
        List<Criteria> criterias = new ArrayList<>();
        if (StringUtils.isNotBlank(buyerId)) {
            criterias.add(Criteria.where("buyerId").is(this.buyerId));
        }
        if (Objects.nonNull(snapUpStartTimeBegin)) {
            criterias.add(Criteria.where("appointmentSaleInfo.snapUpStartTime").lte(snapUpStartTimeBegin));
            criterias.add(Criteria.where("appointmentSaleInfo.snapUpEndTime").gt(snapUpStartTimeBegin));
        }

        return criterias;
    }

    /**
     * 公共条件
     *
     * @return
     */
    public Criteria getWhereCriteria() {
        List<Criteria> criteriaList = this.getCommonCriteria();
        if (CollectionUtils.isEmpty(criteriaList)) {
            return new Criteria();
        }
        return new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
    }


    @Override
    public String getSortColumn() {
        return "createTime";
    }

    @Override
    public String getSortRole() {
        return "desc";
    }

    @Override
    public String getSortType() {
        return "Date";
    }
}
