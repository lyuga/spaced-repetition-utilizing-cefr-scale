package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Deck {
	private static Scanner sc = new Scanner(System.in);
	private String deckName;

	public Deck(String deckName) {
		this.deckName = deckName;
	}

	public void addCard() {
		try {
			SQLiteDatabase db = new SQLiteDatabase(deckName);
			db.connect();
			System.out.print("Enter a word: ");
			String word = sc.nextLine();
			Flashcard card = new Flashcard(word, LocalDateTime.now());
			db.registerFlashcard(card);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("The card has been added to the deck.");
	}

	public void deleteCard() {
		try {
			SQLiteDatabase db = new SQLiteDatabase(deckName);
			db.connect();
			System.out.println("Enter a word to delete: ");
			String word = sc.nextLine();
			if (db.wordExists(word)) {
				db.deleteFlashcard(word);
				System.out.println("The card has been deleted from the deck.");
			} else {
				System.out.println("There's no card containing a specified word.");
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startReview() {
		try {
			SQLiteDatabase db = new SQLiteDatabase(deckName);
			db.connect();

			ArrayList<Flashcard> flashcards = db.retrieveFlashcardsToReview();
			int size = flashcards.size();

			for (int i = 0; i < size; i++) {
				Flashcard card = flashcards.get(i);
				System.out.println("\nReview: " + (i + 1) + " of " + size);
				System.out.println("Front Side:\n" + card.getWord());
				System.out.print("Press enter to continue.");
				System.in.read();
				sc.nextLine();
				System.out.print("Back Side:\n" + card.getTranslation());

				int quality;
				do {
					System.out.print("Grade the repetition quality [0-5]: ");
					quality = Integer.parseInt(sc.nextLine());
				} while (quality < 0 || quality > 5);

				new SpacedRepetition(card, quality).calculate();
				db.updateFlashcard(card);
			}

			db.close();
			System.out.println("\nThere's no card to review today.\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}