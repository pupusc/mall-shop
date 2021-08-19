package com.wanmi.sbc.setting.api.response.popularsearchterms;


import com.wanmi.sbc.setting.bean.vo.PopularSearchTermsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>热门搜索VO</p>
 * @author weiwenhao
 * @date 2020-04-17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PopularSearchTermsListResponse implements Serializable {


    private static final long serialVersionUID = 563916525140823817L;
    /**
     * 热门搜索词列表
     */
    @ApiModelProperty(name = "热门搜索词")
    private List<PopularSearchTermsVO> popularSearchTermsVO;
}
