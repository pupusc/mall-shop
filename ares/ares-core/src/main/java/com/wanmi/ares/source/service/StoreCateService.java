package com.wanmi.ares.source.service;

import com.wanmi.ares.report.customer.dao.StoreCateMapper;
import com.wanmi.ares.source.model.root.StoreCate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 店铺商品分类Service
 * @author bail 2018/01/16
 */
@Slf4j
@Service
public class StoreCateService {

    @Resource
    private StoreCateMapper storeCateMapper;


    /**
     * 新增店铺分类
     * @param storeCate
     * @return
     */
    public int insertStoreCate(StoreCate storeCate){
        return storeCateMapper.insert(storeCate);
    }

    /**
     * 修改店铺分类
     * @param storeCate
     * @return
     */
    public int updateStoreCate(StoreCate storeCate){
        return storeCateMapper.updateById(storeCate);
    }

    /**
     * 删除店铺分类
     * @param ids
     * @throws Exception
     */
    public void deleteStoreCate(List<String> ids) throws Exception{
        storeCateMapper.deleteByIds(ids);
    }

}
