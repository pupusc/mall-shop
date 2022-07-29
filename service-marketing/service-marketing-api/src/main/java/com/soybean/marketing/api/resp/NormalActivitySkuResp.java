package com.soybean.marketing.api.resp;

import lombok.Data;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 5:05 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NormalActivitySkuResp {

    private Integer id;

    /**
     * normalActivityId
     */
    private Integer normalActivityId;

    /**
     * SkuId
     */
    private String skuId;

    /**
     * SkuNo
     */
    private String skuNo;

    /**
     * SpuId
     */
    private String spuId;

    /**
     * spuNo
     */
    private String spuNo;

    /**
     *  数量
     */
    private Integer num;
}
