package com.wanmi.sbc.elastic.api.response.storeInformation;

import com.wanmi.sbc.elastic.bean.vo.companyAccount.EsCompanyAccountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ResultsMapper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @Author yangzhen
 * @Description //Es 商家结算账号分页结果
 * @Date 15:43 2020/12/9
 * @Param
 * @return
 **/
@Data
@ApiModel
public class EsCompanyAccountSearchResponse implements Serializable {
    public static final Logger LOGGER = LoggerFactory.getLogger(EsCompanyAccountSearchResponse.class);

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private Long total;

    /**
     * 查询结果
     */
    @ApiModelProperty(value = "查询结果")
    private List<EsCompanyAccountVO> data;

    /**
     * 设置结果总数
     *
     * @param response ES总的搜索结果数
     * @return
     */
    public EsCompanyAccountSearchResponse addTotalNum(SearchResponse response) {
        this.total = response.getHits().getTotalHits();
        return this;
    }



    /**
     * 转换 ES 查询结果
     *
     * @param response      查询结果
     * @param resultsMapper 结果映射
     * @return
     */
    private EsCompanyAccountSearchResponse addQueryResults(SearchResponse response, ResultsMapper resultsMapper) {
        if (Objects.nonNull(resultsMapper)) {
            data = resultsMapper.mapResults(response, EsCompanyAccountVO.class, null).getContent();
        }

        if (CollectionUtils.isNotEmpty(data)) {
            boolean hasHighLight = false;
            if (response.getHits().getHits().length > 0) {
                if (MapUtils.isNotEmpty(response.getHits().getAt(0).getHighlightFields())) {
                    hasHighLight = true;
                }
            }

            if (hasHighLight) {
                IntStream.range(0, data.size()).parallel().forEach(index -> {
                    EsCompanyAccountVO companyAccount = data.get(index);
                    SearchHit sh = response.getHits().getAt(index);
                    sh.getHighlightFields().entrySet().stream().forEach(entry -> {
                        try {
                            PropertyUtils.setProperty(companyAccount, entry.getKey(), entry.getValue());
                        } catch (Exception e) {
                            LOGGER.error("Set EsGoodsInfo highLight property error = {}, Property key = {}, value = " +
                                    "{}", e, entry.getKey(), entry.getValue());
                        }
                    });
                });
            }
        }

        return this;
    }

    /**
     * 返回空结果
     *
     * @return
     */
    public static EsCompanyAccountSearchResponse empty() {
        EsCompanyAccountSearchResponse response = new EsCompanyAccountSearchResponse();
        response.setTotal(0L);
        response.setData(Collections.emptyList());

        return response;
    }


    /**
     * 根据ES查询返回结果 构建 EsSearchResponse实例
     *
     * @param searchResponse
     * @param resultsMapper
     * @return
     */
    public static EsCompanyAccountSearchResponse build(SearchResponse searchResponse, ResultsMapper resultsMapper) {
        return new EsCompanyAccountSearchResponse().addQueryResults(searchResponse, resultsMapper).addTotalNum(searchResponse);
    }

}
