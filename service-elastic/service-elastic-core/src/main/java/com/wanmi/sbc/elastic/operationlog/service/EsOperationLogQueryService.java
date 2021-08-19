package com.wanmi.sbc.elastic.operationlog.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.operationlog.EsOperationLogListRequest;
import com.wanmi.sbc.elastic.api.response.operationlog.EsOperationLogPageResponse;
import com.wanmi.sbc.elastic.bean.vo.operationlog.EsOperationLogVO;
import com.wanmi.sbc.elastic.operationlog.model.root.EsOperationLog;
import com.wanmi.sbc.elastic.operationlog.repository.EsOperationLogRepository;
import com.wanmi.sbc.setting.api.provider.OperationLogQueryProvider;
import com.wanmi.sbc.setting.api.request.OperationLogListRequest;
import com.wanmi.sbc.setting.bean.vo.OperationLogVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/16 10:27
 * @description <p> 操作日志 </p>
 */
@Slf4j
@Service
public class EsOperationLogQueryService {

    @Autowired
    private EsOperationLogRepository esOperationLogRepository;

    @Autowired
    private OperationLogQueryProvider operationLogQueryProvider;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 操作日志分页查询
     * @param queryRequest
     * @return
     */
    public BaseResponse<EsOperationLogPageResponse> queryOpLogByCriteria(EsOperationLogListRequest queryRequest) {
        NativeSearchQuery searchQuery = queryRequest.esCriteria();
        Page<EsOperationLog> operationLogPage = esOperationLogRepository.search(searchQuery);
        Page<EsOperationLogVO> page = operationLogPage.map(source -> {
            EsOperationLogVO target = EsOperationLogVO.builder().build();
            KsBeanUtil.copyProperties(source, target);
            return target;
        });
        MicroServicePage<EsOperationLogVO> microServicePage = new MicroServicePage<>(page, queryRequest.getPageable());
        EsOperationLogPageResponse pageResponse = new EsOperationLogPageResponse();
        pageResponse.setOpLogPage(microServicePage);
        return BaseResponse.success(pageResponse);
    }


    /**
     * 操作日志初始化
     * @param request
     */
    public void init(EsOperationLogListRequest request) {
        //手动删除索引时，重新设置mapping
        if(!elasticsearchTemplate.indexExists(EsOperationLog.class)){
            elasticsearchTemplate.createIndex(EsOperationLog.class);
            elasticsearchTemplate.putMapping(EsOperationLog.class);
        }
        Boolean flg = Boolean.TRUE;
        int pageNum = request.getPageNum();
        int pageSize = 2000;

        OperationLogListRequest queryRequest = KsBeanUtil.convert(request, OperationLogListRequest.class);
        try {
            while (flg) {
                queryRequest.putSort("createTime", SortType.DESC.toValue());
                queryRequest.setPageNum(pageNum);
                queryRequest.setPageSize(pageSize);
                List<OperationLogVO> operationLogVOList = operationLogQueryProvider.queryOpLogByCriteria(queryRequest)
                        .getContext().getOpLogPage().getContent();
                if (CollectionUtils.isEmpty(operationLogVOList)) {
                    flg = Boolean.FALSE;
                    log.info("==========ES初始化操作日志列表，结束pageNum:{}==============", pageNum);
                } else {
                    List<EsOperationLog> newInfos = KsBeanUtil.convert(operationLogVOList, EsOperationLog.class);
                    esOperationLogRepository.saveAll(newInfos);
                    log.info("==========ES初始化操作日志列表成功，当前pageNum:{}==============", pageNum);
                    pageNum++;
                }
            }
        } catch (Exception e) {
            log.info("==========ES初始化操作日志列表异常，异常pageNum:{}==============", pageNum);
            throw new SbcRuntimeException("K-120011", new Object[]{pageNum});
        }

    }
}
