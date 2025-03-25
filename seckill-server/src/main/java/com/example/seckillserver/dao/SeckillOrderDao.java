package com.example.seckillserver.dao;


import com.example.seckillserver.dao.po.SeckillOrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface SeckillOrderDao extends JpaRepository<SeckillOrderPO, Long>, JpaSpecificationExecutor<SeckillOrderPO>
{


}
