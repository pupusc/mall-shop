package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 开票项目列表
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectListByCompanyInfoIdResponse implements Serializable {


    private static final long serialVersionUID = 1071529300177733319L;

    /**
     * 开票项目VO列表 {@link InvoiceProjectListVO}
     */
    @ApiModelProperty(value = "开票项目VO列表")
    private List<InvoiceProjectListVO> invoiceProjectListDTOList;

}
