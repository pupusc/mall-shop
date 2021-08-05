package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-04 11:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeAccountRecordRequest implements Serializable {

    /**
     * 公司id
     */
    @ApiModelProperty(value = "公司id")
    private Long companyInfoId;


    /**
     * 开始时间,非空，格式：yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty(value = "开始时间")
    @NotNull
    private String beginTime;

    /**
     * 结束时间，非空，格式：yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty(value = "结束时间")
    @NotNull
    private String endTime;





}
