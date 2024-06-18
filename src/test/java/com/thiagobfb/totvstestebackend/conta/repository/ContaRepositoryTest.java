package com.thiagobfb.totvstestebackend.conta.repository;

import com.thiagobfb.totvstestebackend.conta.model.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContaRepositoryTest {

    @Autowired
    private ContaRepository contaRepository;

    @BeforeEach
    public void setUp() {
        Conta conta1 = new Conta();
        conta1.setDataVencimento(LocalDate.of(2023, 1, 1));
        conta1.setDataPagamento(LocalDate.of(2023, 1, 1));
        conta1.setDescricao("Conta de Luz");
        conta1.setSituacao("paga");
        conta1.setValor(BigDecimal.valueOf(100));

        Conta conta2 = new Conta();
        conta2.setDataVencimento(LocalDate.of(2023, 1, 1));
        conta2.setDataPagamento(LocalDate.of(2023, 1, 2));
        conta2.setDescricao("Conta de Água");
        conta2.setSituacao("paga");
        conta2.setValor(BigDecimal.valueOf(50));

        Conta conta3 = new Conta();
        conta3.setDataVencimento(LocalDate.of(2023, 2, 1));
        conta3.setDescricao("Internet");
        conta3.setSituacao("a receber");
        conta3.setValor(BigDecimal.valueOf(200));

        contaRepository.save(conta1);
        contaRepository.save(conta2);
        contaRepository.save(conta3);
    }

    @Test
    public void testFindAllByDataVencimentoAndDescricao() {
        Specification<Conta> spec = Specification
                .where(ContaSpecification.hasDataVencimento(LocalDate.of(2023, 1, 1)))
                .and(ContaSpecification.hasDescricao("Conta de Luz"));

        Page<Conta> result = contaRepository.findAll(spec, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDescricao()).isEqualTo("Conta de Luz");
    }

    @Test
    public void testFindTotalPagoBetweenDates() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 2);

        BigDecimal total = contaRepository.findTotalPagoBetweenDates(startDate, endDate);
        assertEquals(0, total.compareTo(new BigDecimal("150.00")));
    }
}
