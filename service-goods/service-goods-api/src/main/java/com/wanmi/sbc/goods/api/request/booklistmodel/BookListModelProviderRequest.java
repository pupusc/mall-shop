package com.wanmi.sbc.goods.api.request.booklistmodel;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 新增书单模版 ❎Add ✅update delete publish findById
     */
//    @NotNull(groups = {Update.class, Delete.class, Publish.class, FindById.class}, message = "id为空")
    private Integer id;

    /**
     * 模版名 ✅Add
     */
    @NotBlank(groups = Add.class, message = "模版名称为空")
    private String name;

    /**
     * 描述 ✅Add
     */
    @NotBlank(groups = Add.class, message = "描述为空")
    private String desc;

    /**
     * 名家名称 ✅Add feature_d_v0.02
     */
    private String famousName;

    /**
     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题 ✅Add 5 名家推荐
     */
    @NotNull(groups = Add.class, message = "模板类型为空")
    private Integer businessType;

    /**
     * 类目 ✅Add
     */
    @NotNull(groups = Add.class, message = "类目id")
    private List<Integer> classifyList;

    /**
     * 头图 ✅Add
     */
    @NotBlank
    private String headImgUrl;

    /**
     * 方图 ✅Add feature_d_v0.02
     */
    @NotBlank(message = "方图不能为空")
    private String headSquareImgUrl;

    /**
     * 头图跳转地址
     */
    private String headImgHref;

    /**
     *  书单链接地址
     */
    private String pageHref;

    /**
     * 是否置顶 0否 1 是 ✅Add feature_d_v0.02
     */
    private Integer hasTop;

    /**
     * 标签类型 标签类型 1 新上 2 热门 3 自定义 ✅Add feature_d_v0.02
     */
    private Integer tagType;

    /**
     * 标签类型名称 1 新上 2 热门 3 自定义 ✅Add feature_d_v0.02
     */
    private Integer tagName;

    /**
     * 标签有效开始时间 ✅Add feature_d_v0.02
     */
    private LocalDateTime tagValidBeginTime;

    /**
     * 标签有效结束时间 ✅Add feature_d_v0.02
     */
    private LocalDateTime tagValidEndTime;
    /**
     * 操作人 发布书单使用
     */
    private String operator;


    public interface Add{}
    public interface Update{}
    public interface Delete{}
    public interface Publish{}
    public interface FindById{}
}
