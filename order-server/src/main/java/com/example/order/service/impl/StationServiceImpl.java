package com.example.order.service.impl;

import com.example.order.bean.dto.StationDTO;
import com.example.order.bean.po.mysql.criteria.AreaPO;
import com.example.order.bean.po.mysql.criteria.OperatorPO;
import com.example.order.bean.po.mysql.criteria.StationPO;
import com.example.order.service.StationService;
//import jakarta.persistence.Cacheable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @CacheConfig(cacheNames = "stationCache")   ///  类里默认缓存名，在这里使用 CacheConfig 就不需要在每一个注解里写value 进行命名了
 *  这种方法不推荐，会让所有类型的数据都缓存到命名空间 stationCache 中，会造成相互清理
 */
@Service
//@CacheConfig(cacheNames = "stationCache")
public class StationServiceImpl implements StationService {

    @PersistenceContext
    EntityManager entityManager;

    /**
     */
    @Override
    @Cacheable(
            value = "stationCacheList",                 // 缓存的名字
            key = "'stations:' + #nameFilter + ':' + #pageable.pageNumber + '-' + #pageable.pageSize", // 缓存key命名规范
            cacheManager = "cacheManagerWithTTL"  // 自定义cacheManager，支持过期时间
    )
    public Page<StationDTO> queryStations(String nameFilter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<StationDTO> query = cb.createQuery(StationDTO.class);
        Root<StationPO> stationRoot = query.from(StationPO.class);

        // Join
        Root<AreaPO> areaRoot = query.from(AreaPO.class);
        Root<OperatorPO> operatorRoot = query.from(OperatorPO.class);

        // Where condition: station.area_id = area.id and area.operator_id = operator.id
        Predicate join1 = cb.equal(stationRoot.get("areaId"), areaRoot.get("id"));
        Predicate join2 = cb.equal(areaRoot.get("operatorId"), operatorRoot.get("id"));

        // Filter condition
        Predicate filter = cb.conjunction();
        if (nameFilter != null && !nameFilter.isEmpty()) {
            filter = cb.like(stationRoot.get("name"), "%" + nameFilter + "%");
        }

        query.select(cb.construct(
                StationDTO.class,
                stationRoot.get("id"),
                stationRoot.get("name"),
                areaRoot.get("name"),
                operatorRoot.get("name")
        )).where(cb.and(join1, join2, filter));

        // 排序
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                Path<?> path = switch (order.getProperty()) {
                    case "stationId" -> stationRoot.get("id");
                    case "stationName" -> stationRoot.get("name");
                    case "areaName" -> areaRoot.get("name");
                    case "operatorName" -> operatorRoot.get("name");
                    default -> stationRoot.get("id");
                };
                orders.add(order.isAscending() ? cb.asc(path) : cb.desc(path));
            }
            query.orderBy(orders);
        }

        // 查询内容
        TypedQuery<StationDTO> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<StationDTO> content = typedQuery.getResultList();

        // 查询总数
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<StationPO> countRoot = countQuery.from(StationPO.class);
        Root<AreaPO> countAreaRoot = countQuery.from(AreaPO.class);
        Root<OperatorPO> countOperatorRoot = countQuery.from(OperatorPO.class);

        countQuery.select(cb.count(countRoot)).where(
                cb.and(
                        cb.equal(countRoot.get("areaId"), countAreaRoot.get("id")),
                        cb.equal(countAreaRoot.get("operatorId"), countOperatorRoot.get("id")),
                        filter
                )
        );

        Long total = entityManager.createQuery(countQuery).getSingleResult();


        return new PageImpl<>(content, pageable, total);
    }

    @Cacheable(value =  "stationCacheById", key = "'station:' + #id")
    public StationDTO getById(Long id) {
        // 查询单条数据
        return  new StationDTO();
    }

    @CachePut(value =  "stationCacheById", key = "'station:' + #station.id")
//    @CacheEvict(value = "stationCacheList", key = "'stations:' + '*'")  // 不可以使用这种方式，因为无效
    @CacheEvict(value = "stationCacheList", allEntries = true)         ///  不会清除命名为 stationCacheById 的缓存
    @Transactional
    public StationDTO update(StationDTO station) {
        // 更新数据库
        return station;
    }

    @CacheEvict(allEntries = true)  // 删除所有缓存，适合批量更新、删除
    public void refreshCache() {
    }
}
