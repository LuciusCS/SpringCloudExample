package com.example.seckillserver.service;


import com.example.seckillserver.api.dto.SeckillOrderDTO;
import com.example.seckillserver.dao.SeckillOrderDao;
import com.example.seckillserver.dao.po.SeckillOrderPO;
import com.example.seckillserver.page.DataAdapter;
import com.example.seckillserver.page.PageOut;
import com.example.seckillserver.page.PageReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;



@Configuration
@Slf4j
@Service
public class SeckillOrderServiceImpl
{

    @Resource
    SeckillOrderDao seckillOrderDao;


    @Autowired
    RedisLockService redisLockService;


    public PageOut<SeckillOrderDTO> findOrderByUserID(Long userId, PageReq pageReq)
    {

        PageRequest jpaPage = PageRequest.of(pageReq.getJpaPage(), pageReq.getPageSize());


        /**
         * 创建条件对象
         */
        SeckillOrderPO checkOrder =
                SeckillOrderPO.builder().userId(userId).build();

        Page<SeckillOrderPO> page = seckillOrderDao.findAll(Example.of(checkOrder), jpaPage);

        PageOut<SeckillOrderDTO> pageData = DataAdapter.adapterPage(page, SeckillOrderDTO.class);


        return pageData;
    }
}
