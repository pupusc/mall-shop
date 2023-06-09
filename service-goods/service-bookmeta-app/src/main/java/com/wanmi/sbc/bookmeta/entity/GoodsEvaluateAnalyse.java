package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/17/13:46
 * @Description:
 */
@Data
public class GoodsEvaluateAnalyse implements Serializable {
    @Column(name = "incr_id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "evaluate_id")
    private String evaluateId;
    @Column(name = "evaluate_content_key")
    private String evaluateContentKey;
    @Column(name = "evaluate_content")
    private String evaluateContent;
    @Column(name = "sku_id")
    private String skuId;
    @Column(name = "spu_id")
    private String spuId;
    @Column(name = "sku_name")
    private String skuName;
}
