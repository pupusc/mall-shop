package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.common.enums.SortType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>分页查询参数结构</p>
 * Created by of628-wenzhi on 2017-12-07-下午8:03.
 */
@ApiModel
@Data
public class BasePageRequest implements Serializable {
    /**
     * 第几页,从0开始
     */
    @ApiModelProperty(value = "第几页,从0开始")
    private Integer pageNum = 0;

    /**
     * 每页显示多少条
     */
    @ApiModelProperty(value = "每页显示多少条")
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String sortColumn;

    /**
     * 排序规则 desc asc
     */
    @ApiModelProperty(value = "排序规则")
    private String sortRole;

    /**
     * 排序类型
     */
    @ApiModelProperty(value = "排序类型")
    private String sortType;

    /**
     * 多重排序
     * 内容：key:字段,value:desc或asc
     */
    @ApiModelProperty(value = "多重排序(key:字段,value:desc或asc)")
    private Map<String, String> sortMap = new LinkedHashMap<>();

    /**
     * 获取分页参数对象与排序条件
     *
     * @return 分页对象
     */
    public PageRequest getPageRequest() {
        Sort sort = getSort();
        if (Objects.nonNull(sort)) {
            //无排序
            return PageRequest.of(pageNum, pageSize, sort);
        } else {
            //无排序
            return PageRequest.of(pageNum, pageSize);
        }
    }

    /**
     * jpa排序
     *
     * @return 排序类
     */
    public Sort getSort() {
        // 单个排序
        if (StringUtils.isNotBlank(this.getSortColumn())) {
            // 判断规则 DESC ASC
            Sort.Direction direction = SortType.ASC.toValue().equalsIgnoreCase(this.getSortRole()) ? Sort.Direction
                    .ASC : Sort.Direction.DESC;
            return Sort.by(direction, this.getSortColumn());
        }

        //多重排序
        if (MapUtils.isNotEmpty(sortMap)) {
            List<Sort.Order> orders =
                    sortMap.keySet().stream().filter(StringUtils::isNotBlank)
                            .map(column -> new Sort.Order(SortType.ASC.toValue().equalsIgnoreCase(sortMap.get(column)
                            ) ? Sort.Direction.ASC : Sort.Direction.DESC, column))
                            .collect(Collectors.toList());
            return Sort.by(orders);
        }
        return null;
    }
}
