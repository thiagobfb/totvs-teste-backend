package com.thiagobfb.totvstestebackend.conta.resource;

import com.thiagobfb.totvstestebackend.conta.model.Conta;
import com.thiagobfb.totvstestebackend.conta.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockito.Mockito.doNothing;

public class ContaResourceTest {

    @InjectMocks
    private ContaResource contaResource;

    @Mock
    private ContaService contaService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void testFindAllWhenCalledWithValidParametersThenReturnPageOfContas() {
        Page<Conta> contaPage = new PageImpl<>(Collections.singletonList(new Conta()));
        when(contaService.findAll(anyInt(), anyInt(), any(LocalDate.class), anyString())).thenReturn(contaPage);

        ResponseEntity<Page<Conta>> response = contaResource.findAll(0, 10, LocalDate.now(), "descricao");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    public void testFindByIdWhenCalledWithValidIdThenReturnConta() {
        Conta conta = new Conta();
        conta.setId(1L);
        when(contaService.findById(anyLong())).thenReturn(conta);

        ResponseEntity<Conta> response = contaResource.findById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testSaveContaWhenCalledWithValidContaThenReturnSavedConta() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDataVencimento(LocalDate.now());
        conta.setValor(BigDecimal.valueOf(100.00));
        when(contaService.saveConta(any(Conta.class))).thenReturn(conta);

        ResponseEntity<Conta> response = contaResource.saveConta(conta);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testDeleteByIdWhenCalledWithValidIdThenReturnNoContent() {
        doNothing().when(contaService).deleteById(anyLong());
        ResponseEntity<Void> response = contaResource.deleteById(1L);
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    public void testUpdateContaWhenCalledWithValidIdAndContaThenReturnUpdatedConta() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDataVencimento(LocalDate.now());
        conta.setValor(BigDecimal.valueOf(100.00));
        when(contaService.updateConta(anyLong(), any(Conta.class))).thenReturn(conta);

        ResponseEntity<Conta> response = contaResource.updateConta(1L, conta);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    public void testUploadCSVContasWhenCalledWithValidCSVThenReturnListOfContas() {
        String csvContent = "dataVencimento,dataPagamento,valor,descricao,situacao\n" +
                "2024-01-01,2024-01-02,100.00,Conta de Luz,Pago\n" +
                "2024-01-01,2024-01-02,50.00,Conta de Água,Pago\n";
        MultipartFile csvFile = new MockMultipartFile("file", "contas.csv", "text/csv", csvContent.getBytes());
        List<Conta> contas = Collections.singletonList(new Conta());
        when(contaService.importContas(any(MultipartFile.class))).thenReturn(contas);

        ResponseEntity<List<Conta>> response = contaResource.uploadCSVContas(csvFile);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}
