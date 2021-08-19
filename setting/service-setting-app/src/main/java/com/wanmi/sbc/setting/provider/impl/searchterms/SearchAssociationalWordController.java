package com.wanmi.sbc.setting.provider.impl.searchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.searchterms.SearchAssociationalWordProvider;
import com.wanmi.sbc.setting.api.request.searchterms.*;
import com.wanmi.sbc.setting.api.response.searchterms.AssociationLongTailWordDeleteResponse;
import com.wanmi.sbc.setting.api.response.searchterms.SearchAssociationalWordDeleteResponse;
import com.wanmi.sbc.setting.api.response.searchterms.SearchAssociationalWordResponse;
import com.wanmi.sbc.setting.searchterms.model.AssociationLongTailWord;
import com.wanmi.sbc.setting.searchterms.model.SearchAssociationalWord;
import com.wanmi.sbc.setting.searchterms.service.SearchAssociationalWordService;
import com.wanmi.sbc.setting.util.error.SearchTermsErrorCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>搜索词API</p>
 *
 * @author weiwenhao
 * @date 2020-04-16
 */
@RestController
public class SearchAssociationalWordController implements SearchAssociationalWordProvider {


    @Autowired
    SearchAssociationalWordService searchAssociationalWordService;

    /**
     * 新增搜索词
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<SearchAssociationalWordResponse> add(@RequestBody @Valid SearchAssociationalWordRequest request) {
        SearchAssociationalWord searchAssociationalWord = new SearchAssociationalWord();
        KsBeanUtil.copyPropertiesThird(request, searchAssociationalWord);
        return BaseResponse.success(searchAssociationalWordService.add(searchAssociationalWord));
    }

    /**
     * 修改搜素词
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<SearchAssociationalWordResponse> modify(@Valid SearchAssociationalWordModifyRequest request) {
        SearchAssociationalWord searchAssociationalWord = new SearchAssociationalWord();
        KsBeanUtil.copyPropertiesThird(request, searchAssociationalWord);
        return BaseResponse.success(searchAssociationalWordService.modify(searchAssociationalWord));
    }

    /**
     * 删除搜索词
     *
     * @param searchAssociationalWordId
     * @return
     */
    @Override
    public BaseResponse deleteSearchAssociationalWord(SearchAssociationalWordDeleteByIdRequest searchAssociationalWordId) {
        SearchAssociationalWordDeleteResponse response = SearchAssociationalWordDeleteResponse.builder()
                .resultNum(searchAssociationalWordService.deleteSearchAssociationalWord(searchAssociationalWordId.getId())).build();
        return BaseResponse.success(response);
    }

    /**
     * 新增联想词
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse addAssociationalWord(@Valid AssociationLongTailWordRequest request) {
        List<AssociationLongTailWord> associationWordList =
                searchAssociationalWordService.getAssociationWordList(request.getSearchAssociationalWordId());
        if (CollectionUtils.isNotEmpty(associationWordList)){
            // 一个搜索词下面最多20个联想词
            if (associationWordList.size() >= 20) {
                throw new SbcRuntimeException(SearchTermsErrorCode.ASSOCIATION_WORDS_QUANTITATIVE_RESTRICTION);
            }
            // 同一搜索词的联想词名称不能重复
            if (associationWordList.stream().anyMatch(word -> StringUtils.equals(request.getAssociationalWord(),
                    word.getAssociationalWord()))) {
                throw new SbcRuntimeException(SearchTermsErrorCode.ASSOCIATION_NAME_EXIST);
            }
        }

        AssociationLongTailWord associationLongTailWord = new AssociationLongTailWord();
        KsBeanUtil.copyPropertiesThird(request, associationLongTailWord);

        // 处理联想词的长尾词
        this.setLongTailWord(associationLongTailWord, request.getLongTailWordList());

        return BaseResponse.success(searchAssociationalWordService.addAssociationalWord(associationLongTailWord));
    }

    /**
     * 修改联想词
     * @param request
     * @return
     */
    @Override
    public BaseResponse modifyAssociationalWord(@Valid AssociationLongTailWordModifyRequest request) {
        AssociationLongTailWord oldAssociationWord =
                searchAssociationalWordService.getAssociationLongTailWordById(request.getAssociationLongTailWordId());
        if (Objects.isNull(oldAssociationWord)){
            throw new SbcRuntimeException(SearchTermsErrorCode.ASSOCIATION_WORD_EXIST);
        }
        // 同一搜索词的联想词名称不能重复
        Integer repeatName =
                searchAssociationalWordService.findByNameExceptSelf(oldAssociationWord.getAssociationLongTailWordId()
                        , oldAssociationWord.getSearchAssociationalWordId(),
                        request.getAssociationalWord());
        if (Objects.nonNull(repeatName) && repeatName > 0) {
            throw new SbcRuntimeException(SearchTermsErrorCode.ASSOCIATION_NAME_EXIST);
        }

        AssociationLongTailWord associationLongTailWord = new AssociationLongTailWord();
        KsBeanUtil.copyPropertiesThird(request, associationLongTailWord);

        // 处理联想词的长尾词
        this.setLongTailWord(associationLongTailWord, request.getLongTailWordList());

        return BaseResponse.success(searchAssociationalWordService.modifyAssociationalWord(associationLongTailWord));
    }

    /**
     * 联想词排序
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse sortAssociationalWord(@Valid List<AssociationLongTailWordSortRequest> request) {
        List<AssociationLongTailWord> listAssociationLongTailWord =
                (List<AssociationLongTailWord>) KsBeanUtil.convert(request,
                AssociationLongTailWord.class);
        return searchAssociationalWordService.sortAssociationalWord(listAssociationLongTailWord);
    }

    /**
     * 删除联想词
     *
     * @param associationLongTailWordId
     * @return
     */
    @Override
    public BaseResponse deleteAssociationLongTailWord(AssociationLongTailWordDeleteByIdRequest associationLongTailWordId) {
        AssociationLongTailWordDeleteResponse response =
                AssociationLongTailWordDeleteResponse.builder().resultNum(searchAssociationalWordService.deleteAssociationLongTailWord(associationLongTailWordId.getAssociationLongTailWordId())).build();
        return BaseResponse.success(response);
    }

    /**
     * 新增/编辑 处理联想词的长尾词
     * @param associationLongTailWord
     * @param longTails
     */
    public void setLongTailWord(AssociationLongTailWord associationLongTailWord, List<String> longTails){
        // 处理联想词的长尾词
        if (CollectionUtils.isNotEmpty(longTails)) {
            // 一个联想词最多可添加3个长尾词
            if (longTails.size() > 3){
                throw new SbcRuntimeException(SearchTermsErrorCode.LONG_TAIL_WORD_MAX_ERROR);
            }
            // 一个联想词的长尾词不能重复
            List<String> newTails = longTails.stream().distinct().collect(Collectors.toList());
            if (longTails.size() != newTails.size()){
                throw new SbcRuntimeException(SearchTermsErrorCode.LONG_TAIL_WORD_EXIST);
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (String longTailWord : longTails) {
                //判断长尾词是否 > 5
                if (longTailWord.length() > 5) {
                    throw new SbcRuntimeException(SearchTermsErrorCode.SEARCH_TERM_LENGTH);
                }
                stringBuilder.append(longTailWord).append(";");
            }
            String associational = stringBuilder.toString();
            associationLongTailWord.setLongTailWord(associational.substring(0, associational.length() - 1));
        }
    }
}
