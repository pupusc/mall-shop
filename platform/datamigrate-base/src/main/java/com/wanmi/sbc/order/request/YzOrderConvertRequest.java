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
//import javax.validation.constraints.NotNull;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@ApiModel
//@Data
//public class YzOrderConvertRequest implements Serializable {
//
//    private static final long serialVersionUID = -6594671115281804611L;
//
//    @ApiModelProperty("页码")
//    private Integer pageNo = NumberUtils.INTEGER_ZERO;
//
//    @ApiModelProperty("每页数据量")
//    @NotNull
//    private Integer pageSize;
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
//    @ApiModelProperty(value = "订单号列表")
//    private List<String> ids;
//
//    @ApiModelProperty(value = "是否查询未转换的")
//    private Boolean convertFlag = Boolean.FALSE;
//}
