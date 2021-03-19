package com.fajar.excelread;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Choice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4629376928261901511L;
	private String statement;
	private String index;
	private boolean rightAnswer;
}
