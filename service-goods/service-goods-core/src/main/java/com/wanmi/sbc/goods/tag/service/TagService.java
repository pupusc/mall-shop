package com.wanmi.sbc.goods.tag.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.tag.model.Tag;
import com.wanmi.sbc.goods.tag.model.TagRel;
import com.wanmi.sbc.goods.tag.repository.TagRelRepository;
import com.wanmi.sbc.goods.tag.repository.TagRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagRelRepository tagRelRepository;

    public void saveTag(Tag tag){
        tagRepository.save(tag);
    }

    public void saveTagRel(TagRel tagRel){
        tagRelRepository.save(tagRel);
    }

    public List<Tag> findTagByGoods(String goodsId){
        return tagRepository.findByGoods(goodsId);
    }

    /**
     * 为商品保存标签。没有时新增，有时直接建立关联关系
     * @param goodsId
     * @param tagStr
     */
    public void saveAndUpdateTagForGoods(String goodsId, String tagStr){
        tagRepository.deleteTagsForGoods(goodsId);
        List<TagRel> tagRels = new ArrayList<>();
        if (StringUtils.isNotEmpty(tagStr)) {
            String[] tags = tagStr.split(",");
            LocalDateTime now = LocalDateTime.now();
            for (String tag : tags) {
                TagRel tagRel = new TagRel();
                tagRel.setCreateTime(now);
                tagRel.setUpdateTime(now);
                tagRel.setGoodsId(goodsId);
                tagRel.setDelFlag(DeleteFlag.NO);
                tagRel.setCategory(1);
                if(tag.startsWith("&")){
                    Tag tagDb = new Tag();
                    tagDb.setCreateTime(now);
                    tagDb.setUpdateTime(now);
                    tagDb.setDelFlag(DeleteFlag.NO);
                    tagDb.setTagName(tag.substring(1));
                    tagRepository.save(tagDb);
                    tagRel.setTagId(tagDb.getId());
                }else{
                    tagRel.setTagId(Long.parseLong(tag));
                }
                tagRels.add(tagRel);
            }
        }
        tagRelRepository.saveAll(tagRels);
    }
}
