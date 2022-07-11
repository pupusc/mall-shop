package com.wanmi.sbc.goods.index.service;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.index.model.NormalModule;
import com.wanmi.sbc.goods.index.model.NormalModuleSku;
import com.wanmi.sbc.goods.index.repository.NormalModuleRepository;
import com.wanmi.sbc.goods.index.repository.NormalModuleSkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/11 11:00 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Service
public class NormalModuleService {

    @Autowired
    private NormalModuleRepository normalModuleRepository;

    @Autowired
    private NormalModuleSkuRepository normalModuleSkuRepository;


    public void add() {
        NormalModule normalModule = new NormalModule();
        normalModule.setName("");
        normalModule.setBeginTime();
        normalModule.setEndTime();
        normalModule.setModelCategory(0);
        normalModule.setPublishState(PublishState.NOT_ENABLE.toValue());
        normalModule.setModelTag("");
        normalModule.setCreateTime(LocalDateTime.now());
        normalModule.setUpdateTime(LocalDateTime.now());
        normalModule.setDelFlag(DeleteFlag.NO);
        normalModuleRepository.save(normalModule);

        List<NormalModuleSku> normalModuleSkus = new ArrayList<>();

        NormalModuleSku normalModuleSku = new NormalModuleSku();
        normalModuleSku.setId(0);
        normalModuleSku.setNormalModelId(normalModule.getId());
        normalModuleSku.setSkuId(0);
        normalModuleSku.setSkuNo(0);
        normalModuleSku.setSpuId(0);
        normalModuleSku.setSpuNo(0);
        normalModuleSku.setCreateTime(LocalDateTime.now());
        normalModuleSku.setUpdateTime(LocalDateTime.now());
        normalModuleSku.setDelFlag(DeleteFlag.NO);
        normalModuleSkuRepository.saveAll(normalModuleSkus);
    }


    public void update() {

        NormalModule normalModule = new NormalModule();
        normalModule.setId();
        normalModule.setName("");
        normalModule.setBeginTime();
        normalModule.setEndTime();
        normalModule.setModelTag("");
        normalModule.setUpdateTime(LocalDateTime.now());
        normalModuleRepository.save(normalModule);

        //删除

    }

    /**
     * 获取列表，无分页
     * @return
     */
    public List<NormalModule> listNoPage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        return normalModuleRepository.findAll(normalModuleRepository.packageWhere(), sort);
    }

    /**
     * 获取列表，分页
     * @return
     */
    public List<NormalModule> list() {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
//        Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize(), sort);
//        return normalModuleRepository.findAll(normalModuleRepository.packageWhere(request), pageable);
        return null;
    }

    public void delete() {

    }
}
