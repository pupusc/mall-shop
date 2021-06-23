package com.wanmi.sbc.marketing.api.response.grouponrecord;

import com.wanmi.sbc.marketing.bean.vo.GrouponRecordVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>拼团活动参团信息表新增结果</p>
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponRecordByCustomerResponse implements Serializable {


    private static final long serialVersionUID = -1304851847616075913L;
    /**
     * 已新增的拼团活动参团信息表信息
     */
    private GrouponRecordVO grouponRecordVO;
}
