package com.wanmi.sbc.sensitivewords;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.sensitivewords.request.SensitiveWordsValidateRequest;
import com.wanmi.sbc.sensitivewords.service.SensitiveWordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 敏感词验证
 * Created by dyt
 */
@RestController
@RequestMapping("/sensitiveWords")
public class SensitiveWordsBaseController {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    /**
     * 分页查询敏感词
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/valid", method = RequestMethod.POST)
    public BaseResponse<String> valid(@RequestBody @Valid SensitiveWordsValidateRequest request) {
        String res = sensitiveWordService.getBadWordTxt(request.getText());
        if(StringUtils.isNotBlank(res)){
            return BaseResponse.success(res);

        }
        return BaseResponse.SUCCESSFUL();
    }
}
