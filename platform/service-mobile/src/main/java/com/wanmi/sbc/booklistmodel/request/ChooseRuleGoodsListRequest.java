package com.wanmi.sbc.booklistmodel.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 10:37 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ChooseRuleGoodsListRequest {


    /**
     * 控件id ✅ Update
     */
    @NotNull(groups = ChooseRuleGoodsListRequest.Update.class, message = "chooseRuleId 不能为空")
    private Integer chooseRuleId;

    /**
     *  书单模板id或者书单类目id ✅ Add
     */
    @NotNull(groups = ChooseRuleGoodsListRequest.Add.class, message = "bookListId不能为空")
    private Integer bookListId;

    /**
     * 过滤规则 1 无库存展示 2 无库存不展示 3 无库存沉底 ✅ Add
     */
    @NotNull(groups = ChooseRuleGoodsListRequest.Add.class, message = "filterRule不能为空")
    private Integer filterRule;

    /**
     * 类型 1按条件 2 按sql 3 制定商品 4 书单 ✅ Add
     */
    @NotNull(groups = ChooseRuleGoodsListRequest.Add.class, message = "filterRule不能为空")
    private Integer chooseType;

    /**
     * 类型 内容 多个以 , 分割
     */
    private String chooseCondition;

    /**
     * 商品列表
     */
    @NotNull(message = "商品列表为空")
    private List<GoodsIdListRequest> goodsIdListRequestList;



    public interface Add{}
    public interface Update{}
}
