package com.wanmi.sbc.goods.storegoodstab.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.request.storegoodstab.StoreGoodsTabAddRequest;
import com.wanmi.sbc.goods.api.request.storegoodstab.StoreGoodsTabDeleteRequest;
import com.wanmi.sbc.goods.api.request.storegoodstab.StoreGoodsTabModifyRequest;
import com.wanmi.sbc.goods.bean.vo.StoreGoodsTabVO;
import com.wanmi.sbc.goods.storegoodstab.model.request.StoreGoodsTabQueryRequest;
import com.wanmi.sbc.goods.storegoodstab.model.root.StoreGoodsTab;
import com.wanmi.sbc.goods.storegoodstab.repository.StoreGoodsTabRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品分类服务
 * Created by bail on 2017/11/14.
 */
@Service
public class StoreGoodsTabService {


    @Autowired
    private StoreGoodsTabRepository storeGoodsTabRepository;


    /**
     * 条件查询店铺商品模板
     *
     * @param storeId 店铺标识
     * @return list
     */
    public List<StoreGoodsTabVO> query(Long storeId) {
        if (storeId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreGoodsTabQueryRequest queryRequest = new StoreGoodsTabQueryRequest();
        queryRequest.setStoreId(storeId);
        queryRequest.setDelFlag(DeleteFlag.NO);
        queryRequest.putSort("sort", SortType.ASC.toValue());
        queryRequest.putSort("createTime", SortType.DESC.toValue());
        StoreGoodsTab goodsTab = new StoreGoodsTab();
        goodsTab.setSort(0);
        goodsTab.setTabName("商品详情");
        goodsTab.setStoreId(storeId);
        List<StoreGoodsTab> tabList = storeGoodsTabRepository.findAll(queryRequest.getWhereCriteria(), queryRequest
                .getSort());
        //添加默认商品详情
        tabList.add(0, goodsTab);
        return tabList.stream().map(storeGoodsTab -> {
            StoreGoodsTabVO storeGoodsTabVO = new StoreGoodsTabVO();
            if (storeGoodsTab.getSort() == 0) {
                storeGoodsTabVO.setIsDefault(DefaultFlag.YES);
            }
            BeanUtils.copyProperties(storeGoodsTab, storeGoodsTabVO);
            return storeGoodsTabVO;
        }).collect(Collectors.toList());
    }

    /**
     * 新增店铺商品详情模板
     *
     * @param saveRequest
     * @throws SbcRuntimeException
     */
    @Transactional
    public StoreGoodsTabVO add(StoreGoodsTabAddRequest saveRequest) {
        if (saveRequest == null || saveRequest.getStoreId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreGoodsTab storeGoodsTab = new StoreGoodsTab();
        BeanUtils.copyProperties(saveRequest, storeGoodsTab);
        //新增时校验名称是否重复
        checkNameExist(storeGoodsTab);

        List<StoreGoodsTab> goodsTabs = storeGoodsTabRepository.queryTabByStoreId(saveRequest.getStoreId());
        if (Objects.nonNull(goodsTabs) && goodsTabs.size() == 0) {
            storeGoodsTab.setSort(1);
        } else if (goodsTabs.size() > 0) {
            //自定义模块的个数不能大于三个
            if (goodsTabs.size() > 2) {
                throw new SbcRuntimeException("K-110802");
            }
            int tabMax = storeGoodsTabRepository.queryMaxSortByStoreId(saveRequest.getStoreId());
            storeGoodsTab.setSort(++tabMax);
        }

        /**2.初始化数据并插入*/
        storeGoodsTab.setDelFlag(DeleteFlag.NO);
        storeGoodsTab.setCreateTime(LocalDateTime.now());
        storeGoodsTab.setUpdateTime(LocalDateTime.now());

        storeGoodsTab.setTabId(storeGoodsTabRepository.save(storeGoodsTab).getTabId());

        StoreGoodsTabVO storeGoodsTabVO = new StoreGoodsTabVO();
        BeanUtils.copyProperties(storeGoodsTab, storeGoodsTabVO);
        return storeGoodsTabVO;
    }


    /**
     * 编辑店铺商品详情模板
     *
     * @param saveRequest 店铺商品详情模板
     * @throws SbcRuntimeException
     */
    @Transactional
    public void edit(StoreGoodsTabModifyRequest saveRequest) {
        if (saveRequest == null || saveRequest.getTabId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreGoodsTab newStoreGoodsTab = new StoreGoodsTab();
        BeanUtils.copyProperties(saveRequest, newStoreGoodsTab);
        StoreGoodsTab oldStoreGoodsTab = storeGoodsTabRepository.findById(saveRequest.getTabId()).orElse(null);

        /**1.参数验证*/
        if (oldStoreGoodsTab == null || DeleteFlag.YES.equals(oldStoreGoodsTab.getDelFlag()) || !Objects.equals
                (oldStoreGoodsTab.getStoreId(), newStoreGoodsTab.getStoreId())) {
            //1.1.待修改的模板项不存在 或 修改的店铺id不一致(数据安全问题)
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        checkNameExist(newStoreGoodsTab);

        /**2.更新模板名称*/
        oldStoreGoodsTab.convertBeforeEdit(newStoreGoodsTab);
        oldStoreGoodsTab.setUpdateTime(LocalDateTime.now());
        storeGoodsTabRepository.save(oldStoreGoodsTab);

    }

    /**
     * 验证商家详情模板名称是否重复
     */
    private void checkNameExist(StoreGoodsTab storeGoodsTab) {
        StoreGoodsTabQueryRequest queryRequest = new StoreGoodsTabQueryRequest();
        queryRequest.setStoreId(storeGoodsTab.getStoreId());
        queryRequest.setTabName(storeGoodsTab.getTabName());
        queryRequest.setDelFlag(DeleteFlag.NO);
        if (storeGoodsTab.getTabId() == null) {
            //新增时,验证重复名称
            if (storeGoodsTabRepository.count(queryRequest.getWhereCriteria()) > 0) {
                throw new SbcRuntimeException("K-110801");
            }
        } else {
            //编辑时,验证重复名称
            if (storeGoodsTabRepository.findAll(queryRequest.getWhereCriteria()).stream().filter(s -> !s.getTabId()
                    .equals
                            (storeGoodsTab.getTabId())).count() > 0) {
                throw new SbcRuntimeException("K-110801");
            }
        }
    }

    /**
     * 删除商家详情模板名称
     *
     * @param saveRequest 待删除的商家详情模板名称
     * @throws SbcRuntimeException
     */
    @Transactional
    public void delete(StoreGoodsTabDeleteRequest saveRequest) {
        if (saveRequest == null || saveRequest.getTabId() == null || saveRequest.getStoreId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreGoodsTab goodsTab = storeGoodsTabRepository.findById(saveRequest.getTabId()).orElse(null);
        if (goodsTab == null || DeleteFlag.YES.equals(goodsTab.getDelFlag()) || !Objects.equals(goodsTab.getStoreId()
                , saveRequest.getStoreId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        goodsTab.setDelFlag(DeleteFlag.YES);
        storeGoodsTabRepository.save(goodsTab);
        List<StoreGoodsTab> tabList = storeGoodsTabRepository.findAllByStoreIdAndDelFlagOrderBySortAsc(saveRequest.
                getStoreId(), DeleteFlag.NO);

        editSort(tabList);
    }

    @Transactional
    public void editSort(List<StoreGoodsTab> tabList) {
        for (int i = 0; i < tabList.size(); i++) {
            StoreGoodsTab storeGoodsTab = tabList.get(i);
            storeGoodsTab.setUpdateTime(LocalDateTime.now());
            int sort = i + 1;
            storeGoodsTab.setSort(sort);
        }
        storeGoodsTabRepository.saveAll(tabList);
    }


}
