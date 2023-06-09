package com.wanmi.sbc.goods.api.response.booklistgoodspublish;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/15 7:57 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListGoodsPublishProviderResponse implements Serializable {

    private Integer id;

    /**
     * 控件id
     */
    private Integer chooseRuleId;

    /**
     * 书单模板id或者书单类目id
     */
    private Integer bookListId;

    /**
     * 分类 1书单模板 2类目
     */
    private Integer category;

    /**
     *
     */
    private String spuId;

    private String spuNo;

    private String skuId;

    private String skuNo;

    private String erpGoodsNo;

    private String erpGoodsInfoNo;

    private Integer orderNum;

    private Date createTime;

    private Date updateTime;
}
