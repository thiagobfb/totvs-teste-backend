package com.thiagobfb.totvstestebackend.conta.service;

import com.thiagobfb.totvstestebackend.conta.model.Conta;
import com.thiagobfb.totvstestebackend.conta.repository.ContaRepository;
import com.thiagobfb.totvstestebackend.conta.repository.ContaSpecification;
import com.thiagobfb.totvstestebackend.exceptions.DataIntegrityException;
import com.thiagobfb.totvstestebackend.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ContaService {

    private final ContaRepository contaRepository;

    public Page<Conta> findAll(Integer page, Integer size, LocalDate dataVencimento, String descricao) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Specification<Conta> contasSpecification = Specification
                .where(ContaSpecification.hasDataVencimento(dataVencimento))
                .and(ContaSpecification.hasDescricao(descricao));
        return contaRepository.findAll(contasSpecification, pageRequest);
    }

    public Conta saveConta(Conta conta) {
        return contaRepository.save(conta);
    }

    public Conta findById(Long id) {
        Optional<Conta>opt =  contaRepository.findById(id);
        return opt.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrato! Id: " + id + ", Tipo: " + Conta.class.getName()));
    }

    public void deleteById(Long id) {
        this.findById(id);
        try {
            contaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir a conta");
        }
    }

    public Conta updateConta(Long id, Conta conta) {
        Conta oldConta = this.findById(id);
        oldConta.setDescricao(conta.getDescricao());

        oldConta.setDataVencimento(conta.getDataVencimento());
        oldConta.setValor(conta.getValor());
        oldConta.setSituacao(conta.getSituacao());
        oldConta.setDataPagamento(conta.getDataPagamento());
        return contaRepository.save(conta);
    }

    public List<Conta> importContas(MultipartFile file) {
        List<Conta> contas = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
               Conta conta = new Conta();
                conta.setSituacao(csvRecord.get("situacao"));
                conta.setDataVencimento(LocalDate.parse(csvRecord.get("dataVencimento")));
                conta.setDescricao(csvRecord.get("descricao"));
                conta.setValor(new BigDecimal(csvRecord.get("valor")));
                conta.setDataPagamento(csvRecord.get("dataPagamento") != null ? LocalDate.parse(csvRecord.get("dataPagamento")) : null);
                contas.add(conta);
            }

            return contaRepository.saveAll(contas);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Coluna esperada não encontrada no CSV", e);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BigDecimal getTotalPagoPeriodo(LocalDate startDate, LocalDate endDate) {
        return contaRepository.findTotalPagoBetweenDates(startDate, endDate);
    }
}
