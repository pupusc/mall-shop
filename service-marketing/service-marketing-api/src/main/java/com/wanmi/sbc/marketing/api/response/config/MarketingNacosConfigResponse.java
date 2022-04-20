package com.wanmi.sbc.marketing.api.response.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/20 5:36 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class MarketingNacosConfigResponse implements Serializable {

    public List<String> appMiniCouponIdList;
}
