package com.fajar.excelread;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1656572418088562100L;
	private String statement;
	private int number;
	private String answer;
	private int duration;
	@Default
	private List<Choice> choices = new LinkedList<>();
	
	public void addChoice(Choice c) {
		choices.add(c);
	}

	public void determineRightAnswer() {
		 for (Choice choice : choices) {
			if (choice.isRightAnswer()) {
				setAnswer(choice.getIndex());
			}
		}
	}
	
	public Choice getChoiceByIndex (String index) {
		for (Choice choice : choices) {
			if (choice.getIndex().toUpperCase().equals(index.toUpperCase())) {
				return choice;
			}
		}
		return null;
	}

}
