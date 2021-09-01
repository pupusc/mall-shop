package com.wanmi.sbc.goods.api.request.chooserule;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 8:32 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ChooseRuleProviderRequest implements Serializable {


    private Integer id;

    /**
     *  书单模板id或者书单类目id
     */
    @NotNull(message = "bookListId不能为空")
    private Integer bookListId;

    /**
     * 过滤规则 1 无库存展示 2 无库存不展示 3 无库存沉底
     */
    @NotNull(groups = Add.class, message = "filterRule不能为空")
    private Integer filterRule;

    /**
     * 类 1书单模板 2类目
     */
    @NotNull(message = "category不能为空")
    private Integer category;

    /**
     * 类型 1按条件 2 按sql 3 制定商品 4 书单
     */
    @NotNull(groups = Add.class, message = "filterRule不能为空")
    private Integer chooseType;

    /**
     * 类型 内容 多个以 , 分割
     */
    private String chooseCondition;

    @NotBlank( message = "filterRule不能为空")
    private String operator;


    public interface Add{}
}
