package com.thiagobfb.totvstestebackend.conta.resource;

import com.thiagobfb.totvstestebackend.conta.model.Conta;
import com.thiagobfb.totvstestebackend.conta.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag( name = "Contas" , description = "Endpoints responsáveis pelas transações de contas" )
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/contas")
public class ContaResource {

    private final ContaService contaService;

    @Operation( description = "Serviço que retorna as contas, com ou sem filtro" )
    @ApiResponses( value = {
            @ApiResponse( responseCode = "200" , description = "Retorna 200 quando a solicitação foi efetuada com sucesso." ),
            @ApiResponse( responseCode = "500" , description = "Retorna 500 quando ocorrer algum erro de negócio." ),
            @ApiResponse( responseCode = "400" , description = "Retorna 400 quando ocorrer algum erro de validação do domínio." ) } )
    @GetMapping
    public ResponseEntity<Page<Conta>> findAll(
            @Parameter(description = "Número da página para paginação") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Tamanho da página para paginação") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Data de vencimento para filtro") @RequestParam(required = false) LocalDate dataVencimento,
            @Parameter(description = "Descrição para filtro") @RequestParam(required = false) String descricao) {
        return ResponseEntity.ok(contaService.findAll(page, size, dataVencimento, descricao));
    }

    @Operation(description = "Serviço que retorna uma conta pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna 200 quando a solicitação foi efetuada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Retorna 500 quando ocorrer algum erro de negócio."),
            @ApiResponse(responseCode = "400", description = "Retorna 400 quando ocorrer algum erro de validação do domínio."),
            @ApiResponse(responseCode = "404", description = "Retorna 404 quando a conta não for encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Conta> findById(
            @Parameter(description = "ID da conta") @PathVariable Long id) {
        Conta conta = contaService.findById(id);
        return (conta != null) ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    @Operation(description = "Serviço que salva uma nova conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna 200 quando a solicitação foi efetuada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Retorna 500 quando ocorrer algum erro de negócio."),
            @ApiResponse(responseCode = "400", description = "Retorna 400 quando ocorrer algum erro de validação do domínio.")
    })
    @PostMapping
    public ResponseEntity<Conta> saveConta(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Conta a ser salva") @RequestBody Conta conta) {
        Conta savedConta = contaService.saveConta(conta);
        return ResponseEntity.ok(savedConta);
    }

    @Operation(description = "Serviço que exclui uma conta pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Retorna 204 quando a solicitação foi efetuada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Retorna 500 quando ocorrer algum erro de negócio."),
            @ApiResponse(responseCode = "404", description = "Retorna 404 quando a conta não for encontrada.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID da conta") @PathVariable Long id) {
        contaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Serviço que atualiza uma conta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna 200 quando a solicitação foi efetuada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Retorna 500 quando ocorrer algum erro de negócio."),
            @ApiResponse(responseCode = "400", description = "Retorna 400 quando ocorrer algum erro de validação do domínio."),
            @ApiResponse(responseCode = "404", description = "Retorna 404 quando a conta não for encontrada.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Conta> updateConta(
            @Parameter(description = "ID da conta") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Conta a ser atualizada") @RequestBody Conta conta) {
        Conta updatedConta = contaService.updateConta(id, conta);
        return (updatedConta != null) ? ResponseEntity.ok(updatedConta) : ResponseEntity.notFound().build();
    }

    @Operation(description = "Serviço que pega um CSV com uma lista de contas e salva no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna 200 quando a solicitação foi efetuada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Retorna 500 quando ocorrer algum erro de negócio."),
            @ApiResponse(responseCode = "400", description = "Retorna 400 quando ocorrer algum erro de validação do domínio."),
    })
    @PostMapping("/upload/csv")
    public ResponseEntity<List<Conta>> uploadCSVContas(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Arquivo CSV das contas a serem adicionadas ao banco") @RequestBody MultipartFile csv) {
        return ResponseEntity.ok(contaService.importContas(csv));
    }
}
