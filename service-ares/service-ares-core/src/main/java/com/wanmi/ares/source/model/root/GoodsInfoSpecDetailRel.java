package com.wanmi.ares.source.model.root;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-9-5
 * \* Time: 16:16
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * SKU规格值关联实体类
 * Created by dyt on 2017/4/11.
 */
@Data
public class GoodsInfoSpecDetailRel implements Serializable {

    /**
     * SKU与规格值关联ID
     */

    private Long specDetailRelId;

    /**
     * 商品编号
     */
    private String goodsId;

    /**
     * SKU编号
     */
    private String goodsInfoId;

    /**
     * 规格值ID
     */
    private Long specDetailId;

    /**
     * 规格ID
     */
    private Long specId;

    /**
     * 规格值自定义名称
     * 分词搜索
     */
    private String detailName;

    /**
     * 创建时间
     */

    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Integer delFlag;


}
