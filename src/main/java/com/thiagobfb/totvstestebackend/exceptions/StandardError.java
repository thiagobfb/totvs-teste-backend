package com.thiagobfb.totvstestebackend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardError implements Serializable {

	private static final long serialVersionUID = 6391728226502304044L;
	
	private Long timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
}

