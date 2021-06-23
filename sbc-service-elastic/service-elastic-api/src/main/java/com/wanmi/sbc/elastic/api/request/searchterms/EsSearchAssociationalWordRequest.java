package com.wanmi.sbc.elastic.api.request.searchterms;

import com.wanmi.sbc.elastic.bean.vo.searchterms.EsSearchAssociationalWordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsSearchAssociationalWordRequest implements Serializable {


    private static final long serialVersionUID = -167186747531183889L;
    /**
     * 设置搜索词信息
     */
    @ApiModelProperty(value = "已保存搜索词信息")
    private List<EsSearchAssociationalWordVO> searchAssociationalWordVOList;


}