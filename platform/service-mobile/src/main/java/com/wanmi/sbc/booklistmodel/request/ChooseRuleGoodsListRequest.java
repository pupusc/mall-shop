package com.wanmi.sbc.booklistmodel.request;

import com.wanmi.sbc.goods.api.request.booklistmodel.GoodsIdListProviderRequest;
import com.wanmi.sbc.goods.api.request.chooserulegoodslist.ChooseRuleGoodsListProviderRequest;
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


    @NotNull(groups = ChooseRuleGoodsListRequest.Update.class, message = "chooseRuleId 不能为空")
    private Integer chooseRuleId;

    /**
     *  书单模板id或者书单类目id
     */
    @NotNull(groups = ChooseRuleGoodsListRequest.Add.class, message = "bookListId不能为空")
    private Integer bookListId;

    /**
     * 过滤规则 1 无库存展示 2 无库存不展示 3 无库存沉底
     */
    @NotNull(groups = ChooseRuleGoodsListRequest.Add.class, message = "filterRule不能为空")
    private Integer filterRule;

    /**
     * 类 1书单模板 2类目
     */
    @NotNull(groups = ChooseRuleGoodsListRequest.Add.class, message = "category不能为空")
    private Integer category;

    /**
     * 类型 1按条件 2 按sql 3 制定商品 4 书单
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
    private List<GoodsIdListRequest> goodsIdListRequestList;



    public interface Add{}
    public interface Update{}
}
