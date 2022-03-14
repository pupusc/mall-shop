package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.constant.SettingErrorCode;
import com.wanmi.sbc.setting.api.provider.SensitiveWordsSaveProvider;
import com.wanmi.sbc.setting.api.request.SensitiveWordsAddRequest;
import com.wanmi.sbc.setting.api.request.SensitiveWordsDelByIdListRequest;
import com.wanmi.sbc.setting.api.request.SensitiveWordsDelByIdRequest;
import com.wanmi.sbc.setting.api.request.SensitiveWordsDeleteResponse;
import com.wanmi.sbc.setting.api.request.SensitiveWordsModifyRequest;
import com.wanmi.sbc.setting.api.request.SensitiveWordsQueryRequest;
import com.wanmi.sbc.setting.api.request.SensitiveWordsSaveResponse;
import com.wanmi.sbc.setting.api.response.SensitiveWordsModifyResponse;
import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import com.wanmi.sbc.setting.sensitivewords.model.root.SensitiveWords;
import com.wanmi.sbc.setting.sensitivewords.service.BadWordService;
import com.wanmi.sbc.setting.sensitivewords.service.SensitiveWordsService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>保存服务接口实现</p>
 *
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@RestController
@Validated
public class SensitiveWordsSaveController implements SensitiveWordsSaveProvider {
    @Autowired
    private SensitiveWordsService sensitiveWordsService;

	@Autowired
	private BadWordService badWordService;

    @Override
    public BaseResponse<SensitiveWordsSaveResponse> add(@RequestBody @Valid SensitiveWordsAddRequest sensitiveWordsAddRequest) {
        String[] wordArr = sensitiveWordsAddRequest.getSensitiveWords().split("\n");
        if (wordArr.length > 100) {
            throw new SbcRuntimeException(SettingErrorCode.SENSITIVE_WORDS_COUNT);
        }
        // 去重
        List<String> wordList = Stream.of(wordArr).distinct().collect(Collectors.toList());
        List<SensitiveWords> wordsList = new ArrayList<>();
        SensitiveWords sensitiveWords;
        SensitiveWordsQueryRequest query = new SensitiveWordsQueryRequest();
        List<SensitiveWords> queryRes;
        int count = 0;
        for (String word : wordList) {
            if (word.length() == 0) {
                continue;
            } else if (word.length() > 30) {
                throw new SbcRuntimeException(SettingErrorCode.SENSITIVE_WORDS_LENGTH, new Object[]{word});
            }
            sensitiveWordsAddRequest.setSensitiveWords(word);
            sensitiveWords = new SensitiveWords();
            KsBeanUtil.copyPropertiesThird(sensitiveWordsAddRequest, sensitiveWords);
            query.setDelFlag(DeleteFlag.NO);
            query.setSensitiveWords(word);
            queryRes = sensitiveWordsService.list(query);
            if (CollectionUtils.isNotEmpty(queryRes)) {
                // 若敏感词已经被添加过,则更新添加时间
                sensitiveWords.setSensitiveId(queryRes.get(0).getSensitiveId());
            }
            wordsList.add(sensitiveWords);
            count++;
        }
        List<SensitiveWordsVO> sensitiveWordsVOList = sensitiveWordsService.add(wordsList);
		badWordService.addBadWordToHashMap(true);
        SensitiveWordsSaveResponse response = SensitiveWordsSaveResponse.builder()
                .sensitiveWordsList(sensitiveWordsVOList)
                .count(count)
                .build();
        return BaseResponse.success(response);
    }

	@Override
	public BaseResponse<SensitiveWordsModifyResponse> modify(@RequestBody @Valid SensitiveWordsModifyRequest sensitiveWordsModifyRequest) {
		SensitiveWords sensitiveWords = new SensitiveWords();
		KsBeanUtil.copyPropertiesThird(sensitiveWordsModifyRequest, sensitiveWords);
		SensitiveWords sensitive = sensitiveWordsService.modify(sensitiveWords);
		badWordService.addBadWordToHashMap(true);
		return BaseResponse.success(new SensitiveWordsModifyResponse(sensitiveWordsService.wrapperVo(sensitive)));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid SensitiveWordsDelByIdRequest sensitiveWordsDelByIdRequest) {
		sensitiveWordsService.deleteById(sensitiveWordsDelByIdRequest.getSensitiveId());
		badWordService.addBadWordToHashMap(true);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid SensitiveWordsDelByIdListRequest sensitiveWordsDelByIdListRequest) {
        SensitiveWordsDeleteResponse response = sensitiveWordsService.deleteByIdList(sensitiveWordsDelByIdListRequest);
		badWordService.addBadWordToHashMap(true);
		return BaseResponse.success(response);
	}

}

