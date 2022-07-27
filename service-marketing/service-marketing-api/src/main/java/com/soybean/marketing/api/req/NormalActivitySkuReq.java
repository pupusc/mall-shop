package com.soybean.marketing.api.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 4:12 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NormalActivitySkuReq {

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
     * 件数
     */
    @NotNull
    private Integer num;
}
