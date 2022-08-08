package com.soybean.mall.wx.mini.user.bean.response;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/8/3 10:44 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxUserPhoneResp {

    /**
     * +6598577225
     */
    private String phoneNumber;

    /**
     * 98577225
     */
    private String purePhoneNumber;

    /**
     * 65
     */
    private String countryCode;
}
