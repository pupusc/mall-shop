package com.wanmi.ares.source.model.root;

import com.wanmi.ares.source.model.root.base.BaseData;
import lombok.*;

/**
 * 业务员基础信息
 * Created by sunkun on 2017/9/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Document(indexName = EsConstants.ES_INDEX_BASIC, type = EsConstants.ES_TYPE_EMPLOYEE)
@ToString(callSuper = true)
public class Employee extends BaseData {

    private static final long serialVersionUID = -8541259601493655201L;

    /**
     * 是否业务员 0:是业务员 1:不是业务员
     */
    private Integer isEmployee;

    /**
     * 名称
     */
    private String name;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 商家id
     */
    private String companyId;


}
