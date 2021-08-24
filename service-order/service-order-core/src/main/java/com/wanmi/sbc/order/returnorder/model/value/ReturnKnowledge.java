package com.wanmi.sbc.order.returnorder.model.value;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.ReflectionUtils;

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
public class ReturnKnowledge {

    /**
     * 申请知豆
     */
    private Long applyKnowledge;

    /**
     * 实退知豆
     */
    private Long actualKnowledge;

    /**
     * 对比
     *
     * @param returnKnowledge
     * @return
     */
    public DiffResult diff(ReturnKnowledge returnKnowledge) {
        return new DiffBuilder(this, returnKnowledge, ToStringStyle.JSON_STYLE)
                .append("applyPoints", applyKnowledge, returnKnowledge.getApplyKnowledge())
                .build();
    }

    /**
     * 合并
     *
     * @param returnKnowledge
     */
    public void merge(ReturnKnowledge returnKnowledge) {
        DiffResult diffResult = this.diff(returnKnowledge);
        diffResult.getDiffs().stream().forEach(
                diff -> {
                    String fieldName = diff.getFieldName();
                    switch (fieldName) {
                        case "applyPoints":
                            mergeSimple(fieldName, diff.getRight());
                            break;
                        default:
                            break;
                    }
                }
        );
    }

    private void mergeSimple(String fieldName, Object right) {
        Field field = ReflectionUtils.findField(ReturnKnowledge.class, fieldName);
        try {
            field.setAccessible(true);
            field.set(this, right);
        } catch (IllegalAccessException e) {
            throw new SbcRuntimeException("K-050113", new Object[]{ReturnKnowledge.class, fieldName});
        }
    }

    /**
     * 拼接所有diff
     *
     * @param knowledge
     * @return
     */
    public List<String> buildDiffStr(ReturnKnowledge knowledge) {
        Function<Object, String> f = (s) -> {
            if (s == null || StringUtils.isBlank(s.toString())) {
                return "空";
            } else {
                return s.toString().trim();
            }
        };
        DiffResult diffResult = this.diff(knowledge);
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
