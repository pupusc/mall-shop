//package com.wanmi.sbc.order.request;
//
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
//import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import org.apache.commons.lang3.math.NumberUtils;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@ApiModel
//@Data
//public class YzOrderUpdateRequest implements Serializable {
//    private static final long serialVersionUID = 7770763305450935753L;
//
//    @ApiModelProperty(value = "创建时间开始")
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
//    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
//    private LocalDateTime createTimeStart;
//
//    @ApiModelProperty(value = "创建时间结束")
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
//    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
//    private LocalDateTime createTimeEnd;
//
//    @ApiModelProperty(value = "订单号列表")
//    private List<String> ids;
//
//
//}
