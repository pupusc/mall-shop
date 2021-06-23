package com.wanmi.sbc.goods.contract.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.store.NoDeleteStoreByIdResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.brand.request.ContractBrandSaveRequest;
import com.wanmi.sbc.goods.brand.service.ContractBrandService;
import com.wanmi.sbc.goods.cate.request.ContractCateSaveRequest;
import com.wanmi.sbc.goods.cate.service.ContractCateService;
import com.wanmi.sbc.goods.contract.request.ContractRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 签约信息服务
 * Created by sunkun on 2017/11/2.
 */
@Service
public class ContractService {

    @Resource
    private ContractCateService contractCateService;

    @Resource
    private ContractBrandService contractBrandService;

    @Resource
    private StoreQueryProvider storeQueryProvider;


    /**
     * 签约信息更新
     *
     * @param request 签约信息
     */
    @Transactional
    public List<Long>  renewal(ContractRequest request) {
        List<Long> ids = new ArrayList<>();//品牌id
        //签约分类删除
        if (CollectionUtils.isNotEmpty(request.getDelCateIds())) {
            contractCateService.deleteByIds(request.getDelCateIds(), request.getStoreId());
        }
        //签约品牌删除
        if (CollectionUtils.isNotEmpty(request.getDelBrandIds())) {
            ids = contractBrandService.deleteByIds(request.getDelBrandIds(), request.getStoreId());
        }
        //签约分类更新
        if (CollectionUtils.isNotEmpty(request.getCateSaveRequests())) {
            renewalCate(request.getCateSaveRequests(), request.getStoreId());
        }
        //签约品牌更新
        if (CollectionUtils.isNotEmpty(request.getBrandSaveRequests())) {
            renewalBrands(request.getBrandSaveRequests(), request.getStoreId());
        }
        return ids;
    }


    /**
     * 更新签约分类
     *
     * @param list 签约分类信息批量数据
     * @param storeId 店铺id
     */
    @Transactional
    public void renewalCate(List<ContractCateSaveRequest> list, Long storeId) {
        list.forEach(info -> {
            info.setStoreId(storeId);
            if (info.getContractCateId() == null) {
                contractCateService.add(info);
            } else {
                contractCateService.update(info);
            }
        });
    }

    /**
     * 更新签约品牌
     *
     * @param list 签约品牌信息
     * @param storeId 瘟铺od
     */
    @Transactional
    public void renewalBrands(List<ContractBrandSaveRequest> list, Long storeId) {
        NoDeleteStoreByIdRequest noDeleteStoreByIdRequest = new NoDeleteStoreByIdRequest();
        noDeleteStoreByIdRequest.setStoreId(storeId);
        BaseResponse<NoDeleteStoreByIdResponse>  noDeleteStoreByIdResponseBaseResponse  = storeQueryProvider.getNoDeleteStoreById(noDeleteStoreByIdRequest);
        StoreVO storeVO = noDeleteStoreByIdResponseBaseResponse.getContext().getStoreVO();
        list.forEach(info -> {

            info.setStoreId(storeId);
            if (info.getContractBrandId() != null && info.getContractBrandId() > 0) {
                //商家自定义品牌修改且店铺已审核 = 非法输入
                if (Objects.nonNull(info.getCheckBrandId())) {
                    if (storeVO.getAuditState() == CheckState.CHECKED) {
                        throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                    }
                }
                contractBrandService.update(info);
            } else {
                //商家自定义品牌新增且店铺已审核 = 非法输入
                if (Objects.isNull(info.getBrandId())) {
                    if (storeVO.getAuditState() == CheckState.CHECKED) {
                        throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                    }
                }
                contractBrandService.add(info);
            }
        });
    }

}
