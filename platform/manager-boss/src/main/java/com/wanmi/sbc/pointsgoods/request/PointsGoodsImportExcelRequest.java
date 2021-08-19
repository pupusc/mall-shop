package com.wanmi.sbc.pointsgoods.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author yang
 * @since 2019/5/21
 */
@Data
public class PointsGoodsImportExcelRequest implements Serializable {

    private static final long serialVersionUID = 3328085586035822273L;

    @NotBlank
    private String ext;

    /**
     * 操作员id
     */
    private String userId;
}
