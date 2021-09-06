package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 3:52 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ChooseRuleResponse {

    /**
     *  控件id
     */
    private Integer chooseRuleId;

    /**
     * 书单或者类目id
     */
    private Integer bookListId;

    /**
     * 分类 1书单模板 2类目
     */
    private Integer category;

    /**
     * 过滤规则 1 无库存展示 2 无库存不展示 3 无库存沉底
     */
    private Integer filterRule;

    /**
     * 类型 1按条件 2 按sql 3 制定商品 4 书单
     */
    private Integer chooseType;

    /**
     * 类型 内容 多个以 , 分割
     */
    private String chooseCondition;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private List<BookListGoodsResponse> bookListGoodsList;
}
