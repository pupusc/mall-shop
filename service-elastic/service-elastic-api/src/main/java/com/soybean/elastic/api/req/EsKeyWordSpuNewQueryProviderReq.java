package com.soybean.elastic.api.req;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsKeyWordSpuNewQueryProviderReq extends EsSortSpuNewQueryProviderReq {

    /**
     * 关键字
     */
//    @NotBlank
    private String keyword;

    /**
     * 书单类别 1图书 2商品
     */
//    @NotNull
    private Integer searchSpuNewCategory;

    /**
     * 不展示的spuid
     */
    private List<String> unSpuIds;

    /**
     * 知识顾问专项商品 0 非知识顾问 其他为非知识顾问
     */
    private Integer cpsSpecial = 0;

    /**
     * 指定展示的spuId
     */
    private List<String> spuIds;


    /**
     * 标签类别 {@link com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum}
     */
    private Integer labelCategory;

    /**
     * 销售 开始价格
     */
    private Integer fromSalePrice;

    /**
     * 销售 结束价格
     */
    private Integer toSalePrice;

    /**
     * 店铺分类id
     */
    private Integer classifyId;

    /**
     * 店铺分类 Name
     */
    private String classifyName;

    /**
     * 出版社 Name
     */
    private String publisherName;

    /**
     *  作者名
     */
    private String authorName;

    /**
     *  奖项Name
     */
    private String awardName;

    /**
     *  丛书Name
     */
    private String clumpName;
}
