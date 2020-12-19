package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Deck {
	public static String userCefrLevel;
	private static Scanner sc = new Scanner(System.in);
	private String deckName;

	public Deck(String deckName) {
		this.deckName = deckName;
	}

	// データベースにテーブル "user_settings" が存在しなければ初期化する
	public void initializeIfNeeded() {
		try {
			SQLiteDatabase db = new SQLiteDatabase(deckName);
			db.connect();
			if (!db.hasTable("user_settings")) {
				System.out.print("CEFR Levelを入力: ");
				String cefrLevel = sc.nextLine();
				db.initialize(cefrLevel);
			}
			userCefrLevel = db.retrieveUserCefrLevel();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addCard() {
		try {
			SQLiteDatabase db = new SQLiteDatabase(deckName);
			db.connect();
			System.out.print("追加する単語を入力: ");
			String word = sc.nextLine();
			Flashcard card = new Flashcard(word, LocalDateTime.now());
			db.registerFlashcard(card);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("カードがデッキに追加されました。");
	}

	public void deleteCard() {
		try {
			SQLiteDatabase db = new SQLiteDatabase(deckName);
			db.connect();
			System.out.print("削除する単語を入力: ");
			String word = sc.nextLine();
			if (db.wordExists(word)) {
				db.deleteFlashcard(word);
				System.out.println("カードがデッキから削除されました。");
			} else {
				System.out.println("指定された単語を含むカードが見つかりません。");
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
				System.out.println("\n復習: " + (i + 1) + " / " + size);
				System.out.println("[表面]\n" + card.getWord());
				System.out.print("エンターで裏面を表示");
				System.in.read();
				sc.nextLine();
				System.out.print("[裏面]\n" + card.getTranslation());

				int quality;
				do {
					System.out.print("Repetition quality を [0-5] で評価: ");
					quality = Integer.parseInt(sc.nextLine());
				} while (quality < 0 || quality > 5);

				new SpacedRepetition(card, quality, userCefrLevel).calculate();
				db.updateFlashcard(card);
			}

			db.close();
			System.out.println("\n本日復習するカードはありません。\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}