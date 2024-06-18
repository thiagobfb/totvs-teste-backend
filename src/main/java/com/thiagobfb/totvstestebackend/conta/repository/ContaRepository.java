package com.thiagobfb.totvstestebackend.conta.repository;

import com.thiagobfb.totvstestebackend.conta.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>, JpaSpecificationExecutor<Conta> {

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.situacao = 'paga' AND c.dataPagamento BETWEEN :startDate AND :endDate")
    BigDecimal findTotalPagoBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
