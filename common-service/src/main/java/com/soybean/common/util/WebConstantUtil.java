package com.soybean.common.util;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/26 5:08 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class WebConstantUtil {

    /**
     * 前端传递的这些字符，统一处理为空
     * @param str
     * @return
     */
    public static String filterSpecialCharacter(String str) {
        return Objects.equals("undefined", str) || Objects.equals("null", str) ? "" : str;
    }
}
