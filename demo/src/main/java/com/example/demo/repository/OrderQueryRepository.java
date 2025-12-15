package com.example.demo.repository;
import com.example.demo.bean.dto.OrderFlatDTO;
import com.example.demo.bean.po.OrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
public interface  OrderQueryRepository  extends JpaRepository<OrderPO, Long> {

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