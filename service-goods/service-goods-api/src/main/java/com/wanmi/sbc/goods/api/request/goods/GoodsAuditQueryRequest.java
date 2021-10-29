package com.wanmi.sbc.goods.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsAuditQueryRequest extends BaseQueryRequest implements Serializable {


    private static final long serialVersionUID = 4238477383852859642L;
    @ApiModelProperty("操作ID列表")
    private List<Long> ids;
    @ApiModelProperty("isbn")
    private List<String> isbn;
    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("分类")
    private Integer category;

    @ApiModelProperty("出版社")
    private String publishName;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("广告法审核状态")
    private Integer adAuditStatus;

    @ApiModelProperty("广告法人工审核状态")
    private Integer adManualAuditStatus;

    @ApiModelProperty("上架审核状态")
    private Integer launchAuditStatus;

    @ApiModelProperty("上架状态")
    private Integer status;

    @ApiModelProperty("拒绝原因")
    private String rejectReason;
    
    
}
