package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/15:43
 * @Description:
 */
@Data
public class GoodsKeyWordsDownLoadBO {
    private Integer id;
    private String name;
    private String goodsName;
    private String spuId;
    private String goUrl;
    private String relSpuId;
    private String relSkuId;
    private Integer type;
}
