package com.wanmi.sbc.goods.classify.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Description: 此对象定义字段严格按照  {@link com.wanmi.sbc.goods.booklistgoodspublish.repository.BookListGoodsPublishRepository#listGoodsPublishLinkModel} 顺序
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/8 2:40 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@AllArgsConstructor
public class BookListModelClassifyLinkResponse {

    /**
     * 书单id
     */
    private Integer bookListModelId;

    /**
     * 书单模版名
     */
    private String name;

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
     * 书单链接地址
     */
    private String pageHref;

    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    private Integer publishState;

    private Integer version;

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;

//    /**
//     * 分类id
//     */
//    private Integer classifyId;
//
//    /**
//     * 父分类
//     */
//    private Integer classifyParentId;
//
//    /**
//     * 分类名字
//     */
//    private String classifyName;
//
//    /**
//     * 图片
//     */
//    private String adImgUrl;
//
//    /**
//     * 跳转
//     */
//    private String adImgHref;
}
