import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
final public class MovieApplication extends JFrame implements ActionListener {

	private static final String AdministratorPassword = "admin";
	private static JDBCDriver driver;
	private static JPanel MasterPanel;
	private static String[] ratings = {"G", "PG", "PG-13", "R", "NC-17", "NR"};
	private static String[] matineeTimes = {"1:00 p.m.", "1:30 p.m."};
	private static String[] afternoonTimes = {"4:00 p.m.", "5:00 p.m."};
	private static String[] eveningTimes = {"7:00 p.m.", "8:00 p.m.", "9:00 p.m."};
	private static String[] lateTimes = {"12:00 a.m."};
	private static String[] YesOrNo = {"Yes", "No"};
	
	/* HomePanel
	 * shown when the application starts */
	private static JPanel HomePanel;
	private static JButton AdministratorButton;
	private static JButton MoviesButton;
	private static JButton CinemasButton;
	
	/* LogInPanel
	 * shown when the user clicks on "Administrator" from the home panel
	 * gives option to log in after entering the password
	 * gives option to go back to the home panel */
	private static JPanel LogInPanel;
	private static JPasswordField password;
	private static JTextArea LogInError;
	private static JButton LogInButton;
	private static JButton LogInBackButton;
	
	/* ModifyChoicePanel
	 * shown when the administrator successfully logs in
	 * gives option to modify movies
	 * gives option to modify cinemas
	 * gives option to go back to the LogInPanel */
	private static JPanel ModifyChoicePanel;
	private static JButton InsertShowtimeButton;
	private static JButton InsertMovieButton;
	private static JButton InsertCinemaButton;
	private static JButton DropShowtimeButton;
	private static JButton DropMovieButton;
	private static JButton DropCinemaButton;
	private static JButton MofifyChoiceBackButton;
	
	/* InsertShowtimePanel
	 * shown when the administrator wants to insert a movie showtime */
	private static JPanel InsertShowtimePanel;
	private static JComboBox<String> SelectMovieBox;
	private static JComboBox<String> SelectCinemaBox;
	private static JComboBox<String> SelectShowtimeBox;
	private static JButton InsertShowtimeBackButton;
	private static JTextArea InsertShowtimeError;
	private static JButton InsertShowtime;
	
	/* InsertMoviePanel
	 * shown when an administrator wants to insert a movie into the database */
	private static JPanel InsertMoviePanel;
	private static JButton InsertMovieBackButton;
	private static JTextField InsertMovieTitle;
	private static JComboBox<String> InsertMovieRatingBox;
	private static JComboBox<String> general;
	private static JTextArea InsertMovieError;
	private static JButton InsertMovie;
	
	/* InsertCinemaPanel
	 * shown when an administrator wants to insert a cinema into the database */
	private static JPanel InsertCinemaPanel;
	private static JButton InsertCinemaBackButton;
	private static JTextField InsertCinemaName;
	private static JTextField InsertCinemaX;
	private static JTextField InsertCinemaY;
	private static JComboBox<String> InsertMatineeBox;
	private static JComboBox<String> InsertAfternoonBox;
	private static JComboBox<String> InsertEveningBox;
	private static JComboBox<String> InsertLateBox;
	private static JComboBox<String> playg;
	private static JComboBox<String> playpg;
	private static JComboBox<String> playpg13;
	private static JComboBox<String> playr;
	private static JComboBox<String> plaync17;
	private static JComboBox<String> playgeneral;
	private static JButton InsertCinema;
	private static JTextArea InsertCinemaError;
	
	/* DropShowtimePanel
	 * shown when the administrator wants to delete a movie showtime */
	private static JPanel DropShowtimePanel;
	private static JButton DropShowtimeBackButton;
	
	/* DropMoviePanel
	 * shown when the administrator wants to delete a movie from the database */
	private static JPanel DropMoviePanel;
	private static JButton DropMovieBackButton;
	private static JComboBox<String> DeleteMovieBox;
	private static JButton DeleteMovieButton;
	private static JTextArea DeleteMovieError;
	
	/* DropCinemaPanel
	 * shown when the administrator wants to delete a cinema from the database */
	private static JPanel DropCinemaPanel;
	private static JButton DropCinemaBackButton;
	private static JComboBox<String> DeleteCinemaBox;
	private static JButton DeleteCinemaButton;
	private static JTextArea DeleteCinemaError;
	
