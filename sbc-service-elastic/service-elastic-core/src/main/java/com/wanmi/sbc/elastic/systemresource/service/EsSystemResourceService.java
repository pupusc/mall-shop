package com.wanmi.sbc.elastic.systemresource.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourcePageRequest;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourceSaveRequest;
import com.wanmi.sbc.elastic.bean.vo.systemresource.EsSystemResourceVO;
import com.wanmi.sbc.elastic.sensitivewords.model.root.EsSensitiveWords;
import com.wanmi.sbc.elastic.systemresource.model.root.EsSystemResource;
import com.wanmi.sbc.elastic.systemresource.repository.EsSystemResourceRepository;
import com.wanmi.sbc.setting.api.provider.systemresource.SystemResourceQueryProvider;
import com.wanmi.sbc.setting.api.request.SensitiveWordsQueryRequest;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourcePageRequest;
import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/14 14:57
 * @description <p> </p>
 */
@Slf4j
@Service
public class EsSystemResourceService {

    @Autowired
    private EsSystemResourceRepository esSystemResourceRepository;

    @Autowired
    private SystemResourceQueryProvider systemResourceQueryProvider;

    public void add(EsSystemResourceSaveRequest request) {
        List<EsSystemResourceVO> systemResourceVOList = request.getSystemResourceVOList();
        if (CollectionUtils.isEmpty(systemResourceVOList)) {
            return;
        }
        List<EsSystemResource> esSystemResourceList = KsBeanUtil.convert(systemResourceVOList, EsSystemResource.class);
        esSystemResourceRepository.saveAll(esSystemResourceList);
    }

    public void init(EsSystemResourcePageRequest request) {
        Boolean flg = Boolean.TRUE;
        int pageNum = request.getPageNum();
        int pageSize = 2000;

        SystemResourcePageRequest queryRequest = KsBeanUtil.convert(request, SystemResourcePageRequest.class);
        try {
            while (flg) {
                queryRequest.putSort("createTime", SortType.DESC.toValue());
                queryRequest.setPageNum(pageNum);
                queryRequest.setPageSize(pageSize);
                List<SystemResourceVO> systemResourceVOList = systemResourceQueryProvider.page(queryRequest).getContext().getSystemResourceVOPage().getContent();
                if (CollectionUtils.isEmpty(systemResourceVOList)) {
                    flg = Boolean.FALSE;
                    log.info("==========ES初始化图片库库列表，结束pageNum:{}==============", pageNum);
                } else {
                    List<EsSystemResource> newInfos = KsBeanUtil.convert(systemResourceVOList, EsSystemResource.class);
                    esSystemResourceRepository.saveAll(newInfos);
                    log.info("==========ES初始化图片库库列表成功，当前pageNum:{}==============", pageNum);
                    pageNum++;
                }
            }
        } catch (Exception e) {
            log.info("==========ES初始化图片库列表异常，异常pageNum:{}==============", pageNum);
            throw new SbcRuntimeException("K-120011", new Object[]{pageNum});
        }


    }
}
