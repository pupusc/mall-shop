package com.wanmi.sbc.elastic.api.request.systemresource;

import com.wanmi.sbc.elastic.bean.vo.systemresource.EsSystemResourceVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/14 14:50
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsSystemResourceSaveRequest implements Serializable {

    private static final long serialVersionUID = -6971620852337539273L;

    private List<EsSystemResourceVO> systemResourceVOList;


}
