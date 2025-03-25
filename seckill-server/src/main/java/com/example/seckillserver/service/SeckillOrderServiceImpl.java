package com.crazymaker.springcloud.seckill.service.impl;

import com.crazymaker.springcloud.common.page.DataAdapter;
import com.crazymaker.springcloud.common.page.PageOut;
import com.crazymaker.springcloud.common.page.PageReq;
import com.crazymaker.springcloud.seckill.api.dto.SeckillOrderDTO;
import com.crazymaker.springcloud.seckill.dao.SeckillOrderDao;
import com.crazymaker.springcloud.seckill.dao.po.SeckillOrderPO;
import com.crazymaker.springcloud.standard.lock.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
