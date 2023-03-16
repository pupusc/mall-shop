package com.wanmi.sbc.windows;

import com.alibaba.fastjson.JSONObject;
import com.ofpay.rex.control.helper.StringUtils;
import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.bookmeta.provider.MetaAwardProvider;
import com.wanmi.sbc.bookmeta.provider.MetaBookFigureProvider;
import com.wanmi.sbc.bookmeta.provider.MetaBookProvider;
import com.wanmi.sbc.bookmeta.provider.MetaFigureProvider;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.goods.api.provider.SuspensionV2.SuspensionProvider;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.provider.excel.GoodsExcelProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SuspensionByTypeRequest;
import com.wanmi.sbc.goods.api.response.SuspensionV2.SuspensionByTypeResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.RankGoodsPublishResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.RankGoodsPublishTempResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingProvider;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyContentRequest;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import com.wanmi.sbc.topic.response.GoodsOrBookResponse;
import com.wanmi.sbc.topic.response.NewBookPointResponse;
import com.wanmi.sbc.topic.response.ThreeGoodBookResponse;
import com.wanmi.sbc.topic.service.TopicService;
import com.wanmi.sbc.util.DitaUtil;
import com.wanmi.sbc.windows.request.MarketingRequest;
import com.wanmi.sbc.windows.request.ThreeGoodBookRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/windows/v2")
@Slf4j
public class SuspensionController {

    @Autowired
    private SuspensionProvider suspensionProvider;

    @Autowired
    private TopicService topicService;

    @Autowired
    private GoodsExcelProvider goodsExcelProvider;

    @Autowired
    private MarketingProvider marketingProvider;

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Autowired
    private MetaFigureProvider metaFigureProvider;
    @Autowired
    private MetaBookProvider metaBookProvider;
    @Autowired
    private MetaAwardProvider metaAwardProvider;
    @Autowired
    private GoodsProvider goodsProvider;
    @Autowired
    private RedisService redisService;

    @Value("classpath:/download/avtivity_goods.xlsx")
    private org.springframework.core.io.Resource templateFile;

    @PostMapping("/getSuspensionByType")
    public BaseResponse<SuspensionByTypeResponse> getByType(@RequestBody @Valid SuspensionByTypeRequest suspensionByTypeRequest) {
        return suspensionProvider.getByType(suspensionByTypeRequest);
    }

    @PostMapping("/test")
    public List<NewBookPointResponse> newBookPoint(@RequestBody @Valid BaseQueryRequest baseQueryRequest)  {
        CustomerGetByIdResponse customer=new CustomerGetByIdResponse();
        return topicService.newBookPoint(baseQueryRequest,customer, baseQueryRequest.getSortType());
    }

    @PostMapping("/test1")
    public List<ThreeGoodBookResponse> threeGoodBook(@RequestBody @Valid ThreeGoodBookRequest threeGoodBookRequest)  {
        return topicService.threeGoodBook(threeGoodBookRequest);
    }

    @PostMapping("/test2")
    public List<GoodsOrBookResponse> GoodsOrBook(@RequestBody @Valid  TopicStoreyContentRequest topicStoreyContentRequest)  {
        CustomerGetByIdResponse customer=new CustomerGetByIdResponse();
        return topicService.bookOrGoods(topicStoreyContentRequest,customer);
    }