	/* MoviesPanel
	 * shown when the user (not administrator) clicks "Show Movies"
	 *  */
	private static JPanel MoviesPanel;
	private static JButton MoviesApplyButton;
	private static JComboBox<String> MoviesSearchRating;
	private static JButton MoviesBackButton;
	
	/* MoviesDetailsPanel
	 * shown when a user DOUBLE CLICKS on a movie */
	private static JPanel MovieDetailsPanel;
	private static JButton MovieDetailsBackButton;
	
	/* CinemasPanel
	 * shown when the user (not administrator) clicks "Show Cinemas"
	 * user can enter address (x,y)
	 * user can enter radius r
	 * user can apply search given x, y, and r */
	private static JPanel CinemasPanel;
	private static JButton CinemasBackButton;
	private static JButton CinemasClearButton;
	private static JButton CinemasSearchButton;
	private static JTextField fieldx;
	private static JTextField fieldy;
	private static JTextField fieldr;
	private static JTextArea CinemasSearchError;
	
	/* CinemasDetailsPanel
	 * shown when user DOUBLE CLICKS on a cinema */
	private static JPanel CinemaDetailsPanel;
	private static JButton CinemaDetailsBackButton;
	
	private MovieApplication() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Movie Application");
		MasterPanel = new JPanel();
		MasterPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		defineHomePanel();
		MasterPanel.add(HomePanel);
		add(MasterPanel);
		pack();
		setVisible(true);
		driver = new JDBCDriver();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MovieApplication();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();

		if (source == LogInButton) {
			if (String.valueOf(password.getPassword()).equals(AdministratorPassword)) {
				password.setText("");
				LogInError.setText("");
				defineModifyChoicePanel();
				switchJPanel(ModifyChoicePanel);
			}
			else {
				password.setText("");
				LogInError.setText("Wrong password.");
			}
		}
		
		/* ******************** InsertShowtimePanel ******************** */
		
		else if (source == InsertShowtime) {
			InsertShowtimeError.setText(driver.insertShowtime(
					SelectMovieBox.getSelectedItem().toString(),
					SelectCinemaBox.getSelectedItem().toString(),
					SelectShowtimeBox.getSelectedItem().toString()));
		}
		
		/* ******************** InsertMoviePanel ******************** */

		else if (source == InsertMovie) {
			InsertMovieError.setText(driver.insertMovie(
					InsertMovieTitle.getText(),
					InsertMovieRatingBox.getSelectedItem().toString(),
					general.getSelectedItem().toString()));
		}
		
		/* ******************** InsertCinemaPanel ******************** */
		
		else if (source == InsertCinema) {
			try {
			InsertCinemaError.setText(driver.insertCinema(
					InsertCinemaName.getText(),
					Integer.parseInt(InsertCinemaX.getText()),
					Integer.parseInt(InsertCinemaY.getText()),
					InsertMatineeBox.getSelectedItem().toString(),
					InsertAfternoonBox.getSelectedItem().toString(),
					InsertEveningBox.getSelectedItem().toString(),
					InsertLateBox.getSelectedItem().toString(),
					playg.getSelectedItem().toString(),
					playpg.getSelectedItem().toString(),
					playpg13.getSelectedItem().toString(),
					playr.getSelectedItem().toString(),
					plaync17.getSelectedItem().toString(),
					playgeneral.getSelectedItem().toString()
					));
			}
			catch (NumberFormatException nfe) {
				InsertCinemaError.setText("Invalid input.");
			}
		}
		
		/* ******************** DropMoviePanel ******************** */
		
		else if (source == DeleteMovieButton) {
			if (DeleteMovieBox.getSelectedItem() == null) {
				DeleteMovieError.setText("No movie to be deleted.");
				return;
			}
			driver.deleteMovie(DeleteMovieBox.getSelectedItem().toString());
			switchJPanel(ModifyChoicePanel);
		}
		
		/* ******************** DropCinemaPanel ******************** */

