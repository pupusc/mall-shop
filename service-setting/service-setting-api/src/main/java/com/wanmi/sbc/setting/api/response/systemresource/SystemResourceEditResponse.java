package com.wanmi.sbc.setting.api.response.systemresource;

import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/14 15:37
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemResourceEditResponse implements Serializable {

    private static final long serialVersionUID = 2513338325834463894L;

    private List<SystemResourceVO> systemResourceVOList;
}
