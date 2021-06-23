package com.wanmi.sbc.elastic.api.request.groupon;

import com.wanmi.sbc.marketing.bean.vo.EsGrouponActivityVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/12 18:50
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsGrouponActivityAddReqquest implements Serializable {

    private static final long serialVersionUID = -1681970353929213779L;

    private List<EsGrouponActivityVO> esGrouponActivityVOList;
}
