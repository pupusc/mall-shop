package com.wanmi.sbc.crm.tagdimension;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.tagdimension.TagDimensionQueryProvider;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionBigJsonRequest;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionListRequest;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionBigJsonResponse;
import com.wanmi.sbc.crm.api.response.tagdimension.TagDimensionListResponse;
import com.wanmi.sbc.crm.bean.enums.DateType;
import com.wanmi.sbc.crm.bean.enums.TagParamColumn;
import com.wanmi.sbc.crm.bean.enums.TerminalSourceType;
import com.wanmi.sbc.crm.bean.vo.TagDimensionVO;
import com.wanmi.sbc.crm.bean.vo.TagParamVO;
import com.wanmi.sbc.crm.tagdimension.response.SelectParamResponse;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListStoreRequest;
import com.wanmi.sbc.customer.api.request.store.StorePageRequest;
import com.wanmi.sbc.customer.api.response.store.ListStoreResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.StoreSimpleVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandListRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandPageRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListByConditionRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsPageRequest;
import com.wanmi.sbc.goods.api.response.brand.GoodsBrandListResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsPageResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Api(description = "标签维度管理API", tags = "TagDimensionController")
@RestController
@RequestMapping(value = "/tagdimension")
public class TagDimensionController {

    @Autowired
    private TagDimensionQueryProvider tagDimensionQueryProvider;


    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;



    @ApiOperation(value = "列表查询标签维度")
    @PostMapping("/list")
    public BaseResponse<TagDimensionListResponse> getList(@RequestBody @Valid TagDimensionListRequest listReq) {
        listReq.putSort("id", "desc");
        return tagDimensionQueryProvider.list(listReq);
    }
    @ApiOperation(value = "列表查询标签维度")
    @GetMapping("/getBigJson")
    public BaseResponse<TagDimensionBigJsonResponse> getBigJson() {
        TagDimensionBigJsonRequest request = new TagDimensionBigJsonRequest();
        BaseResponse<TagDimensionBigJsonResponse> baseResponse = tagDimensionQueryProvider.getBigJson(request);
        TagDimensionBigJsonResponse responseBaseRespon = baseResponse.getContext();
        responseBaseRespon = getTagDimensionBigJson(responseBaseRespon);
        baseResponse.setContext(responseBaseRespon);
        return baseResponse;
    }

    @ApiOperation(value = "列表查询标签维度")
    @GetMapping("/getPreferenceBigJson")
    public BaseResponse<TagDimensionBigJsonResponse> getPreferenceBigJson(){
        BaseResponse<TagDimensionBigJsonResponse> baseResponse = tagDimensionQueryProvider.getPreferenceBigJson();
        TagDimensionBigJsonResponse responseBaseRespon = baseResponse.getContext();
        responseBaseRespon = getTagDimensionBigJson(responseBaseRespon);
        baseResponse.setContext(responseBaseRespon);
        return baseResponse;
    }


    @ApiOperation(value = "列表查询标签维度")
    @PostMapping("/getOtherTagBigJson")
    public BaseResponse<TagDimensionBigJsonResponse> getOhterTagBigJson(@RequestBody @Valid TagDimensionBigJsonRequest request) {
        BaseResponse<TagDimensionBigJsonResponse> baseResponse = tagDimensionQueryProvider.getBigJson(request);
        TagDimensionBigJsonResponse responseBaseRespon = baseResponse.getContext();
        if(CollectionUtils.isNotEmpty(request.getDimensionIdList())){
            responseBaseRespon = getTagDimensionBigJson(responseBaseRespon);
        }
        baseResponse.setContext(responseBaseRespon);
        return baseResponse;
    }

