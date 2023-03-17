package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.bo.SearchTermBo;
import com.wanmi.sbc.bookmeta.entity.GoodsEvaluateAnalyse;
import com.wanmi.sbc.bookmeta.entity.SearchTerm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/13:50
 * @Description:
 */
@Repository
public interface SearchTermMapper {
    List<SearchTerm> getTree(@Param("parentId") int parentId,@Param("defaultSearchKeyword") String defaultSearchKeyword);

    int delete(int id);

    int update(SearchTerm searchTerm);

    int insert(SearchTermBo bo);

    int isExistEvaluateId(@Param("evaluateId") String evaluateId);

    int insertEvaluateAnalyse(GoodsEvaluateAnalyse GoodsEvaluateAnalyse);

    int isExistEvaluateAnalyse(GoodsEvaluateAnalyse GoodsEvaluateAnalyse);
}
