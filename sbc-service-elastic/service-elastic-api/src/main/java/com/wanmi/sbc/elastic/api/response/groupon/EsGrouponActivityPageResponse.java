package com.wanmi.sbc.elastic.api.response.groupon;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityForManagerVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsGrouponActivityPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 拼团活动信息表分页结果--封装最低拼团价
     */
    private MicroServicePage<GrouponActivityForManagerVO> grouponActivityVOPage;

}