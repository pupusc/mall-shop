package com.wanmi.sbc.marketing.api.response.grouponactivity;

import com.wanmi.sbc.marketing.bean.vo.EsGrouponActivityVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>拼团活动信息表新增结果</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityAddResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 拼团活动信息
     */
    private List<String> grouponActivityInfos;


    private List<EsGrouponActivityVO> esGrouponActivityVOList;

}
