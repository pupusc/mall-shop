package com.wanmi.sbc.order.provider.impl.ztemp;

import com.soybean.mall.order.dszt.TransferService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.erp.api.provider.ShopCenterSaleAfterProvider;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq;
import com.wanmi.sbc.order.api.enums.ThirdInvokeCategoryEnum;
import com.wanmi.sbc.order.api.enums.ThirdInvokePublishStatusEnum;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.third.ThirdInvokeService;
import com.wanmi.sbc.order.third.model.ThirdInvokeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Slf4j
@Component
public class ExportReturnComponent {
    @Autowired
    private TransferService transferService;
    @Autowired
    private ShopCenterSaleAfterProvider shopCenterSaleAfterProvider;
    @Autowired
    private ThirdInvokeService thirdInvokeService;

    @Transactional
    public boolean syncData(ReturnOrder returnOrder) {
        //创建售后订单
        ThirdInvokeDTO thirdInvokeDTO = thirdInvokeService.add(returnOrder.getId(), ThirdInvokeCategoryEnum.INVOKE_RETURN_ORDER);
        if (Objects.equals(thirdInvokeDTO.getPushStatus(), ThirdInvokePublishStatusEnum.SUCCESS.getCode())) {
            log.info("ProviderTradeService singlePushOrder businessId:{} 已经推送成功，重复提送", thirdInvokeDTO.getBusinessId());
            return true;
        }
        //调用推送接口
        SaleAfterCreateNewReq saleAfterCreateNewReq = transferService.changeSaleAfterCreateReq4Sync(returnOrder);
        if (saleAfterCreateNewReq == null) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "创建退单的同步参数失败");
        }
        //saleAfterCreateNewReq.setSaleAfterCreateEnum(5);
        saleAfterCreateNewReq.getSaleAfterOrderBO().setImportFlag(1); //导入标记
        BaseResponse<Long> saleAfter = shopCenterSaleAfterProvider.createSaleAfter(saleAfterCreateNewReq);
        //推送成功
        if (Objects.equals(saleAfter.getCode(), CommonErrorCode.SUCCESSFUL)) {
            thirdInvokeService.update(thirdInvokeDTO.getId(), saleAfter.getContext().toString(), ThirdInvokePublishStatusEnum.SUCCESS, "SUCCESS");
            return true;
        }
        thirdInvokeService.update(thirdInvokeDTO.getId(), saleAfter.getContext().toString(), ThirdInvokePublishStatusEnum.FAIL, saleAfter.getMessage());
        return false;
    }
}
