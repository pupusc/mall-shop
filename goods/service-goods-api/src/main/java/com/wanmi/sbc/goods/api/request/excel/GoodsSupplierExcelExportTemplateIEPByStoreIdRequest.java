package com.wanmi.sbc.goods.api.request.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.goodsexcel.GoodsExcelExportTemplateByStoreIdRequest
 * 商家根据店铺编号导出模板请求对象
 * @author lipeng
 * @dateTime 2018/11/2 上午10:00
 */
@ApiModel
@Data
public class GoodsSupplierExcelExportTemplateIEPByStoreIdRequest implements Serializable {

    private static final long serialVersionUID = -6039756679784698582L;

    @ApiModelProperty(value = "店铺Id")
    @NotNull
    private Long storeId;
}
