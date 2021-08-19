package com.wanmi.sbc.thirdplatform.linkedmall.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goodscatethirdcaterel.GoodsCateThirdCateRelQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.LinkedMallGoodsDelRequest;
import com.wanmi.sbc.goods.api.request.goods.LinkedMallGoodsModifyRequest;
import com.wanmi.sbc.goods.api.request.linkedmall.SyncItemRequest;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallGoodsDelResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallGoodsModifyResponse;
import com.wanmi.sbc.goods.bean.dto.LinkedMallItemDelDTO;
import com.wanmi.sbc.goods.bean.dto.LinkedMallItemModificationDTO;
import com.wanmi.sbc.linkedmall.api.provider.goods.LinkedMallGoodsQueryProvider;
import com.wanmi.sbc.linkedmall.api.provider.signature.SignatureProvider;
import com.wanmi.sbc.linkedmall.api.request.signature.ModificationItemRequest;
import com.wanmi.sbc.linkedmall.api.request.signature.SignatureVerifyRequest;
import com.wanmi.sbc.linkedmall.api.request.signature.SyncItemDeletionRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * linkedmall回调接口
 */
@Api(description = "linkedmall回调接口", tags = "BossLinkedMallController")
@RestController
@RequestMapping("/linkedmall")
@Slf4j
public class BossLinkedMallController {
    @Autowired
    private LinkedMallGoodsQueryProvider linkedMallGoodsQueryProvider;
    @Autowired
    private GoodsProvider goodsProvider;
    @Autowired
    private SignatureProvider signatureProvider;
    @Autowired
    private GoodsCateThirdCateRelQueryProvider goodsCateThirdCateRelQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private EsStandardProvider esStandardProvider;

    /**
     * 添加商品后信息推送接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "添加商品后信息推送接口")
    @PostMapping("/syncItemIncrement")
    public BaseResponse syncItemIncrement(@RequestBody SyncItemRequest request) throws IOException {
        boolean verify = signatureProvider.verifySignature(new SignatureVerifyRequest(originalString(request), request.getSignature())).getContext();
        if (!verify) {
            throw new SbcRuntimeException("K-200001");
        }
        List<String> standardIds = goodsProvider.addLinkedMallGoods(request).getContext().getStandardIds();
        //刷新商品库ES
        if(CollectionUtils.isNotEmpty(standardIds)) {
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(standardIds).build());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * linkedmall修改商品信息后，同步商品修改信息到客户侧
     *
     * @param request
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "linkedmall修改商品信息后，同步商品修改信息")
    @PostMapping("/syncItemModification")
    public BaseResponse syncItemModification(@RequestBody ModificationItemRequest request) throws IOException {
        boolean verify = signatureProvider.verifySignature(new SignatureVerifyRequest(originalString(request), request.getSignature())).getContext();
        if (!verify) {
            throw new SbcRuntimeException("K-200001");
        }
        List<ModificationItemRequest.LinkedMallItemModification> itemListEntity = request.getItemListEntity();
        List<LinkedMallItemModificationDTO> linkedMallItemModificationDTOS = KsBeanUtil.copyListProperties(itemListEntity, LinkedMallItemModificationDTO.class);
        LinkedMallGoodsModifyResponse response = goodsProvider.linkedmallModify(new LinkedMallGoodsModifyRequest(linkedMallItemModificationDTOS)).getContext();
        List<String> esGoodsInfoIds = response.getGoodsInfoIds();
        if (esGoodsInfoIds.size() > 0) {
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(esGoodsInfoIds).build());
        }
        List<String> standardIds = response.getStandardIds();
        if (CollectionUtils.isNotEmpty(standardIds)) {
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(esGoodsInfoIds).build());
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除商品后信息推送接口
     *
     * @param request
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "删除商品后信息推送接口")
    @PostMapping("/syncItemDeletion")
    public BaseResponse syncItemDeletion(@RequestBody SyncItemDeletionRequest request) throws IOException {
        boolean verify = signatureProvider.verifySignature(new SignatureVerifyRequest(originalString(request), request.getSignature())).getContext();
        if (!verify) {
            throw new SbcRuntimeException("K-200001");
        }
        List<SyncItemDeletionRequest.LinkedMallDeletionItem> itemIdListEntity = request.getItemIdListEntity();
        LinkedMallGoodsDelResponse response = goodsProvider.linkedmallDel(new LinkedMallGoodsDelRequest(KsBeanUtil.copyListProperties(itemIdListEntity, LinkedMallItemDelDTO.class))).getContext();
        List<String> esGoodsInfoIds = response.getGoodsInfoIds();
        if (esGoodsInfoIds.size() > 0) {
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(esGoodsInfoIds).build());
        }
        //初始化商品库ES
        if (CollectionUtils.isNotEmpty(response.getStandardIds())) {
            esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(response.getStandardIds()).build());
        }
        //删除商品库ES
        if (CollectionUtils.isNotEmpty(response.getDelStandardIds())) {
            esStandardProvider.deleteByIds(EsStandardDeleteByIdsRequest.builder().goodsIds(response.getDelStandardIds()).build());
        }
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 拼装linkedmall签名时用的字符串
     *
     * @param object
     * @return
     */
    private String originalString(Object object) {
        Class<?> aClass = object.getClass();
        Map<String, Object> data = new HashMap<>();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            Object value = null;
            declaredField.setAccessible(true);
//            以下不参与验签
            if (name != "signature" && name != "signatureMethod" && name != "serialVersionUID") {
                try {
                    value = declaredField.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (value != null) {
                data.put(name, value);
            }
        }
        String dataToBeSigned = "";
        List arrayList = new ArrayList(data.entrySet());
        Collections.sort(arrayList, new Comparator() {
            public int compare(Object arg1, Object arg2) {
                Map.Entry obj1 = (Map.Entry) arg1;
                Map.Entry obj2 = (Map.Entry) arg2;
                return (obj1.getKey()).toString().compareTo(obj2.getKey().toString());
            }
        });
        for (Iterator iter = arrayList.iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            dataToBeSigned = dataToBeSigned + (dataToBeSigned.equals("") ? "" : "&")
                    + entry.getKey() + "=" + entry.getValue();
        }
        return dataToBeSigned;
    }

}
