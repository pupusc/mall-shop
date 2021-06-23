package com.wanmi.sbc.elastic.api.request.base;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 请求基类
 * Created by aqlu on 15/11/30.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsBaseQueryRequest extends BaseRequest implements Serializable {

    /**
     * 第几页
     */
    @ApiModelProperty(value = "第几页")
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
    @ApiModelProperty(value = "排序规则 desc asc")
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
    @ApiModelProperty(value = "多重排序", notes = "内容：key:字段,value:desc或asc")
    private Map<String, String> sortMap = new LinkedHashMap<>();

//    private List<FieldSortBuilder> fieldSortBuilderList;



    /**
     * 获取分页参数对象
     *
     * @return
     */
    public PageRequest getPageable() {
        return PageRequest.of(pageNum, pageSize);
    }

    /**
     * 填序排序
     *
     * @param column
     * @param sort
     */
    public void putSort(String column, String sort) {
        sortMap.put(column, sort);
    }
}
