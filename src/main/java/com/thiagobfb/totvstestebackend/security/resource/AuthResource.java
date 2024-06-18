package com.thiagobfb.totvstestebackend.security.resource;

import com.thiagobfb.totvstestebackend.security.dto.UserSS;
import com.thiagobfb.totvstestebackend.security.service.UserService;
import com.thiagobfb.totvstestebackend.security.utils.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@Tag( name = "Autenticação" , description = "Endpoints responsáveis por resetar o token" )
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	private final JWTUtil jwtUtil;

	@Operation( description = "Serviço que atualiza o token de autorização" )
	@ApiResponses( value = {
			@ApiResponse( responseCode = "204" , description = "Retorna 204 quando a solicitação foi efetuada com sucesso." ),
			@ApiResponse( responseCode = "500" , description = "Retorna 500 quando ocorrer algum erro de negócio." ) } )
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}

}
