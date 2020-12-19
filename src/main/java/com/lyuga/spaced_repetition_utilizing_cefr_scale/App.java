package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		Deck deck = new Deck("english_vocabulary");
		deck.initializeIfNeeded();

		Scanner sc = new Scanner(System.in);
		loop: while (true) {
			int selection;
			do {
				System.out.println("[1] 復習を開始");
				System.out.println("[2] 新しいカードを追加");
				System.out.println("[3] カードを削除");
				System.out.println("[4] 終了");
				System.out.print("オプション [1-4] を選択: ");
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
