//package com.wanmi.sbc.order.request;
//
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.sun.org.apache.xpath.internal.operations.Bool;
//import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
//import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import org.apache.commons.lang3.math.NumberUtils;
//
//import javax.validation.constraints.NotNull;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//@ApiModel
//@Data
//public class YzOrderMigrateRequest implements Serializable {
//
//    private static final long serialVersionUID = 5505324535104110388L;
//
//    @ApiModelProperty("页码")
//    private Integer pageNo = NumberUtils.INTEGER_ONE;
//
//    @ApiModelProperty("创建时间-开始")
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
//    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
//    @NotNull
//    private LocalDateTime createAtStart;
//
//    @ApiModelProperty("创建时间-结束")
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
//    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
//    @NotNull
//    private  LocalDateTime createAtEnd;
//
//    @ApiModelProperty("订单状态")
//    private String status;
//
//    @ApiModelProperty(value = "是否只查询当前页，true/false")
//    private Boolean onlyOnePage = Boolean.FALSE;
//
//    @ApiModelProperty(value = "有赞订单号")
//    private String tid;
//
//    @ApiModelProperty(value = "更新标识")
//    private Boolean updateFlag = Boolean.FALSE;
//
//}
