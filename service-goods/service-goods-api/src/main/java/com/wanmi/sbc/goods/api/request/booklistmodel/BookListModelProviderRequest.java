package com.wanmi.sbc.goods.api.request.booklistmodel;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 5:43 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookListModelProviderRequest implements Serializable {

    @NotNull(groups = {Update.class, Delete.class}, message = "id为空")
    private Integer id;

    /**
     * 模版名
     */
    @NotBlank(groups = Add.class, message = "模版名称为空")
    private String name;

    /**
     * 描述
     */
    @NotBlank(groups = Add.class, message = "描述为空")
    private String desc;

    /**
     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     */
    @NotNull(groups = Add.class, message = "模板类型为空")
    private Integer businessType;

    /**
     * 头图
     */
    @NotBlank
    private String headImgUrl;

    /**
     * 头图跳转地址
     */
    private String headImgHref;

    /**
     *  书单链接地址
     */
    private String pageHref;

    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    @NotNull(groups = Add.class, message = "发布状态为空")
    private Integer publishState;

    @NotBlank(groups = {Add.class, Update.class}, message = "操作人为空")
    private String operator;


    public interface Add{}
    public interface Update{}
    public interface Delete{}
}
