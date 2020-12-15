package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class SpacedRepetition {
	private static final float WEIGHT_COEFFICIENT = 0.8f;
	private static final float EASINESS_FLOOR = 1.3f;
	private static final int QUALITY_THRESHOLD = 3;
	private Flashcard card;
	private int quality;
	private String userCefrLevel;
	private float updatedEasiness;
	private int updatedRepetitions;
	private int updatedInterval;

	public SpacedRepetition(Flashcard card, int quality, String userCefrLevel) {
		this.card = card;
		this.quality = quality;
		this.userCefrLevel = userCefrLevel;
	}

	// Spaced repetitionに必要な計算をすべて行う
	public void calculate() {
		if (isGreaterThanOrEqualTo(card.getCefrLevel())) {
			quality = calculateWeightedQuality();
		}
		card.setEasinessFactor(calculateEasinessFactor());
		card.setRepetitions(calculateRepetitions());
		card.setInterval(calculateInterval());
		card.setNextDueDate(calculateNextDueDate());
	}

	// ユーザのCEFRレベルが単語のレベル以上であれば、真を返却
	private boolean isGreaterThanOrEqualTo(String cefrLevel) {
		int result = userCefrLevel.compareToIgnoreCase(cefrLevel);
		if (result >= 0) {
			return true;
		} else {
			return false;
		}
	}

	private int calculateWeightedQuality() {
		return Math.round(quality * WEIGHT_COEFFICIENT);
	}

	private float calculateEasinessFactor() {
		float easiness = card.getEasinessFactor();
		// Easiness factorを再計算し、その値は必ず1.3以上にする
		updatedEasiness = (float) Math.max((easiness - 0.8 + 0.28 * quality - 0.02 * quality * quality),
				EASINESS_FLOOR);
		return updatedEasiness;
	}

	private int calculateRepetitions() {
		int repetitions = card.getRepetitions();
		// 回答されたqualityが3未満であった場合、repetitionsは1から再スタート
		return updatedRepetitions = quality >= QUALITY_THRESHOLD ? repetitions + 1 : 1;
	}

	private int calculateInterval() {
		int interval = card.getInterval();
		switch (updatedRepetitions) {
		case 0:
		case 1:
			updatedInterval = 1;
			break;
		case 2:
			updatedInterval = 6;
			break;
		default:
			updatedInterval = Math.round(interval * updatedEasiness);
		}
		return updatedInterval;
	}

	private LocalDateTime calculateNextDueDate() {
		int millisecondsInDay = 1000 * 60 * 60 * 24;
		long now = System.currentTimeMillis();
		long nextDueDate = now + millisecondsInDay * updatedInterval;
		// Unix epochから、システムのタイムゾーンにおけるLocalDateTimeへ変換を行う
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(nextDueDate), ZoneId.systemDefault());
	}
}
