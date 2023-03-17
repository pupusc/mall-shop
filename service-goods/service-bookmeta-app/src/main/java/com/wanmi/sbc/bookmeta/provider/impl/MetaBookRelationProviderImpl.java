package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.*;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public Integer insert(RelationAddBO BO) {
        List<MetaBookRelation> metaBookRelation = metaBookRelationMapper.getMetaBookRelation(BO.getBookId());
        if (metaBookRelation.size() > 0) {
            for (MetaBookRelation relation : metaBookRelation) {
                List<MetaBookRelationBook> metaBookRelationBook = metaBookRelationBookMapper.getMetaBookRelationBook(relation.getId());
                List<MetaBookRelationKey> metaBookRelationKeys = metaBookRelationKeyMapper.getKeyById(relation.getId());
                if (metaBookRelationBook.size() > 0) {
                    for (MetaBookRelationBook book : metaBookRelationBook) {
                        metaBookRelationBookMapper.deleteMetaBookRelationBook(book.getId());
                    }
                }
                if (metaBookRelationKeys.size() > 0) {
                    for (MetaBookRelationKey key : metaBookRelationKeys) {
                        metaBookRelationKeyMapper.deleteSelective(key.getId());
                    }
                }
                metaBookRelationMapper.deleteSelective(relation.getId());
            }
        }
        int i = 0;
        List<MetaBookRelationAddBO> list = BO.getList();
        if (list.size() > 0) {
            for (MetaBookRelationAddBO addBO : list) {
                List<MetaBookRelationBookAddBo> metaBookRelationBookBo = addBO.getMetaBookRelationBook();
                MetaBookRelation convert = KsBeanUtil.convert(addBO, MetaBookRelation.class);
                metaBookRelationMapper.insertSelective(convert);
                i++;
                int id = convert.getId();
                List<MetaBookRelationBook> convertBook = KsBeanUtil.convertList(metaBookRelationBookBo, MetaBookRelationBook.class);
                if (convertBook.size() > 0) {
                    for (MetaBookRelationBook book : convertBook) {
                        book.setRelationId(id);
                        metaBookRelationBookMapper.insertMetaBookRelationBook(book);
                    }
                }
                List<MetaBookRelationKeyAddBo> metaBookRelationKeyAddBo = addBO.getMetaBookRelationKey();
                if (metaBookRelationKeyAddBo.size() > 0) {
                    List<MetaBookRelationKey> convertKey = KsBeanUtil.convertList(metaBookRelationKeyAddBo, MetaBookRelationKey.class);
                    for (MetaBookRelationKey key : convertKey) {
                        key.setRelationId(id);
                        metaBookRelationKeyMapper.insertSelective(key);
                    }
                }
            }
        }
        return i;
    }

    @Override
    public Integer delete(MetaBookRelationAddBO addBO) {
        List<MetaBookRelationKeyAddBo> metaBookRelationKeyAddBo = addBO.getMetaBookRelationKey();
        List<MetaBookRelationBookAddBo> metaBookRelationBookBo = addBO.getMetaBookRelationBook();
        MetaBookRelation convert = KsBeanUtil.convert(addBO, MetaBookRelation.class);
        List<MetaBookRelationBook> convertBook = KsBeanUtil.convertList(metaBookRelationBookBo, MetaBookRelationBook.class);
        List<MetaBookRelationKey> convertKey = KsBeanUtil.convertList(metaBookRelationKeyAddBo, MetaBookRelationKey.class);
        int i = metaBookRelationMapper.deleteSelective(convert.getId());
        for (MetaBookRelationBook book : convertBook) {
            metaBookRelationBookMapper.deleteMetaBookRelationBook(book.getId());
        }
        for (MetaBookRelationKey key : convertKey) {
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

    @Override
    public List<MetaBookRelationAddBO> selectAll(MetaBookRelationDelBO addBO) {
        List<MetaBookRelation> metaBookRelation = metaBookRelationMapper.getMetaBookRelation(addBO.getBookId());
        List<MetaBookRelationAddBO> list = new ArrayList<>();
        for (MetaBookRelation temp : metaBookRelation) {
            List<MetaBookRelationBook> metaBookRelationBook = metaBookRelationBookMapper.getMetaBookRelationBook(temp.getId());
            List<MetaBookRelationKey> metaBookRelationKeys = metaBookRelationKeyMapper.getKeyById(temp.getId());
            MetaBookRelationAddBO convert = KsBeanUtil.convert(temp, MetaBookRelationAddBO.class);
            convert.setMetaBookRelationBook(KsBeanUtil.convert(metaBookRelationBook, MetaBookRelationBookAddBo.class));
            convert.setMetaBookRelationKey(KsBeanUtil.convert(metaBookRelationKeys, MetaBookRelationKeyAddBo.class));
            list.add(convert);
        }
        return list;
    }

    @Override
    public int updateAll(MetaBookRelationAddBO addBO) {
        List<MetaBookRelationKeyAddBo> metaBookRelationKeyAddBo = addBO.getMetaBookRelationKey();
        List<MetaBookRelationBookAddBo> metaBookRelationBookBo = addBO.getMetaBookRelationBook();
        MetaBookRelation convert = KsBeanUtil.convert(addBO, MetaBookRelation.class);
        List<MetaBookRelationBook> convertBook = KsBeanUtil.convertList(metaBookRelationBookBo, MetaBookRelationBook.class);
        List<MetaBookRelationKey> convertKey = KsBeanUtil.convertList(metaBookRelationKeyAddBo, MetaBookRelationKey.class);
        int i = metaBookRelationMapper.updateMetaBookRelation(convert);
        int id = convert.getId();
        for (MetaBookRelationBook book : convertBook) {
            metaBookRelationBookMapper.updateMetaBookRelationBook(book);
        }
        for (MetaBookRelationKey key : convertKey) {
            metaBookRelationKeyMapper.updateSelective(key);
        }
        return i;
    }

}
