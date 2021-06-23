package com.wanmi.sbc.setting.api.request.searchterms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/17 16:42
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssociationLongTailWordByIdsRequest implements Serializable {

    private List<Long> idList;
}
