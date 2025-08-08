package com.example.seckillserver.dao;


import com.example.seckillserver.dao.po.SeckillSkuPO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



/**

 */
@Repository
public interface SeckillSkuDao extends
        JpaRepository<SeckillSkuPO, Long>, JpaSpecificationExecutor<SeckillSkuPO>
{

    @Transactional
    @Modifying
    @Query("update SeckillSkuPO  g set g.stockCount = g.stockCount-1  where g.id = :id" )
    int decreaseStockCountById(@Param("id" ) Long id);
}
