package com.wanmi.sbc.windows;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.goods.api.provider.SuspensionV2.SuspensionProvider;
import com.wanmi.sbc.goods.api.provider.excel.GoodsExcelProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SuspensionByTypeRequest;
import com.wanmi.sbc.goods.api.response.SuspensionV2.SuspensionByTypeResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyContentRequest;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import com.wanmi.sbc.topic.response.GoodsOrBookResponse;
import com.wanmi.sbc.topic.response.NewBookPointResponse;
import com.wanmi.sbc.topic.response.ThreeGoodBookResponse;
import com.wanmi.sbc.topic.service.TopicService;
import com.wanmi.sbc.windows.request.MarketingRequest;
import com.wanmi.sbc.windows.request.ThreeGoodBookRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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


    @PostMapping("/getSuspensionByType")
    public BaseResponse<SuspensionByTypeResponse> getByType(@RequestBody @Valid SuspensionByTypeRequest suspensionByTypeRequest) {
        return suspensionProvider.getByType(suspensionByTypeRequest);
    }

    @PostMapping("/test")
    public List<NewBookPointResponse> newBookPoint(@RequestBody @Valid BaseQueryRequest baseQueryRequest)  {
        CustomerGetByIdResponse customer=new CustomerGetByIdResponse();
        return topicService.newBookPoint(baseQueryRequest,customer);
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


}
