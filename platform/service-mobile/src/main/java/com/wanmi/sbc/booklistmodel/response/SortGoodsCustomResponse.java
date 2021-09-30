package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

import java.io.Serializable;

/**
 * Description: 商品列表简化对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 3:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SortGoodsCustomResponse extends GoodsCustomResponse implements Serializable {

    private static final long serialVersionUID = 4861366734625705656L;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 类型  1商品 2书单
     */
    private Integer type;

    /**
     * 丝带第一行文字
     */
    private String atmosphereFirstTitle;
    /**
     * 丝带第二行文字
     */
    private String atmosphereSecondTitle;
    /**
     * 价格
     */
    private String atmospherePrice;

}
