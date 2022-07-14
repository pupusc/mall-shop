package com.wanmi.sbc.goods.api.request.index;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Description: 栏目商品
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/11 4:32 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NormalModuleSkuReq {


    private Integer id;

    @NotBlank
    private String skuId;

    @NotBlank
    private String skuNo;

    @NotBlank
    private String spuId;

    @NotBlank
    private String spuNo;

    /**
     * 标签名称
     */
    @NotBlank
    private String skuTag;
}
