package com.wanmi.sbc.goods.api.response.classify;

import lombok.Data;

import java.io.Serializable;
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
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    private Integer publishState;

    private Integer version;

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;
}
