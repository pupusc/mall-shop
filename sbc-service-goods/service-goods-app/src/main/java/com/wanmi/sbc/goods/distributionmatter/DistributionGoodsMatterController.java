package com.wanmi.sbc.goods.distributionmatter;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.MD5Util;
import com.wanmi.sbc.goods.api.provider.distributionmatter.DistributionGoodsMatterProvider;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.goods.api.request.distributionmatter.DeleteByIdListRequest;
import com.wanmi.sbc.goods.api.request.distributionmatter.DistributionGoodsMatteAddRequest;
import com.wanmi.sbc.goods.api.request.distributionmatter.DistributionGoodsMatterModifyRequest;
import com.wanmi.sbc.goods.api.request.distributionmatter.UpdateRecommendNumRequest;
import com.wanmi.sbc.goods.bean.enums.MarketingType;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.distributionmatter.model.root.DistributionGoodsMatter;
import com.wanmi.sbc.goods.distributionmatter.service.DistributionGoodsMatterService;
import com.wanmi.sbc.goods.bean.enums.MatterType;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.bean.dto.MarketingMaterialDTO;
import java.util.List;
import com.wanmi.sbc.setting.api.request.MiniProgramQrCodeRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@Validated
public class DistributionGoodsMatterController implements DistributionGoodsMatterProvider {

    @Autowired
    private DistributionGoodsMatterService distributionGoodsMatterService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private RedisService redisService;

    /**
     * 新增商品素材
     *
     * @param distributionGoodsMatteAddRequest
     */
    @Override
    public BaseResponse add(@RequestBody @Valid DistributionGoodsMatteAddRequest distributionGoodsMatteAddRequest) {
        DistributionGoodsMatter distributionGoodsMatter =
                KsBeanUtil.copyPropertiesThird(distributionGoodsMatteAddRequest, DistributionGoodsMatter.class);
        GoodsInfo goodsInfo = goodsInfoService.findOne(distributionGoodsMatteAddRequest.getGoodsInfoId());
        distributionGoodsMatter.setGoodsInfo(goodsInfo);
        //营销素材配置图片链接的，预生成小程序码
        if(distributionGoodsMatteAddRequest.getMatterType().equals(MatterType.MARKETING)){
           List<MarketingMaterialDTO> marketingMaterialDTOS =
                   JSONObject.parseArray(distributionGoodsMatteAddRequest.getMatter(),MarketingMaterialDTO.class);
            marketingMaterialDTOS.forEach(item->{
                //把链接加密，生成redisKey，作为参数传递，用来生成小程序码
                if(StringUtils.isNotBlank(item.getLink()) && item.getLink()!=null){
                    String redisKey = (MD5Util.md5Hex(item.getLink(), "utf-8")).toUpperCase().substring(16);
                    if (StringUtils.isNotBlank(redisKey)) {
                        redisService.setString(redisKey, item.getLink(), 15000000L);
                        item.setRedisKey("FX"+redisKey);
                        //生成码
                        MiniProgramQrCodeRequest request = new MiniProgramQrCodeRequest();
                        request.setPage("pages/sharepage/sharepage");
                        request.setScene("FX"+redisKey);
                        //生成透明底色的码
                        request.setIs_hyaline(true);
                        String codeUrl = wechatAuthProvider.getWxaCodeUnlimit(request).getContext().toString();
                        //若成功生成，
                        if(StringUtils.isNotBlank(codeUrl)){
                            item.setLinkSrc(codeUrl);
                        }
                    }
                }
            });
            //重新设值
            distributionGoodsMatter.setMatter(JSONObject.toJSONString(marketingMaterialDTOS));
        }
        distributionGoodsMatterService.add(distributionGoodsMatter);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<DistributionGoodsMatterModifyRequest> updataQrcode(@RequestBody @Valid DistributionGoodsMatterModifyRequest distributionGoodsMatterModifyRequest){
        if(distributionGoodsMatterModifyRequest.getMatterType().equals(MatterType.MARKETING)){
            List<MarketingMaterialDTO> marketingMaterialDTOS =
                    JSONObject.parseArray(distributionGoodsMatterModifyRequest.getMatter(),MarketingMaterialDTO.class);
            marketingMaterialDTOS.forEach(item->{
                //把链接加密，生成redisKey，作为参数传递，用来生成小程序码
                if(StringUtils.isNotBlank(item.getLink()) && item.getLink()!=null){
                    String redisKey = (MD5Util.md5Hex(item.getLink(), "utf-8")).toUpperCase().substring(16);
                    if (StringUtils.isNotBlank(redisKey)) {
                        redisService.setString(redisKey, item.getLink(), 15000000L);
                        item.setRedisKey("FX"+redisKey);
                        //生成码
                        MiniProgramQrCodeRequest request = new MiniProgramQrCodeRequest();
                        request.setPage("pages/sharepage/sharepage");
                        request.setScene("FX"+redisKey);
                        //生成透明底色的码
                        request.setIs_hyaline(true);
                        String codeUrl = wechatAuthProvider.getWxaCodeUnlimit(request).getContext().toString();
                        //若成功生成，
                        if(StringUtils.isNotBlank(codeUrl)){
                            item.setLinkSrc(codeUrl);
                        }
                    }
                }
            });
            //重新设值
            distributionGoodsMatterModifyRequest.setMatter(JSONObject.toJSONString(marketingMaterialDTOS));
        }
        return BaseResponse.success(distributionGoodsMatterModifyRequest);
    }

    /**
     * 修改商品素材
     *
     * @param distributionGoodsMatterModifyRequest
     */
    @Override
    public BaseResponse modify(@RequestBody @Valid DistributionGoodsMatterModifyRequest distributionGoodsMatterModifyRequest) {
        DistributionGoodsMatter distributionGoodsMatter = KsBeanUtil.copyPropertiesThird(distributionGoodsMatterModifyRequest, DistributionGoodsMatter.class);
        if(distributionGoodsMatter.getGoodsInfo()!=null){
            GoodsInfo goodsInfo = goodsInfoService.findOne(distributionGoodsMatterModifyRequest.getGoodsInfoId());
            distributionGoodsMatter.setGoodsInfo(goodsInfo);
        }
        distributionGoodsMatterService.update(distributionGoodsMatter);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量删除商品素材
     *
     * @param deleteByIdListRequest
     */
    @Override
    public BaseResponse deleteList(@RequestBody @Valid DeleteByIdListRequest deleteByIdListRequest) {
        distributionGoodsMatterService.delete(deleteByIdListRequest.getIds());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updataRecomendNumById(@RequestBody @Valid UpdateRecommendNumRequest updateRecommendNumRequest) {
        DistributionGoodsMatter distributionGoodsMatter = distributionGoodsMatterService.queryById(updateRecommendNumRequest.getId());
        if(!Objects.isNull(distributionGoodsMatter)){
            //查到，分享次数加1
            distributionGoodsMatter.setRecommendNum(distributionGoodsMatter.getRecommendNum()+1);
            //更新，不更新时间
            distributionGoodsMatterService.updataRecomendNumById(distributionGoodsMatter);
        }
        return BaseResponse.SUCCESSFUL();
    }
}
