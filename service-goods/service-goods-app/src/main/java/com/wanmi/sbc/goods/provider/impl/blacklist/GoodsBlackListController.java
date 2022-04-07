package com.wanmi.sbc.goods.provider.impl.blacklist;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.request.blacklist.*;
import com.wanmi.sbc.goods.api.request.blacklist.response.GoodsBlackListData;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.index.IndexFeatureVo;
import com.wanmi.sbc.goods.blacklist.model.root.GoodsBlackListDTO;
import com.wanmi.sbc.goods.blacklist.service.GoodsBlackListService;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.service.ClassifyService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/23 6:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@Slf4j
public class GoodsBlackListController implements GoodsBlackListProvider {

    @Autowired
    private GoodsBlackListService goodsBlackListService;
    @Autowired
    private GoodsInfoService goodsInfoService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ClassifyService classifyService;

    /**
     * 新增黑名单
     * @param goodsBlackListProviderRequest
     * @return
     */
    @Override
    public BaseResponse add(GoodsBlackListProviderRequest goodsBlackListProviderRequest) {
        goodsBlackListService.add(goodsBlackListProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除黑名单
     * @param id
     */
    @Override
    public BaseResponse delete(Integer id){
        goodsBlackListService.delete(id);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse update(GoodsBlackListCreateOrUpdateRequest goodsBlackListCreateOrUpdateRequest){
        goodsBlackListService.update(goodsBlackListCreateOrUpdateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 刷新黑名单到缓存
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    @Override
    public BaseResponse<GoodsBlackListPageProviderResponse> flushBlackListCache(GoodsBlackListCacheProviderRequest goodsBlackListPageProviderRequest) {
        GoodsBlackListPageProviderResponse goodsBlackListPageProviderResponse =
                goodsBlackListService.flushBlackListCache(goodsBlackListPageProviderRequest);
        return BaseResponse.success(goodsBlackListPageProviderResponse);
    }

    /**
     * 获取黑名单列表
     * @param goodsBlackListCacheProviderRequest
     * @return
     */
    @Override
    public BaseResponse<MicroServicePage<GoodsBlackListData>> list(@RequestBody GoodsBlackListCacheProviderRequest goodsBlackListCacheProviderRequest) {
        Page<GoodsBlackListDTO> page = goodsBlackListService.pageSimple(goodsBlackListCacheProviderRequest);
        List<GoodsBlackListDTO> blackListModels = page.getContent();

        List<GoodsBlackListData> dataList = new ArrayList<>();
        Map<String, List<GoodsBlackListData>> skuMap = new HashMap<>();
        Map<String, List<GoodsBlackListData>> spuMap = new HashMap<>();
        Map<String, List<GoodsBlackListData>> classifyMap = new HashMap<>();
        for (GoodsBlackListDTO blackListModel : blackListModels) {
            GoodsBlackListData goodsBlackListData = new GoodsBlackListData();
            BeanUtils.copyProperties(blackListModel, goodsBlackListData);
            goodsBlackListData.setCreateTime(blackListModel.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            dataList.add(goodsBlackListData);

            Integer businessType = blackListModel.getBusinessType();
            if(businessType == 1){
                skuMap.compute(blackListModel.getBusinessId(), (k ,v) -> {
                    if(v == null) {
                        v = new ArrayList<>();
                    }
                    v.add(goodsBlackListData);
                    return v;
                });
            }else if(businessType == 2){
                spuMap.compute(blackListModel.getBusinessId(), (k ,v) -> {
                    if(v == null) {
                        v = new ArrayList<>();
                    }
                    v.add(goodsBlackListData);
                    return v;
                });
            }else if(businessType == 3 || businessType == 4){
                classifyMap.compute(blackListModel.getBusinessId(), (k ,v) -> {
                    if(v == null) {
                        v = new ArrayList<>();
                    }
                    v.add(goodsBlackListData);
                    return v;
                });
            }
        }

        if(!skuMap.isEmpty()){
            List<GoodsInfo> goodsInfos = goodsInfoService.findByIds(new ArrayList<>(skuMap.keySet()));
            for (GoodsInfo goodsInfo : goodsInfos) {
                List<GoodsBlackListData> datas = skuMap.get(goodsInfo.getGoodsInfoId());
                for (GoodsBlackListData data : datas) {
                    data.setItemCode(goodsInfo.getGoodsInfoNo());
                    data.setItemName(goodsInfo.getGoodsInfoName());
                }
            }
        }
        if(!spuMap.isEmpty()){
            List<Goods> goodsList = goodsService.listByGoodsIds(new ArrayList<>(spuMap.keySet()));
            for (Goods goods : goodsList) {
                List<GoodsBlackListData> datas = spuMap.get(goods.getGoodsId());
                for (GoodsBlackListData data : datas) {
                    data.setItemCode(goods.getGoodsNo());
                    data.setItemName(goods.getGoodsName());
                }
            }
        }
        if(!classifyMap.isEmpty()){
            List<Integer> classifyIdList = new ArrayList<>();
            classifyMap.keySet().forEach(i -> classifyIdList.add(Integer.parseInt(i)));
            List<ClassifyDTO> classifyList = classifyService.listNoPage(classifyIdList);
            for (ClassifyDTO classifyDTO : classifyList) {
                List<GoodsBlackListData> datas = classifyMap.get(classifyDTO.getId());
                for (GoodsBlackListData data : datas) {
                    data.setItemCode(classifyDTO.getId().toString());
                    data.setItemName(classifyDTO.getClassifyName());
                }
            }
        }
        MicroServicePage<GoodsBlackListData> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(page.getTotalElements());
        microServicePage.setContent(dataList);
        return BaseResponse.success(microServicePage);
    }

    /**
     * 获取黑名单列表
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    @Override
    public BaseResponse<GoodsBlackListPageProviderResponse> listNoPage(GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest) {
        GoodsBlackListPageProviderResponse goodsBlackListPageProviderResponse =
                goodsBlackListService.listNoPage(goodsBlackListPageProviderRequest);
        return BaseResponse.success(goodsBlackListPageProviderResponse);
    }
}
