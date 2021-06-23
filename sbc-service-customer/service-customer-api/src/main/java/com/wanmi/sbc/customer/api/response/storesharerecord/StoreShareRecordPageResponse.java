package com.wanmi.sbc.customer.api.response.storesharerecord;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.StoreShareRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>商城分享分页结果</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreShareRecordPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商城分享分页结果
     */
    @ApiModelProperty(value = "商城分享分页结果")
    private MicroServicePage<StoreShareRecordVO> storeShareRecordVOPage;
}
