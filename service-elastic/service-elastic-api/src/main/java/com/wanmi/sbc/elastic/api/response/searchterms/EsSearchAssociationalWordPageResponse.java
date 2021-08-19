package com.wanmi.sbc.elastic.api.response.searchterms;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.searchterms.EsSearchAssociationalWordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author houshuai
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsSearchAssociationalWordPageResponse implements Serializable {

    private static final long serialVersionUID = -54213792882996817L;
    /**
     * 设置搜索词信息
     */
    @ApiModelProperty(value = "已保存搜索词信息")
    private MicroServicePage<EsSearchAssociationalWordVO> searchAssociationalWordPage;


}