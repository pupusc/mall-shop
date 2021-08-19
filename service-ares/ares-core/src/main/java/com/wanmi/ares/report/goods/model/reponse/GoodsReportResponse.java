package com.wanmi.ares.report.goods.model.reponse;

import com.wanmi.ares.report.goods.model.root.GoodsReport;
import com.wanmi.ares.source.model.root.GoodsBrand;
import com.wanmi.ares.source.model.root.GoodsCate;
import com.wanmi.ares.source.model.root.StoreCate;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Es 商品查询 返回结果
 *
 * @author liangck
 * @version 1.0
 * @since 16/6/28 17:29
 */
@Data
public class GoodsReportResponse implements Serializable{

    /**
     * 商品模块统计报表结果
     */
    private Page<GoodsReport> reportPage = new PageImpl<>(new ArrayList<>());

    /**
     * 商品分类结果
     */
    private List<GoodsCate> cates = new ArrayList<>();

    /**
     * 商品品牌结果
     */
    private List<GoodsBrand> brands = new ArrayList<>();

    /**
     * 商品店铺分类结果
     */
    private List<StoreCate> storeCates = new ArrayList<>();

    /**
     * 总数据大小
     */
    private Long count = NumberUtils.LONG_ZERO;

}
