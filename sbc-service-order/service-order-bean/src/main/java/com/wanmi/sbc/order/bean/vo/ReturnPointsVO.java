package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yang
 * @since 2019/4/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ReturnPointsVO implements Serializable {

    private static final long serialVersionUID = -8843163723589695161L;

    /**
     * 申请积分
     */
    @ApiModelProperty(value = "申请积分")
    private Long applyPoints;

    /**
     * 实退积分
     */
    @ApiModelProperty(value = "实退积分")
    private Long actualPoints;

    /**
     * 对比
     *
     * @param returnPointsVO
     * @return
     */
    public DiffResult diff(ReturnPointsVO returnPointsVO) {
        return new DiffBuilder(this, returnPointsVO, ToStringStyle.JSON_STYLE)
                .append("applyPoints", applyPoints, returnPointsVO.getApplyPoints())
                .append("actualPoints", actualPoints, returnPointsVO.getActualPoints())
                .build();
    }

    /**
     * 合并
     *
     * @param newPoints
     */
    public void merge(ReturnPointsVO newPoints) {
        DiffResult diffResult = this.diff(newPoints);
        diffResult.getDiffs().stream().forEach(
                diff -> {
                    String fieldName = diff.getFieldName();
                    switch (fieldName) {
                        case "applyPoints":
                            mergeSimple(fieldName, diff.getRight());
                            break;
                        case "actualPoints":
                            mergeSimple(fieldName, diff.getRight());
                            break;
                        default:
                            break;
                    }
                }
        );
    }

    private void mergeSimple(String fieldName, Object right) {
        Field field = ReflectionUtils.findField(ReturnPointsVO.class, fieldName);
        try {
            field.setAccessible(true);
            field.set(this, right);
        } catch (IllegalAccessException e) {
            throw new SbcRuntimeException("K-050113", new Object[]{ReturnPointsVO.class, fieldName});
        }
    }

    /**
     * 拼接所有diff
     *
     * @param PointsVO
     * @return
     */
    public List<String> buildDiffStr(ReturnPointsVO PointsVO) {
        Function<Object, String> f = (s) -> {
            if (s == null || StringUtils.isBlank(s.toString())) {
                return "空";
            } else {
                return s.toString().trim();
            }
        };
        DiffResult diffResult = this.diff(PointsVO);
        return diffResult.getDiffs().stream().map(
                diff -> {
                    String result = "";
                    switch (diff.getFieldName()) {
                        case "applyPoints":
                            result = String.format("申请退积分数 由 %s 变更为 %s",
                                    f.apply(diff.getLeft().toString()),
                                    f.apply(diff.getRight().toString())
                            );
                            break;
                        default:
                            break;
                    }
                    return result;
                }
        ).collect(Collectors.toList());
    }

}
