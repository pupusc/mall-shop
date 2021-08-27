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
public class ReturnKnowledgeVO implements Serializable {

    private static final long serialVersionUID = -8843163723589695161L;

    /**
     * 申请知豆
     */
    @ApiModelProperty(value = "申请知豆")
    private Long applyKnowledge;

    /**
     * 实退知豆
     */
    @ApiModelProperty(value = "实退知豆")
    private Long actualKnowledge;

    /**
     * 对比
     *
     * @param returnKnowledgeVO
     * @return
     */
    public DiffResult diff(ReturnKnowledgeVO returnKnowledgeVO) {
        return new DiffBuilder(this, returnKnowledgeVO, ToStringStyle.JSON_STYLE)
                .append("applyKnowledge", applyKnowledge, returnKnowledgeVO.getApplyKnowledge())
                .append("actualKnowledge", actualKnowledge, returnKnowledgeVO.getActualKnowledge())
                .build();
    }

    /**
     * 合并
     *
     * @param returnKnowledgeVO
     */
    public void merge(ReturnKnowledgeVO returnKnowledgeVO) {
        DiffResult diffResult = this.diff(returnKnowledgeVO);
        diffResult.getDiffs().stream().forEach(
                diff -> {
                    String fieldName = diff.getFieldName();
                    switch (fieldName) {
                        case "applyKnowledge":
                            mergeSimple(fieldName, diff.getRight());
                            break;
                        case "actualKnowledge":
                            mergeSimple(fieldName, diff.getRight());
                            break;
                        default:
                            break;
                    }
                }
        );
    }

    private void mergeSimple(String fieldName, Object right) {
        Field field = ReflectionUtils.findField(ReturnKnowledgeVO.class, fieldName);
        try {
            field.setAccessible(true);
            field.set(this, right);
        } catch (IllegalAccessException e) {
            throw new SbcRuntimeException("K-050113", new Object[]{ReturnKnowledgeVO.class, fieldName});
        }
    }

    /**
     * 拼接所有diff
     *
     * @param knowledgeVO
     * @return
     */
    public List<String> buildDiffStr(ReturnKnowledgeVO knowledgeVO) {
        Function<Object, String> f = (s) -> {
            if (s == null || StringUtils.isBlank(s.toString())) {
                return "空";
            } else {
                return s.toString().trim();
            }
        };
        DiffResult diffResult = this.diff(knowledgeVO);
        return diffResult.getDiffs().stream().map(
                diff -> {
                    String result = "";
                    switch (diff.getFieldName()) {
                        case "applyKnowledge":
                            result = String.format("申请退知豆数 由 %s 变更为 %s",
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
