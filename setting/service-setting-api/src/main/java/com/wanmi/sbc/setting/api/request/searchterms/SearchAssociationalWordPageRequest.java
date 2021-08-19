package com.wanmi.sbc.setting.api.request.searchterms;


import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * <p>搜索词分页</p>
 * @author weiwenhao
 * @date 2020-04-17
 */
@Builder
@ApiModel
@Data
public class SearchAssociationalWordPageRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -5886364662827513234L;


}
