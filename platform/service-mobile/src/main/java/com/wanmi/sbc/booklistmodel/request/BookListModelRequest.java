package com.wanmi.sbc.booklistmodel.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 10:35 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelRequest {

    @NotNull(groups = {BookListModelRequest.Update.class, BookListModelRequest.Delete.class, BookListModelRequest.Publish.class, BookListModelRequest.FindById.class}, message = "id为空")
    private Integer id;

    /**
     * 模版名
     */
    @NotBlank(groups = BookListModelRequest.Add.class, message = "模版名称为空")
    private String name;

    /**
     * 描述
     */
    @NotBlank(groups = BookListModelRequest.Add.class, message = "描述为空")
    private String desc;

    /**
     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     */
    @NotNull(groups = BookListModelRequest.Add.class, message = "模板类型为空")
    private Integer businessType;

    /**
     * 类目
     */
    @NotNull(groups = BookListModelRequest.Add.class, message = "类目id")
    private List<Integer> classifyList;

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



    public interface Add{}
    public interface Update{}
    public interface Delete{}
    public interface Publish{}
    public interface FindById{}
}
