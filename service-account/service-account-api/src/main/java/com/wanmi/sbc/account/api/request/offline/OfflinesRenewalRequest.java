package com.wanmi.sbc.account.api.request.offline;

import com.wanmi.sbc.account.api.request.AccountBaseRequest;
import com.wanmi.sbc.account.bean.dto.OfflineAccountSaveDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 更新账务信息请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflinesRenewalRequest extends AccountBaseRequest {
    private static final long serialVersionUID = -6225568103797655251L;

    /**
     * 线下商户信息批量数据
     */
    @ApiModelProperty(value = "线下商户信息批量数据")
    @NotEmpty
    private List<OfflineAccountSaveDTO> saveDTOList;

    /**
     * 需删除的线下账户id
     */
    @ApiModelProperty(value = "需删除的线下账户id")
    @NotEmpty
    private List<Long> ids;

    /**
     * 公司信息Id
     */
    @ApiModelProperty(value = "公司信息Id")
    @NotNull
    private Long companyInfoId;
}
