package com.wanmi.sbc.order.bean.dto.yzorder.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 物流详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressDetailDTO implements Serializable {

    /**
     * 物流状态code
     */
    private Integer express_status_code;

    /**
     * 物流状态
     */
    private String express_status;

    /**
     * 物流公司编码
     */
    private String com;

    /**
     * 物流公司名称
     */
    private String express_company_name;

    /**
     * 物流进度详情
     */
    private String express_progress_info;
}
