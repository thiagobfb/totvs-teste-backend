package com.thiagobfb.totvstestebackend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldMessage implements Serializable {

	private static final long serialVersionUID = 174879296746231970L;
	
	private String fieldName;
	private String message;

}
