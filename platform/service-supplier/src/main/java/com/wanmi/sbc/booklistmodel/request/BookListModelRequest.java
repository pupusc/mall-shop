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

    /**
     * 新增书单模版 ✅update
     */
    @NotNull(groups = {BookListModelRequest.Update.class}, message = "id为空")
    private Integer id;

    /**
     * 模版名 ✅Add
     */
    @NotBlank(groups = BookListModelRequest.Add.class, message = "模版名称为空")
    private String name;

    /**
     * 描述 ✅Add
     */
    @NotBlank(groups = BookListModelRequest.Add.class, message = "描述为空")
    private String desc;

    /**
     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题 ✅Add
     */
    @NotNull(groups = BookListModelRequest.Add.class, message = "模板类型为空")
    private Integer businessType;

    /**
     * 类目 ✅Add
     */
    @NotNull(groups = BookListModelRequest.Add.class, message = "类目id")
    private List<Integer> classifyList;

    /**
     * 头图 ✅Add
     */
    @NotBlank(message = "头图不能为空")
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
}