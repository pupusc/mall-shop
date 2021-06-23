package com.wanmi.sbc.goods.api.request.cyclebuy;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;


/**
 * <p>分页查询周期购活动请求参数</p>
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuyPageRequest extends BaseQueryRequest {


    /**
     * 周期购活动名称
     */
    @ApiModelProperty(value = "周期购活动名称")
    private String activityName;

    /**
     * 上下架 0：上架 1:下架
     */
    @ApiModelProperty(value = "上下架 0：上架 1:下架")
    private AddedFlag addedFlag;





}
