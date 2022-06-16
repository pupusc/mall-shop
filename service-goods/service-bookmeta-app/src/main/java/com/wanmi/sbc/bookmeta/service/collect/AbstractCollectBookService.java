package com.wanmi.sbc.bookmeta.service.collect;

import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaBookResp;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.mapper.MetaAwardMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookClumpMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookContentMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookGroupMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureAwardMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaProducerMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaPublisherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/16 8:17 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public abstract class AbstractCollectBookService {

    @Autowired
    protected MetaAwardMapper metaAwardMapper;

    @Autowired
    protected MetaFigureAwardMapper metaFigureAwardMapper;

    @Autowired
    protected MetaBookFigureMapper metaBookFigureMapper;

    @Autowired
    protected MetaBookRcmmdMapper metaBookRcmmdMapper;

    //作者信息
    @Autowired
    protected MetaFigureMapper metaFigureMapper;

    //丛书
    @Autowired
    protected MetaBookClumpMapper metaBookClumpMapper;

    @Autowired
    protected MetaPublisherMapper metaPublisherMapper;

    @Autowired
    protected MetaProducerMapper metaProducerMapper;

    @Autowired
    protected MetaBookGroupMapper metaBookGroupMapper;

    @Autowired
    protected MetaBookContentMapper metaBookContentMapper;

    @Autowired
    protected MetaLabelMapper metaLabelMapper;

    @Autowired
    protected MetaBookLabelMapper metaBookLabelMapper;

    @Autowired
    protected MetaBookMapper metaBookMapper;

    /**
     * 封装书单信息
     * @param bookIds
     * @return
     */
    protected List<CollectMetaBookResp> collectBookByIds(List<Integer> bookIds){
        List<CollectMetaBookResp> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(bookIds)) {
            return result;
        }

        List<MetaBook> metaBooks = metaBookMapper.collectMetaBookByIds(bookIds);
        for (MetaBook metaBook : metaBooks) {
            result.add(this.changeMetaBook2Resp(metaBook));
        }
        return result;
    }

    /**
     * 封装书单信息
     * @param publisherIds
     * @return
     */
    protected List<CollectMetaBookResp> collectBookByPublisherIds(List<Integer> publisherIds){
        List<CollectMetaBookResp> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(publisherIds)) {
            return result;
        }

        List<MetaBook> metaBooks = metaBookMapper.collectMetaBookByPublisherIds(publisherIds);
        for (MetaBook metaBook : metaBooks) {
            result.add(this.changeMetaBook2Resp(metaBook));
        }
        return result;
    }

    /**
     * 封装书单信息
     * @param bookGroupIds
     * @return
     */
    protected List<CollectMetaBookResp> collectBookByBookGroupIds(List<Integer> bookGroupIds){
        List<CollectMetaBookResp> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(bookGroupIds)) {
            return result;
        }

        List<MetaBook> metaBooks = metaBookMapper.collectMetaBookByBookGroupIds(bookGroupIds);
        for (MetaBook metaBook : metaBooks) {
            result.add(this.changeMetaBook2Resp(metaBook));
        }
        return result;
    }


    /**
     * 封装书单信息
     * @param bookProducerIds
     * @return
     */
    protected List<CollectMetaBookResp> collectBookByBookProducerIds(List<Integer> bookProducerIds){
        List<CollectMetaBookResp> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(bookProducerIds)) {
            return result;
        }

        List<MetaBook> metaBooks = metaBookMapper.collectMetaBookByBookProducerIds(bookProducerIds);
        for (MetaBook metaBook : metaBooks) {
            result.add(this.changeMetaBook2Resp(metaBook));
        }
        return result;
    }


    /**
     * 对象转换
     * @param metaBook
     * @return
     */
    protected CollectMetaBookResp changeMetaBook2Resp(MetaBook metaBook) {
        CollectMetaBookResp collectMetaBookResp = new CollectMetaBookResp();
        collectMetaBookResp.setBookId(metaBook.getId());
        collectMetaBookResp.setIsbn(metaBook.getIsbn());
        return collectMetaBookResp;
    }
}
