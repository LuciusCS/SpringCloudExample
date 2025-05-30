package com.example.seckillserver.dao;


import com.example.seckillserver.dao.po.SeckillSegmentStockPO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**

 */
@Repository
public interface SeckillSegmentStockDao extends
        JpaRepository<SeckillSegmentStockPO, Long>, JpaSpecificationExecutor<SeckillSegmentStockPO>
{

    @Transactional
    @Modifying
    @Query("update SeckillSegmentStockPO  s set s.stockCount = s.stockCount-1  where s.skuId = :skuId and s.segmentIndex = :segmentIndex" )
    int decreaseStock(@Param("skuId") Long skuId, @Param("segmentIndex") Integer segmentIndex);


    @Transactional
    @Modifying
    @Query("delete  from  SeckillSegmentStockPO s where s.skuId = :skuId" )
    int deleteStockBySku(@Param("skuId") Long skuId);

    @Query("select sum(s.stockCount) from SeckillSegmentStockPO s where s.skuId = :skuId " )
    int sumStockCountById(@Param("skuId") Long skuId);


}
