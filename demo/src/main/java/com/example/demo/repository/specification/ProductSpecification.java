package com.example.demo.repository.specification;

import com.example.demo.bean.form.ProductQueryForm;
import com.example.demo.bean.po.ProductPO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<ProductPO> build(ProductQueryForm form) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (form.getArtistId() != null) {
                predicates.add(
                        cb.equal(root.get("artistId"), form.getArtistId()));
            }

            if (StringUtils.hasText(form.getThemeColor())) {
                predicates.add(
                        cb.equal(root.get("themeColor"), form.getThemeColor()));
            }

            if (StringUtils.hasText(form.getTitle())) {
                predicates.add(
                        cb.like(root.get("title"), "%" + form.getTitle() + "%"));
            }

            if (form.getType() != null) {
                predicates.add(
                        cb.equal(root.get("type"), form.getType()));
            }

            // ⭐ tags：按逗号拆分，OR 关系
            if (StringUtils.hasText(form.getTags())) {
                String[] tagArr = form.getTags().split(",");

                List<Predicate> tagPredicates = new ArrayList<>();
                for (String tag : tagArr) {
                    tagPredicates.add(
                            cb.like(root.get("tags"), "%" + tag.trim() + "%"));
                }
                predicates.add(cb.or(tagPredicates.toArray(new Predicate[0])));
            }

            if (predicates.isEmpty()) {
                return null;
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
