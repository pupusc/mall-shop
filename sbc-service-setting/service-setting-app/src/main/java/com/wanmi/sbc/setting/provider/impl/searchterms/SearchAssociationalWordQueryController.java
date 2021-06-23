package com.wanmi.sbc.setting.provider.impl.searchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.searchterms.SearchAssociationalWordQueryProvider;
import com.wanmi.sbc.setting.api.request.searchterms.AssociationLongTailWordByIdsRequest;
import com.wanmi.sbc.setting.api.request.searchterms.AssociationLongTailWordLikeRequest;
import com.wanmi.sbc.setting.api.request.searchterms.SearchAssociationalWordPageRequest;
import com.wanmi.sbc.setting.api.response.searchterms.AssociationLongTailWordByIdsResponse;
import com.wanmi.sbc.setting.api.response.searchterms.AssociationLongTailWordLikeResponse;
import com.wanmi.sbc.setting.api.response.searchterms.SearchAssociationalWordPageResponse;
import com.wanmi.sbc.setting.bean.vo.AssociationLongTailWordVO;
import com.wanmi.sbc.setting.bean.vo.SearchAssociationalWordVO;
import com.wanmi.sbc.setting.searchterms.model.AssociationLongTailWord;
import com.wanmi.sbc.setting.searchterms.repository.AssociationLongTailWordRepossitory;
import com.wanmi.sbc.setting.searchterms.service.SearchAssociationalWordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>搜索词API</p>
 *
 * @author weiwenhao
 * @date 2020-04-17
 */
@RestController
public class SearchAssociationalWordQueryController implements SearchAssociationalWordQueryProvider {


    @Autowired
    SearchAssociationalWordService searchAssociationalWordService;

    @Autowired
    AssociationLongTailWordRepossitory associationLongTailWordRepossitory;


    /**
     * 搜索词的分页查询
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<SearchAssociationalWordPageResponse> page(@Valid SearchAssociationalWordPageRequest request) {
        MicroServicePage<SearchAssociationalWordVO> page =
                searchAssociationalWordService.searchAssociationalWordPage(request);
        List<SearchAssociationalWordVO> searchAssociationalWords = page.getContent();
        for (SearchAssociationalWordVO search : searchAssociationalWords) {
            List<AssociationLongTailWord> associationLongTailWords =
                    associationLongTailWordRepossitory.findBySearchAssociationalWordIdAndDelFlagOrderBySortNumberAscCreateTimeDesc(search.getId(), DeleteFlag.NO);
            if (CollectionUtils.isNotEmpty(associationLongTailWords)) {
                List<AssociationLongTailWordVO> associationLongTailWordVO =
                        associationLongTailWords.stream().map(association -> {
                            AssociationLongTailWordVO associationLongTailWordList = new AssociationLongTailWordVO();
                            KsBeanUtil.copyPropertiesThird(association, associationLongTailWordList);
                            return associationLongTailWordList;
                        }).collect(Collectors.toList());
                search.setAssociationLongTailWordList(associationLongTailWordVO);
            }
        }
        return BaseResponse.success(SearchAssociationalWordPageResponse.builder().searchAssociationalWordPage(page).build());
    }

    /**
     * 模糊搜索词联想词
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<AssociationLongTailWordLikeResponse> likeAssociationalWord(@Valid AssociationLongTailWordLikeRequest request) {
        return BaseResponse.success(searchAssociationalWordService.likeAssociationalWord(request.getAssociationalWord()));
    }

    /**
     * 通过关联联想词id查询
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<AssociationLongTailWordByIdsResponse> listByIds(@Valid AssociationLongTailWordByIdsRequest request) {

        List<AssociationLongTailWord> result = associationLongTailWordRepossitory.findBySearchAssociationalWordIdInAndDelFlagOrderBySortNumberAscCreateTimeDesc(request.getIdList(), DeleteFlag.NO);
        AssociationLongTailWordByIdsResponse response = AssociationLongTailWordByIdsResponse.builder()
                .longTailWordVOList(Collections.emptyList())
                .build();
        if (CollectionUtils.isNotEmpty(result)) {
            List<AssociationLongTailWordVO> newList = KsBeanUtil.convert(result, AssociationLongTailWordVO.class);
            response.setLongTailWordVOList(newList);
            return BaseResponse.success(response);
        }
        return BaseResponse.success(response);
    }
}
