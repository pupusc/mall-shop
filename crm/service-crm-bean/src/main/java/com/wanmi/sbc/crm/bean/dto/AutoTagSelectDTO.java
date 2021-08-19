package com.wanmi.sbc.crm.bean.dto;

import com.wanmi.sbc.crm.bean.enums.RelationType;
import com.wanmi.sbc.crm.bean.vo.RangeParamVo;
import com.wanmi.sbc.crm.bean.vo.TagParamVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AutoTagSelectDTO
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/8/25 18:27
 **/
@ApiModel
@Data
public class AutoTagSelectDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long count;

    private Long maxLen;

    private Long selectedId;

    private RelationType relationType;

    private String columnType;

    private List<TagParamVO> dataSource;

    private List<TagParamVO> dataSourceFirst;

    private Map<Long,AutoTagSelectValuesDTO> autoTagSelectValues;
}
