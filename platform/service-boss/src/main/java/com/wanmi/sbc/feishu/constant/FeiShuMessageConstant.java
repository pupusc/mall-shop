package com.wanmi.sbc.feishu.constant;

import java.text.MessageFormat;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/27 2:04 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class FeiShuMessageConstant {

    /**
     * 库存阀值
     */
    public static Integer FEI_SHU_STOCK_LIMIT = 20;


    /**
     * 成本价 阀值
     */
    public static String FEI_SHU_COST_PRICE_LT_LIMIT = "10";

    /**
     * 成本价 阀值
     */
    public static String FEI_SHU_COST_PRICE_GT_LIMIT = "40";

    /**
     * 库存小于飞书通知
     */
    public static String FEI_SHU_STOCK_NOTIFY = "【库存预警】{0} {1}库存于{2}变为{3}";


    /**
     * 成本价飞书同志
     *
     */
    public static String FEI_SHU_COST_PRICE_NOTIFY = "【毛利预警】{0}{1}当前售价{2}，于{3}成本价由{4}调整为{5}，原毛利率 {6}% 变为 {7}%";


}
