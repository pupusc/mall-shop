package com.wanmi.sbc.bookmeta.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaAwardBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdFigureBO;
import com.wanmi.sbc.bookmeta.bo.RecomentBookBo;
import com.wanmi.sbc.bookmeta.entity.*;
import com.wanmi.sbc.bookmeta.enums.BookContentTypeEnum;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.*;
import com.wanmi.sbc.bookmeta.provider.MetaAwardProvider;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-05-28 00:33:00
 */
@Service
public class MetaFigureService {
    @Resource
    private MetaFigureMapper metaFigureMapper;
    @Resource
    private MetaBookFigureMapper metaBookFigureMapper;
    @Resource
    private MetaBookContentMapper metaBookContentMapper;
    @Resource
    private MetaBookRcmmdMapper metaBookRcmmdMapper;

    @Resource
    private GoodsInfoQueryProvider goodsInfoQueryProvider;
    @Autowired
    private MarketingPluginProvider marketingPluginProvider;
    @Autowired
    private MetaBookMapper metaBookMapper;
    @Autowired
    private MetaAwardProvider metaAwardProvider;



    public List<MetaFigure> listFigureByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }

        Example example = new Example(MetaFigure.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", ids);
        return metaFigureMapper.selectByExample(example);
    }

    public List<MetaFigure> listFigureByBookIds(List<Integer> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        Example example = new Example(MetaBookFigure.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("bookId", ids);
        List<Integer> figureIds = this.metaBookFigureMapper.selectByExample(example).stream().map(MetaBookFigure::getFigureId).collect(Collectors.toList());
        return listFigureByIds(figureIds);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            return;
        }
        this.metaFigureMapper.deleteById(id);
        //书籍人物
        MetaBookFigure metaBookFigure = new MetaBookFigure();
        metaBookFigure.setFigureId(id);
        this.metaBookFigureMapper.delete(metaBookFigure);
        //人物推荐
        Example example = new Example(MetaBookRcmmd.class);
        example.createCriteria().andEqualTo("bizId", id)
                .andIn("bizType", Arrays.asList(
                        BookRcmmdTypeEnum.EDITOR.getCode(),
                        BookRcmmdTypeEnum.MEDIA.getCode(),
                        BookRcmmdTypeEnum.ORGAN.getCode(),
                        BookRcmmdTypeEnum.EXPERT.getCode()));
        this.metaBookRcmmdMapper.deleteByExample(example);
        //出版内容（作序人）
        MetaBookContent metaBookContent = new MetaBookContent();
        metaBookContent.setFigureId(id);
        metaBookContent.setType(BookContentTypeEnum.PRELUDE.getCode());
        this.metaBookContentMapper.delete(metaBookContent);
    }

    public List<MetaBookRcmmdFigureBO> listFigureByskuId(String skuId) {
        //通过skuId获取bookId
        String isbn = goodsInfoQueryProvider.isbnBySkuId(skuId);
        MetaBook metaBook=new MetaBook();
        metaBook.setIsbn(isbn);
        Integer bookId = metaBookMapper.queryAllByLimit(metaBook, 0, 10).get(0).getId();
        //找到所有推荐人
        List<MetaBookRcmmdFigureBO> metaBookRcmmdFigureList = metaBookRcmmdMapper.RcommdFigureByBookId(bookId);

        //对于每一个推荐人，找到其推荐列表
        List<MetaBookRcmmdFigureBO> result = metaBookRcmmdFigureList.stream().map(bs -> {
            if(BookRcmmdTypeEnum.WENMIAO.getCode().equals(bs.getBizType())){
                if(null==bs.getDescr()){
                    return null;//文喵必有推荐语
                }else{//文喵不用给商品列表
                    return bs;
                }
            }
            //如果是奖项，仅透出奖项名
            if(BookRcmmdTypeEnum.AWARD.getCode().equals(bs.getBizType())){
                MetaAwardBO award = metaAwardProvider.queryById(bs.getBizId()).getContext();
                bs.setName(award.getName());
                return bs;
            }
            if(null==bs.getBizId() && null == bs.getDescr()){
                return null;//没有推荐人和推荐语的跳过
            }
            List<String> isbnList = metaBookRcmmdMapper.RcommdBookByFigureId(bs.getBizId(), bookId);
            if (isbnList.size() != 0) {
                //说明这个推荐人有其他可推荐的,构建推荐商品的详细信息
                GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
                goodsInfoViewByIdsRequest.setIsbnList(isbnList);
                BaseResponse<GoodsInfoViewByIdsResponse> goodsInfoViewByIdsResponseBaseResponse = goodsInfoQueryProvider.goodsInfoByIsbns(goodsInfoViewByIdsRequest);

                if(goodsInfoViewByIdsResponseBaseResponse.getCode().equals(CommonErrorCode.FAILED)){
                    return bs;
                }
                List<String> skuIdList = goodsInfoViewByIdsResponseBaseResponse.getContext().getGoodsInfos().stream().map(g -> g.getGoodsInfoId()).collect(Collectors.toList());
                goodsInfoViewByIdsRequest.getIsbnList().clear();
                goodsInfoViewByIdsRequest.setGoodsInfoIds(skuIdList);
                List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listSimpleView(goodsInfoViewByIdsRequest).getContext().getGoodsInfos();
                //用户信息
                String c = "{\"checkState\":\"CHECKED\",\"createTime\":\"2023-02-03T15:07:27\",\"customerAccount\":\"15618961858\",\"customerDetail\":{\"contactName\":\"书友_izw9\",\"contactPhone\":\"15618961858\",\"createTime\":\"2023-02-03T15:07:27\",\"customerDetailId\":\"2c9a00d184efa38001861619fbd60235\",\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerName\":\"书友_izw9\",\"customerStatus\":\"ENABLE\",\"delFlag\":\"NO\",\"employeeId\":\"2c9a00027f1f3e36017f202dfce40002\",\"isDistributor\":\"NO\",\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"},\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerLevelId\":3,\"customerPassword\":\"a8568f6a11ca32de1429db6450278bfd\",\"customerSaltVal\":\"64f88c8c7b53457f55671acc856bf60b7ffffe79ba037b8753c005d1265444ad\",\"customerType\":\"PLATFORM\",\"delFlag\":\"NO\",\"enterpriseCheckState\":\"INIT\",\"fanDengUserNo\":\"600395394\",\"growthValue\":0,\"loginErrorCount\":0,\"loginIp\":\"192.168.56.108\",\"loginTime\":\"2023-02-17T10:37:58\",\"payErrorTime\":0,\"pointsAvailable\":0,\"pointsUsed\":0,\"safeLevel\":20,\"storeCustomerRelaListByAll\":[],\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"}\n";
                CustomerGetByIdResponse customer = JSON.parseObject(c, CustomerGetByIdResponse.class);
                //填充价格信息
                MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
                filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
                filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
                List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();
                if(null==goodsInfoVOList || goodsInfoVOList.size()==0){
                    //没有商品信息直接返回
                    return bs;
                }
                //构建返回类型
                bs.getRecomentBookBoList().addAll(KsBeanUtil.convertList(goodsInfoVOList, MetaBookRcmmdFigureBO.RecomentBookVo.class));
                return bs;
            }
            return null;
        }).collect(Collectors.toList());
        List<MetaBookRcmmdFigureBO> collect = result.stream().filter(r -> null != r).collect(Collectors.toList());
        return collect;
    }

}
