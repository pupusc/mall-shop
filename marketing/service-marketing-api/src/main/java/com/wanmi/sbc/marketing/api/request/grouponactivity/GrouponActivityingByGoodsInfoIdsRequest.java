package com.wanmi.sbc.marketing.api.request.grouponactivity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:03 2019/5/24
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityingByGoodsInfoIdsRequest implements Serializable {

    private static final long serialVersionUID = -4553374123124168032L;

    /**
     * 单品ids
     */
    @ApiModelProperty(value = "活动ID")
    @NotEmpty
    private List<String> goodsInfoIds;

}
