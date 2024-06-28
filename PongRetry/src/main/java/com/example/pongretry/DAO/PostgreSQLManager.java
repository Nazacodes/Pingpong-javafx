package com.example.pongretry.DAO;

import com.example.pongretry.controllers.Gamesettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLManager {
    // Singleton instance
    private static PostgreSQLManager instance;

    // JDBC connection parameters
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    // SQL queries
    private static final String UPDATE_SCORE_QUERY = "UPDATE games SET player1_score = ?, player2_score = ? WHERE game_name = ?";
    private static final String SELECT_SETTINGS_QUERY = "SELECT racket_size, ball_speed, ball_frequency_increase, amount_to_win FROM game_settings ORDER BY id DESC LIMIT 1";
    private static final String INSERT_SETTINGS_QUERY = "INSERT INTO game_settings (game_name, racket_size, ball_speed, ball_frequency_increase, amount_to_win) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_PLAYERS_QUERY = "SELECT name FROM players ORDER BY id";
    private static final String INSERT_PLAYER_QUERY = "INSERT INTO players (player_name, player_number) VALUES (?, ?)";
    private static final String INSERT_SCORE_QUERY = "INSERT INTO player_scores (player_number, score) VALUES (?, ?)";
    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS game_player_settings";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE game_player_settings ("
            + "id SERIAL PRIMARY KEY,"
            + "game_id INTEGER,"
            + "player1_name VARCHAR(255),"
            + "player2_name VARCHAR(255),"
            + "player1_score INTEGER,"
            + "player2_score INTEGER,"
            + "racket_size INTEGER,"
            + "ball_speed INTEGER,"
            + "ball_frequency_increase INTEGER,"
            + "amount_to_win INTEGER)";

    // Private constructor to prevent instantiation
    private PostgreSQLManager() {
        // Load the PostgreSQL JDBC driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to get the Singleton instance
    public static PostgreSQLManager getInstance() {
        if (instance == null) {
            synchronized (PostgreSQLManager.class) {
                if (instance == null) {
                    instance = new PostgreSQLManager();
                }
            }
        }
        return instance;
    }

    // Method to acquire a connection to the database
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    // Method to load game settings from the database
    public Gamesettings loadSettingsFromDatabase() throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SETTINGS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int racketSize = resultSet.getInt("racket_size");
                int ballSpeed = resultSet.getInt("ball_speed");
                int ballFrequencyIncrease = resultSet.getInt("ball_frequency_increase");
                int amountToWin = resultSet.getInt("amount_to_win");
                return new Gamesettings(racketSize, ballSpeed, ballFrequencyIncrease, amountToWin);
            }
        }
        return null;
    }
    public Gamebuilder loadGameInfoFromDatabase(String gameName) throws SQLException {
        Gamebuilder gameInfo = null;
        String query = "SELECT * FROM game_player_settings WHERE game_name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, gameName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String gameId = resultSet.getString("game_name");
                    String playerName1 = resultSet.getString("player1_name");
                    String playerName2 = resultSet.getString("player2_name");
                    int player1Score = resultSet.getInt("player1_score");
                    int player2Score = resultSet.getInt("player2_score");
                    int racketSize = resultSet.getInt("racket_size");
                    int ballSpeed = resultSet.getInt("ball_speed");
                    int ballFrequencyIncrease = resultSet.getInt("ball_frequency_increase");
                    int amountToWin = resultSet.getInt("amount_to_win");

                    gameInfo = new Gamebuilder(gameId, gameName, playerName1, playerName2, player1Score, player2Score, racketSize, ballSpeed, ballFrequencyIncrease, amountToWin);
                }
            }
        }
        return gameInfo;
    }


    // Method to save game settings to the database
    // Method to save game settings and name to the database
    public void saveSettingsToDatabase(String gameName, Gamesettings gameSettings) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SETTINGS_QUERY)) {
            statement.setString(1, gameName);
            statement.setInt(2, gameSettings.getRacketSize());
            statement.setInt(3, gameSettings.getBallSpeed());
            statement.setInt(4, gameSettings.getBallFrequencyIncrease());
            statement.setInt(5, gameSettings.getAmountToWin());
            statement.executeUpdate();
            System.out.println("Game settings saved to the database.");
        }
    }



    // Method to save player names to the database
    public void savePlayerNameToDatabase(String playerName, int playerNumber) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_PLAYER_QUERY)) {
            statement.setString(1, playerName);
            statement.setInt(2, playerNumber);
            statement.executeUpdate();
            System.out.println("Player name saved to the database.");
        }
    }


    public void savePlayerScoreToDatabase(int playerNumber, int score) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SCORE_QUERY)) {
            statement.setInt(1, playerNumber);
            statement.setInt(2, score);
            statement.executeUpdate();
            System.out.println("Player score saved to the database.");
        }
    }

    // Method to save both player names and scores to the database
    public void saveBothPlayerNamesAndScoresToDatabase(String playerName1, int score1, String playerName2, int score2) throws SQLException {
        savePlayerNameToDatabase(playerName1, 1); // Save player 1 name
        savePlayerNameToDatabase(playerName2, 2); // Save player 2 name
        savePlayerScoreToDatabase(1, score1); // Save player 1 score
        savePlayerScoreToDatabase(2, score2); // Save player 2 score
    }

    public void updateScoreInDatabase(String gameName, int player1Score, int player2Score) throws SQLException {
        // SQL query to update player scores in the game_player_settings table
        String sql = "UPDATE game_player_settings SET player1_score = ?, player2_score = ? WHERE game_name = ?";

        // Try-with-resources to automatically close the connection
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters in the prepared statement
            statement.setInt(1, player1Score);
            statement.setInt(2, player2Score);
            statement.setString(3, gameName);

            // Execute the update operation
            statement.executeUpdate();

            System.out.println("Player scores updated in the game_player_settings table.");
        }
    }




    public void saveGameInfoToDatabase(String gameName, String playerName1, String playerName2, int player1Score, int player2Score, Gamesettings gameSettings) throws SQLException {
        // Get the count of existing games
        int gameCount = getGameCountFromDatabase();

        // Increment the count by 1 for the new game
        int gameId = gameCount + 1;

        // Save player names, game name, and game ID to the game_player_settings table
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO game_player_settings (id, game_name, player1_name, player2_name, player1_score, player2_score, racket_size, ball_speed, ball_frequency_increase, amount_to_win) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, gameId);
            statement.setString(2, gameName);
            statement.setString(3, playerName1);
            statement.setString(4, playerName2);
            statement.setInt(5, player1Score);
            statement.setInt(6, player2Score);
            statement.setInt(7, gameSettings.getRacketSize());
            statement.setInt(8, gameSettings.getBallSpeed());
            statement.setInt(9, gameSettings.getBallFrequencyIncrease());
            statement.setInt(10, gameSettings.getAmountToWin());
            statement.executeUpdate();
            System.out.println("Game information saved to the game_player_settings table.");
        }
    }

    private int getGameCountFromDatabase() throws SQLException {
        int gameCount = 0;

        // SQL query to count the number of games in the database
        String sql = "SELECT COUNT(*) AS game_count FROM game_player_settings";

        // Try-with-resources to automatically close the connection
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Retrieve the game count from the result set
            if (resultSet.next()) {
                gameCount = resultSet.getInt("game_count");
            }
        }

        return gameCount;
    }



    public List<String> getAllGameNames() throws SQLException {
        List<String> gameNames = new ArrayList<>();

        // SQL query to select all game names from the game_player_settings table
        String sql = "SELECT DISTINCT game_name FROM game_player_settings";

        // Try-with-resources to automatically close the connection
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate over the result set and add each game name to the list
            while (resultSet.next()) {
                String gameName = resultSet.getString("game_name");
                gameNames.add(gameName);
            }
        }

        return gameNames;
    }
}
