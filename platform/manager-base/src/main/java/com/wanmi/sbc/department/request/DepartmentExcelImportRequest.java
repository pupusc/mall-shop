package com.wanmi.sbc.department.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-07
 */
@Data
public class DepartmentExcelImportRequest implements Serializable {

    private static final long serialVersionUID = -9015124894584238109L;

    @NotBlank
    private String ext;

    /**
     * 公司ID
     */
    private Long companyInfoId;

    /**
     * 操作员id
     */
    private String userId;
}
