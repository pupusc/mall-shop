package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.bo.MetaAwardBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdFigureBO;
import com.wanmi.sbc.bookmeta.entity.MetaAward;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaFigureAward;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaAwardMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureAwardMapper;
import com.wanmi.sbc.bookmeta.provider.MetaAwardProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-05-31 18:31:00
 */
@Service
public class MetaAwardService {
    @Resource
    private MetaAwardMapper metaAwardMapper;
    @Resource
    private MetaFigureAwardMapper metaFigureAwardMapper;
    @Resource
    private MetaBookRcmmdMapper metaBookRcmmdMapper;
    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;
    @Autowired
    private MetaBookMapper metaBookMapper;
    @Autowired
    private MetaAwardProvider metaAwardProvider;
    
    public void deleteById(Integer id) {
        if (id == null) {
            return;
        }
        //奖项
        this.metaAwardMapper.deleteById(id);
        //人物奖项
        MetaFigureAward metaFigureAward = new MetaFigureAward();
        metaFigureAward.setAwardId(id);
        this.metaFigureAwardMapper.delete(metaFigureAward);
        //获奖推荐
        MetaBookRcmmd metaBookRcmmd = new MetaBookRcmmd();
        metaBookRcmmd.setBizType(BookRcmmdTypeEnum.AWARD.getCode());
        metaBookRcmmd.setBizId(id);
        this.metaBookRcmmdMapper.delete(metaBookRcmmd);
    }

    public List<MetaAward> listEntityByIds(List<Integer> awardIds) {
        if (CollectionUtils.isEmpty(awardIds)) {
            return Collections.EMPTY_LIST;
        }

        Example example = new Example(MetaAward.class);
        example.createCriteria().andEqualTo("delFlag", 0).andIn("id", awardIds);
        return this.metaAwardMapper.selectByExample(example);
    }

    public BusinessResponse<List<MetaAwardBO>> queryBySku(String sku) {
        //通过skuId获取bookId
        String isbn = goodsInfoQueryProvider.isbnBySkuId(sku);
        MetaBook metaBook=new MetaBook();
        metaBook.setIsbn(isbn);
        Integer bookId = metaBookMapper.queryAllByLimit(metaBook, 0, 10).get(0).getId();
        //找到所有推荐人
        List<MetaAwardBO> collect = metaBookRcmmdMapper.RcommdFigureByBookId(bookId).stream().map(bs -> {
            if (BookRcmmdTypeEnum.AWARD.getCode().equals(bs.getBizType())) {
                MetaAwardBO award = metaAwardProvider.queryById(bs.getBizId()).getContext();
                return award;
            }
            return null;
        }).filter(metaAwardBO -> {
            return null != metaAwardBO;
        }).collect(Collectors.toList());
        return BusinessResponse.success(collect);
    }
}
