package com.soybean.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/28 5:45 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@AllArgsConstructor
@Getter
public enum PayAccountEnums {

    WX_H5("1531357121", "h5微信"),
    ALI_H5("2021002133668832", "h5支付宝"),
    WX_MINI_PROGRAM("1527344361", "小程序微信"),
    WX_MINI_PROGRAM_VIDEO("1623480267", "小程序视频号");

    private String code;
    private String message;


}
