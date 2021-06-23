package com.wanmi.sbc.setting.searchterms.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.searchterms.SearchAssociationalWordPageRequest;
import com.wanmi.sbc.setting.api.response.searchterms.AssociationLongTailWordLikeResponse;
import com.wanmi.sbc.setting.api.response.searchterms.AssociationLongTailWordResponse;
import com.wanmi.sbc.setting.api.response.searchterms.SearchAssociationalWordResponse;
import com.wanmi.sbc.setting.bean.vo.AssociationLongTailLikeWordVO;
import com.wanmi.sbc.setting.bean.vo.AssociationLongTailWordVO;
import com.wanmi.sbc.setting.bean.vo.SearchAssociationalWordVO;
import com.wanmi.sbc.setting.searchterms.model.AssociationLongTailWord;
import com.wanmi.sbc.setting.searchterms.model.SearchAssociationalWord;
import com.wanmi.sbc.setting.searchterms.repository.AssociationLongTailWordRepossitory;
import com.wanmi.sbc.setting.searchterms.repository.SearchAssociationalWordRepository;
import com.wanmi.sbc.setting.util.error.SearchTermsErrorCode;
import jodd.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>搜索词Service</p>
 *
 * @author weiwenhao
 * @date 2020-04-16
 */
@Service
public class SearchAssociationalWordService {

    @Autowired
    SearchAssociationalWordRepository searchAssociationalWordRepository;

    @Autowired
    AssociationLongTailWordRepossitory associationLongTailWordRepossitory;

    @Autowired
    private EntityManager entityManager;

    /**
     * 新增搜索词
     *
     * @param searchAssociationalWord
     * @return
     */
    public SearchAssociationalWordResponse add(SearchAssociationalWord searchAssociationalWord) {
        // 搜索词去重
        Integer repeatName =
                searchAssociationalWordRepository.countBySearchTermsAndDelFlag(searchAssociationalWord.getSearchTerms(),
                DeleteFlag.NO);
        if (Objects.nonNull(repeatName) && repeatName > 0) {
            throw new SbcRuntimeException(SearchTermsErrorCode.SEARCH_ASSOCIATION_NAME_EXIST);
        }

        searchAssociationalWord.setCreateTime(LocalDateTime.now());
        searchAssociationalWord.setDelFlag(DeleteFlag.NO);
        SearchAssociationalWordVO searchAssociationalWordVO = new SearchAssociationalWordVO();
        KsBeanUtil.copyPropertiesThird(searchAssociationalWordRepository.save(searchAssociationalWord),
                searchAssociationalWordVO);
        return new SearchAssociationalWordResponse(searchAssociationalWordVO);
    }

    /**
     * 修改搜索词
     *
     * @param searchAssociationalWord
     * @return
     */
    public SearchAssociationalWordResponse modify(SearchAssociationalWord searchAssociationalWord) {
        SearchAssociationalWord searchWord =
                searchAssociationalWordRepository.findById(searchAssociationalWord.getId()).orElseThrow(() -> new SbcRuntimeException(SearchTermsErrorCode.NOT_EXIST));

        // 搜索词去重
        Integer repeatName = searchAssociationalWordRepository.findByNameExceptSelf(searchAssociationalWord.getId(),
                searchAssociationalWord.getSearchTerms());
        if (Objects.nonNull(repeatName) && repeatName > 0) {
            throw new SbcRuntimeException(SearchTermsErrorCode.SEARCH_ASSOCIATION_NAME_EXIST);
        }

        if (searchAssociationalWord.getUpdatePerson() != null) {
            searchWord.setUpdatePerson(searchAssociationalWord.getUpdatePerson());
        }
        if (searchAssociationalWord.getSearchTerms() != null) {
            searchWord.setSearchTerms(searchAssociationalWord.getSearchTerms());
        }
        searchWord.setUpdateTime(LocalDateTime.now());
        searchWord.setDelFlag(DeleteFlag.NO);
        SearchAssociationalWordVO searchAssociationalWordVO = new SearchAssociationalWordVO();
        KsBeanUtil.copyPropertiesThird(searchAssociationalWordRepository.save(searchWord),
                searchAssociationalWordVO);
        return new SearchAssociationalWordResponse(searchAssociationalWordVO);
    }

