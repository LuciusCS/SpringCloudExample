package com.example.demo.repository;

import com.example.demo.bean.dto.PurchasedContentFlatDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchasedContentRepository {

    @Query("""
        select new com.example.demo.bean.dto.PurchasedContentFlatDTO(
            i.productId,
            i.productTitle,

            c.artistWorkId,
            c.workName,

            c.artistWorkVersionId,
            c.previewUrl,
            c.originalUrl,

            o.payTime
        )
        from OrderPO o
        join o.items i
        join i.contents c
        where o.userId = :userId
          and o.payStatus = 2
          and o.orderStatus in (1, 2)
          and c.contentType = 'SALE'
        order by o.payTime desc
    """)
    List<PurchasedContentFlatDTO> findPurchasedContents(Long userId);
}
