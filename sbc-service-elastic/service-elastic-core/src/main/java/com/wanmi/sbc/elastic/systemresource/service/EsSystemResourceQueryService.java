package com.wanmi.sbc.elastic.systemresource.service;

import com.wanmi.osd.OsdClient;
import com.wanmi.osd.bean.OsdClientParam;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourcePageRequest;
import com.wanmi.sbc.elastic.api.response.systemresource.EsSystemRessourcePageResponse;
import com.wanmi.sbc.elastic.bean.vo.systemresource.EsSystemResourceVO;
import com.wanmi.sbc.elastic.systemresource.model.root.EsSystemResource;
import com.wanmi.sbc.elastic.systemresource.repository.EsSystemResourceRepository;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.systemconfig.SystemConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.vo.SystemConfigVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/14 10:31
 * @description <p> </p>
 */
@Service
public class EsSystemResourceQueryService {

    @Autowired
    private EsSystemResourceRepository esSystemResourceRepository;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    public BaseResponse<EsSystemRessourcePageResponse> page(EsSystemResourcePageRequest request) {
        request.setDelFlag(DeleteFlag.NO);
        // 查询可用云服务
        SystemConfigVO availableYun = this.getAvailableYun();

        OsdClientParam osdClientParam = OsdClientParam.builder()
                .configType(availableYun.getConfigType())
                .context(availableYun.getContext())
                .build();

        NativeSearchQuery searchQuery = request.esCriteria();
        Page<EsSystemResource> systemResourcePage = esSystemResourceRepository.search(searchQuery);
        Page<EsSystemResourceVO> newPage = systemResourcePage.map(entity -> {
                    //获取url
                    EsSystemResourceVO esSystemResourceVO = EsSystemResourceVO.builder().build();
                    String resourceUrl = OsdClient.instance().getResourceUrl(osdClientParam, entity.getArtworkUrl());
                    entity.setArtworkUrl(resourceUrl);
                    BeanUtils.copyProperties(entity, esSystemResourceVO);
                    return esSystemResourceVO;
                }
        );
        MicroServicePage<EsSystemResourceVO> microPage = new MicroServicePage<>(newPage, request.getPageable());
        EsSystemRessourcePageResponse finalRes = new EsSystemRessourcePageResponse(microPage);
        return BaseResponse.success(finalRes);

    }

    /**
     * 查询可用的云配置
     *
     * @return
     */
    private SystemConfigVO getAvailableYun() {
        SystemConfigQueryRequest queryRequest = SystemConfigQueryRequest.builder()
                .configKey(ConfigKey.RESOURCESERVER.toString())
                .status(EnableStatus.ENABLE.toValue())
                .delFlag(DeleteFlag.NO)
                .build();
        BaseResponse<SystemConfigResponse> response = systemConfigQueryProvider.list(queryRequest);
        List<SystemConfigVO> systemConfigList = response.getContext().getSystemConfigVOList();
        if (CollectionUtils.isEmpty(systemConfigList)) {
            throw new SbcRuntimeException("K-061001");
        }
        return systemConfigList.get(0);
    }
}
