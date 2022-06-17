package com.soybean.elastic.api.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsKeyWordSpuNewQueryProviderReq extends EsSortSpuNewQueryProviderReq {

    /**
     * 关键字
     */
    @NotBlank
    private String keyword;

    /**
     * 删除标志 0不删除 1删除
     */
    private Integer delFlag = 0;

    /**
     * 书单类别 1图书 2商品
     */
    @NotNull
    private Integer searchSpuNewCategory;
}
