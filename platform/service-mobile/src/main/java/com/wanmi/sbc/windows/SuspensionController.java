package com.wanmi.sbc.windows;

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

    @PostMapping("/test6")
    public BaseResponse listFigureById(@RequestParam(value = "spuId") String spuId,@RequestParam(value = "skuId") String skuId)  {
        return metaFigureProvider.listFigureRecomdById(spuId,skuId);
    }

    @PostMapping("/goodsDetail")
    public BaseResponse getGoodsDetialById(@RequestParam(value = "spuId",required = false) String spuId,@RequestParam(value = "skuId,",required = false) String skuId)  {
        log.info("test10_start");
        BaseResponse goodsDetialById = goodsProvider.getGoodsDetialById(spuId, skuId);
        log.info("test10_end");
        return goodsDetialById;
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
    @PostMapping("/test14")
    public BaseResponse getGoodsOrBookSaveByRedis(@RequestBody TopicStoreyContentRequest topicStoreyContentRequest)  {
        return BaseResponse.success(topicService.getGoodsOrBookSaveByRedis(topicStoreyContentRequest));
    }
    /**
     * 下载模板
     */
    @PostMapping("/test5")
    public void exportBookListModelById(@RequestParam(value = "id") Integer id)  {
            InputStream is = null;
            org.springframework.core.io.Resource file=templateFile;
            try{

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                is = file.getInputStream();
                Workbook wk = WorkbookFactory.create(is);

                Sheet expressCompanySheet = wk.getSheetAt(0);
                List<RankGoodsPublishTempResponse> context = bookListModelProvider.getPublishGoodsById(id);
                AtomicInteger rowCount= new AtomicInteger(1);
                context.stream().forEach(res -> {
                    Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                    row.createCell(0).setCellValue(res.getSkuNo());
                    row.createCell(1).setCellValue(res.getName());
                    if(null!=res.getSaleNum()){
                        row.createCell(2).setCellValue(res.getSaleNum().toString());
                    }
                    if(null!=res.getRankText()) {
                        row.createCell(3).setCellValue(res.getRankText());
                    }
                });
                wk.write(outputStream);
                String fileName = URLEncoder.encode("avtivity_goods.xlsx", "UTF-8");
                HttpUtil.getResponse().setHeader("Access-Control-Expose-Headers","Content-Disposition");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(outputStream.toByteArray());
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    /**
     * 下载模板
     */
    @PostMapping("/loadExcel")
    public BaseResponse loadExcel(MultipartFile multipartFile) {
        int addNum = 0;
        int updateNum = 0;
        String res=null;
        Workbook wb = null;
        try {
            try {
                wb = new HSSFWorkbook(new POIFSFileSystem(multipartFile.getInputStream()));
            } catch (Exception e) {
                wb = new XSSFWorkbook(multipartFile.getInputStream());        //XSSF不能读取Excel2003以前（包括2003）的版本
            }

            Sheet sheet = wb.getSheetAt(0);

            if (sheet == null) {
                return null;
            }

            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了第一行的所有行
            for (int rowNum = firstRowNum+1; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数


                int lastCellNum = row.getLastCellNum();
                String[] cells = new String[row.getLastCellNum()];

                //循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    cell.setCellType(CellType.STRING);
                    cells[cellNum] = cell.getStringCellValue();
                }
                RankGoodsPublishResponse rankGoodsPublishResponse=new RankGoodsPublishResponse();
                rankGoodsPublishResponse.setSkuNo(cells[0]);
                rankGoodsPublishResponse.setSaleNum(Integer.parseInt(cells[1]));
                rankGoodsPublishResponse.setRankText(cells[2]);
                res = bookListModelProvider.importById(rankGoodsPublishResponse);
                if(null!=res) {
                    String[] split = res.split(",");
                    addNum = addNum + Integer.parseInt(split[1]);
                    updateNum = updateNum + Integer.parseInt(split[0]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                multipartFile.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BaseResponse baseResponse=new BaseResponse<>();
            return baseResponse.info("操作成功", "成功增加" + addNum + "条," + "成功更新" + updateNum + "条");
        }

    }

}
