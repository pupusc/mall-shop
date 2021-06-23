package com.wanmi.sbc.marketing.api.response.grouponactivity;

import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>拼团活动信息表列表结果</p>
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 拼团活动信息表列表结果
     */
    private List<GrouponActivityVO> grouponActivityVOList;
}
