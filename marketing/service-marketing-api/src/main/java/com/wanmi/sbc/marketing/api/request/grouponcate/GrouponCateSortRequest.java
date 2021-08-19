package com.wanmi.sbc.marketing.api.request.grouponcate;

import com.wanmi.sbc.marketing.bean.vo.GrouponCateSortVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: chenli
 * @Date: 2019/5/16
 * @Description: 拼团分类排序request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrouponCateSortRequest implements Serializable {

    private static final long serialVersionUID = -2593150363142815967L;
    /**
     * 拼团分类排序
     */
    @ApiModelProperty(value = "拼团分类排序")
    @NotNull
    List<GrouponCateSortVO> grouponCateSortVOList;
}
