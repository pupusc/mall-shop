package com.wanmi.sbc.goods.api.response.index;

import lombok.Data;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 12:54 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Data
public class NormalModuleSkuResp {


    private Integer id;

    /**
     * 栏目id
     */
    private Integer normalModelId;
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
    private String SpuId;

    /**
     * spuNo
     */
    private String spuNo;

    /**
     * 排序数
     */
    private Integer sortNum;

    /**
     * 标签值
     */
    private String skuTag;
}
