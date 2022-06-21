package com.soybean.mall.goods.dto;

import lombok.Data;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/11 2:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SpuRecomBookListDTO {

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
     * 页面展示内容
     */
    private String bookListNameShow;

    /**
     * 商品信息
     */
    private Spu spu;

    /**
     * 商品对象信息
     */
    @Data
    public static class Spu {

        private String spuId;

        /**
         * 排序
         */
        private Integer sortNum;

    }
}
