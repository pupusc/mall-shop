package com.wanmi.sbc.order.api.response.groupon;

import com.wanmi.sbc.order.bean.vo.GrouponDetailVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>团明细</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponDetailListResponse implements Serializable {
    private static final long serialVersionUID = -9076705454351078810L;


    /***
     * 团明细
     */
    private List<GrouponDetailVO> grouponDetails;


}
