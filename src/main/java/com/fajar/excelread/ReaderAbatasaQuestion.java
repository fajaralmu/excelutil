package com.fajar.excelread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReaderAbatasaQuestion {
	static final String INPUT_DIR = "E:\\Activity\\ABATASA\\";
	static final String RAW_FILE = "RAW_SOAL_SEMIFINAL.xlsx";
	static final String OUTPUT_FILE = "OUTPUT_SOAL_SEMIINAL.xls";
	private static Workbook inputWorkbook;
	private static XSSFWorkbook outputXwb ;
	private static LinkedList<Question> questions;

	public static void main(String[] args) throws InvalidFormatException, IOException {

		read();
		write();

	}

	private static void write() throws IOException {
		 
		outputXwb = new XSSFWorkbook();
		XSSFSheet sheet = outputXwb.createSheet();
		XSSFRow headerRow = sheet.createRow(0);
		//nomor	konten	opsi_a	opsi_b	opsi_c	opsi_d	jawaban	durasi

		createHeaderRow(headerRow, "nomor","konten","opsi_a","opsi_b","opsi_c","opsi_d","jawaban","durasi" );
		writeQuestions(sheet);
		outputXwb.write(new FileOutputStream(INPUT_DIR+OUTPUT_FILE));
	}

	private static void writeQuestions(XSSFSheet sheet) {
		for (int i = 0; i < questions.size(); i++) {
			XSSFRow row = sheet.createRow(i+1);
			writeQuestionItem(row, questions.get(i));
		}
	}

	private static void writeQuestionItem(XSSFRow row, Question q) {
		XSSFCell cellNum = row.createCell(0);
		cellNum.setCellValue(row.getRowNum());
		XSSFCell cellKonten = row.createCell(1);
		cellKonten.setCellValue(q.getStatement());
//		System.out.println(q.getChoices());
		XSSFCell cellOpsiA = row.createCell(2);
		cellOpsiA.setCellValue(q.getChoiceByIndex("a").getStatement());
		XSSFCell cellOpsiB = row.createCell(3);
		cellOpsiB.setCellValue(q.getChoiceByIndex("b").getStatement());
		XSSFCell cellOpsiC = row.createCell(4);
		cellOpsiC.setCellValue(
				q.getChoiceByIndex("c").getStatement()
				);
		XSSFCell cellOpsiD = row.createCell(5);
		cellOpsiD.setCellValue(q.getChoiceByIndex("d").getStatement());
		
		XSSFCell cellAnswer = row.createCell(6); 
		cellAnswer.setCellValue(q.getAnswer().toUpperCase());
		XSSFCell cellDuration = row.createCell(7);
		cellDuration.setCellValue(q.getDuration());
		
	}

	private static void createHeaderRow(XSSFRow row, String ... labels) {
		 for (int i = 0; i < labels.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(labels[i]);  
		}
		
	}

	private static void read() throws InvalidFormatException, IOException {
		inputWorkbook = WorkbookFactory.create(new File(INPUT_DIR + RAW_FILE));

		// Retrieving the number of sheets in the Workbook
		System.out.println("Workbook has " + inputWorkbook.getNumberOfSheets() + " Sheets : ");
		Sheet sheet = inputWorkbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.rowIterator();
		List<Row> rowList = toList(rowIterator);
		questions = new LinkedList<>();
		for (int i = 0; i < rowList.size(); i += 3) {
			Row row = rowList.get(i);
			Question q = getQuestion(i, row, rowList);
			questions.add(q);
		}

	}

	private static Question getQuestion(final int i, Row row, List<Row> rowList) {
		Cell cell = row.getCell(0);
		String statement = cell.getStringCellValue().split("\\.")[1];
		Row choiceRow = rowList.get(i + 1);

		Question q = Question.builder().number((i + 3) / 3).duration(20).statement(statement).build();
		System.out.println(q.getNumber() + " " + q.getStatement());
		List<Choice> choices = getChoices(choiceRow);
		q.setChoices(choices);
		q.determineRightAnswer();
		System.out.println("Answer: " + q.getAnswer());
		return q;
	}

	private static List<Choice> getChoices(Row choiceRow) {

		List<Choice> choices = new LinkedList<>();

		choices.add(getChoiceItem(choiceRow.getCell(0)));
		choices.add(getChoiceItem(choiceRow.getCell(1)));
		choices.add(getChoiceItem(choiceRow.getCell(2)));
		choices.add(getChoiceItem(choiceRow.getCell(3)));

		if (!choices.get(0).isRightAnswer() && !choices.get(1).isRightAnswer() && !choices.get(2).isRightAnswer()
				&& !choices.get(3).isRightAnswer()) {
			throw new IllegalArgumentException("Invalid Choices");
		}

		return choices;
	}

	private static Choice getChoiceItem(Cell cell) {
		boolean right = printChoice(cell);
		String[] statement = cell.getStringCellValue().split("\\.");
		Choice c = Choice.builder().index(statement[0].trim().toUpperCase()).rightAnswer(right).statement(statement[1].trim())
				.build();
		System.out.println(c.getIndex() + "." + c.getStatement() + (c.isRightAnswer() ? " ---------> CORRECT" : ""));
		return c;
	}

	private static boolean printChoice(Cell cell) {
		XSSFCellStyle style = (XSSFCellStyle) cell.getCellStyle();

		return style.getFont().getBold();
	}

	private static <T> List<T> toList(Iterator<T> it) {
		List<T> list = new LinkedList<T>();
		while (it.hasNext()) {
			list.add(it.next());
		}
		// TODO Auto-generated method stub
		return list;
	}

}
