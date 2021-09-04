package com.wanmi.sbc.goods.api.request.chooserulegoodslist;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/4 3:26 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ChooseRuleGoodsListProviderRequest implements Serializable {

    @NotNull(groups = ChooseRuleGoodsListProviderRequest.Update.class, message = "chooseRuleId 不能为空")
    private Integer chooseRuleId;

    /**
     *  书单模板id或者书单类目id
     */
    @NotNull(groups = ChooseRuleGoodsListProviderRequest.Add.class, message = "bookListId不能为空")
    private Integer bookListId;

    /**
     * 过滤规则 1 无库存展示 2 无库存不展示 3 无库存沉底
     */
    @NotNull(groups = ChooseRuleGoodsListProviderRequest.Add.class, message = "filterRule不能为空")
    private Integer filterRule;

    /**
     * 类 1书单模板 2类目
     */
    @NotNull(groups = ChooseRuleGoodsListProviderRequest.Add.class, message = "category不能为空")
    private Integer category;

    /**
     * 类型 1按条件 2 按sql 3 制定商品 4 书单
     */
    @NotNull(groups = ChooseRuleGoodsListProviderRequest.Add.class, message = "filterRule不能为空")
    private Integer chooseType;

    /**
     * 类型 内容 多个以 , 分割
     */
    private String chooseCondition;

    @NotBlank(groups = ChooseRuleGoodsListProviderRequest.Add.class,message = "filterRule不能为空")
    private String operator;

    /**
     * goodIdList
     */
    @NotBlank(groups = ChooseRuleGoodsListProviderRequest.Add.class,message = "商品列表id不能为空")
    private List<GoodsIdListProviderRequest> goodsIdListProviderRequestList;

    public interface Add{}
    public interface Update{}
}
