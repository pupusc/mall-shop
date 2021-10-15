package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 3:51 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelResponse {

    /**
     * 书单id
     */
    private Integer id;

    /**
     * 模版名
     */
    private String name;

    /**
     * 名家名人
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
     * 头图方图
     */
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
     * 是否置顶 0否 1 是
     */
    private Integer hasTop;

    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    private Integer publishState;

    private Date createTime;

    private Date updateTime;
}
