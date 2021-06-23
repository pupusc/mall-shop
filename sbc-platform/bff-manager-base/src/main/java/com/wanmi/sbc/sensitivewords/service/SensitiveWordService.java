package com.wanmi.sbc.sensitivewords.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.provider.SensitiveWordsQueryProvider;
import com.wanmi.sbc.setting.api.request.SensitiveWordsBadWordRequest;
import com.wanmi.sbc.setting.api.request.SensitiveWordsListRequest;
import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 敏感词处理服务
 */
@Service
public class SensitiveWordService {

    @Autowired
    private SensitiveWordsQueryProvider queryProvider;

    /**
     * 获取符合的敏感词
     * @param text 内容文本
     * @return 敏感词
     */
    public Set<String> getBadWord(String text){
        SensitiveWordsBadWordRequest badWordRequest = new SensitiveWordsBadWordRequest();
        badWordRequest.setTxt(text);
        return queryProvider.getBadWord(badWordRequest).getContext();
    }

    /**
     * 获取敏感词数据
     * @return 敏感词数据
     */
    public String getBadWordTxt(String text){
        Set<String> res = getBadWord(text);
        if(CollectionUtils.isEmpty(res)){
            return null;
        }
        return "中包含敏感词[".concat(StringUtils.join(res,",")).concat("]");
    }

    /**
     * 获取所有敏感词数据  (批量式)
     * @return 敏感词数据
     */
    public Set<String> getAllBadWord(){
        List<SensitiveWordsVO> wordsVOS = queryProvider.list(SensitiveWordsListRequest.builder().delFlag(DeleteFlag.NO).build())
                .getContext().getSensitiveWordsVOList();
        return wordsVOS.stream().map(SensitiveWordsVO::getSensitiveWords).collect(Collectors.toSet());
    }


    /**
     * 获取包含敏感词数据 (批量式)
     * @return 敏感词内容文本
     */
    public String getBadWordTxt(Set<String> words, String text){
        Set<String> res = getBadWord(words, text);
        if(CollectionUtils.isEmpty(res)){
            return null;
        }
        return "中包含敏感词[".concat(StringUtils.join(res,",")).concat("]");
    }

    /**
     * 获取包含敏感词数据 (批量式)
     * @param words 所有敏感词
     * @param text 文本
     * @return 敏感词内容
     */
    private Set<String> getBadWord(Set<String> words, String text){
        return words.stream().filter(text::contains).collect(Collectors.toSet());
    }


}
