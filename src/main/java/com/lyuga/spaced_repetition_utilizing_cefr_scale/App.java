package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.time.LocalDateTime;

public class App {
	public static void main(String[] args) {
		Flashcard card = new Flashcard("fine", LocalDateTime.now());
		System.out.println("cefrLevel: " + card.getCefrLevel());
		System.out.println("translation:\n" + card.getTranslation());
	}
}