    private TagDimensionBigJsonResponse getTagDimensionBigJson(TagDimensionBigJsonResponse responseBaseRespon){
        //初始化10条商品数据作为默认值
        GoodsPageRequest pageRequest = new GoodsPageRequest();
        pageRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<CheckStatus> checkStatusList = new ArrayList<>();
        checkStatusList.add(CheckStatus.CHECKED);
        checkStatusList.add(CheckStatus.FORBADE);
        pageRequest.setAuditStatusList(checkStatusList);
        pageRequest.putSort("createTime", SortType.DESC.toValue());
        List<GoodsSimpleVO> goodsList = getGoodsSimpleList(pageRequest);

        //初始化10条品牌数据作为默认值
        GoodsBrandListRequest queryRequest =GoodsBrandListRequest.builder().delFlag(DeleteFlag.NO.toValue()).build();
//        List<GoodsBrandSimpleVO> brandList = getGoodsBrandSimpleList(queryRequest);
        List<GoodsBrandSimpleVO> brandList = new ArrayList<>();

        //初始化10条商家数据作为默认值
        ListStoreRequest storeRequest = ListStoreRequest.builder().auditState(CheckState.CHECKED).build();
//        List<StoreSimpleVO> storeList = getStoreSimpleList(storeRequest);
        List<StoreSimpleVO> storeList = new ArrayList<>();

        //初始化10条一级分类数据作为默认值
        GoodsCateListByConditionRequest firstCateRequest = new GoodsCateListByConditionRequest();
        firstCateRequest.setCateGrade(1);
//        List<GoodsCateSimpleVO>  firstCateList = getGoodsCateSImpleList(firstCateRequest);
        List<GoodsCateSimpleVO>  firstCateList = new ArrayList<>();

        //初始化10条三级分类数据作为默认值
        firstCateRequest.setCateGrade(3);
//        List<GoodsCateSimpleVO>  thirdCateList = getGoodsCateSImpleList(firstCateRequest);
        List<GoodsCateSimpleVO>  thirdCateList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(responseBaseRespon.getOtherList())){
            setListDefaultValue(responseBaseRespon.getOtherList(),goodsList,brandList,storeList,firstCateList,thirdCateList);
        }
        if(CollectionUtils.isNotEmpty(responseBaseRespon.getQuotaList())){
            setListDefaultValue(responseBaseRespon.getQuotaList(),goodsList,brandList,storeList,firstCateList,thirdCateList);
        }
        if(CollectionUtils.isNotEmpty(responseBaseRespon.getPreferenceParamList())){
            setListDefaultValue(responseBaseRespon.getPreferenceParamList(),goodsList,brandList,storeList,firstCateList,thirdCateList);
        }
        return responseBaseRespon;
    }


    /**
     * @Author lvzhenwei
     * @Description 搜索商品数据信息
     * @Date 16:23 2020/8/26
     * @Param [pageRequest]
     * @return com.wanmi.sbc.common.base.BaseResponse<java.util.List<com.wanmi.sbc.goods.bean.vo.GoodsSimpleVO>>
     **/
    @ApiOperation(value = "搜索商品数据信息")
    @PostMapping("/getGoodsSimpleInfoList")
    public BaseResponse<SelectParamResponse> getGoodsSimpleInfoList(@RequestBody GoodsPageRequest pageRequest){
        pageRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<CheckStatus> checkStatusList = new ArrayList<>();
        checkStatusList.add(CheckStatus.CHECKED);
        checkStatusList.add(CheckStatus.FORBADE);
        pageRequest.setAuditStatusList(checkStatusList);
        pageRequest.putSort("createTime", SortType.DESC.toValue());
        List<GoodsSimpleVO> goodsSimpleVOList = getGoodsSimpleList(pageRequest);
        List<Map<String,String>> goodsMapList = new ArrayList<>();
        goodsSimpleVOList.forEach(goodsSimpleVO -> {
            Map<String,String> goodsMap = new HashMap<>();
            goodsMap.put("id",goodsSimpleVO.getGoodsId());
            goodsMap.put("value",goodsSimpleVO.getGoodsName());
            goodsMapList.add(goodsMap);
        });
        return BaseResponse.success(SelectParamResponse.builder().selectParamList(goodsMapList).build());
    }

    /**
     * @Author lvzhenwei
     * @Description 搜索商品品牌数据信息
     * @Date 20:33 2020/8/26
     * @Param [pageRequest]
     * @return com.wanmi.sbc.common.base.BaseResponse<java.util.List<com.wanmi.sbc.goods.bean.vo.GoodsBrandSimpleVO>>
     **/
    @ApiOperation(value = "搜索商品品牌数据信息")
    @PostMapping("/getGoodsBrandSimpleInfoList")
    public BaseResponse<SelectParamResponse> getGoodsBrandSimpleInfoList(@RequestBody GoodsBrandListRequest request){
        request.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsBrandSimpleVO> goodsBrandSimpleVOList = getGoodsBrandSimpleList(request);
        List<Map<String,String>> goodsBrandMapList = new ArrayList<>();
        goodsBrandSimpleVOList.forEach(goodsBrandSimpleVO -> {
            Map<String,String> goodsBrandMap = new HashMap<>();
            goodsBrandMap.put("id",String.valueOf(goodsBrandSimpleVO.getBrandId()));
            goodsBrandMap.put("value",goodsBrandSimpleVO.getBrandName());
            goodsBrandMapList.add(goodsBrandMap);
        });
        return BaseResponse.success(SelectParamResponse.builder().selectParamList(goodsBrandMapList).build());
    }

    /**
     * @Author lvzhenwei
     * @Description 搜索商品分类数据信息
     * @Date 20:36 2020/8/26
     * @Param [pageRequest]
     * @return com.wanmi.sbc.common.base.BaseResponse<java.util.List<com.wanmi.sbc.goods.bean.vo.GoodsCateSimpleVO>>
     **/
    @ApiOperation(value = "搜索商品分类数据信息")
    @PostMapping("/getGoodsCateSImpleInfoList")
    public BaseResponse<SelectParamResponse> getGoodsCateSImpleInfoList(@RequestBody GoodsCateListByConditionRequest pageRequest){
        List<GoodsCateSimpleVO> goodsCateSImpleVOList = getGoodsCateSImpleList(pageRequest);
        List<Map<String,String>> goodsCateMapList = new ArrayList<>();
        goodsCateSImpleVOList.forEach(goodsCateSimpleVO -> {
            Map<String,String> goodsCatMap = new HashMap<>();
            goodsCatMap.put("id",String.valueOf(goodsCateSimpleVO.getCateId()));
            goodsCatMap.put("value",goodsCateSimpleVO.getCateName());
            goodsCateMapList.add(goodsCatMap);
        });
        return BaseResponse.success(SelectParamResponse.builder().selectParamList(goodsCateMapList).build());
    }

    @ApiOperation(value = "搜索店铺数据信息")
    @PostMapping("/getStoreSimpleInfoList")
    public BaseResponse<SelectParamResponse> getStoreSimpleInfoList(@RequestBody ListStoreRequest listStoreRequest){
        listStoreRequest.setDelFlag(DeleteFlag.NO);
        listStoreRequest.setAuditState(CheckState.CHECKED);
        List<StoreSimpleVO> storeImpleVOList = getStoreSimpleList(listStoreRequest);
        List<Map<String,String>> storeMapList = new ArrayList<>();
        storeImpleVOList.forEach(storeSimpleVO -> {
            Map<String,String> storeMap = new HashMap<>();
            storeMap.put("id",String.valueOf(storeSimpleVO.getStoreId()));
            storeMap.put("value",storeSimpleVO.getStoreName());
            storeMapList.add(storeMap);
        });
        return BaseResponse.success(SelectParamResponse.builder().selectParamList(storeMapList).build());
    }

    private List<GoodsSimpleVO> getGoodsSimpleList(GoodsPageRequest pageRequest){
        BaseResponse<GoodsPageResponse> pageResponse = goodsQueryProvider.page(pageRequest);
        List<GoodsVO> goodses = pageResponse.getContext().getGoodsPage().getContent();
        List<GoodsSimpleVO> goodsList = KsBeanUtil.convert(goodses, GoodsSimpleVO.class);;
        return goodsList;
    }

    private List<GoodsBrandSimpleVO> getGoodsBrandSimpleList(GoodsBrandListRequest request){
        //初始化10条品牌数据作为默认值
        GoodsBrandListResponse goodsBrandListResponse =
                goodsBrandQueryProvider.list(request).getContext();
        List<GoodsBrandSimpleVO> brandList = KsBeanUtil.convert(goodsBrandListResponse.getGoodsBrandVOList(), GoodsBrandSimpleVO.class);
        return brandList;
    }

    private List<GoodsCateSimpleVO> getGoodsCateSImpleList(GoodsCateListByConditionRequest firstCateRequest){
        List<GoodsCateSimpleVO>  cateList = goodsCateQueryProvider.pageByCondition(firstCateRequest).getContext().getGoodsCateVOList();
        return cateList;
    }

    private List<StoreSimpleVO> getStoreSimpleList(ListStoreRequest listStoreRequest){
        //初始化10条品牌数据作为默认值
        ListStoreResponse  storeResponse = storeQueryProvider.listStore(listStoreRequest).getContext();
        List<StoreSimpleVO> storeList = KsBeanUtil.convert(storeResponse.getStoreVOList(), StoreSimpleVO.class);
        return storeList;
    }

    private void setListDefaultValue(List<TagDimensionVO> list,List<GoodsSimpleVO> goodsList, List<GoodsBrandSimpleVO> brandList, List<StoreSimpleVO> storeList,List<GoodsCateSimpleVO>  firstCateList,List<GoodsCateSimpleVO>  thirdCateList){
        String strTime = "00:00-01:00,01:00-02:00,02:00-03:00,03:00-04:00,04:00-05:00,05:00-06:00,06:00-07:00,07:00-08:00,08:00-09:00,09:00-10:00,10:00-11:00,11:00-12:00,12:00-13:00,13:00-14:00,14:00-15:00,15:00-16:00,16:00-17:00,17:00-18:00,18:00-19:00,19:00-20:00,20:00-21:00,21:00-22:00,22:00-23:00,23:00-24:00";
        list.stream().forEach(item->{
            item.getTagParamVOList().stream().forEach(_item ->{
                switch (TagParamColumn.valueOf(_item.getColumnName().toUpperCase())){
                    case GOODS_ID:
                        _item.setDefaultValue(goodsList.stream().map(goods->{
                            Map<String,String> rsMap = new HashMap<String,String>();
                            rsMap.put("id",goods.getGoodsId());
                            rsMap.put("value",goods.getGoodsName());
                            return rsMap;
                        }).collect(Collectors.toList()));
                        break;
                    case BRAND_ID:
                        _item.setDefaultValue(brandList.stream().map(brand->{
                            Map<String,String> rsMap = new HashMap<String,String>();
                            rsMap.put("id",String.valueOf(brand.getBrandId()));
                            rsMap.put("value",brand.getBrandName());
                            return rsMap;
                        }).collect(Collectors.toList()));
                        break;
                    case  STORE_ID:
                        _item.setDefaultValue(storeList.stream().map(store->{
                            Map<String,String> rsMap = new HashMap<String,String>();
                            rsMap.put("id",String.valueOf(store.getStoreId()));
                            rsMap.put("value",store.getStoreName());
                            return rsMap;
                        }).collect(Collectors.toList()));
                        break;
                    case  CATE_TOP_ID:
                        _item.setDefaultValue(firstCateList.stream().map(cate->{
                            Map<String,String> rsMap = new HashMap<String,String>();
                            rsMap.put("id",String.valueOf(cate.getCateId()));
                            rsMap.put("value",cate.getCateName());
                            return rsMap;
                        }).collect(Collectors.toList()));
                        break;
                    case CATE_ID:
                        _item.setDefaultValue(thirdCateList.stream().map(cate->{
                            Map<String,String> rsMap = new HashMap<String,String>();
                            rsMap.put("id",String.valueOf(cate.getCateId()));
                            rsMap.put("value",cate.getCateName());
                            return rsMap;
                        }).collect(Collectors.toList()));
                        break;
                    case  TERMINAL_SOURCE:
                        if(_item.getColumnType()!=0){
                            _item.setDefaultValue(new ArrayList<String>(){{
                                add(TerminalSourceType.WEIXIN.toValue());
                                add(TerminalSourceType.APP.toValue());
                                add(TerminalSourceType.PC.toValue());
                                add(TerminalSourceType.H5.toValue());
                            }}.stream().map(str->{
                                Map<String,String> rsMap = new HashMap<String,String>();
                                rsMap.put("id",str);
                                rsMap.put("value",str);
                                return rsMap;
                            }).collect(Collectors.toList()));
                        }
                        break;
                    case TIME:
                        if(_item.getColumnType()!=0){
                            _item.setDefaultValue(Stream.of(strTime.split(",")).map(str->{
                                Map<String,String> rsMap = new HashMap<String,String>();
                                rsMap.put("id",str);
                                rsMap.put("value",str);
                                return rsMap;
                            }).collect(Collectors.toList()));
                        }
                        break;
                    case DATE:
                        if(_item.getColumnType()!=0){
                            _item.setDefaultValue(new ArrayList<String>(){{
                                add(DateType.MONDAY.toValue());
                                add(DateType.TUESDAY.toValue());
                                add(DateType.WEDNESDAY.toValue());
                                add(DateType.THURSDAY.toValue());
                                add(DateType.FRIDAY.toValue());
                                add(DateType.SATURDAY.toValue());
                                add(DateType.SUNDAY.toValue());
                            }}.stream().map(str->{
                                Map<String,String> rsMap = new HashMap<String,String>();
                                rsMap.put("id",str);
                                rsMap.put("value",str);
                                return rsMap;
                            }).collect(Collectors.toList()));
                        }
                        break;
                }
            });
        });
    }


}
