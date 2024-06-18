package com.thiagobfb.totvstestebackend.conta.service;

import com.thiagobfb.totvstestebackend.conta.model.Conta;
import com.thiagobfb.totvstestebackend.conta.repository.ContaRepository;
import com.thiagobfb.totvstestebackend.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    private List<Conta> contas;

    private Conta contaTest;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        Conta conta1 = new Conta();
        conta1.setDataVencimento(LocalDate.of(2024, 1, 1));
        conta1.setDescricao("Conta de Luz");
        conta1.setValor(BigDecimal.valueOf(100));

        Conta conta2 = new Conta();
        conta2.setDataVencimento(LocalDate.of(2024, 1, 1));
        conta2.setDescricao("Conta de Água");
        conta2.setValor(BigDecimal.valueOf(50));

        Conta conta3 = new Conta();
        conta3.setDataVencimento(LocalDate.of(2024, 2, 1));
        conta3.setDescricao("Internet");
        conta3.setValor(BigDecimal.valueOf(200));

        contas = Arrays.asList(conta1, conta2, conta3);

        contaTest = new Conta();
        contaTest.setId(1L);
        contaTest.setDataVencimento(LocalDate.of(2024, 1, 1));
        contaTest.setDescricao("IPTU");
        contaTest.setValor(BigDecimal.valueOf(300));
    }

    @Test
    public void testFindAllWithParameters() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate dataVencimento = LocalDate.of(2024, 1, 1);
        String descricao = "Conta";

        Page<Conta> expectedPage = new PageImpl<>(contas, pageable, contas.size());
        when(contaRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);
        Page<Conta> result = contaService.findAll(0, 10, dataVencimento, descricao);
        assertEquals(expectedPage, result);
    }

    @Test
    public void testFindAllWithoutParameters() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> expectedPage = new PageImpl<>(contas, pageable, contas.size());
        when(contaRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);
        Page<Conta> result = contaService.findAll(0, 10, null, null);
        assertEquals(expectedPage, result);
    }

    @Test
    public void testSaveContas() {
        Conta c1 = new Conta();
        c1.setDataVencimento(LocalDate.of(2024, 1, 1));
        c1.setDescricao("Cartão de Crédito");
        c1.setValor(BigDecimal.valueOf(300));
        when(contaRepository.save(any(Conta.class))).thenReturn(c1);
        Conta result = contaService.saveConta(c1);
        assertEquals(c1, result);
    }

    @Test
    public void testFindByIdWhenContaExistsThenReturnConta() {
        Long id = 1L;
        Conta expectedConta = new Conta();
        expectedConta.setId(id);
        expectedConta.setDataVencimento(LocalDate.of(2024, 1, 1));
        expectedConta.setDescricao("IPTU");
        expectedConta.setValor(BigDecimal.valueOf(300));
        when(contaRepository.findById(id)).thenReturn(Optional.of(expectedConta));
        Conta result = contaService.findById(id);
        assertEquals(expectedConta, result);
    }

    @Test
    public void testDeleteByIdWhenContaExists() {
        Conta c1 = new Conta();
        c1.setId(1L);
        c1.setDataVencimento(LocalDate.of(2024, 1, 1));
        c1.setDescricao("IPTU Atualizando");
        c1.setValor(BigDecimal.valueOf(400));
        c1.setSituacao("A pagar");
        when(contaRepository.findById(1L)).thenReturn(Optional.of(c1));
        contaService.deleteById(c1.getId());
        verify(contaRepository, times(1)).deleteById(c1.getId());
    }

    @Test
    public void testDeleteByIdWhenInextistentContaThenThrowsObjectNotFoundException() {
        Conta nonExistingConta = new Conta();
        nonExistingConta.setId(99L);
        nonExistingConta.setDataVencimento(LocalDate.of(2024, 1, 1));
        nonExistingConta.setDescricao("Inexistente");
        nonExistingConta.setValor(BigDecimal.valueOf(400));

        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> contaService.deleteById(nonExistingConta.getId()));
        verify(contaRepository, times(1)).findById(99L);
        verify(contaRepository, times(0)).deleteById(nonExistingConta.getId());
    }

    @Test
    public void testUpdateContasWhenValidContasThenReturnUpdatedContas() {
        Conta updatedConta = new Conta();
        updatedConta.setId(1L);
        updatedConta.setDataVencimento(LocalDate.of(2024, 1, 1));
        updatedConta.setDescricao("IPTU Atualizando");
        updatedConta.setValor(BigDecimal.valueOf(400));
        updatedConta.setSituacao("Pago");
        updatedConta.setDataPagamento(LocalDate.of(2024, 1, 2));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaTest));
        when(contaRepository.save(any(Conta.class))).thenReturn(updatedConta);

        Conta result = contaService.updateConta(1L, updatedConta);

        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, times(1)).save(updatedConta);
        assertEquals(updatedConta, result);
    }

    @Test
    public void testUpdateContasWhenNonExistingIdThenThrowObjectNotFoundException() {
        Conta nonExistingConta = new Conta();
        nonExistingConta.setId(99L);
        nonExistingConta.setDataVencimento(LocalDate.of(2024, 1, 1));
        nonExistingConta.setDescricao("Inexistente");
        nonExistingConta.setValor(BigDecimal.valueOf(400));

        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> contaService.updateConta(99L, nonExistingConta));
        verify(contaRepository, times(1)).findById(99L);
        verify(contaRepository, times(0)).save(any(Conta.class));
    }

    @Test
    public void testUpdateContasWhenNullContasThenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> contaService.updateConta(86L,null));
        verify(contaRepository, times(0)).findById(anyLong());
        verify(contaRepository, times(0)).save(any(Conta.class));
    }

    @Test
    public void testImportContasWhenCSVIsValid() {
        String csvContent = "dataVencimento,dataPagamento,valor,descricao,situacao\n" +
                "2024-01-01,2024-01-02,100.00,Conta de Luz,Pago\n" +
                "2024-01-01,2024-01-02,50.00,Conta de Água,Pago\n";
        Conta conta1 = new Conta();
        conta1.setDataVencimento(LocalDate.of(2024, 1, 1));
        conta1.setDescricao("Conta de Luz");
        conta1.setValor(BigDecimal.valueOf(100));
        conta1.setSituacao("Pago");

        Conta conta2 = new Conta();
        conta2.setDataVencimento(LocalDate.of(2024, 1, 1));
        conta2.setDescricao("Conta de Água");
        conta2.setValor(BigDecimal.valueOf(50));
        conta2.setSituacao("Pago");

        when(contaRepository.saveAll(anyList())).thenReturn(Arrays.asList(conta1, conta2));
        MultipartFile file = new MockMultipartFile("file", "contas.csv", "text/csv", csvContent.getBytes());
        List<Conta>contaList = contaService.importContas(file);
        assertEquals(2, contaList.size());
    }

    @Test
    public void testImportContasWhenCSVIsInvalid() {
        String csvContent = "dataPagamento,valor,descricao,situacao\n" +
                "2024-01-02,100.00,Conta de Luz,Pago\n" +
                "2024-01-02,50.00,Conta de Água,Pago\n";
        MultipartFile file = new MockMultipartFile("file", "contas.csv", "text/csv", csvContent.getBytes());
        Exception exception = assertThrows(RuntimeException.class, () -> contaService.importContas(file));
        assertTrue(exception.getMessage().contains("Coluna esperada não encontrada no CSV"));
        verify(contaRepository, times(0)).saveAll(any());
    }

    @Test
    public void testGetTotalPagoPeriodo() {
        Conta conta1 = new Conta();
        conta1.setDataVencimento(LocalDate.of(2024, 1, 1));
        conta1.setDataPagamento(LocalDate.of(2024, 1, 1));
        conta1.setDescricao("Conta de Luz");
        conta1.setValor(BigDecimal.valueOf(100));
        conta1.setSituacao("paga");

        Conta conta2 = new Conta();
        conta2.setDataVencimento(LocalDate.of(2024, 1, 1));
        conta2.setDataPagamento(LocalDate.of(2024, 1, 2));
        conta2.setDescricao("Conta de Água");
        conta2.setValor(BigDecimal.valueOf(50));
        conta2.setSituacao("paga");

        LocalDate startDate = conta1.getDataPagamento();
        LocalDate endDate = conta2.getDataPagamento();

        when(contaRepository.findTotalPagoBetweenDates(startDate, endDate)).thenReturn(BigDecimal.valueOf(150));
        BigDecimal total = contaService.getTotalPagoPeriodo(startDate, endDate);
        assertEquals(BigDecimal.valueOf(150), total);
        verify(contaRepository, times(1)).findTotalPagoBetweenDates(any(LocalDate.class), any(LocalDate.class));
    }
}
