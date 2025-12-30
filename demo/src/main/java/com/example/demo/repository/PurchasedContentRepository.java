package com.example.demo.repository;

import com.example.demo.bean.dto.PurchasedContentFlatDTO;
import com.example.demo.bean.po.OrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchasedContentRepository extends JpaRepository<OrderPO, Long> {

    @Query("""
                select new com.example.demo.bean.dto.PurchasedContentFlatDTO(
                    i.productId,
                    i.productTitle,

                    c.artistWorkId,
                    c.workName,

                    c.artistWorkVersionId,
                    c.previewUrl,
                    c.originalUrl,

                    i.productType,

                    o.payTime
                )
                from OrderContentPO c, OrderItemPO i, OrderPO o
                where c.orderItem.id = i.id
                  and i.orderId = o.id
                  and o.userId = :userId
                  and o.payStatus = 2
                  and o.orderStatus in (0, 1, 2)
                  and c.contentType = 'SALE'
                order by o.payTime desc
            """)
    List<PurchasedContentFlatDTO> findPurchasedContents(Long userId);
}
