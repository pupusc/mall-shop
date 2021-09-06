package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 3:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListGoodsResponse {

    /**
     * bookListGoods 商品id
     */
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


    private String spuId;

    /**
     * 当前不存在
     */
    private String spuNo;

    private String skuId;

    private String skuNo;

    private Integer orderNum;

    private Date createTime;

    private Date updateTime;
}
