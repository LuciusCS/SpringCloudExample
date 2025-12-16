package com.example.demo.repository;
import com.example.demo.bean.dto.OrderFlatDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
public interface  OrderQueryRepository  extends Repository<Object, Long> {

    @Query("""
        select new com.example.demo.bean.dto.OrderFlatDTO(
            o.orderNo,
            o.orderStatus,
            o.payStatus,
            o.payAmount,
            o.orderTime,

            i.productId,
            i.productTitle,
            i.buyMode,
            i.subtotalAmount,

            c.artistWorkId,
            c.workName,
            c.artistWorkVersionId,
            c.previewUrl,
            c.contentType,
            c.price
        )
        from OrderPO o
        join o.items i
        join i.contents c
        where o.orderNo = :orderNo
        order by i.id, c.id
    """)
    List<OrderFlatDTO> findOrderDetail(String orderNo);
}