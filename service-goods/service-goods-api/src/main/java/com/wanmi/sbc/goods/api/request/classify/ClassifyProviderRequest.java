package com.wanmi.sbc.goods.api.request.classify;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/19 2:06 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ClassifyProviderRequest implements Serializable {

    @NotNull(groups = {Update.class, Delete.class}, message = "id不能为空")
    private Integer id;

    /**
     * 父亲节点
     */
    private Integer parentId;

    /**
     * 分类名称
     */
//    @NotBlank(groups = {Add.class, Update.class}, message = "名称不能为空")
    private String classifyName;

    /**
     * 是否首页展示 0 不展示 1 展示
     */
    private Integer hasShowIndex;

    /**
     * 是否首页展示 顺序
     */
    private Integer indexOrderNum;

    public interface Add{}
    public interface Update{}
    public interface Delete{}
}
