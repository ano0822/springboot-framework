package com.codingapi.springboot.fast.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@AllArgsConstructor
public class JPAQuery {

    private final EntityManager entityManager;

    public List<?> listQuery(SQLBuilder builder) {
        return listQuery(builder.getClazz(),builder.getSQL(),builder.getParams());
    }

    public List<?> listQuery(Class<?> clazz, String sql, Object... params) {
        TypedQuery<?> query = entityManager.createQuery(sql, clazz);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return query.getResultList();
    }

    public Page<?> pageQuery(SQLBuilder builder,PageRequest pageRequest) {
        return pageQuery(builder.getClazz(), builder.getSQL(), builder.getCountSQL(),pageRequest,builder.getParams());
    }


    public Page<?> pageQuery(Class<?> clazz, String sql, PageRequest pageRequest, Object... params) {
        return pageQuery(clazz,sql,"select count(1) " + sql,pageRequest,params);
    }

    public Page<?> pageQuery(Class<?> clazz, String sql, String countSql, PageRequest pageRequest, Object... params) {
        TypedQuery<?> query = entityManager.createQuery(sql, clazz);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        query.setMaxResults(pageRequest.getPageSize());
        return new PageImpl<>(query.getResultList(), pageRequest, countQuery(countSql, params));
    }


    private long countQuery(String sql, Object... params) {
        TypedQuery<Long> query = entityManager.createQuery(sql, Long.class);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return query.getSingleResult();
    }

}
