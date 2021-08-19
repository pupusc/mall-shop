package com.wanmi.sbc.customer.provider.impl.points;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailBatchAddRequest;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import com.wanmi.sbc.customer.points.service.CustomerPointsDetailService;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>会员积分明细保存服务接口实现</p>
 */
@RestController
@Validated
public class CustomerPointsDetailController implements CustomerPointsDetailSaveProvider {
    @Autowired
    private CustomerPointsDetailService customerPointsDetailService;

    @Override
    public BaseResponse add(@RequestBody @Valid CustomerPointsDetailAddRequest request) {
        // 增加积分
        if (request.getType().equals(OperateType.GROWTH)) {
            if (request.getServiceType().equals(PointsServiceType.REGISTER)) {// 注册
                customerPointsDetailService.increasePoints(request, ConfigType.POINTS_BASIC_RULE_REGISTER);
            } else if (request.getServiceType().equals(PointsServiceType.BINDINGWECHAT)) {// 绑定微信
                customerPointsDetailService.increasePoints(request, ConfigType.POINTS_BASIC_RULE_BIND_WECHAT);
            } else if (request.getServiceType().equals(PointsServiceType.PERFECTINFO)) {// 完善个人信息
                customerPointsDetailService.increasePoints(request, ConfigType.POINTS_BASIC_RULE_COMPLETE_INFORMATION);
            } else if (request.getServiceType().equals(PointsServiceType.ADDSHIPPINGADDRESS)) {// 添加收货地址
                customerPointsDetailService.increasePoints(request, ConfigType.POINTS_BASIC_RULE_ADD_DELIVERY_ADDRESS);
            } else if (request.getServiceType().equals(PointsServiceType.FOCUSONSTORE)) {// 收藏店铺
                customerPointsDetailService.increasePoints(request, ConfigType.POINTS_BASIC_RULE_FOLLOW_STORE);
            } else if (request.getServiceType() == PointsServiceType.SHARE) {// 分享商品
                customerPointsDetailService.increasePoints(request, ConfigType.POINTS_BASIC_RULE_SHARE_GOODS);
            } else if (request.getServiceType() == PointsServiceType.EVALUATE) {// 评论商品
                customerPointsDetailService.increasePoints(request, ConfigType.POINTS_BASIC_RULE_COMMENT_GOODS);
            } else if(request.getServiceType() == PointsServiceType.SHAREREGISTER){// 分享注册
                customerPointsDetailService.increasePoints(request, ConfigType.POINTS_BASIC_RULE_SHARE_REGISTER);
            } else if(request.getServiceType() == PointsServiceType.SHAREPURCHASE){// 分享购买
                customerPointsDetailService.increasePoints(request, ConfigType.POINTS_BASIC_RULE_SHARE_BUY);
            } else { // 订单完成、会员导入
                customerPointsDetailService.increasePoints(request, null);
            }
        } else {
            // 扣除积分
            customerPointsDetailService.deductPoints(request);
        }
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse returnPoints(@RequestBody @Valid CustomerPointsDetailAddRequest request) {
        customerPointsDetailService.returnPoints(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse batchAdd(@RequestBody CustomerPointsDetailBatchAddRequest request) {
        customerPointsDetailService.increasePoints(request);
        return  BaseResponse.SUCCESSFUL();
    }

}
