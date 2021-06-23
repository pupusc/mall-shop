package com.wanmi.sbc.elastic.sensitivewords.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsQueryRequest;
import com.wanmi.sbc.elastic.api.response.sensitivewords.EsSensitiveWordsPageResponse;
import com.wanmi.sbc.elastic.bean.vo.sensitivewords.EsSensitiveWordsVO;
import com.wanmi.sbc.elastic.sensitivewords.model.root.EsSensitiveWords;
import com.wanmi.sbc.elastic.sensitivewords.repository.EsSensitiveWordsRepository;
import com.wanmi.sbc.setting.api.response.SensitiveWordsPageResponse;
import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;


/**
 * @author houshuai
 * @date 2020/12/11 16:19
 * @description <p> </p>
 */
@Service
public class EsSensitiveWordsQueryService {

    @Autowired
    private EsSensitiveWordsRepository esSensitiveWordsRepository;

    /**
     * 敏感词库列表
     * @param request
     * @return
     */
    public BaseResponse<EsSensitiveWordsPageResponse> page(EsSensitiveWordsQueryRequest request) {

        //查询条件
        NativeSearchQuery searchQuery = request.esCriteria();
        Page<EsSensitiveWords> sensitiveWordsPage = esSensitiveWordsRepository.search(searchQuery);
        Page<EsSensitiveWordsVO> newPage = sensitiveWordsPage.map(entity -> {
            EsSensitiveWordsVO sensitiveWordsVO = EsSensitiveWordsVO.builder().build();
            BeanUtils.copyProperties(entity, sensitiveWordsVO);
            return sensitiveWordsVO;
        });
        MicroServicePage<EsSensitiveWordsVO> microPage = new MicroServicePage<>(newPage, request.getPageable());
        EsSensitiveWordsPageResponse finalRes = new EsSensitiveWordsPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }
}
