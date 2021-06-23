package com.wanmi.sbc.account.request;

import com.wanmi.sbc.account.bean.dto.CompanyAccountSaveDTO;
import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkun on 2017/11/3.
 */
@ApiModel
@Data
public class OfflineAccountRequest extends BaseRequest {

    @ApiModelProperty(value = "删除的Ids")
    private List<Long> deleteIds = new ArrayList<>();

    @ApiModelProperty(value = "线下收款账户")
    private List<CompanyAccountSaveDTO> offlineAccounts = new ArrayList<>();
}
