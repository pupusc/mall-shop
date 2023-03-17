package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.GoodsEvaluateAnalyseBo;
import com.wanmi.sbc.bookmeta.bo.SearchTermBo;
import com.wanmi.sbc.bookmeta.entity.GoodsEvaluateAnalyse;
import com.wanmi.sbc.bookmeta.entity.SaleNum;
import com.wanmi.sbc.bookmeta.entity.SearchTerm;
import com.wanmi.sbc.bookmeta.mapper.SearchTermMapper;
import com.wanmi.sbc.bookmeta.provider.SearchTermProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/13:49
 * @Description:
 */
@Validated
@RestController
public class SearchTermProviderImpl implements SearchTermProvider {
    @Resource
    private SearchTermMapper searchTermMapper;

    @Override
    public List<SearchTermBo> getSearchTermTree(SearchTermBo searchTermBo) {
        List<SearchTerm> tree = searchTermMapper.getTree(0, searchTermBo.getDefaultSearchKeyword());
        List<SearchTermBo> objects = KsBeanUtil.convertList(tree, SearchTermBo.class);
        for (SearchTermBo parent : objects) {
            parent.setChildrenList(KsBeanUtil.convertList(searchTermMapper.getTree(parent.getId(), searchTermBo.getDefaultSearchKeyword()), SearchTermBo.class));
        }
        return objects;
    }

    @Override
    public int deleteSearchTerm(SearchTermBo bo) {
        List<SearchTerm> children = searchTermMapper.getTree(bo.getId(), null);
        for (SearchTerm child : children) {
            searchTermMapper.delete(child.getId());
        }
        return searchTermMapper.delete(bo.getId());
    }

    @Override
    public int updateSearchTerm(SearchTermBo bo) {
        SearchTerm convert = KsBeanUtil.convert(bo, SearchTerm.class);
        return searchTermMapper.update(convert);
    }

    @Override
    public int addSearchTerm(SearchTermBo bo) {
        return searchTermMapper.insert(bo);
    }

    @Override
    public BusinessResponse<String> importGoodsEvaluateAnalyse(List<GoodsEvaluateAnalyseBo> list) {
        int addCount = 0;
        for (GoodsEvaluateAnalyseBo bo : list) {
            if (StringUtils.isBlank(bo.getEvaluateId())) {
                return BusinessResponse.error("failed,EvaluateId can't be blank");
            }
            boolean isExistEvaluateId = searchTermMapper.isExistEvaluateId(bo.getEvaluateId()) > 0;
            if (isExistEvaluateId) {
                GoodsEvaluateAnalyse convert = KsBeanUtil.convert(bo, GoodsEvaluateAnalyse.class);
                boolean isExistEvaluateAnalyse = searchTermMapper.isExistEvaluateAnalyse(convert) > 0;
                if (!isExistEvaluateAnalyse) {
                    searchTermMapper.insertEvaluateAnalyse(convert);
                    addCount++;
                }
            }
        }
        return BusinessResponse.success("Success,add" + addCount + "!");
    }
}
