package com.wanmi.sbc.bookmeta.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MetaBookRcmmdFigureBO implements Serializable {

    private static final long serialVersionUID = -5851012260507440063L;
    private Integer id;
    /**
     * 业务type：1获奖推荐；2编辑推荐；3媒体推荐；4专业机构推荐；5名家推荐；6书中引用推荐；7讲稿引用推荐；
     */
    private Integer bizType;
    /**
     * 描述信息
     */
    private String descr;
    /**
     * 书籍id
     */
    private Integer bookId;
    /**
     * 推荐id
     */
    private Integer bizId;
    /**
     * 推荐人名称
     */
    private String name;
    /**
     * 头衔
     */
    private String jobTitle;

//    /**
//     * isbn
//     */
//    private String isbn;


    private List<RecomentBookVo> recomentBookBoList=new ArrayList<>();

    @Data
    public static class RecomentBookVo {
        private String goodsInfoId;

        /**
         * 商品编号
         */
        private String goodsId;

        /**
         * 商品SKU名称
         */
        private String goodsInfoName;

        /**
         * 商品SKU编码
         */
        private String goodsInfoNo;

        /**
         * 商品图片
         */
        private String goodsInfoImg;
        /**
         * 商品市场价
         */
        private BigDecimal marketPrice;

        /**
         * 最新计算的会员价
         * 为空，以市场价为准
         */
        private BigDecimal salePrice;
    }
}
