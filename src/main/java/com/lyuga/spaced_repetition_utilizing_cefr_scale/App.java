package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		Deck deck = new Deck("english_vocabulary");

		loop: while (true) {
			int selection;
			do {
				System.out.println("[1] Start reviewing cards");
				System.out.println("[2] Add a new card");
				System.out.println("[3] Delete a card");
				System.out.println("[4] Quit");
				System.out.print("Enter option [1-4]: ");
				Scanner sc = new Scanner(System.in);
				selection = Integer.parseInt(sc.nextLine());
			} while (selection < 0 || selection > 4);

			switch (selection) {
			case 1:
				deck.startReview();
				break;
			case 2:
				deck.addCard();
				break;
			case 3:
				deck.deleteCard();
				break;
			case 4:
				break loop;
			}
		}
	}
}
