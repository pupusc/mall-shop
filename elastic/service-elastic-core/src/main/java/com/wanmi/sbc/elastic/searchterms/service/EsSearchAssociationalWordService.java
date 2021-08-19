package com.wanmi.sbc.elastic.searchterms.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordPageRequest;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordRequest;
import com.wanmi.sbc.elastic.bean.vo.searchterms.EsSearchAssociationalWordVO;
import com.wanmi.sbc.elastic.searchterms.model.root.EsSearchAssociationalWord;
import com.wanmi.sbc.elastic.searchterms.repository.EsSearchAssociationalWordRepository;
import com.wanmi.sbc.setting.api.provider.searchterms.SearchAssociationalWordQueryProvider;
import com.wanmi.sbc.setting.api.request.searchterms.SearchAssociationalWordPageRequest;
import com.wanmi.sbc.setting.bean.vo.SearchAssociationalWordVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author houshuai
 * @date 2020/12/17 15:36
 * @description <p> 搜索联想词 </p>
 */
@Slf4j
@Service
public class EsSearchAssociationalWordService {

    @Autowired
    private SearchAssociationalWordQueryProvider searchAssociationalWordQueryProvider;

    @Autowired
    private EsSearchAssociationalWordRepository esSearchAssociationalWordRepository;

    @Autowired
    public ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 新增es搜索联想词
     *
     * @param request 请求参数
     */
    public void add(EsSearchAssociationalWordRequest request) {
        List<EsSearchAssociationalWordVO> searchAssociationalWordVOList = request.getSearchAssociationalWordVOList();
        if (CollectionUtils.isEmpty(searchAssociationalWordVOList)) {
            return;
        }
        List<EsSearchAssociationalWord> words = KsBeanUtil.convert(searchAssociationalWordVOList, EsSearchAssociationalWord.class);
        saveAll(words);
    }


    /**
     * 删除搜索联想词
     * @param id
     */
    public void deleteById(Long id) {
        Optional<EsSearchAssociationalWord> optional = esSearchAssociationalWordRepository.findById(id);
        optional.ifPresent(entity -> {
            entity.setDelFlag(DeleteFlag.YES);
            saveAll(Collections.singletonList(entity));
        });
    }

    /**
     * 初始化搜索词
     * @param request
     */
    public void init(EsSearchAssociationalWordPageRequest request) {
        Boolean flg = Boolean.TRUE;
        int pageNum = request.getPageNum();
        int pageSize = 2000;

        SearchAssociationalWordPageRequest pageRequest = KsBeanUtil.convert(request, SearchAssociationalWordPageRequest.class);
        try {
            while (flg) {
                pageRequest.putSort("createTime", SortType.DESC.toValue());
                pageRequest.setPageNum(pageNum);
                pageRequest.setPageSize(pageSize);
                List<SearchAssociationalWordVO> searchAssociationalWordVOList = searchAssociationalWordQueryProvider.page(pageRequest).getContext().getSearchAssociationalWordPage().getContent();

                if (CollectionUtils.isEmpty(searchAssociationalWordVOList)) {
                    flg = Boolean.FALSE;
                    log.info("==========ES初始化搜索词库列表，结束pageNum:{}==============", pageNum);
                } else {
                    List<EsSearchAssociationalWord> newInfos = KsBeanUtil.convert(searchAssociationalWordVOList, EsSearchAssociationalWord.class);
                    saveAll(newInfos);
                    log.info("==========ES初始化搜索词库列表成功，当前pageNum:{}==============", pageNum);
                    pageNum++;
                }
            }
        } catch (Exception e) {
            log.info("==========ES初始化搜索词库列表异常，异常pageNum:{}==============", pageNum);
            throw new SbcRuntimeException("K-120011", new Object[]{pageNum});
        }
    }

    private void saveAll(List<EsSearchAssociationalWord> newInfos) {
        //手动删除索引时，重新设置mapping
        if(!elasticsearchTemplate.indexExists(EsSearchAssociationalWord.class)){
            elasticsearchTemplate.createIndex(EsSearchAssociationalWord.class);
            elasticsearchTemplate.putMapping(EsSearchAssociationalWord.class);
        }
        esSearchAssociationalWordRepository.saveAll(newInfos);
    }
}
