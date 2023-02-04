package com.wanmi.sbc.setting.api.response.defaultsearchterms;

import com.wanmi.sbc.setting.bean.vo.DefaultSearchTermsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 搜索栏默认搜索VO
 * @Author zh
 * @Date  2023/2/4 10:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class DefaultSearchTermsListResponse implements Serializable {

    private static final long serialVersionUID = 5630125409239593499L;

    /**
     * 热门搜索词
     */
    @ApiModelProperty(name = "默认搜索词")
    private DefaultSearchTermsVO defaultSearchTermsVO;

    /**
     * 热门搜索词列表
     */
    @ApiModelProperty(name = "默认搜索词列表")
    private List<DefaultSearchTermsVO> defaultSearchTermsListVO;
}