    @PostMapping("/test3")
    public BaseResponse loadExcel(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "topicStoreId") Integer topicStoreyId, @RequestParam(value = "topicStoreySearchId") Integer topicStoreySearchId)  {
        return goodsExcelProvider.loadExcel(file,topicStoreyId,topicStoreySearchId);
    }

    @PostMapping("/test4")
    public BaseResponse getMarketingInfo(@RequestBody MarketingRequest marketingRequest)  {
        return marketingProvider.getMarketingInfo(marketingRequest.getMarketingIdList());
    }

    @PostMapping("/test6")
    public BaseResponse listFigureById(@RequestParam(value = "spuId") String spuId,@RequestParam(value = "skuId") String skuId)  {
        return metaFigureProvider.listFigureRecomdById(spuId,skuId);
    }

    @PostMapping("/goodsDetail")
    public BaseResponse getGoodsDetialById(@RequestParam(value = "spuId",required = false) String spuId,@RequestParam(value = "skuId,",required = false) String skuId)  {
        log.info("test10_start");
        BaseResponse goodsDetialById = topicService.getGoodsDetialById1(spuId, skuId);
        log.info("test10_end");
        return goodsDetialById;
    }

    @PostMapping("/getFreightTempAreaBySpu")
    public BaseResponse getFreightTempAreaBySpu(@RequestParam(value = "spuId",required = false) String spuId)  {
        return goodsProvider.getFreightTempAreaBySpu(spuId);
    }

    @PostMapping("/getMarketingLabel")
    public BaseResponse getMarketingLabel(@RequestParam(value = "spuId",required = false) String spuId,@RequestParam(value = "skuId,",required = false) String skuId)  {
        Map map=new HashMap<>();
        if(null!=skuId){
           String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID"+ ":" + skuId);
           if(null==old_json){
               return null;
           }
           map= JSONObject.parseObject(old_json,Map.class);
       }else {
            if(null==spuId){
                return null;
            }
            Map skuIdMap =(Map) goodsProvider.getSkuBySpu(spuId).getContext();
            String old_json = redisService.getString("ELASTIC_SAVE:GOODS_MARKING_SKU_ID"+ ":" + skuIdMap.get("goods_info_id").toString());
            if(null==old_json){
                return null;
            }
            map= JSONObject.parseObject(old_json,Map.class);
        }
        return BaseResponse.success(map);
    }

    @PostMapping("/getTagsLabel")
    public BaseResponse getTagsLabel(@RequestParam(value = "spuId,",required = false) String spuId)  {
        Map map=new HashMap<>();
        if(null!=spuId){
            String old_json = redisService.getString("ELASTIC_SAVE:GOODS_TAGS_SPU_ID"+ ":" + spuId);
            if(null==old_json){
                return null;
            }
            map= JSONObject.parseObject(old_json,Map.class);
        }else {
            return null;
        }
        return BaseResponse.success(map);
    }

    @PostMapping("/test7")
    public BusinessResponse<MetaBookRecommentKeyBo> getRecommentKey(@RequestParam(value = "spuId") String spuId)  {
        return metaBookProvider.getRecommentKey(spuId);
    }

    @PostMapping("/test8")
    public MetaBookQueryByPageBo bookLabelQueryByPage(@RequestBody MetaBookQueryByPageBo metaBookQueryByPageBo) {
        return metaBookProvider.queryBookLabelBypage(metaBookQueryByPageBo);
    }

    @PostMapping("/test9")
    BusinessResponse<List<MetaAwardBO>> queryBySku(@RequestParam(value = "sku") String sku) {
        return this.metaAwardProvider.queryBySku(sku);
    }
    @GetMapping("/test11")
    public BaseResponse threeBookSaveRedis(@RequestParam(value = "topicStoreyId") Integer topicStoreyId)  {
        return topicService.threeBookSaveRedis(topicStoreyId);
    }
    @PostMapping("/test12")
    public BaseResponse getThreeBookSaveByRedis(@RequestBody TopicStoreyContentRequest topicStoreyContentRequest)  {
        return BaseResponse.success(topicService.getThreeBookSaveByRedis(topicStoreyContentRequest));
    }
    @GetMapping("/test13")
    public BaseResponse goodsOrBookSaveRedis(@RequestParam(value = "topicStoreyId") Integer topicStoreyId)  {
         return topicService.goodsOrBookSaveRedis(topicStoreyId);
    }
//    @PostMapping("/test14")
//    public BaseResponse getGoodsOrBookSaveByRedis(@RequestBody TopicStoreyContentRequest topicStoreyContentRequest)  {
//        return BaseResponse.success(topicService.getGoodsOrBookSaveByRedis(topicStoreyContentRequest,));
//    }

}
