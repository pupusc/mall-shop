package com.soybean.mall.goods.response;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 2:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListSpuResp {

    /**
     * 书单id
     */
    private Long bookListId;

    /**
     * 书单/榜单类型
     */
    private Integer bookListBusinessType;

    /**
     * 书单/榜单名称
     */
    private String bookListName;

    /**
     * 书单榜单简介
     */
    private String bookListDesc;

    /**
     * 商品总数量
     */
    private Integer surplusSupNum;

    /**
     * 商品列表
     */
    private List<Spu> spus;

    @Data
    public static class Spu {

        private String spuId;


        /**
         * 商品主图
         */
        private String pic;

        /**
         * 无背景图
         */
        private String unBackgroundPic;

        /**
         * 排序
         */
        private Integer sortNum;

    }
}
