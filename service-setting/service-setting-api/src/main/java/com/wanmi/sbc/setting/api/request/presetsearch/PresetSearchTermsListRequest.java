package com.wanmi.sbc.setting.api.request.presetsearch;


import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class PresetSearchTermsListRequest implements Serializable {

    private List<PresetSearchTermsRequest> requestList;
}
