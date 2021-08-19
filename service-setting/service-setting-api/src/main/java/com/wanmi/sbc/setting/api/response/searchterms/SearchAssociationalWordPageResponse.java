package com.wanmi.sbc.setting.api.response.searchterms;


import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.SearchAssociationalWordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>搜索词结果</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchAssociationalWordPageResponse implements Serializable {


    private static final long serialVersionUID = -567087871151219186L;

    /**
     * 设置搜索词信息
     */
    @ApiModelProperty(value = "已保存搜索词信息")
    private MicroServicePage<SearchAssociationalWordVO> searchAssociationalWordPage;


}
