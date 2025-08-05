package com.example.order.service.impl;

import com.example.order.bean.dto.StationDTO;
import com.example.order.bean.po.mysql.criteria.AreaPO;
import com.example.order.bean.po.mysql.criteria.OperatorPO;
import com.example.order.bean.po.mysql.criteria.StationPO;
import com.example.order.service.StationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StationServiceImpl implements StationService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
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
}
