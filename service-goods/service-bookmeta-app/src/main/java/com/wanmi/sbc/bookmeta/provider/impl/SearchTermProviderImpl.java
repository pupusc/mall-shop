package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.SearchTermBo;
import com.wanmi.sbc.bookmeta.entity.SearchTerm;
import com.wanmi.sbc.bookmeta.mapper.SearchTermMapper;
import com.wanmi.sbc.bookmeta.provider.SearchTermProvider;
import com.wanmi.sbc.common.util.KsBeanUtil;
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
        List<SearchTermBo> tree = searchTermMapper.getTree(searchTermBo);
        for (SearchTermBo parent: tree) {
            parent.setChildrenList(searchTermMapper.getTree(parent));
        }
        return tree;
    }

    @Override
    public int deleteSearchTerm(SearchTermBo bo) {
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
}
