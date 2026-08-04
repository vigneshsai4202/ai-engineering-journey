package com.vignesh.ai;
import java.time.LocalDate;
import java.time.LocalTime;
import dev.langchain4j.agent.tool.Tool;

public class UtliltyTools {
	@Tool("Mutliply two numbers")
	public int Multiply(int a,int b) {
		return a*b;
	}
	@Tool("Get date")
	public String getCurrentDate() {
		return LocalDate.now().toString();
	}
	
	@Tool("Get todays time")
	public String getCurrentTime() {
		return LocalTime.now().toString();
	}
	@Tool("Convert text to uppercase")
	public String toUpperCase(String text) {
		return text.toUpperCase();
			
	}
	@Tool("Split textto words")
	public int wordcount(String text) {
		return text.trim().split("//s+").length;
	}
	

}
