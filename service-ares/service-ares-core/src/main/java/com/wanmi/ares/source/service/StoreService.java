package com.wanmi.ares.source.service;

import com.wanmi.ares.enums.StoreState;
import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.customer.dao.ReplayStoreMapper;
import com.wanmi.ares.report.customer.dao.StoreMapper;
import com.wanmi.ares.source.model.root.Store;
import com.wanmi.ares.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺基础信息service
 * Created by bail on 2018/1/16.
 */
@Slf4j
@Service
public class StoreService {

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private ReplayStoreMapper replayStoreMapper;

    /**
     * 新增店铺信息
     * @param store
     * @return
     */
    public int insertStore(Store store){
        return storeMapper.insert(store);
    }

    /**
     * 更新店铺信息
     * @param store
     * @return
     */
    public int updateStoreById(Store store){
        return storeMapper.updateById(store);
    }

    /**
     * 获取正常运营的店铺
     * @param date
     * @param companyInfoId
     * @return
     */
    public List<Store> queryOpenStore(String date, String companyInfoId){
        //提取所有正常的店铺ID
        Map<String, Object> params = new HashMap<>();
        params.put("delFlag", "0");
        params.put("contractStartDate", date);
        params.put("contractEndDate", date);
        params.put("storeState", StoreState.OPENING.name());
        if(StringUtils.isNotBlank(companyInfoId)){
            params.put("companyInfoId", companyInfoId);
        }
        return replayStoreMapper.queryByCondition(params);
    }

    /**
     * 获取店铺名称
     * @param query
     * @return
     */
    public String getStoreName(ExportQuery query){
        String storeName = "";
        if(!Constants.bossId.equals(query.getCompanyId())) {
            storeName = replayStoreMapper.findCompanyName(query.getCompanyId());
            if(org.apache.commons.lang3.StringUtils.isNotBlank(storeName)) {
                storeName = storeName.concat("_");
            }
        }
        return storeName;
    }
}
