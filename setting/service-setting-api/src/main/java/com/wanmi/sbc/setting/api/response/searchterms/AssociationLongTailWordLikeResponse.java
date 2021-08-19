package com.wanmi.sbc.setting.api.response.searchterms;

import com.wanmi.sbc.setting.bean.vo.AssociationLongTailLikeWordVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>搜索词结果</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociationLongTailWordLikeResponse implements Serializable {


    private static final long serialVersionUID = 1763431479791211242L;


    /**
     * 搜索词&联想词信息
     */
    List<AssociationLongTailLikeWordVO>  AssociationLongTailLikeWordList;

}
