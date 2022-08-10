package com.soybean.mall.wx.mini.user.bean.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/8/3 2:47 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxGetPhoneOldReq {

    //登陆1
    @NotBlank
    private String encryptedData;
    @NotBlank
    private String iv;
    @NotBlank
    private String sessionKey;
}
