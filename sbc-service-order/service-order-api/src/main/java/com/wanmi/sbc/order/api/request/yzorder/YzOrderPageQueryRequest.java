package com.wanmi.sbc.order.api.request.yzorder;

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
public class YzOrderPageQueryRequest extends BaseQueryRequest {

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


}
