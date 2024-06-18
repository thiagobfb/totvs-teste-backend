package com.thiagobfb.totvstestebackend.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredenciaisDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -5351703617775535342L;
	
	private String username;
	private String password;


}