		else if (source == DeleteCinemaButton) {
			if (DeleteCinemaBox.getSelectedItem() == null) {
				DeleteCinemaError.setText("No cinema to be deleted");
				return;
			}
			driver.deleteCinema(DeleteCinemaBox.getSelectedItem().toString());
			switchJPanel(ModifyChoicePanel);
		}
		
	} /* public void actionPerformed(ActionEvent e) */

	private void switchJPanel(JPanel jpanel) {
		MasterPanel.removeAll();
		MasterPanel.add(jpanel);
		MasterPanel.repaint();
		MasterPanel.revalidate();
		pack();
	}
	
	private void defineHomePanel() {
		
		HomePanel = new JPanel();
		HomePanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		HomePanel.setLayout(new GridBagLayout());
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		JLabel WelcomeText = new JLabel("WELCOME");
		AdministratorButton = new JButton("Administrator");
		AdministratorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defineLogInPanel();
				switchJPanel(LogInPanel);
			}
		});
		MoviesButton = new JButton("View Movies");
		MoviesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				defineMoviesPanel();
				switchJPanel(MoviesPanel);
			}
		});
		CinemasButton = new JButton("View Cinemas");
		CinemasButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defineCinemasPanel();
				switchJPanel(CinemasPanel);
			}
		});
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.insets = i;
		HomePanel.add(WelcomeText, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		HomePanel.add(AdministratorButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		HomePanel.add(MoviesButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		HomePanel.add(CinemasButton, c);
		
	}
	
	private void defineLogInPanel() {
		
		LogInPanel = new JPanel();
		LogInPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		LogInPanel.setLayout(new GridBagLayout());
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		JLabel LogInText = new JLabel("Administrator Log-In");
		password = new JPasswordField(5);
		LogInError = new JTextArea();
		LogInButton = new JButton("Go");
		LogInButton.addActionListener(this);
		LogInBackButton = new JButton("Back");
		LogInBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(HomePanel);
			}
		});
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.insets = i;
		LogInPanel.add(LogInText, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		LogInPanel.add(password, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.insets = i;
		LogInPanel.add(LogInButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.insets = i;
		LogInPanel.add(LogInBackButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.insets = i;
		LogInPanel.add(LogInError, c);
		
	}
	
	private void defineModifyChoicePanel() {
		
		ModifyChoicePanel = new JPanel();
		ModifyChoicePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		ModifyChoicePanel.setLayout(new GridBagLayout());
		InsertShowtimeButton = new JButton("Add Showtime");
		InsertShowtimeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defineInsertShowtimePanel();
				switchJPanel(InsertShowtimePanel);
			}
		});
		InsertMovieButton = new JButton("Add Movie");
		InsertMovieButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defineInsertMoviePanel();
				switchJPanel(InsertMoviePanel);
			}
		});
		InsertCinemaButton = new JButton("Add Cinema");
		InsertCinemaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defineInsertCinemaPanel();
				switchJPanel(InsertCinemaPanel);
			}
		});
		DropShowtimeButton = new JButton("Delete Showtime");
		DropShowtimeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defineDropShowtimePanel();
				switchJPanel(DropShowtimePanel);
			}
		});
		DropMovieButton = new JButton("Delete Movie");
		DropMovieButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defineDropMoviePanel();
				switchJPanel(DropMoviePanel);
			}
		});
		DropCinemaButton = new JButton("Delete Cinema");
		DropCinemaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defineDropCinemaPanel();
				switchJPanel(DropCinemaPanel);
			}
		});
		MofifyChoiceBackButton = new JButton("Back");
		MofifyChoiceBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(LogInPanel);
			}
		});
		GridBagConstraints c;
		Insets i = new Insets(5, 5, 5, 5);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = i;
		ModifyChoicePanel.add(new JLabel("Select an option."), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.insets = i;
		ModifyChoicePanel.add(MofifyChoiceBackButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		ModifyChoicePanel.add(InsertShowtimeButton, c);
		c.gridy = 2;
		ModifyChoicePanel.add(InsertMovieButton, c);
		c.gridy = 3;
		ModifyChoicePanel.add(InsertCinemaButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		ModifyChoicePanel.add(DropShowtimeButton, c);
		c.gridy = 2;
		ModifyChoicePanel.add(DropMovieButton, c);
		c.gridy = 3;
		ModifyChoicePanel.add(DropCinemaButton, c);
		
	}
	
	private void defineMoviesPanel() {
		
		MoviesPanel = new JPanel();
		MoviesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		MoviesPanel.setLayout(new GridBagLayout());
		JPanel top = new JPanel();
		top.setBorder(new EmptyBorder(10, 10, 10, 10));
		top.setLayout(new GridBagLayout());
		JScrollPane scrollpane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		String[] columnNames = {"Movie Title", "Movie Rating"};
		String[][] data = driver.loadMovieData("All");
		if (data.length > 0) TableManager.sortTable(data, 0);
		TableModel model = new DefaultTableModel(data, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					defineMovieDetailsPanel(table.getValueAt(table.getSelectedRow(), 0).toString());
					switchJPanel(MovieDetailsPanel);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		MoviesApplyButton = new JButton("Apply");
		
		MoviesBackButton = new JButton("Back");
		MoviesBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(HomePanel);
			}
		});
		MoviesSearchRating = new JComboBox<>(ratings);
		MoviesSearchRating.addItem("All");
		MoviesSearchRating.setSelectedIndex(6);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.insets = i;
		top.add(new JLabel("Apply Filter to Movies"), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		top.add(MoviesSearchRating, c);
		c.gridx = 1;
		top.add(MoviesApplyButton, c);
		c.gridx = 2;
		top.add(MoviesBackButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.insets = i;
		top.add(new JLabel("Double click to see cinemas showing the movie."), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = i;
		MoviesPanel.add(top, c);
		c.gridy = 1;
		scrollpane.getViewport().add(table);
		MoviesPanel.add(scrollpane, c);
		
		MoviesApplyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[][] d = driver.loadMovieData(MoviesSearchRating.getSelectedItem().toString());
				TableManager.sortTable(data, 0);
				TableModel m = new DefaultTableModel(d, columnNames) {
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				JTable t = new JTable(m);
				t.getColumnModel().getColumn(0).setPreferredWidth(300);
				t.getColumnModel().getColumn(1).setPreferredWidth(100);
				t.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {
							defineMovieDetailsPanel(table.getValueAt(table.getSelectedRow(), 0).toString());
							switchJPanel(MovieDetailsPanel);
						}
					}
					@Override
					public void mouseEntered(MouseEvent e) {}
					@Override
					public void mouseExited(MouseEvent e) {}
					@Override
					public void mousePressed(MouseEvent e) {}
					@Override
					public void mouseReleased(MouseEvent e) {}
				});
				scrollpane.getViewport().add(t);
			}
		});
		
	}
	
	private void defineCinemasPanel() {
		
		CinemasPanel = new JPanel();
		CinemasPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		CinemasPanel.setLayout(new GridLayout(1, 2));
		JPanel left = new JPanel();
		left.setBorder(new EmptyBorder(25, 25, 25, 25));
		left.setLayout(new GridBagLayout());
		JScrollPane scrollpane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		String[][] data = driver.loadCinemaData(null, null, null);
		if (data.length > 0) TableManager.sortTable(data, 0);
		String[] columnNames = {"Cinema Name", "Address", "Distance"};
		TableModel model = new DefaultTableModel(data, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					defineCinemaDetailsPanel(table.getValueAt(table.getSelectedRow(), 0).toString());
					switchJPanel(CinemaDetailsPanel);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		CinemasBackButton = new JButton("Back");
		CinemasBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(HomePanel);
			}
		});
		CinemasClearButton = new JButton("Clear");
		CinemasClearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fieldx.setText("");
				fieldy.setText("");
				fieldr.setText("");
				CinemasSearchError.setText("");
			}
		});
		CinemasSearchButton = new JButton("Search");
		CinemasSearchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[][] d = driver.loadCinemaData(fieldx.getText(), fieldy.getText(), fieldr.getText());
				TableManager.sortTable(d, 0);
				String[] columnNames = {"Cinema Name", "Address", "Distance"};
				TableModel m = new DefaultTableModel(d, columnNames) {
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				JTable t = new JTable(m);
				t.getColumnModel().getColumn(0).setPreferredWidth(200);
				t.getColumnModel().getColumn(1).setPreferredWidth(100);
				t.getColumnModel().getColumn(2).setPreferredWidth(100);
				t.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {
							defineCinemaDetailsPanel(t.getValueAt(t.getSelectedRow(), 0).toString());
							switchJPanel(CinemaDetailsPanel);
						}
					}
					@Override
					public void mouseEntered(MouseEvent e) {}
					@Override
					public void mouseExited(MouseEvent e) {}
					@Override
					public void mousePressed(MouseEvent e) {}
					@Override
					public void mouseReleased(MouseEvent e) {}
				});
				scrollpane.getViewport().add(t);
			}
		});
		fieldx = new JTextField(5);
		fieldx.setText("");
		fieldy = new JTextField(5);
		fieldy.setText("");
		fieldr = new JTextField(5);
		fieldr.setText("");
		CinemasSearchError = new JTextArea();
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = i;
		left.add(new JLabel("Search Cinema"), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		left.add(new JLabel("x: "), c);
		c.gridx = 1;
		left.add(fieldx, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = i;
		left.add(new JLabel("y: "), c);
		c.gridx = 1;
		left.add(fieldy, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = i;
		left.add(new JLabel("radius: "), c);
		c.gridx = 1;
		left.add(fieldr, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = i;
		left.add(CinemasClearButton, c);
		c.gridx = 1;
		left.add(CinemasSearchButton, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.insets = i;
		left.add(CinemasSearchError, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.insets = i;
		left.add(CinemasBackButton, c);
		
		CinemasPanel.add(left);
		scrollpane.getViewport().add(table);
		CinemasPanel.add(scrollpane);
		pack();
		
	} /* defineCinemasPanel */
	
	private void defineMovieDetailsPanel(String title) {
		
		MovieDetailsPanel = new JPanel();
		MovieDetailsPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		MovieDetailsPanel.setLayout(new GridBagLayout());
		MovieDetailsBackButton = new JButton("Back");
		MovieDetailsBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchJPanel(MoviesPanel);
			}
		});
		JScrollPane scrollpane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		String[][] data = driver.loadCinemasShowingMovie(title);
		if (data.length > 0) TableManager.sortTable(data, 0);
		String[] columnNames = {"Cinema Name", "Address", "All Showtimes"};
		TableModel model = new DefaultTableModel(data, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(500);
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = i;
		MovieDetailsPanel.add(new JLabel("Cinemas showing " + title), c);
		
		c.gridy = 1;
		scrollpane.getViewport().add(table);
		MovieDetailsPanel.add(scrollpane, c);
		
		c.gridy = 2;
		MovieDetailsPanel.add(MovieDetailsBackButton, c);
		
	} /* defineMovieDetailsPanel */
	
	private void defineCinemaDetailsPanel(String name) {
		
		CinemaDetailsPanel = new JPanel();
		CinemaDetailsPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		CinemaDetailsPanel.setLayout(new GridBagLayout());
		CinemaDetailsBackButton = new JButton("Back");
		CinemaDetailsBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchJPanel(CinemasPanel);
			}
		});
		JScrollPane scrollpane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		String[][] data = driver.loadMoviesAtCinema(name);
		if (data.length > 0) TableManager.sortTable(data, 0);
		String[] columnNames = {"Movie Title", "Rating", "All Showtimes"};
		TableModel model = new DefaultTableModel(data, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(500);
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = i;
		CinemaDetailsPanel.add(new JLabel("Movies showing at " + name), c);
		
		c.gridy = 1;
		scrollpane.getViewport().add(table);
		CinemaDetailsPanel.add(scrollpane, c);
		
		c.gridy = 2;
		CinemaDetailsPanel.add(CinemaDetailsBackButton, c);
		
	}
	
	private void defineInsertShowtimePanel() {
		
		InsertShowtimePanel = new JPanel();
		InsertShowtimePanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		InsertShowtimePanel.setLayout(new GridBagLayout());
		SelectMovieBox = new JComboBox<>();
		SelectCinemaBox = new JComboBox<>();
		SelectShowtimeBox = new JComboBox<>();
		driver.loadMovies(SelectMovieBox);
		driver.loadCinemas(SelectCinemaBox);
		driver.loadShowtimes(SelectShowtimeBox, SelectCinemaBox.getSelectedItem().toString());
		SelectCinemaBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				driver.loadShowtimes(SelectShowtimeBox, SelectCinemaBox.getSelectedItem().toString());
			}
		});
		InsertShowtimeBackButton = new JButton("Back");
		InsertShowtimeBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(ModifyChoicePanel);
			}
		});
		InsertShowtime = new JButton("Insert Showtime");
		InsertShowtime.addActionListener(this);
		InsertShowtimeError = new JTextArea();
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = i;
		InsertShowtimePanel.add(new JLabel("Insert Showtime"), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		InsertShowtimePanel.add(new JLabel("Movie Title: "), c);
		c.gridx = 1;
		InsertShowtimePanel.add(SelectMovieBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = i;
		InsertShowtimePanel.add(new JLabel("Cinema name: "), c);
		c.gridx = 1;
		InsertShowtimePanel.add(SelectCinemaBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = i;
		InsertShowtimePanel.add(new JLabel("Showtime: "), c);
		c.gridx = 1;
		InsertShowtimePanel.add(SelectShowtimeBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.insets = i;
		InsertShowtimePanel.add(InsertShowtime, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.insets = i;
		InsertShowtimePanel.add(InsertShowtimeError, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.insets = i;
		InsertShowtimePanel.add(InsertShowtimeBackButton, c);
		
	}
	
	private void defineInsertMoviePanel() {
		
		InsertMoviePanel = new JPanel();
		InsertMoviePanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		InsertMoviePanel.setLayout(new GridBagLayout());
		InsertMovieBackButton = new JButton("Back");
		InsertMovieBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(ModifyChoicePanel);
			}
		});
		InsertMovieTitle = new JTextField(10);
		InsertMovieRatingBox = new JComboBox<>(ratings);
		general = new JComboBox<>(YesOrNo);
		InsertMovieError = new JTextArea();
		InsertMovie = new JButton("Insert Movie");
		InsertMovie.addActionListener(this);
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = i;
		InsertMoviePanel.add(new JLabel("Insert Movie"), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		InsertMoviePanel.add(new JLabel("Movie Title: "), c);
		c.gridx = 1;
		InsertMoviePanel.add(InsertMovieTitle, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = i;
		InsertMoviePanel.add(new JLabel("Movie Rating: "), c);
		c.gridx = 1;
		InsertMoviePanel.add(InsertMovieRatingBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = i;
		InsertMoviePanel.add(new JLabel("General? "), c);
		c.gridx = 1;
		InsertMoviePanel.add(general, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.insets = i;
		InsertMoviePanel.add(InsertMovie, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.insets = i;
		InsertMoviePanel.add(InsertMovieError, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.insets = i;
		InsertMoviePanel.add(InsertMovieBackButton, c);
		
	}
	
	private void defineInsertCinemaPanel() {
		
		InsertCinemaPanel = new JPanel();
		InsertCinemaPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		InsertCinemaPanel.setLayout(new GridBagLayout());
		InsertCinemaName = new JTextField(10);
		InsertCinemaX = new JTextField(2);
		InsertCinemaY = new JTextField(2);
		InsertMatineeBox = new JComboBox<>(matineeTimes);
		InsertAfternoonBox = new JComboBox<>(afternoonTimes);
		InsertEveningBox = new JComboBox<>(eveningTimes);
		InsertLateBox = new JComboBox<>(lateTimes);
		
		playg = new JComboBox<>(YesOrNo);
		playpg = new JComboBox<>(YesOrNo);
		playpg13 = new JComboBox<>(YesOrNo);
		playr = new JComboBox<>(YesOrNo);
		plaync17 = new JComboBox<>(YesOrNo);
		playgeneral = new JComboBox<>(YesOrNo);
		
		InsertCinema = new JButton("Insert Cinema");
		InsertCinema.addActionListener(this);
		InsertCinemaError = new JTextArea();
		InsertCinemaBackButton = new JButton("Back");
		InsertCinemaBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(ModifyChoicePanel);
			}
		});
		Insets i = new Insets(5, 5, 5, 5);
		GridBagConstraints c;
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Insert Cinema"), c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Cinema Title: "), c);
		c.gridwidth = 2;
		c.gridx = 1;
		InsertCinemaPanel.add(InsertCinemaName, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Address (x,y): "), c);
		c.gridx = 1;
		InsertCinemaPanel.add(InsertCinemaX, c);
		c.gridx = 2;
		InsertCinemaPanel.add(InsertCinemaY, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Matinee showtime: "), c);
		c.gridx = 1;
		c.gridwidth = 2;
		InsertCinemaPanel.add(InsertMatineeBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Afternoon showtime:"), c);
		c.gridx = 1;
		c.gridwidth = 2;
		InsertCinemaPanel.add(InsertAfternoonBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Evening showtime: "), c);
		c.gridx = 1;
		c.gridwidth = 2;
		InsertCinemaPanel.add(InsertEveningBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Late showtime: "), c);
		c.gridx = 1;
		c.gridwidth = 2;
		InsertCinemaPanel.add(InsertLateBox, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Plays G rated movies: "), c);
		c.gridx = 1;
		InsertCinemaPanel.add(playg, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Plays PG rated movies: "), c);
		c.gridx = 1;
		InsertCinemaPanel.add(playpg, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Plays PG-13 rated movies: "), c);
		c.gridx = 1;
		InsertCinemaPanel.add(playpg13, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Plays R rated movies: "), c);
		c.gridx = 1;
		InsertCinemaPanel.add(playr, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Plays NC-17 rated movies: "), c);
		c.gridx = 1;
		InsertCinemaPanel.add(plaync17, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 12;
		c.gridwidth = 1;
		c.insets = i;
		InsertCinemaPanel.add(new JLabel("Plays general movies: "), c);
		c.gridx = 1;
		InsertCinemaPanel.add(playgeneral, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 13;
		c.gridwidth = 3;
		c.insets = i;
		InsertCinemaPanel.add(InsertCinema, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 14;
		c.gridwidth = 3;
		c.insets = i;
		InsertCinemaPanel.add(InsertCinemaError, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 15;
		c.gridwidth = 3;
		c.insets = i;
		InsertCinemaPanel.add(InsertCinemaBackButton, c);
		
	}
	
	private void defineDropShowtimePanel() {
		DropShowtimePanel = new JPanel();
		DropShowtimeBackButton = new JButton("Back");
		DropShowtimeBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(ModifyChoicePanel);
			}
		});
		DropShowtimePanel.add(DropShowtimeBackButton);
	}
	
	private void defineDropMoviePanel() {
		
		DropMoviePanel = new JPanel();
		DropMoviePanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		DropMoviePanel.setLayout(new GridBagLayout());
		DeleteMovieBox = new JComboBox<>();	
		DeleteMovieButton = new JButton("Delete");
		DeleteMovieButton.addActionListener(this);
		DropMovieBackButton = new JButton("Back");
		DropMovieBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(ModifyChoicePanel);
			}
		});
		DeleteMovieError = new JTextArea();
		driver.loadMovies(DeleteMovieBox);
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = i;
		DropMoviePanel.add(new JLabel("Delete a movie."), c);
		c.gridy = 1;
		DropMoviePanel.add(DeleteMovieBox, c);
		c.gridy = 2;
		DropMoviePanel.add(DeleteMovieButton, c);
		c.gridy = 3;
		DropMoviePanel.add(DeleteMovieError, c);
		c.gridy = 4;
		DropMoviePanel.add(DropMovieBackButton, c);
		
	}
	
	private void defineDropCinemaPanel() {
		
		DropCinemaPanel = new JPanel();
		DropCinemaPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		DropCinemaPanel.setLayout(new GridBagLayout());
		DeleteCinemaBox = new JComboBox<>();
		DeleteCinemaButton = new JButton("Delete");
		DeleteCinemaButton.addActionListener(this);
		DeleteMovieBox = new JComboBox<>();
		DropCinemaBackButton = new JButton("Back");
		DropCinemaBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchJPanel(ModifyChoicePanel);
			}
		});
		DeleteCinemaError = new JTextArea();
		driver.loadCinemas(DeleteCinemaBox);
		GridBagConstraints c;
		Insets i = new Insets(10, 10, 10, 10);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = i;
		DropCinemaPanel.add(new JLabel("Delete a cinema."), c);
		
		c.gridy = 1;
		DropCinemaPanel.add(DeleteCinemaBox, c);
		
		c.gridy = 2;
		DropCinemaPanel.add(DeleteCinemaButton, c);
		
		c.gridy = 3;
		DropCinemaPanel.add(DeleteCinemaError, c);
		
		c.gridy = 4;
		DropCinemaPanel.add(DropCinemaBackButton, c);
		
	}
	
} /* MovieApplication class */