package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.GoodsEvaluateAnalyseBo;
import com.wanmi.sbc.bookmeta.bo.SearchTermBo;
import com.wanmi.sbc.bookmeta.entity.GoodsEvaluateAnalyse;
import com.wanmi.sbc.bookmeta.entity.SaleNum;
import com.wanmi.sbc.bookmeta.entity.SearchTerm;
import com.wanmi.sbc.bookmeta.mapper.SearchTermMapper;
import com.wanmi.sbc.bookmeta.provider.SearchTermProvider;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/13:49
 * @Description:
 */
@Validated
@Slf4j
@RestController
public class SearchTermProviderImpl implements SearchTermProvider {
    @Resource
    private SearchTermMapper searchTermMapper;

    @Override
    public List<SearchTermBo> getSearchTermTree(SearchTermBo searchTermBo) {
        List<SearchTermBo> objects = new ArrayList<SearchTermBo>();
        try{
            List<SearchTerm> tree = searchTermMapper.getTree(0, searchTermBo.getDefaultSearchKeyword());
            objects = KsBeanUtil.convertList(tree, SearchTermBo.class);
            for (SearchTermBo parent : objects) {
                parent.setChildrenList(KsBeanUtil.convertList(searchTermMapper.getTree(parent.getId(), searchTermBo.getDefaultSearchKeyword()), SearchTermBo.class));
            }
        }catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "getSearchTermTree",
                    Objects.isNull(searchTermBo) ? "" : JSON.toJSONString(searchTermBo),
                    e);
        }
        return objects;
    }

    @Override
    public int deleteSearchTerm(SearchTermBo bo) {
        int deleted = 0;
        try {
            List<SearchTerm> children = searchTermMapper.getTree(bo.getId(), null);
            for (SearchTerm child : children) {
                searchTermMapper.delete(child.getId());
            }
            deleted = searchTermMapper.delete(bo.getId());
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "deleteSearchTerm",
                    Objects.isNull(bo) ? "" : JSON.toJSONString(bo),
                    e);
        }
        return deleted;
    }

    @Override
    public int updateSearchTerm(SearchTermBo bo) {
        SearchTerm convert = KsBeanUtil.convert(bo, SearchTerm.class);
        int update = 0;
        try {
            update = searchTermMapper.update(convert);
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "updateSearchTerm",
                    Objects.isNull(bo) ? "" : JSON.toJSONString(bo),
                    e);
        }
        return update;
    }

    @Override
    public int addSearchTerm(SearchTermBo bo) {
        int insert = 0;
        try {
            insert = searchTermMapper.insert(bo);
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "addSearchTerm",
                    Objects.isNull(bo) ? "" : JSON.toJSONString(bo),
                    e);
        }
        return insert;
    }

    @Override
    public BusinessResponse<String> importGoodsEvaluateAnalyse(List<GoodsEvaluateAnalyseBo> list) {
        int addCount = 0;
        try {
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
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "importGoodsEvaluateAnalyse",
                    Objects.isNull(list) ? "" : JSON.toJSONString(list),
                    e);
        }
        return BusinessResponse.success("Success,add" + addCount + "!");
    }
}
