package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.bean.dto.DistributionInviteNewRecordDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionInviteNewRecordBatchUpdateRequest implements Serializable{

    private List<DistributionInviteNewRecordDTO> list;

}
