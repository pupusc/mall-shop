package com.wanmi.sbc.setting.api.response.yunservice;

import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author houshuai
 * @date 2020/12/14 14:24
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YunUploadResourceResponse {

    private SystemResourceVO systemResourceVO;

    private String resourceUrl;
}
