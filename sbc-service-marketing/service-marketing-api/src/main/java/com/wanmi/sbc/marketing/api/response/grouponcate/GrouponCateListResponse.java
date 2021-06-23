package com.wanmi.sbc.marketing.api.response.grouponcate;

import com.wanmi.sbc.marketing.bean.vo.GrouponCateVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>拼团活动信息表列表结果</p>
 *
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponCateListResponse implements Serializable {
    private static final long serialVersionUID = 5049111608854809462L;

    /**
     * 拼团活动信息表列表结果
     */
    private List<GrouponCateVO> grouponCateVOList;
}
