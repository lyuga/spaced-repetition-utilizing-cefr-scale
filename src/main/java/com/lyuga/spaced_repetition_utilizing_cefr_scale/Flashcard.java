package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.time.LocalDateTime;

public class Flashcard {
	private int id;
	private String word;
	private String cefrLevel;
	private String translation;
	private int repetitions = 0;
	private float easinessFactor = 2.5f;
	private int interval = 1;
	private LocalDateTime nextDueDate;

	public Flashcard(String word, LocalDateTime creationDate) {
		this.setWord(word);
		this.setNextDueDate(creationDate.plusDays(1));
		CambridgeDictionaryParser cdParser = new CambridgeDictionaryParser();
		LongmanDictionaryParser ldParser = new LongmanDictionaryParser();
		setCefrLevel(cdParser.fetchTranslation(word));
		setTranslation(ldParser.fetchTranslation(word));
	}

	public Flashcard() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getCefrLevel() {
		return cefrLevel;
	}

	public void setCefrLevel(String cefrLevel) {
		this.cefrLevel = cefrLevel;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	public float getEasinessFactor() {
		return easinessFactor;
	}

	public void setEasinessFactor(float easinessFactor) {
		this.easinessFactor = easinessFactor;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public LocalDateTime getNextDueDate() {
		return nextDueDate;
	}

	public void setNextDueDate(LocalDateTime nextDueDate) {
		this.nextDueDate = nextDueDate;
	}
}