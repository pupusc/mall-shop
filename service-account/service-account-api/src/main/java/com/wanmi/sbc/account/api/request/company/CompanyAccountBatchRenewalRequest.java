package com.wanmi.sbc.account.api.request.company;

import com.wanmi.sbc.account.bean.dto.CompanyAccountSaveDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 更新商家财务信息参数
 * Created by daiyitian on 2018/10/15.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAccountBatchRenewalRequest implements Serializable {

    private static final long serialVersionUID = 1462266013674905380L;

    /**
     * 更新商家财务信息
     */
    @ApiModelProperty(value = "更新商家财务信息")
    private List<CompanyAccountSaveDTO> companyAccountSaveDTOList;

    /**
     * 需要删除的线下账户编号
     */
    @ApiModelProperty(value = "需要删除的线下账户编号")
    private List<Long> accountIds;

    /**
     * 公司信息id
     */
    @ApiModelProperty(value = "公司信息id")
    @NotNull
    private Long companyInfoId;

}
