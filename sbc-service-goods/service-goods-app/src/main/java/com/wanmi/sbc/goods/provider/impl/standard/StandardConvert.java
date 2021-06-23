package com.wanmi.sbc.goods.provider.impl.standard;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsAddRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsBaseRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsModifyRequest;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardPropDetailRel;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standard.request.StandardSaveRequest;
import com.wanmi.sbc.goods.standardimages.model.root.StandardImage;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpec;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-08 16:24
 */
public class StandardConvert {

    /**
     * 标准商品添加请求对象转化为服务的保存对象
     * @param addRequest {@link StandardGoodsAddRequest}
     * @return
     */
    protected static StandardSaveRequest convertAddRequest2saveRequest(StandardGoodsAddRequest addRequest){

        StandardSaveRequest saveRequest = new StandardSaveRequest();

        converBaseReq2SaveReq(addRequest,saveRequest);

        return saveRequest;
    }

    /**
     * 标准商品修改请求对象转化为服务的保存对象
     * @param modifyRequest {@link StandardGoodsAddRequest}
     * @return
     */
    protected static StandardSaveRequest convertModifyRequest2saveRequest(StandardGoodsModifyRequest modifyRequest){

        StandardSaveRequest saveRequest = new StandardSaveRequest();

        converBaseReq2SaveReq(modifyRequest,saveRequest);

        return saveRequest;
    }

    /**
     * 标准商品基类对象转化为服务的保存对象
     * @param baseRequest
     * @param saveRequest
     */
    private static void converBaseReq2SaveReq(StandardGoodsBaseRequest baseRequest,StandardSaveRequest saveRequest){

        saveRequest.setGoods(KsBeanUtil.copyPropertiesThird(baseRequest.getGoods(), StandardGoods.class));
        saveRequest.setGoodsInfos(KsBeanUtil.convertList(baseRequest.getGoodsInfos(), StandardSku.class));
        saveRequest.setGoodsPropDetailRels(KsBeanUtil.convertList(baseRequest.getGoodsPropDetailRels(), StandardPropDetailRel.class));
        saveRequest.setGoodsSpecs(KsBeanUtil.convertList(baseRequest.getGoodsSpecs(), StandardSpec.class));
        saveRequest.setGoodsSpecDetails(KsBeanUtil.convertList(baseRequest.getGoodsSpecDetails(), StandardSpecDetail.class));
        saveRequest.setImages(KsBeanUtil.convertList(baseRequest.getImages(), StandardImage.class));

    }

}
