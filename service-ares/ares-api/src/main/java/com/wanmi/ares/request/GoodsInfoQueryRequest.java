package com.wanmi.ares.request;

import com.wanmi.ms.domain.PageRequest;
import lombok.Data;

import java.util.List;

/**
 * 商品sku信息
 * Created by sunkun on 2017/9/21.
 */
@Data
public class GoodsInfoQueryRequest {

    /**
     * 页码
     */
    private Long pageNum = 0L;

    /**
     * 页面大小
     */
    private Long pageSize = 10L;

    /**
     * 搜索关键字
     * 范围仅限SkuName或SkuNo
     */
    private String keyWord;

    /**
     * 批量SpuID查询
     */
    private List<String> goodsIds;

    /**
     * 批量ID查询
     */
    private List<String> goodsInfoIds;

    /**
     * 批量cateIds查询
     */
    private List<Long> goodsCateIds;

    /**
     * brandId查询
     */
    private Long brandId;

    /**
     * 获取分页参数对象
     * @return
     */
    public PageRequest getPageable(){
        return new PageRequest(pageNum.intValue() ,pageSize.intValue());
    }
}
