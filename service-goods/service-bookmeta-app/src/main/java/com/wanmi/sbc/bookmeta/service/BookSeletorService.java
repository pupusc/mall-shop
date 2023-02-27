package com.wanmi.sbc.bookmeta.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.RecomentBookBo;
import com.wanmi.sbc.bookmeta.bo.SeletorBookInfoBo;
import com.wanmi.sbc.bookmeta.entity.BookSeletor;
import com.wanmi.sbc.bookmeta.mapper.BookSeletorMapper;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookSeletorService {


    @Resource
    private GoodsInfoQueryProvider goodsInfoQueryProvider;


    @Resource
    private BookSeletorMapper bookSeletorMapper;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    public List<BookSeletor> getByPage(BookSeletor bookSeletor,Integer pageInt,Integer pageSize ){
       return bookSeletorMapper.queryAllByLimit(bookSeletor, pageInt, pageSize);
    }

    public int add(BookSeletor bookSeletor){
        return bookSeletorMapper.insert(bookSeletor);
    }

    public int update(BookSeletor bookSeletor){
        return bookSeletorMapper.updateByPrimaryKeySelective(bookSeletor);
    }

    public List<SeletorBookInfoBo> getRecomment(String isbn) {
        List<SeletorBookInfoBo> seletorBookInfoBoList=new ArrayList<>();
        //找到所有推荐人
        BookSeletor bookSeletor=new BookSeletor();
        bookSeletor.setIsbn(isbn);
        List<BookSeletor> bookSeletorList = bookSeletorMapper.select(bookSeletor);

        //对于每一个推荐人，找到其推荐列表
        bookSeletorList.stream().forEach(bs->{
            List<String> isbnList = bookSeletorMapper.selectIsbnById(bs.getId());
            if(isbnList.size()!=1){
                //说明这个推荐人有其他可推荐的,构建推荐商品的详细信息
                GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest=new GoodsInfoViewByIdsRequest();
                goodsInfoViewByIdsRequest.setIsbnList(isbnList);
                List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.goodsInfoByIsbns(goodsInfoViewByIdsRequest).getContext().getGoodsInfos();
                //用户信息
                String c="{\"checkState\":\"CHECKED\",\"createTime\":\"2023-02-03T15:07:27\",\"customerAccount\":\"15618961858\",\"customerDetail\":{\"contactName\":\"书友_izw9\",\"contactPhone\":\"15618961858\",\"createTime\":\"2023-02-03T15:07:27\",\"customerDetailId\":\"2c9a00d184efa38001861619fbd60235\",\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerName\":\"书友_izw9\",\"customerStatus\":\"ENABLE\",\"delFlag\":\"NO\",\"employeeId\":\"2c9a00027f1f3e36017f202dfce40002\",\"isDistributor\":\"NO\",\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"},\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerLevelId\":3,\"customerPassword\":\"a8568f6a11ca32de1429db6450278bfd\",\"customerSaltVal\":\"64f88c8c7b53457f55671acc856bf60b7ffffe79ba037b8753c005d1265444ad\",\"customerType\":\"PLATFORM\",\"delFlag\":\"NO\",\"enterpriseCheckState\":\"INIT\",\"fanDengUserNo\":\"600395394\",\"growthValue\":0,\"loginErrorCount\":0,\"loginIp\":\"192.168.56.108\",\"loginTime\":\"2023-02-17T10:37:58\",\"payErrorTime\":0,\"pointsAvailable\":0,\"pointsUsed\":0,\"safeLevel\":20,\"storeCustomerRelaListByAll\":[],\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"}\n";
                CustomerGetByIdResponse customer= JSON.parseObject(c, CustomerGetByIdResponse.class);
                //填充价格信息
                MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
                filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
                filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
                List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();

                //构建返回类型
                SeletorBookInfoBo seletorBookInfoBo=new SeletorBookInfoBo();
                BeanUtils.copyProperties(bs,seletorBookInfoBo);
                List<RecomentBookBo> recomentBookBoList = KsBeanUtil.convertList(goodsInfoVOList, RecomentBookBo.class);
                seletorBookInfoBo.setRecomentBookBoList(recomentBookBoList);
                seletorBookInfoBoList.add(seletorBookInfoBo);
            }
        });
        return null;
    }
}
