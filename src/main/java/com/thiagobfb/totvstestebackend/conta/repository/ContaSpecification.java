package com.thiagobfb.totvstestebackend.conta.repository;

import com.thiagobfb.totvstestebackend.conta.model.Conta;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ContaSpecification {

    public static Specification<Conta> hasDataVencimento(LocalDate dataVencimento) {
        return (root, query, criteriaBuilder) ->
                dataVencimento == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("dataVencimento"), dataVencimento);
    }

    public static Specification<Conta> hasDescricao(String descricao) {
        return (root, query, criteriaBuilder) ->
                descricao == null ? criteriaBuilder.conjunction() :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
    }
}