    /**
     * 搜索词分页
     *
     * @param request
     * @return
     */
    public MicroServicePage<SearchAssociationalWordVO> searchAssociationalWordPage(SearchAssociationalWordPageRequest request) {
        //查询列表
        String sql = "SELECT t.* FROM search_associational_word t ";
        //查询记录的总数量
        String countSql = "SELECT count(1) count FROM search_associational_word t ";
        //条件查询
        String whereSql = "WHERE 1 = 1 and t.del_flag=0";

        whereSql += " order by t.create_time desc";

        Query query = entityManager.createNativeQuery(sql.concat(whereSql));
        query.setFirstResult(request.getPageNum() * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        query.unwrap(SQLQuery.class).addEntity("t", SearchAssociationalWord.class);
        List<SearchAssociationalWordVO> responsesList =
                ((List<SearchAssociationalWord>) query.getResultList()).stream().map(source -> {
                    SearchAssociationalWordVO response = new SearchAssociationalWordVO();
                    BeanUtils.copyProperties(source, response);
                    return response;
                }).collect(Collectors.toList());

        long count = 0;

        if (CollectionUtils.isNotEmpty(responsesList)) {
            Query queryCount = entityManager.createNativeQuery(countSql.concat(whereSql));
            count = Long.parseLong(queryCount.getSingleResult().toString());
        }
        return new MicroServicePage<>(responsesList, request.getPageable(), count);
    }


    /**
     * 新增联想词
     *
     * @param associationLongTailWord
     * @return
     */
    public AssociationLongTailWordResponse addAssociationalWord(AssociationLongTailWord associationLongTailWord) {
        associationLongTailWord.setCreateTime(LocalDateTime.now());
        associationLongTailWord.setDelFlag(DeleteFlag.NO);
        AssociationLongTailWordVO associationLongTailWordVO = new AssociationLongTailWordVO();
        KsBeanUtil.copyPropertiesThird(associationLongTailWordRepossitory.save(associationLongTailWord),
                associationLongTailWordVO);
        return new AssociationLongTailWordResponse(associationLongTailWordVO);
    }

    /**
     * 修改联想词
     *
     * @param associationLongTailWord
     * @return
     */
    public AssociationLongTailWordResponse modifyAssociationalWord(AssociationLongTailWord associationLongTailWord) {
        AssociationLongTailWord associationWord =
                associationLongTailWordRepossitory.findById(associationLongTailWord.getAssociationLongTailWordId()).orElseThrow(() -> new SbcRuntimeException(SearchTermsErrorCode.NOT_EXIST));
        if (associationLongTailWord.getAssociationalWord() != null) {
            associationWord.setAssociationalWord(associationLongTailWord.getAssociationalWord());
        }
        if (associationLongTailWord.getLongTailWord() != null) {
            associationWord.setLongTailWord(associationLongTailWord.getLongTailWord());
        }
        if (associationLongTailWord.getUpdatePerson() != null) {
            associationWord.setUpdatePerson(associationLongTailWord.getUpdatePerson());
        }
        associationWord.setUpdateTime(LocalDateTime.now());
        associationWord.setDelFlag(DeleteFlag.NO);
        AssociationLongTailWordVO associationLongTailWordVO = new AssociationLongTailWordVO();
        KsBeanUtil.copyPropertiesThird(associationLongTailWordRepossitory.save(associationWord),
                associationLongTailWordVO);
        return new AssociationLongTailWordResponse(associationLongTailWordVO);
    }


    /**
     * 联想词排序
     *
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse sortAssociationalWord(List<AssociationLongTailWord> request) {
        for (AssociationLongTailWord associationLongTailWord : request) {
            associationLongTailWordRepossitory.sortAssociationalWord(associationLongTailWord.getSortNumber(),
                    associationLongTailWord.getAssociationLongTailWordId());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除搜索词
     *
     * @param searchAssociationalWordId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteSearchAssociationalWord(Long searchAssociationalWordId) {
        return searchAssociationalWordRepository.deleteSearchAssociationalWord(searchAssociationalWordId);
    }

    /**
     * 删除联想词
     *
     * @param associationLongTailWordId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteAssociationLongTailWord(Long associationLongTailWordId) {
        return associationLongTailWordRepossitory.deleteAssociationLongTailWord(associationLongTailWordId);
    }

    /**
     * 查询搜索词下面联想词的个数
     *
     * @param searchAssociationalWordId
     * @return
     */
    public Long countAssociationLongTailWord(Long searchAssociationalWordId) {
        return associationLongTailWordRepossitory.countAssociationLongTailWord(searchAssociationalWordId);
    }

    /**
     * 根据搜索词id查询该搜索词下的联想词list
     * @param searchAssociationalWordId
     * @return
     */
    public List<AssociationLongTailWord> getAssociationWordList (Long searchAssociationalWordId) {
        return associationLongTailWordRepossitory.findBySearchAssociationalWordIdAndDelFlag(searchAssociationalWordId,
                DeleteFlag.NO);
    }

    /**
     * 除自己外，联想词是否重复
     * @param searchAssociationalWordId   搜索词ID
     * @param associationalWord 联想词名称
     * @param associationLongTailWordId 联想词id
     * @return
     */
    public Integer findByNameExceptSelf(Long associationLongTailWordId, Long searchAssociationalWordId,
                                        String associationalWord) {
        return associationLongTailWordRepossitory.findByNameExceptSelf(associationLongTailWordId, searchAssociationalWordId,
                associationalWord);
    }

    /**
     * 根据联想词id查询联想词
     * @param associationLongTailWordId
     * @return
     */
    public AssociationLongTailWord getAssociationLongTailWordById(Long associationLongTailWordId){
        return associationLongTailWordRepossitory.findById(associationLongTailWordId).orElse(null);
    }

    /**
     * 模糊搜索联想词
     *
     * @param associationalWord
     * @return
     */
    public AssociationLongTailWordLikeResponse likeAssociationalWord(String associationalWord) {
        List<AssociationLongTailLikeWordVO> associationLongTailLikeWordVOLists = new ArrayList<>();
        if(StringUtils.isBlank(associationalWord)){
            return new AssociationLongTailWordLikeResponse(associationLongTailLikeWordVOLists);
        }
        List<SearchAssociationalWord> listSerach =
                searchAssociationalWordRepository.findAllBySearchTermsContainingAndDelFlag(associationalWord,
                        DeleteFlag.NO);
        if (CollectionUtils.isNotEmpty(listSerach)) {
            for (SearchAssociationalWord searchAssociationalWord : listSerach) {
                List<AssociationLongTailWord> associationLongTailWordList =
                        associationLongTailWordRepossitory.findBySearchAssociationalWordIdAndDelFlag(searchAssociationalWord.getId(), DeleteFlag.NO);
                for (AssociationLongTailWord associationLongTailWord : associationLongTailWordList) {
                    AssociationLongTailLikeWordVO associationLongTailLikeWordVO = new AssociationLongTailLikeWordVO();
                    if (StringUtil.isNotBlank(associationLongTailWord.getLongTailWord())) {
                        associationLongTailLikeWordVO.setLongTailWord(associationLongTailWord.getLongTailWord().split(";"));
                    }
                    KsBeanUtil.copyPropertiesThird(associationLongTailWord, associationLongTailLikeWordVO);
                    associationLongTailLikeWordVOLists.add(associationLongTailLikeWordVO);
                }
            }
        }
        return new AssociationLongTailWordLikeResponse(associationLongTailLikeWordVOLists);
    }
}
