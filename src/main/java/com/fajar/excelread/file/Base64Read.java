package com.fajar.excelread.file;

import static com.fajar.excelread.ReaderAbatasaQuestion.INPUT_DIR;
import static com.fajar.excelread.ReaderAbatasaQuestion.RAW_FILE;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.fajar.excelread.Question;
import com.fajar.excelread.ReaderAbatasaQuestion;

public class Base64Read {

	static String filePath = INPUT_DIR + RAW_FILE;
	static Workbook xwb;

	public static void main(String[] args) throws IOException, InvalidFormatException {
		String base64 = encodeFileToBase64(filePath);
		xwb = WorkbookFactory.create(base64ToInputStream(base64));
//		xwb = WorkbookFactory.create(new FileInputStream(rawFile(filePath)));
		Sheet sheet = xwb.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.rowIterator();
		List<Row> rowList = ReaderAbatasaQuestion.toList(rowIterator);
		LinkedList<Question> questions = new LinkedList<>();
		for (int i = 0; i < rowList.size(); i += 3) {
			Row row = rowList.get(i);
			Question q = ReaderAbatasaQuestion.getQuestion(i, row, rowList);
			questions.add(q);
		}

	}

	private static File rawFile(String fileName) {

		return new File(fileName);
	}

	private static String encodeFileToBase64(String fileName) throws IOException {
		File file = rawFile(fileName);
		byte[] encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file));
		String base64 = new String(encoded, StandardCharsets.US_ASCII);
		System.out.println("Base 64: "+base64);
		return base64;
	}

	private static ByteArrayInputStream base64ToInputStream(String base64) throws UnsupportedEncodingException {
		byte[] decodedString =  Base64.getDecoder().decode(base64.getBytes("UTF-8"));
        return new ByteArrayInputStream(decodedString);
	}
	
	private static ByteArrayInputStream encodeFileToBinary(String fileName) throws IOException {

		File file = rawFile(fileName);
		String content = FileUtils.readFileToString(file);
//	    System.out.println("CONTENT: "+content);
		byte[] encoded =  FileUtils.readFileToByteArray(file);
		return new ByteArrayInputStream(encoded);
	}
}
