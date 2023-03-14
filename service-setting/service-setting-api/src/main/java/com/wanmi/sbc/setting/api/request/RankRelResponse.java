package com.wanmi.sbc.setting.api.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankRelResponse implements Serializable {
    private List<Integer> relIdList;
}
