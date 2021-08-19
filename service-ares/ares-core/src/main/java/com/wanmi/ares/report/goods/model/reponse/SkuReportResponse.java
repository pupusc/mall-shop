package com.wanmi.ares.report.goods.model.reponse;

import com.wanmi.ares.report.goods.model.root.SkuReport;
import com.wanmi.ares.source.model.root.GoodsInfo;
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
public class SkuReportResponse implements Serializable{

    /**
     * Sku统计报表结果
     */
    private Page<SkuReport> reportPage = new PageImpl<>(new ArrayList<>());

    /**
     * Sku实现结果
     */
    private List<GoodsInfo> goodsInfo = new ArrayList<>();

    private Long count = NumberUtils.LONG_ZERO;

}
