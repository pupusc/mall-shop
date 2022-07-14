package com.wanmi.sbc.goods.api.request.blacklist;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

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
    @NotNull
    private List<String> businessId;

    /**
     * 业务分类 1 新品榜 2 畅销排行榜 3 特价书榜 4、不显示会员价的商品 5、库存编码 6、积分黑名单 7、底部分类 8、首页商品搜索所有场景不展示 9、首页商品搜索非知识顾问不展示, 10
     */
    @NotNull
    private Integer businessCategory;

    /**
     * 业务类型 1、商品skuId 2、商品spuId 3、一级分类id 4、二级分类id
     */
    @NotNull
    private Integer businessType;
}
