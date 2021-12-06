package com.wanmi.sbc.marketing.api.request.coupon;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/12/6 2:57 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CouponCodeByFileCustomizeProviderRequest implements Serializable {

    private String path;
}
