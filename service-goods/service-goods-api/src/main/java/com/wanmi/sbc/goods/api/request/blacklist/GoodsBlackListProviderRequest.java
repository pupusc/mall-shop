package com.wanmi.sbc.goods.api.request.blacklist;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/20 2:32 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsBlackListProviderRequest implements Serializable {

    private Integer id;

    /**
     * 业务名字
     */
    @NotBlank
    private String businessName;

    /**
     * 业务id
     */
    @NotBlank
    private String businessId;

    /**
     * 业务分类 1 新品榜 2 畅销排行榜 3 特价书榜 4 会员不打折商品
     */
    @NotNull
    private Integer businessCategory;

    /**
     * 业务类型 1、商品skuId 2、商品spuId 3、一级分类id 4、二级分类id
     */
    @NotNull
    private Integer businessType;
}
