package com.wanmi.sbc.goods.booklistgoodspublish.repository;


import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookListGoodsPublishRepository extends JpaRepository<BookListGoodsPublishDTO, Integer>, JpaSpecificationExecutor<BookListGoodsPublishDTO> {
}
