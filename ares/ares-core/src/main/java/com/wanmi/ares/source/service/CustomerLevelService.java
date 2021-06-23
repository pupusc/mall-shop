package com.wanmi.ares.source.service;

import com.wanmi.ares.report.customer.dao.CustomerLevelMapper;
import com.wanmi.ares.source.model.root.CustomerLevel;
import com.wanmi.ares.source.model.root.ReplayStoreLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 客户等级
 */
@Slf4j
@Service
public class CustomerLevelService {

    @Resource
    private CustomerLevelMapper customerLevelMapper;

    @Resource
    private CustomerAndLevelService customerAndLevelService;

    /**
     * 主键查询
     * @param levelId
     * @return
     */
    public CustomerLevel queryByLevelId(String levelId){
        return customerLevelMapper.queryById(levelId);
    }

    /**
     * 主键批量查询
     * @param levelIds
     * @return
     */
    public List<CustomerLevel> queryByLevelIds(List<String> levelIds){
        return customerLevelMapper.queryByIds(levelIds);
    }

    /**
     * 主键批量查询品台客户等级
     * @param levelIds
     * @return
     */
    public List<CustomerLevel> queryCustomerLevelByIds(List<String> levelIds){
        return customerLevelMapper.queryCustomerLevelByIds(levelIds);
    }

    /**
     * @Author lvzhenwei
     * @Description 查询店铺会员等级
     * @Date 11:23 2019/9/20
     * @Param [levelIds]
     * @return java.util.List<com.wanmi.ares.source.model.root.ReplayStoreLevel>
     **/
    public List<ReplayStoreLevel> queryStoreCustomerLevelByIds(List<String> levelIds){
        return customerLevelMapper.queryStoreCustomerLevelByIds(levelIds);
    }

    /**
     * 查询所有等级数据
     * @return
     */
    public List<CustomerLevel> queryAll(){
        return customerLevelMapper.queryAll();
    }

    /**
     * 新增等级
     * @param customerLevel
     * @return
     */
    public int insertCustomerLevel(CustomerLevel customerLevel){
        return customerLevelMapper.insert(customerLevel);
    }

    /**
     * 修改客户等级
     * @param customerLevel
     * @return
     */
    public int updateCustomerLevel(CustomerLevel customerLevel){
        return customerLevelMapper.updateById(customerLevel);
    }

    public void deleteLevel(String levelId) throws Exception{
        if(StringUtils.isBlank(levelId)){
            return;
        }
        CustomerLevel defaultLevels = getDefaultLevel();
        //默认等级为空，则不执行
        if(Objects.isNull(defaultLevels)){
            return;
        }

        customerLevelMapper.deleteById(levelId);
        customerAndLevelService.updateLevelId(levelId, defaultLevels.getId());
    }


    /**
     * 获取默认等级
     * @return
     */
    private CustomerLevel getDefaultLevel(){
        return customerLevelMapper.getDefaultLevel();
    }

}
