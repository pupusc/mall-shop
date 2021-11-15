package com.wanmi.sbc.goods.api.response.classify;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/21 3:06 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelClassifyLinkProviderResponse implements Serializable {

    /**
     * 书单id
     */
    private Integer bookListModelId;

    /**
     * 书单模版名
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
     * 头图方图 feature_d_v0.02
     */
    private String headSquareImgUrl;

    /**
     * 头图跳转地址
     */
    private String headImgHref;

    /**
     * 书单链接地址
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
    private Integer tagName;

    /**
     * 标签有效开始时间 ✅Add feature_d_v0.02
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tagValidBeginTime;

    /**
     * 标签有效结束时间 ✅Add feature_d_v0.02
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tagValidEndTime;

    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    private Integer publishState;

    private Integer version;

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;
}
