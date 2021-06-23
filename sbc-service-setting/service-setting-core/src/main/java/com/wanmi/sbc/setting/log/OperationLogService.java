package com.wanmi.sbc.setting.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.WebUtil;
import com.wanmi.sbc.setting.api.request.OperationLogListRequest;
import com.wanmi.sbc.setting.bean.vo.OperationLogVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.wanmi.sbc.setting.log.OperationLogWhereCriteriaBuilder.buildQueryOpLogByCriteria;

/**
 * 操作日志Service
 * Created by aqlu on 15/12/4.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class OperationLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogService.class);

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private IpInfoRepository ipInfoRepository;

    //API接口获取
    private final String IPLOOKUP = "http://ip.taobao.com/service/getIpInfo.php?ip=";

    /**
     * 查询操作日志
     *
     * @param queryRequest
     * @return
     */
    public List<OperationLog> query(OperationLogListRequest queryRequest) {
        return operationLogRepository.findTop10ByEmployeeIdAndOpModuleAndCompanyInfoIdAndStoreIdOrderByOpTimeDesc(queryRequest.getEmployeeId(),
                queryRequest.getOpModule(), queryRequest.getCompanyInfoId(), queryRequest.getStoreId());
    }

    /**
     * 根据查询条件查询操作日志
     *
     * @param queryRequest
     * @return
     */
    public Page<OperationLog> queryOpLogByCriteria(OperationLogListRequest queryRequest) {
        return operationLogRepository.findAll(buildQueryOpLogByCriteria(queryRequest), queryRequest.getPageable());
    }

    /**
     * 根据查询条件导出操作日志
     *
     * @param queryRequest
     * @return
     */
    public List<OperationLog> exportOpLogByCriteria(OperationLogListRequest queryRequest) {
        return operationLogRepository.findAll(buildQueryOpLogByCriteria(queryRequest));
    }

    /**
     * 新增操作日志
     *
     * @param operationLog 操作日志
     * @throws SbcRuntimeException
     */
    @Transactional(rollbackFor = Exception.class)
    public OperationLogVO add(OperationLog operationLog) {
        if (StringUtils.isNotBlank(operationLog.getOpIp())) {
            if (HttpUtil.LOCAL_ADDRESS.equals(operationLog.getOpIp())) {
                operationLog.setOpIsp("内网");
            } else {
                List<IpInfo> ipInfos = ipInfoRepository.findByIp(operationLog.getOpIp());
                if (CollectionUtils.isEmpty(ipInfos)) {
                    IpInfo info = new IpInfo();
                    try {
                        String res = WebUtil.get(IPLOOKUP.concat(operationLog.getOpIp()));
                        if (StringUtils.isNotBlank(res)) {
                            JSONObject jsonObject = JSON.parseObject(res);
                            if ("0".equals(jsonObject.getString("code")) && jsonObject.getJSONObject("data") != null) {

                                JSONObject data = jsonObject.getJSONObject("data");
                                info.setIp(operationLog.getOpIp());
                                info.setCountry(Objects.toString(data.getString("country"), StringUtils.EMPTY));

                                info.setProvince(Objects.toString(data.getString("province"), StringUtils.EMPTY));
                                info.setCity(Objects.toString(data.getString("city"), StringUtils.EMPTY));
                                info.setIsp(Objects.toString(data.getString("isp"), StringUtils.EMPTY));
                                ipInfoRepository.save(info);
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error("根据IP为：".concat(operationLog.getOpIp()).concat("获取地区信息失败"), e);
                    }
                    if (ipInfos == null) {
                        ipInfos = Lists.newArrayList();
                    }
                    ipInfos.add(info);
                }
                operationLog.setOpCountry(ipInfos.get(0).getCountry());
                operationLog.setOpProvince(ipInfos.get(0).getProvince());
                operationLog.setOpCity(ipInfos.get(0).getCity());
                operationLog.setOpIsp(ipInfos.get(0).getIsp());
            }
        }
        OperationLog result = operationLogRepository.save(operationLog);
        OperationLogVO operationLogVO = new OperationLogVO();
        BeanUtils.copyProperties(result, operationLogVO);
        return operationLogVO;
    }
}
