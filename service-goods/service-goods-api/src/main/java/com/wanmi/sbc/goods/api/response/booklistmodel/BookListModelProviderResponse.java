package com.wanmi.sbc.goods.api.response.booklistmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 6:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelProviderResponse implements Serializable {


    private Integer id;

    /**
     * 模版名
     */
    private String name;

    /**
     * 名家名人 feature_d_v0.02
     */
    private String famousName;

    /**
     * 描述
     */
    private String desc;

    /**
     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     */
    private Integer businessType;

    /**
     * 头图
     */
    private String headImgUrl;

    /**
     * 头图跳转地址
     */
    private String headImgHref;

    /**
     * 头图方图 feature_d_v0.02
     */
    private String headSquareImgUrl;

    /**
     *  书单链接地址
     */
    private String pageHref;

    /**
     * 是否置顶 0否 1 是 feature_d_v0.02
     */
    private Integer hasTop;

    /**
     * 标签类型 标签类型 1 新上 2 热门 3 自定义 ✅Add feature_d_v0.02
     */
    private Integer tagType;

    /**
     * 标签类型名称 1 新上 2 热门 3 自定义 ✅Add feature_d_v0.02
     */
    private String tagName;


    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    private Integer publishState;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

}
