package com.wanmi.sbc.crm.tagdimension.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SelectParamResponse
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/8/26 20:58
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectParamResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Map<String,String>> selectParamList;
}
