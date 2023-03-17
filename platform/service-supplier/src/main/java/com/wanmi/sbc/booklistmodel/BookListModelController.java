package com.wanmi.sbc.booklistmodel;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.booklistmodel.request.BookListMixRequest;
import com.wanmi.sbc.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.booklistmodel.request.BookListModelRequest;
import com.wanmi.sbc.booklistmodel.request.GoodsIdListRequest;
import com.wanmi.sbc.booklistmodel.response.BookListGoodsResponse;
import com.wanmi.sbc.booklistmodel.response.BookListMixResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsCustomQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsCustomQueryProviderRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.HasTopEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListMixProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.RankGoodsPublishResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.RankGoodsPublishTempResponse;
import com.wanmi.sbc.util.CommonUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 1:09 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@RequestMapping("/bookListModel")
public class BookListModelController {

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Resource
    private CommonUtil commonUtil;


    @Autowired
    private EsGoodsCustomQueryProvider esGoodsCustomQueryProvider;


    @Value("classpath:/download/avtivity_goods.xlsx")
    private org.springframework.core.io.Resource templateFile;
    /**
     * 新增书单
     * @param bookListMixRequest
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@Validated @RequestBody BookListMixRequest bookListMixRequest) {
        if(bookListMixRequest.getChooseRuleGoodsListModel() == null
                || bookListMixRequest.getBookListModel() == null
                || CollectionUtils.isEmpty(bookListMixRequest.getChooseRuleGoodsListModel().getGoodsIdListRequestList())
        ) {
            throw new IllegalArgumentException("请求参数错误");
        }

        if (bookListMixRequest.getChooseRuleGoodsListModel().getGoodsIdListRequestList().size() > 100) {
            throw new IllegalArgumentException("商品最大为100");
        }
        List<GoodsIdListRequest> checkParam = bookListMixRequest.getChooseRuleGoodsListModel().getGoodsIdListRequestList().stream()
                .filter(ex -> StringUtils.isEmpty(ex.getSpuId())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(checkParam)) {
            throw new IllegalArgumentException("请求参数错误");
        }


        String bookListMixRequestStr = JSON.toJSONString(bookListMixRequest);
        BookListMixProviderRequest request = JSON.parseObject(bookListMixRequestStr, BookListMixProviderRequest.class);
        request.setOperator(commonUtil.getOperatorId());
        if (request.getChooseRuleGoodsListModel() != null) {
            request.getChooseRuleGoodsListModel().setCategory(CategoryEnum.BOOK_LIST_MODEL.getCode());
        }
        bookListModelProvider.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     *  修改书单
     * @param bookListMixRequest
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@Validated @RequestBody BookListMixRequest bookListMixRequest) {
        if(bookListMixRequest.getChooseRuleGoodsListModel() == null
                || bookListMixRequest.getBookListModel() == null
                || CollectionUtils.isEmpty(bookListMixRequest.getChooseRuleGoodsListModel().getGoodsIdListRequestList())
                ) {
            throw new IllegalArgumentException("请求参数错误");
        }
        if (bookListMixRequest.getChooseRuleGoodsListModel().getGoodsIdListRequestList().size() > 100) {
            throw new IllegalArgumentException("商品最大为100");
        }
        List<GoodsIdListRequest> checkParam = bookListMixRequest.getChooseRuleGoodsListModel().getGoodsIdListRequestList().stream()
                .filter(ex -> StringUtils.isEmpty(ex.getSkuId())
                                || StringUtils.isEmpty(ex.getSpuId())
                                || StringUtils.isEmpty(ex.getSkuNo())
                        /*|| StringUtils.isEmpty(ex.getErpGoodsNo())
                        || StringUtils.isEmpty(ex.getErpGoodsInfoNo())*/).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(checkParam)) {
            throw new IllegalArgumentException("请求参数错误");
        }

        String bookListMixJsonStr = JSON.toJSONString(bookListMixRequest);
        BookListMixProviderRequest request = JSON.parseObject(bookListMixJsonStr, BookListMixProviderRequest.class);
        request.setOperator(commonUtil.getOperatorId());
        if (request.getChooseRuleGoodsListModel() != null) {
            request.getChooseRuleGoodsListModel().setCategory(CategoryEnum.BOOK_LIST_MODEL.getCode());
        }
        bookListModelProvider.update(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 删除书单
     * @param id
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Integer id) {

        BookListModelProviderRequest request = new BookListModelProviderRequest();
        request.setId(id);
        request.setOperator(commonUtil.getOperatorId());
        bookListModelProvider.delete(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 书单列表书单
     * @param bookListModelPageRequest
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @PostMapping("/listByPage")
    public BaseResponse<MicroServicePage<BookListModelProviderResponse>> listByPage(
            @RequestBody BookListModelPageRequest bookListModelPageRequest){
        BookListModelPageProviderRequest request = new BookListModelPageProviderRequest();
        BeanUtils.copyProperties(bookListModelPageRequest, request);
        if (bookListModelPageRequest.getBusinessType() != null) {
            request.setBusinessTypeList(Collections.singletonList(bookListModelPageRequest.getBusinessType()));
        }
        if (bookListModelPageRequest.getPublishState() != null) {
            request.setPublishStateList(Collections.singletonList(bookListModelPageRequest.getPublishState()));
        }
        return bookListModelProvider.listByPage(request);
    }


    /**
     * 发布书单
     * @param id
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @GetMapping("/publish/{id}")
    public BaseResponse publish(@PathVariable("id") Integer id){
        BookListModelProviderRequest request = new BookListModelProviderRequest();
        request.setId(id);
        request.setOperator(commonUtil.getOperatorId());
        return bookListModelProvider.publish(request);
    }


    /**
     * 根据id获取书单
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @GetMapping("/findById/{id}")
    public BaseResponse<BookListMixResponse> findById(@PathVariable("id") Integer id){
        BookListModelProviderRequest bookListModelProviderRequest = new BookListModelProviderRequest();
        bookListModelProviderRequest.setId(id);
        BaseResponse<BookListMixProviderResponse> response = bookListModelProvider.findById(bookListModelProviderRequest);
        BookListMixProviderResponse resultResponse = response.getContext();
        String bookListMixStr = JSON.toJSONString(resultResponse);
        BookListMixResponse bookListMixResponse = JSON.parseObject(bookListMixStr, BookListMixResponse.class);
        List<BookListGoodsResponse> bookListGoodsList = new ArrayList<>();
        if (bookListMixResponse != null && bookListMixResponse.getChooseRuleMode() != null && !CollectionUtils.isEmpty(bookListMixResponse.getChooseRuleMode().getBookListGoodsList())) {
            bookListGoodsList.addAll(bookListMixResponse.getChooseRuleMode().getBookListGoodsList());
        }

        if (!CollectionUtils.isEmpty(bookListGoodsList)) {
            Set<String> goodsIdNo = bookListGoodsList.stream().map(BookListGoodsResponse::getSpuId).collect(Collectors.toSet());
            EsGoodsCustomQueryProviderRequest request = new EsGoodsCustomQueryProviderRequest();
            request.setGoodIdList(goodsIdNo);
            request.setPageNum(0);
            request.setPageSize(100);
            BaseResponse<MicroServicePage<EsGoodsVO>> microServicePageBaseResponse = esGoodsCustomQueryProvider.listEsGoodsNormal(request);
            MicroServicePage<EsGoodsVO> contextPage = microServicePageBaseResponse.getContext();
            if (!CollectionUtils.isEmpty(contextPage.getContent())) {
                Map<String, EsGoodsVO> spuId2EsGoodsVoMap = new HashMap<>();
                for (EsGoodsVO esGoodsParam : contextPage.getContent()) {
                    spuId2EsGoodsVoMap.put(esGoodsParam.getId(), esGoodsParam);
                }
                //填充商品展示信息
                for (BookListGoodsResponse bookListGoodsResponseParam : bookListGoodsList) {
                    EsGoodsVO esGoodsVO = spuId2EsGoodsVoMap.get(bookListGoodsResponseParam.getSpuId());
                    if (esGoodsVO == null) {
                        continue;
                    }
                    //获取最小的 goodsInfo
                    List<GoodsInfoNestVO> goodsInfos = esGoodsVO.getGoodsInfos();
                    if (!CollectionUtils.isEmpty(goodsInfos)) {
                        BigDecimal marketPrice = goodsInfos.get(0).getMarketPrice();
                        String specText = goodsInfos.get(0).getSpecText();
                        for (GoodsInfoNestVO goodsInfo : esGoodsVO.getGoodsInfos()) {
                            if (marketPrice.compareTo(goodsInfo.getMarketPrice()) > 0) {
                                marketPrice = goodsInfo.getMarketPrice();
                                specText = goodsInfo.getSpecText();
                            }
                        }
                        bookListGoodsResponseParam.setMarketPrice(marketPrice);
                        bookListGoodsResponseParam.setSpecText(specText);
                    }

                    bookListGoodsResponseParam.setBuyPoint(esGoodsVO.getBuyPoint());
                    bookListGoodsResponseParam.setGoodsInfoName(esGoodsVO.getGoodsName());

                }
            }
        }
        return BaseResponse.success(bookListMixResponse);
    }


    /**
     * 置顶 0取消 1置顶 feature_d_v0.02
     * @menu 后台CMS2.0
     * @status undone
     * @return
     */
    @GetMapping("/top/{bookListModelId}/{hasTop}")
    public BaseResponse top(@PathVariable("bookListModelId") Integer bookListModelId, @PathVariable("hasTop") Integer hasTop){
        if (HasTopEnum.getByCode(hasTop) == null) {
            throw new IllegalStateException("K-000009");
        }
        BaseResponse<BookListModelProviderResponse> simpleBookListModelResponse =
                bookListModelProvider.findSimpleById(BookListModelProviderRequest.builder().id(bookListModelId).build());
        BookListModelProviderResponse simpleBookListModel = simpleBookListModelResponse.getContext();
        if (Objects.equals(simpleBookListModel.getHasTop(), hasTop)) {
            return BaseResponse.SUCCESSFUL();
        }
        bookListModelProvider.top(bookListModelId, hasTop);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 下载模板
     */
    @GetMapping("/exportExcel/{id}/{encrypted}")
    public void exportBookListModelById(@PathVariable(name = "id") Integer id)  {
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
                row.createCell(0).setCellValue(res.getSkuId());
                if(null!=res.getSaleNum()){
                    row.createCell(1).setCellValue(res.getSaleNum().toString());
                }
                if(null!=res.getRankText()) {
                    row.createCell(2).setCellValue(res.getRankText());
                }
                row.createCell(3).setCellValue(res.getName());
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
                rankGoodsPublishResponse.setSkuId(cells[0]);
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
