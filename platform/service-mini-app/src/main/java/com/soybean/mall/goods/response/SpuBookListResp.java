package com.soybean.mall.goods.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 商品书单结果信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 12:17 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SpuBookListResp {

    private String spuId;

    /**
     * 商品标题
     */
    private String spuName;

    /**
     * 商品副标题
     */
    private String spuSubName;

    /**
     * 商品分类 1 图书 2 商品
     */
    private Integer spuCategory;

    /**
     * 主播推荐列表 1、樊登讲书 2非凡精读 3樊登直播 4李蕾慢读
     */
    private List<Integer> anchorRecoms;

    /**
     * 商品主图
     */
    private String pic;

    /**
     * 书单/榜单信息
     */
    private BookList bookList;

    /**
     * 销售价格(最小sku价格)
     */
    private BigDecimal salesPrice;

    /**
     * 商品库存
     */
    private Long stock;


    /**
     * 氛围图
     */
    private Atmosphere atmosphere;


    /**
     * 书单或者榜单信息
     */
    @Data
    public class BookList {
        /**
         * 书单名称
         */
        private String bookListName;

        /**
         * 排序
         */
        private Integer sortNum;
    }

    /**
     * 商品信息
     */
    @Data
    public class Book {

        /**
         * 作者信息
         */
        private List<String> authorNames;

        /**
         * 评分
         */
        private Double score;

        /**
         * 出版社
         */
        private String publisher;

        /**
         * 定价
         */
        private Double fixPrice;

        /**    private String elementThree;

         * 图书标签
         */
        private List<Tag> tags;

        /**
         * 图书标签
         */
        @Data
        public class Tag {
            /**
             * 标签id
             */
            private Integer tageId;

            /**
             * 标签名
             */
            private String tagName;
        }
    }

    /**
     * 氛围图信息
     */
    @Data
    public class Atmosphere {
        /**
         * 图片地址
         */
        private String imageUrl;

        /**
         * 氛围类型1通用2积分
         */
        private Integer atmosType;

        /**
         * 元素1,左侧第一行文字
         */
        private String elementOne;

        /**
         * 元素2：左侧第二行文字
         */
        private String elementTwo;

        /**
         * 元素3：右侧第一行文字
         */
        private String elementThree;

        /**
         * 元素4：右侧第二行文字
         */
        private String elementFour;

    }

}
