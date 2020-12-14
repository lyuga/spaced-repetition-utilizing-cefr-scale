package com.lyuga.spaced_repetition_utilizing_cefr_scale;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class SQLiteDatabase {
	private String fileName;
	private Connection conn;

	public SQLiteDatabase(String fileName) {
		this.fileName = fileName;
	}

	public void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + fileName + ".db");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasTable(String tableName) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'");
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void initialize(String cefrLevel) throws SQLException {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE user_settings(cefrLevel TEXT);");
			stmt.execute("CREATE TABLE flashcards(" + "id INTEGER PRIMARY KEY," + "word TEXT," + "cefrLevel TEXT,"
					+ "translation TEXT," + "repetitions INTEGER," + "easinessFactor REAL," + "interval INTEGER,"
					+ "nextDueDate INTEGER)");
			PreparedStatement ps = conn.prepareStatement("INSERT INTO user_settings VALUES(?);");
			ps.setString(1, cefrLevel.toUpperCase());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean wordExists(String word) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT EXISTS(SELECT 1 FROM flashcards WHERE word='" + word + "');");
			if (rs != null && rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void registerFlashcard(Flashcard card) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO flashcards VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
			ps.setString(2, card.getWord());
			ps.setString(3, card.getCefrLevel());
			ps.setString(4, card.getTranslation());
			ps.setInt(5, card.getRepetitions());
			ps.setFloat(6, card.getEasinessFactor());
			ps.setInt(7, card.getInterval());
			ps.setLong(8, ZonedDateTime.of(card.getNextDueDate(), ZoneId.systemDefault()).toInstant().toEpochMilli());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteFlashcard(String word) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("DELETE FROM flashcards WHERE word = '" + word + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateFlashcard(Flashcard card) {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE flashcards SET " + "repetitions = ?,"
					+ "easinessFactor = ?," + "interval = ?," + "nextDueDate = ? " + "WHERE id = ?");
			ps.setInt(1, card.getRepetitions());
			ps.setFloat(2, card.getEasinessFactor());
			ps.setInt(3, card.getInterval());
			ps.setLong(4, ZonedDateTime.of(card.getNextDueDate(), ZoneId.systemDefault()).toInstant().toEpochMilli());
			ps.setInt(5, card.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Flashcard retrieveFlashcard(String word) {
		Flashcard card = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT 1 FROM flashcards WHERE word='" + word + "'");
			if (rs != null && rs.next()) {
				card = constructFlashcardFromResultset(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return card;
	}

	public ArrayList<Flashcard> retrieveFlashcardsToReview() {
		ArrayList<Flashcard> flashcards = new ArrayList<Flashcard>();
		try {
			long nowInEpochMilli = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant()
					.toEpochMilli();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM flashcards WHERE nextDueDate <=" + nowInEpochMilli);
			while (rs.next()) {
				flashcards.add(constructFlashcardFromResultset(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flashcards;
	}

	private Flashcard constructFlashcardFromResultset(ResultSet rs) throws SQLException {
		Flashcard card = new Flashcard();
		card.setId(rs.getInt("id"));
		card.setWord(rs.getString("word"));
		card.setCefrLevel(rs.getString("cefrLevel"));
		card.setTranslation(rs.getString("translation"));
		card.setRepetitions(rs.getInt("repetitions"));
		card.setEasinessFactor(rs.getFloat("easinessFactor"));
		card.setInterval(rs.getInt("interval"));
		card.setNextDueDate(
				LocalDateTime.ofInstant(Instant.ofEpochMilli(rs.getLong("nextDueDate")), ZoneId.systemDefault()));
		return card;
	}
}