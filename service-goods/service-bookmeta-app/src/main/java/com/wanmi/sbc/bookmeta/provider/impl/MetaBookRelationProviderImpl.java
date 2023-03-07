package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.MetaBookRelationAddBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRelationBookAddBo;
import com.wanmi.sbc.bookmeta.bo.MetaBookRelationDelBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRelationKeyAddBo;
import com.wanmi.sbc.bookmeta.entity.MetaBookRelation;
import com.wanmi.sbc.bookmeta.entity.MetaBookRelationBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookRelationKey;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRelationBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRelationKeyMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRelationMapper;
import com.wanmi.sbc.bookmeta.provider.MetaBookRelationProvider;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/16:29
 * @Description:
 */
@Validated
@RestController
public class MetaBookRelationProviderImpl implements MetaBookRelationProvider {
    @Resource
    MetaBookRelationMapper metaBookRelationMapper;
    @Resource
    MetaBookRelationBookMapper metaBookRelationBookMapper;
    @Resource
    MetaBookRelationKeyMapper metaBookRelationKeyMapper;

    @Override
    @Transactional
    public Integer insert(MetaBookRelationAddBO addBO) {
        List<MetaBookRelationKeyAddBo> metaBookRelationKeyAddBo = addBO.getMetaBookRelationKeyAddBo();
        List<MetaBookRelationBookAddBo> metaBookRelationBookBo = addBO.getMetaBookRelationBook();
        MetaBookRelation convert = KsBeanUtil.convert(addBO, MetaBookRelation.class);
        List<MetaBookRelationBook> convertBook = KsBeanUtil.convertList(metaBookRelationBookBo, MetaBookRelationBook.class);
        List<MetaBookRelationKey> convertKey = KsBeanUtil.convertList(metaBookRelationKeyAddBo, MetaBookRelationKey.class);
        int i = metaBookRelationMapper.insertSelective(convert);
        int id = convert.getId();
        for (MetaBookRelationBook book:convertBook) {
            book.setRelationId(id);
            metaBookRelationBookMapper.insertMetaBookRelationBook(book);
        }
        for (MetaBookRelationKey key:convertKey) {
            key.setRelationId(id);
            metaBookRelationKeyMapper.insertSelective(key);
        }
        return i;
    }

    @Override
    public Integer delete(MetaBookRelationAddBO addBO) {
        List<MetaBookRelationKeyAddBo> metaBookRelationKeyAddBo = addBO.getMetaBookRelationKeyAddBo();
        List<MetaBookRelationBookAddBo> metaBookRelationBookBo = addBO.getMetaBookRelationBook();
        MetaBookRelation convert = KsBeanUtil.convert(addBO, MetaBookRelation.class);
        List<MetaBookRelationBook> convertBook = KsBeanUtil.convertList(metaBookRelationBookBo, MetaBookRelationBook.class);
        List<MetaBookRelationKey> convertKey = KsBeanUtil.convertList(metaBookRelationKeyAddBo, MetaBookRelationKey.class);
        int i = metaBookRelationMapper.deleteSelective(convert.getId());
        for (MetaBookRelationBook book:convertBook) {
            metaBookRelationBookMapper.deleteMetaBookRelationBook(book.getId());
        }
        for (MetaBookRelationKey key:convertKey) {
            metaBookRelationKeyMapper.deleteSelective(key.getId());
        }
        return i;
    }

    @Override
    public Integer deleteKey(MetaBookRelationDelBO delBOBO) {
        return metaBookRelationKeyMapper.deleteSelective(delBOBO.getId());
    }

    @Override
    public Integer deleteBook(MetaBookRelationDelBO delBOBO) {
        return metaBookRelationBookMapper.deleteMetaBookRelationBook(delBOBO.getId());
    }
}
