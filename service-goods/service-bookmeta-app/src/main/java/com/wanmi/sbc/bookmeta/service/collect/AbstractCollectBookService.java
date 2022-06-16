package com.wanmi.sbc.bookmeta.service.collect;

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


    public void collectBook(List<Integer> bookIds){

    }
}
