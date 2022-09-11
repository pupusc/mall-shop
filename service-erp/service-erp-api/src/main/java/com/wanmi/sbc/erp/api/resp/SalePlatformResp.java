package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/11 2:03 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SalePlatformResp {

    /**
     * 平台店铺id
     */
    private Long tid;

    /**
     * 平台店铺名称
     */
    private String name;

    /**
     * 平台店铺Code
     */
    private String shopCode;

    /**
     * 销售渠道ID
     */
    private Integer saleChannelId;
}
