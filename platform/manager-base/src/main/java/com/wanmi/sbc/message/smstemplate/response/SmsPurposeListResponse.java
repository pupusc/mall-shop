package com.wanmi.sbc.message.smstemplate.response;

import com.wanmi.sbc.message.bean.vo.SmsPurposeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SmsPurposeListResponse
 * @Description 用途列表
 * @Author dyt
 * @Date 2019/12/9 14:55
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsPurposeListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用途列表")
    private List<SmsPurposeVO> smsPurposeList;

}
