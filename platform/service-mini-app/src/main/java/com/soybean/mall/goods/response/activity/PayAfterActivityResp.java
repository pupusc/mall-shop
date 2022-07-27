package com.soybean.mall.goods.response.activity;

import lombok.Data;

/**
 * Description: 支付后 活动信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/22 3:00 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class PayAfterActivityResp {


    private Integer normalActivityId;

    /**
     * 活动名
     */
    private String normalActivityName;

    /**
     * 展示标题
     */
    private String showTitle;
}
