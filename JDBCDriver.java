import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JComboBox;

final class JDBCDriver {
	
	protected final String url = "jdbc:mysql://localhost:3306/";
	protected final String schema = "csci370project4jdbc";
	protected final String user = "root";
	protected final String password = "rootpassword";
	protected Connection connection;
	protected Statement statement;
	protected ResultSet rs;
	protected String sql;
	
	public JDBCDriver() {
		try {
			connection = DriverManager.getConnection(url+schema, user, password);
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
	public void testQuery() {
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery("select * from cinemas");
			while (rs.next()) {
				System.out.println(rs.getString("title") + "\t(" + rs.getString("x") + ", " + rs.getString("y") + ")");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		
	}
	
	public String insertMovie(String title, String rating, String isgeneral) {
		try {
			
			title = title.trim();
			if (title.equals(""))
				return "Enter a title.";
			
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT * from movies WHERE title = \'" + title + "\'");
			if (rs.next()) {
				return (title + " already exists!");
			}
			
			
			sql = "INSERT INTO " + schema + ".Movies (title, rating, isgeneral) values"
					+ "(\'" + title + "\',\'" + rating + "\',\'" + (isgeneral.equals("Yes")?"1":"0") + "\')";
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			return (title + " was added.");
			
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return "SQLException occured.";
		}
	}
	
	public String insertCinema(String name,
			int x,
			int y,
			String matinee,
			String afternoon,
			String evening,
			String late,
			String playg,
			String playpg,
			String playpg13,
			String playr,
			String plaync17,
			String playgeneral) {
		try {
			
			name = name.trim();
			if (name.equals(""))
				return "Enter a cinema name.";
			
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT * from cinemas WHERE name = \'" + name + "\'");
			
			if (rs.next()) {
				return (name + " already exists!");
			}
			
			else {
				sql = "INSERT INTO " + schema + ".Cinemas (name, x, y, matinee, afternoon, evening, late, playg, playpg, playpg13, playr, plaync17, playgeneral) values"
						+ "(\'" + name + "\', "
						+ Integer.toString(x) + ","
						+ Integer.toString(y) + ","
						+ "\'" + matinee + "\',"
						+ "\'" + afternoon + "\',"
						+ "\'" + evening + "\',"
						+ "\'" + late + "\',"
						+ "\'" + (playg.equals("Yes")?"1":"0") + "\',"
						+ "\'" + (playpg.equals("Yes")?"1":"0") + "\',"
						+ "\'" + (playpg13.equals("Yes")?"1":"0") + "\',"
						+ "\'" + (playr.equals("Yes")?"1":"0") + "\',"
						+ "\'" + (plaync17.equals("Yes")?"1":"0") + "\',"
						+ "\'" + (playgeneral.equals("Yes")?"1":"0") + "\')";
				statement = connection.createStatement();
				statement.execute(sql);
				return (name + " was added.");
			}
			
		}
		
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return "SQLException occured.";
		}
	}
	
	public String insertShowtime(String movieTitle, String cinemaName, String time) {
		try {
			/* Check to see if a showtime with given input already exists.
			 * Run a query.
			 * See if it returns more than 0 rows.
			 * If it does, return an error message. */
			sql = "SELECT s.*, m.*, c.* "
					+ "FROM showtimes as s "
					+ "INNER JOIN movies as m on m.id = s.movieid "
					+ "INNER JOIN cinemas as c on c.id = s.cinemaid "
					+ "WHERE m.title = \'" + movieTitle + "\' "
						+ "AND c.name = \'" + cinemaName + "\'"
						+ "AND s.showtime = \'" + time + "\'";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if (rs.next())
				return "Showtime already exists!";
			
			/* Get the movieid to be potentially added to the showtime table.
			 * Get the rating of the movie to see if it is allowed to play in the cinema.
			 * We know a row will return since the above query returns rows. */
			int movieid;
			String rating;
			String general;
			sql = "SELECT movies.* FROM csci370project4jdbc.movies WHERE title = \'" + movieTitle + "\'";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			rs.next();
			movieid = Integer.parseInt(rs.getString("id"));
			rating = rs.getString("rating");
			general = rs.getString("isgeneral");
			
			/* Get the cinemaid to be potentially added to the showtime table. */
			int cinemaid;
			boolean[] play = new boolean[6];
			sql = "SELECT cinemas.* from csci370project4jdbc.cinemas WHERE name = \'" + cinemaName + "\'";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			rs.next();
			cinemaid = Integer.parseInt(rs.getString("id"));
			play[0] = (rs.getString("playg").equals("1")?true:false);
			play[1] = (rs.getString("playpg").equals("1")?true:false);
			play[2] = (rs.getString("playpg13").equals("1")?true:false);
			play[3] = (rs.getString("playr").equals("1")?true:false);
			play[4] = (rs.getString("plaync17").equals("1")?true:false);
			play[5] = (rs.getString("playgeneral").equals("1")?true:false);
			
			/* Check what the cinema plays against the input.
			 * Return an error if the cinema does not play the type of movie.
			 * Reasons for rejection:
			 * movie rating
			 * general/limited */
			if (rating.equals("G") && (!play[0]))
				return (rating + " movies do not play at " + cinemaName + ".");
			if (rating.equals("PG") && (!play[1]))
				return (rating + " movies do not play at " + cinemaName + ".");
			if (rating.equals("PG13") && (!play[2]))
				return (rating + " movies do not play at " + cinemaName + ".");
			if (rating.equals("R") && (!play[3]))
				return (rating + " movies do not play at " + cinemaName + ".");
			if (rating.equals("NC-17") && (!play[4]))
				return (rating + " movies do not play at " + cinemaName + ".");
			if (general.equals("1") && (!play[5]))
				/* If the movie is general release but the cinema plays limited releases only: */
				return ("General movies do not play at " + cinemaName + ".");
			
			sql = "INSERT INTO " + schema + ".Showtimes (movieid, cinemaid, showtime) values "
					+ "(" + movieid + "," + cinemaid + ",\'" + time + "\')";
			statement = connection.createStatement();
			statement.execute(sql);

			return "Showtime added.";
		}
		catch (SQLException e) {
			System.out.println(movieTitle);
			System.out.println(cinemaName);
			System.out.println(time);
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return "SQLException occured.";
		}
	}
	
	public void loadMovies(JComboBox<String> box) {
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT title FROM movies");
			while (rs.next()) {
				box.addItem(rs.getString("title"));
			}
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
	public String deleteMovie(String title) {
		try {
			
			/* Store the movieid. */
			int movieid;
			sql = "SELECT id FROM " + schema + ".Movies WHERE title = \'" + title + "\'";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			rs.next();
			movieid = Integer.parseInt(rs.getString("id"));
			
			/* Delete the movie. */
			sql = "DELETE FROM " + schema + ".Movies WHERE title = \'" + title + "\'";
			statement = connection.createStatement();
			statement.execute(sql);
			
			/* Delete any rows which contain the movieid stored from before. */
			sql = "DELETE FROM " + schema + ".Showtimes WHERE movieid = " + Integer.toString(movieid);
			statement = connection.createStatement();
			statement.execute(sql);
			
			return "Any records of " + title + " were deleted.";
			
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return "SQLException occured.";
		}
	}
	
	public String deleteCinema(String name) {
		try {
			
			/* Store the cinemaid. */
			int cinemaid;
			sql = "SELECT id FROM " + schema + ".Cinemas WHERE name = \'" + name + "\'";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			rs.next();
			cinemaid = Integer.parseInt(rs.getString("id"));
			
			/* Delete the cinema. */
			sql = "DELETE FROM " + schema + ".Cinemas WHERE name = \'" + name + "\'";
			statement = connection.createStatement();
			statement.execute(sql);
			
			/* Delete any rows which contain the cinemaid stored from before. */
			sql = "DELETE FROM " + schema + ".Showtimes WHERE cinemaid = " + Integer.toString(cinemaid);
			statement = connection.createStatement();
			statement.execute(sql);
			
			return "Any records of " + name + " were deleted.";
			
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return "SQLException occured.";
		}
	}
	
	public void loadCinemas(JComboBox<String> box) {
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT name from cinemas");
			while (rs.next()) {
				box.addItem(rs.getString("name"));
			}
		}
		catch (SQLException e) {
			
		}
	}
	
	public void loadShowtimes(JComboBox<String> box, String cinemaName) {
		try {
			box.removeAllItems();
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT matinee, afternoon, evening, late FROM Cinemas where name = \'" + cinemaName + "\'");
			while (rs.next()) {
				box.addItem(rs.getString("matinee"));
				box.addItem(rs.getString("afternoon"));
				box.addItem(rs.getString("evening"));
				box.addItem(rs.getString("late"));
			}
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
	}

	public String[][] loadMovieData(String ratingFilter) {
		try {
			if (ratingFilter.equals("All")) {
				/* Return table with all of the movies.
				 * First, get the number of movies so an array can be made. */
				statement = connection.createStatement();
				rs = statement.executeQuery("SELECT COUNT(*) AS MovieCount FROM " + schema + ".Movies");
				if (!rs.next()) return null;
				int movieCount = Integer.parseInt(rs.getString("MovieCount"));
				String[][] data = new String[movieCount][2];
				/* Second, query the database to get the (title,rating) pairs. */
				statement = connection.createStatement();
				rs = statement.executeQuery("SELECT title, rating FROM " + schema + ".Movies");
				int row=0;
				while (rs.next()) {
					data[row][0] = rs.getString("title");
					data[row][1] = rs.getString("rating");
					row++;
				}
				return data;
			}
			else {
				/* Return table with movies of a certain rating.
				 * First, get the number of movies with the rating. */
				statement = connection.createStatement();
				sql = "SELECT COUNT(*) AS MovieCount FROM " + schema + ".Movies WHERE rating = \'" + ratingFilter + "\'";
				rs = statement.executeQuery(sql);
				if (!rs.next()) return null;
				int movieCount = Integer.parseInt(rs.getString("MovieCount"));
				String[][] data = new String[movieCount][2];
				/* Query the database to retrieve filtered movies. */
				statement = connection.createStatement();
				sql = "SELECT title, rating FROM " + schema + ".Movies WHERE rating = \'" + ratingFilter + "\'";
				rs = statement.executeQuery(sql);
				int row = 0;
				while (rs.next()) {
					data[row][0] = rs.getString("title");
					data[row][1] = rs.getString("rating");
					row++;
				}
				return data;
			}
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return null;
		}
	}

	public String[][] loadCinemaData(String xInput, String yInput, String rInput) {
		sql = null;
		try {
			if (xInput==null || yInput==null || rInput==null ||
				xInput.trim().isEmpty() || yInput.trim().isEmpty() || rInput.trim().isEmpty() ||
				Double.parseDouble(xInput)<0.0 || Double.parseDouble(xInput)>100.0 ||
				Double.parseDouble(yInput)<0.0 || Double.parseDouble(yInput)>100.0 ||
				Double.parseDouble(rInput)<0.0) {
				/* Return all cinemas. */
				statement = connection.createStatement();
				sql = "SELECT COUNT(*) AS CinemaCount FROM " + schema + ".Cinemas";
				rs = statement.executeQuery(sql);
				if (!rs.next()) return null;
				int cinemaCount = Integer.parseInt(rs.getString("CinemaCount"));
				String[][] data = new String[cinemaCount][3];
				statement = connection.createStatement();
				sql = "SELECT name, x, y FROM " + schema + ".Cinemas";
				rs = statement.executeQuery(sql);
				int row = 0;
				while (rs.next()) {
					data[row][0] = rs.getString("name");
					data[row][1] = "(" + rs.getString("x") + "," + rs.getString("y") + ")";
					data[row][2] = "N/A";
					row++;
				}
				return data;
			}
			else {
				/* Return cinemas based on the radius and current position. */
				statement = connection.createStatement();
				sql = "SELECT COUNT(*) AS CinemaCount "
						+ "FROM " + schema + ".Cinemas as c "
						+ "WHERE " + "sqrt(power(" + xInput + "-c.x, 2) + power(" + yInput + "-c.y,2)) < " + rInput;
				rs = statement.executeQuery(sql);
				if (!rs.next()) return null;
				int cinemaCount = Integer.parseInt(rs.getString("CinemaCount"));
				String[][] data = new String[cinemaCount][3];
				statement = connection.createStatement();
				sql = "SELECT c.name, "
						+ "c.x, "
						+ "c.y, "
						+ "sqrt(power(" + xInput + "-c.x, 2) + power(" + yInput + "-c.y,2)) as distance "
						+ "FROM " + schema + ".Cinemas as c "
						+ "WHERE " + "sqrt(power(" + xInput + "-c.x, 2) + power(" + yInput + "-c.y,2)) < " + rInput;
				rs = statement.executeQuery(sql);
				int row = 0;
				while (rs.next()) {
					data[row][0] = rs.getString("Name");
					data[row][1] = "(" + rs.getString("x") + "," + rs.getString("y") + ")";
					data[row][2] = String.format("%.2f", Double.parseDouble(rs.getString("distance")));
					row++;
				}
				return data;
			}
		}
		catch (NumberFormatException e) {
			/* If something went wrong with parsing doubles,
			 * call on this same function but with null parameters
			 * so all cinemas are returned in a String[][]. */
			return loadCinemaData(null, null, null);
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return null;
		}
	}

	public String[][] loadCinemasShowingMovie(String title) {
		try {
			/* The inner query outputs list of cinemas displaying the titled film with the address of the cinema. */
			String innerQuery = "SELECT c.name as CinemaName, "
					+ "CONCAT(\'(\', c.x, \',\', c.y, \')\') AS Address, "
					+ "COUNT(c.name) as ShowtimeCount "
					+ "FROM " + schema + ".Showtimes as s "
					+ "INNER JOIN " + schema + ".Movies AS m ON s.movieid = m.id "
					+ "INNER JOIN "+ schema + ".Cinemas AS c ON s.cinemaid = c.id "
					+ "WHERE m.title = \'" + title + "\' "
					+ "GROUP BY c.name";
			/* The outer query outputs the number of cinemas displaying the film.
			 * Use this query to create the table size.
			 * This number is at least 1 since the title was selected from showings. */
			String outerQuery = "SELECT COUNT(*) AS CinemaCount FROM (" + innerQuery + ") as InnerQuery";
			statement = connection.createStatement();
			rs = statement.executeQuery(outerQuery);
			if (!rs.next()) return null;
			int cinemaCount = Integer.parseInt(rs.getString("CinemaCount"));
			String[][] data = new String[cinemaCount][3];
			statement = connection.createStatement();
			rs = statement.executeQuery(innerQuery);
			int row = 0;
			while (rs.next()) {
				data[row][0] = rs.getString("CinemaName");
				data[row][1] = rs.getString("Address");
				data[row][2] = "";
				row++;
			}
			/* Go through data table.
			 * For each row, insert the showtimes in the third column. */
			String cinemaName;
			String incompleteQuery = "SELECT s.showtime "
					+ "FROM " + schema + ".Showtimes as s "
					+ "INNER JOIN movies as m on s.movieid = m.id "
					+ "INNER JOIN cinemas as c on s.cinemaid = c.id "
					+ "WHERE m.title = \'" + title + "\' AND c.name = \'";
			for (int r=0; r<data.length; r++) {
				cinemaName = data[r][0];
				statement = connection.createStatement();
				rs = statement.executeQuery(incompleteQuery + cinemaName + "\'");
				while (rs.next()) {
					if (data[r][2].isEmpty()) {
						data[r][2] = rs.getString("showtime");
					}
					else {
						data[r][2] = data[r][2] + ", " + rs.getString("showtime");
					}
				}
			}
			return data;
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return null;
		}
	}

	public String[][] loadMoviesAtCinema(String name) {
		try {
			/* The inner query outputs the movies playing at a specific cinema. */
			String innerQuery = "SELECT m.title, m.rating, s.showtime "
					+ "FROM " + schema + ".Showtimes AS s "
					+ "INNER JOIN movies AS m ON s.movieid = m.id "
					+ "INNER JOIN cinemas AS c on s.cinemaid = c.id "
					+ "WHERE c.name = \'" + name + "\' "
					+ "GROUP BY m.title";
			String outerQuery = "SELECT COUNT(*) AS MovieCount FROM (" + innerQuery + ") AS InnerQuery";
			statement = connection.createStatement();
			rs = statement.executeQuery(outerQuery);
			if (!rs.next()) return null;
			int movieCount = Integer.parseInt(rs.getString("MovieCount"));
			String[][] data = new String[movieCount][3];
			statement = connection.createStatement();
			rs = statement.executeQuery(innerQuery);
			int row = 0;
			while (rs.next()) {
				data[row][0] = rs.getString("title");
				data[row][1] = rs.getString("rating");
				data[row][2] = "";
				row++;
			}
			/* Go through the data table.
			 * For each row, insert the showtimes in the third column. */
			String movieTitle;
			String incompleteQuery = "SELECT s.showtime "
					+ "FROM showtimes as s "
					+ "INNER JOIN movies as m on s.movieid = m.id "
					+ "INNER JOIN cinemas as c on s.cinemaid = c.id "
					+ "WHERE c.name = \'" + name + "\' AND m.title = \'";
			for (int r=0; r<data.length; r++) {
				movieTitle = data[r][0];
				statement = connection.createStatement();
				rs = statement.executeQuery(incompleteQuery + movieTitle + "\'");
				while (rs.next()) {
					if (data[r][2].isEmpty()) {
						data[r][2] = rs.getString("showtime");
					}
					else {
						data[r][2] = data[r][2] + ", " + rs.getString("showtime");
					}
				}
			}
			return data;
		}
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    return null;
		}
	}
	
} /* JDBCDriver class */