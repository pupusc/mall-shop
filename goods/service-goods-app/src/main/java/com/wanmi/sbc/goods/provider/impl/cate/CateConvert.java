package com.wanmi.sbc.goods.provider.impl.cate;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.response.cate.ContractCateByIdResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.cate.model.root.ContractCate;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-08 11:34
 */
public class CateConvert {

    protected static ContractCateByIdResponse convertContractCate2ByIdResponse(ContractCate contractCate){

        ContractCateByIdResponse contractCateByIdResponse = new ContractCateByIdResponse();

        contractCateByIdResponse.setContractCateId(contractCate.getContractCateId());
        contractCateByIdResponse.setCateRate(contractCate.getCateRate());
        contractCateByIdResponse.setGoodsCate(KsBeanUtil.copyPropertiesThird(contractCate.getGoodsCate(), GoodsCateVO.class));
        contractCateByIdResponse.setQualificationPics(contractCate.getQualificationPics());
        contractCateByIdResponse.setStoreId(contractCate.getStoreId());

        return contractCateByIdResponse;
    }

}
