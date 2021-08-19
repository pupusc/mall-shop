package com.wanmi.sbc.marketing.api.response.grouponactivity;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityForManagerVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>拼团活动信息表分页结果</p>
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityPage4MangerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 拼团活动信息表分页结果--封装最低拼团价
     */
    private MicroServicePage<GrouponActivityForManagerVO> grouponActivityVOPage;

}
