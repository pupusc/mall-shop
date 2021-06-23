package com.wanmi.sbc.setting.api.response.searchterms;

import com.wanmi.sbc.setting.bean.vo.AssociationLongTailWordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/17 16:43
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssociationLongTailWordByIdsResponse implements Serializable {

    private static final long serialVersionUID = 8090609004460587022L;

    List<AssociationLongTailWordVO> longTailWordVOList;
}
