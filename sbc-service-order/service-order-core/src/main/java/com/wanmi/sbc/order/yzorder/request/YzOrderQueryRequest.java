package com.wanmi.sbc.order.yzorder.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.util.XssUtils;
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
public class YzOrderQueryRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -3287076655253763024L;

    @ApiModelProperty(value = "订单号")
    private String id;

    @ApiModelProperty(value = "订单号列表")
    private List<String> ids;

    @ApiModelProperty(value = "订单状态列表")
    private List<String> status;

    @ApiModelProperty(value = "创建时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "是否查询未转换的")
    private Boolean convertFlag;

    @ApiModelProperty(value = "快递方式")
    private List<Long> expressType;

    @ApiModelProperty("发货记录(物流信息)补偿")
    private Boolean deliverCompensateFlag;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty(value = "是否查询周期购订单")
    private Boolean cycleBuyFlag;
    /**
     * 封装公共条件
     * @return
     */
    public Criteria getCriteria() {
        List<Criteria> criterias = new ArrayList<>();

        if(StringUtils.isNotBlank(id)) {
            criterias.add(XssUtils.regex("id", id));
        }

        if(StringUtils.isNotBlank(goodsName)) {
            criterias.add(Criteria.where("full_order_info.orders.title").is(goodsName));
        }

        if(CollectionUtils.isNotEmpty(ids)) {
            criterias.add(Criteria.where("id").in(ids));
        }

        if(CollectionUtils.isNotEmpty(status)) {
            criterias.add(Criteria.where("full_order_info.order_info.status").in(status));
        }

        if(Objects.nonNull(createTimeStart)) {
            criterias.add(Criteria.where("full_order_info.order_info.created").gte(createTimeStart));
        }

        if(Objects.nonNull(createTimeEnd)) {
            criterias.add(Criteria.where("full_order_info.order_info.created").lt(createTimeEnd));
        }

        if(CollectionUtils.isNotEmpty(expressType)) {
            criterias.add(Criteria.where("full_order_info.order_info.express_type").in(expressType));
        }

        if(Objects.nonNull(deliverCompensateFlag)) {
            criterias.add(Criteria.where("delivery_order_detail.dist_order_d_t_os.express_info.express_detail.express_progress_info").exists(Boolean.FALSE));
        }

        if(Objects.nonNull(convertFlag)) {
            criterias.add(Criteria.where("convertFlag").is(convertFlag));
        }

        if(Boolean.TRUE.equals(cycleBuyFlag)) {
            criterias.add(Criteria.where("full_order_info.orders.item_type").is(24L));
        }

        if (CollectionUtils.isEmpty(criterias)) {
            return new Criteria();
        }
        return new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
    }


}
