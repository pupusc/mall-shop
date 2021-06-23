package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.bean.dto.GrouponGoodsInfoForEditDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午2:00 2019/5/16
 * @Description: 批量编辑拼团活动商品请求对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoBatchEditRequest extends BaseRequest {

    private static final long serialVersionUID = -8457755137691316871L;

    /**
     * 拼团活动单品列表
     */
    @ApiModelProperty(value = "拼团活动单品列表")
    @NotEmpty
    private List<GrouponGoodsInfoForEditDTO> goodsInfos;

}
