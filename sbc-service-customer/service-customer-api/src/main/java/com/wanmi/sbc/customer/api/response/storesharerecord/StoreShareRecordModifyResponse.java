package com.wanmi.sbc.customer.api.response.storesharerecord;

import com.wanmi.sbc.customer.bean.vo.StoreShareRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>商城分享修改结果</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreShareRecordModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的商城分享信息
     */
    @ApiModelProperty(value = "已修改的商城分享信息")
    private StoreShareRecordVO storeShareRecordVO;
}
