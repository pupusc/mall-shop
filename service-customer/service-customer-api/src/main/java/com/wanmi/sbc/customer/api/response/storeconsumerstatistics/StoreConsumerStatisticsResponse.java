package com.wanmi.sbc.customer.api.response.storeconsumerstatistics;

import com.wanmi.sbc.customer.bean.vo.StoreConsumerStatisticsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>店铺客户消费统计表新增结果</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreConsumerStatisticsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺客户消费统计表信息
     */
    @ApiModelProperty(value = "店铺客户消费统计表信息")
    private StoreConsumerStatisticsVO storeConsumerStatisticsVO;
}
