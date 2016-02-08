package com.shoppingSystem;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.util.Calendar;
import java.util.regex.*;

class MainFrame extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JLabel LogoLabel;
	JLabel headLabel;
	JButton notifyButton;
	LoginPanel loginPanel;
	SignupPanel signUpPanel;
	SqlPanel sqlPanel;
	ButtonPanel buttonPanel;
	// PostandSearchPanel postandsearch;
	ResultPanel resultPanel;
	Connection conn = null;
	ArrayList<String> requester = new ArrayList<String>();
	ArrayList<String> Relation = new ArrayList<String>();
	int countrequest = 0;
	JTextArea resultArea = null;
	JScrollPane scrollPane = null;
	int trigger = 0;
	JLabel showLabel;
	int hasRequest = 0;
	StringBuffer SQLOut = new StringBuffer();
	String productCategory = null;

	MainFrame() {
		setResizable(false);
		setLayout(null);
		setSize(1100, 700);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		setLocation((width - 1100) / 2, (height - 700) / 2);
		setTitle("This is GUI for database homework");
		SetLogo();
		setLoginPanel();
		setSignupPanel();
		setSqlPanel();
		setButtonPanel();
		setResultPanel();
		// postandsearch.disablePanel();
		buttonPanel.disablePanel();
	}

	public void disableResult() {
		resultArea.setText("");
		resultArea.setEditable(false);
		resultArea.setEnabled(false);
		scrollPane.setEnabled(false);
	}

	public void setResultPanel() {

		resultArea = new JTextArea(10, 30);
		resultArea.setLineWrap(true);
		scrollPane = new JScrollPane(resultArea);
		headLabel = new JLabel("Shopping System");
		add(scrollPane);
		add(headLabel);

		headLabel.setFont(new Font("Serif", Font.BOLD, 30));
		headLabel.setBounds(240, 20, 360, 60);
		scrollPane.setBounds(20, 100, 740, 250);
	}

	public void SetLogo() {
		Image image;
		try {
			image = ImageIO.read(new File("usc_viterbi_logo.jpg"));
			ImageIcon icon = new ImageIcon(image);
			LogoLabel = new JLabel();
			LogoLabel.setIcon(icon);
			LogoLabel.setBounds(830, 500, 300, 150);

			add(LogoLabel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // this generates an image file

	}

	public void setButtonPanel() {
		buttonPanel = new ButtonPanel();
		buttonPanel.setBounds(30, 380, 700, 90);
		this.add(buttonPanel);
		
		buttonPanel.buttons[0].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				StringBuffer result = new StringBuffer();
				/* Fill this function */
				/* Press this your account button, you should be able to list */
				/*
				 * current log in customer information in the result panel
				 * (Including Email, First Name, Last Name, Address)
				 */
				/* You can define the output format */
				conn = ConnectDB.openConnection();
				String queryStr = "SELECT cus.UserEmail, us.Fname, us.Lname, addr.AptNumber, addr.StreetAddr, addr.City, addr.State, addr.Zip"
						+ " FROM Customers cus, Users us, Addresses addr Where cus.UserEmail = '"
						+ loginPanel.username.getText()
						+ "' AND cus.UserEmail = us.UserEmail AND us.AddrID = addr.AddrID";
				SQLOut.append(queryStr + "\n\n");
				setSQLOutput(SQLOut);
				try {
					Statement stmt = conn.createStatement();
					ResultSet re = stmt.executeQuery(queryStr);
					String email = null, firstName = null, lastName = null, streetAddr = null, city = null, state = null;
					int aptNumber = 0, zip = 0;
					String tableHead = "---------------------------------------------------------------------------------------------------------------------"
							+ "----------------------------------------------------------\n"
							+ "Email                         FirstName           LastName            AptNumber    StreetAddr                                        "
							+ "City               State         Zip    \n"
							+ "---------------------------------------------------------------------------------------------------------------------------------"
							+ "-----------------------------------------------\n";
					String resultTable = "";
					while (re.next()) {
						email = re.getString(1);
						firstName = re.getString(2);
						lastName = re.getString(3);
						aptNumber = re.getInt(4);
						streetAddr = re.getString(5);
						city = re.getString(6);
						state = re.getString(7);
						zip = re.getInt(8);
						resultTable += email + "          " + firstName
								+ "     " + lastName + "     " + aptNumber
								+ "     " + streetAddr + "     " + city + "  "
								+ state + "  " + zip + "\n";
					}
					resultArea.setText(tableHead + resultTable);
					ConnectDB.closeConnection(conn);

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					ConnectDB.closeConnection(conn);
					return;
				}
			}
		});

		buttonPanel.buttons[1].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				StringBuffer result = new StringBuffer();
				/* Fill this function */
				/*
				 * Press this all products button, you should be able to list
				 * all the products which are visible to you
				 */
				/* You can define the output format */
				conn = ConnectDB.openConnection();
				String queryStr = "SELECT pro.ProductID, pro.ProCategory, pro.Brand, pro.ProName, pro.Price From Products pro";
				SQLOut.append(queryStr + "\n\n");
				setSQLOutput(SQLOut);
				try {
					Statement stmt = conn.createStatement();
					ResultSet re = stmt.executeQuery(queryStr);
					String productID = null, category = null, brand = null, name = null;
					double price = 0;
					String resultTable = "";
					String tableHead = "---------------------------------------------------------------------------------------------------------------------"
							+ "----------------------------------------------------------\n"
							+ "ProductID    Category  Brand                       Name                                         Price   \n"
							+ "---------------------------------------------------------------------------------------------------------------------------------"
							+ "-----------------------------------------------\n";
					while (re.next()) {
						productID = re.getString(1);
						category = re.getString(2);
						brand = re.getString(3);
						name = re.getString(4);
						price = re.getDouble(5);
						resultTable += productID + "        " + category
								+ "        " + brand + "      " + name
								+ "     " + "$" + price + "\n";
					}
					resultArea.setText(tableHead + resultTable);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					ConnectDB.closeConnection(conn);
					return;
				}
			}
		});

		buttonPanel.buttons[2].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Frame0 frame = new Frame0();
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);
				frame.btn1.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						/* Fill this function */
						/*
						 * Press this choose category Button, after choosing and
						 * pressing OK
						 */
						/*
						 * you should be able to list all products belong to
						 * this category
						 */
						conn = ConnectDB.openConnection();
						String category = frame.combo.getSelectedItem()
								.toString();
						String queryStr = "SELECT pro.ProductID, pro.ProName, pro.ProCategory, pro.Brand, pro.Price, pro.StockQty FROM Products pro WHERE pro.ProCategory"
								+ " = '" + category + "'";
						SQLOut.append(queryStr + "\n\n");
						setSQLOutput(SQLOut);
						try {
							Statement stmt = conn.createStatement();
							ResultSet re = stmt.executeQuery(queryStr);
							String productID = null, proName = null, proCategory = null, brand = null;
							double price = 0;
							int stockQty = 0;
							String resultTable = "";
							String tableHead = "---------------------------------------------------------------------------------------------------------------------"
									+ "----------------------------------------------------------\n"
									+ "ProductID     Name                                         Category            Brand                      Price  StockQuantity \n"
									+ "---------------------------------------------------------------------------------------------------------------------------------"
									+ "-----------------------------------------------\n";
							while (re.next()) {
								productID = re.getString(1);
								proName = re.getString(2);
								proCategory = re.getString(3);
								productCategory = proCategory;
								brand = re.getString(4);
								price = re.getDouble(5);
								stockQty = re.getInt(6);
								resultTable += productID + "   " + proName
										+ "  " + proCategory + "   " + brand
										+ "   " + "$" + price + "   "
										+ stockQty + "\n";
							}
							resultArea.setText(tableHead + resultTable);
							frame.setVisible(false);
							ConnectDB.closeConnection(conn);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							ConnectDB.closeConnection(conn);
							return;
						}
					}
				});
			}
		});

		buttonPanel.buttons[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Frame1 frame = new Frame1("Please input Price Range ");
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);

				frame.btn1.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						/* Fill this function */
						/*
						 * Press this set price range Button, you should be able
						 * to set price range
						 */
						/*
						 * Pressing "Set Price Range" button, a new window will
						 * pop out.
						 */
						/*
						 * Then you can enter "Min_Price" & "Max_Price" and
						 * press "Search" button,
						 */
						/*
						 * and then all products belong to category that you
						 * choose should be shown in the result panel.
						 */
						conn = ConnectDB.openConnection();

						try {
							double minPrice = Double
									.parseDouble(frame.txtfield[0].getText());
							double maxPrice = Double
									.parseDouble(frame.txtfield[1].getText());
							String queryStr = null;
							if (productCategory != null) {
								queryStr = "SELECT pro.ProductID, pro.ProName, pro.ProCategory, pro.Brand, pro.Price, pro.StockQty FROM Products pro"
										+ " WHERE pro.Price >= "
										+ minPrice
										+ " AND pro.Price <= "
										+ maxPrice
										+ "AND pro.ProCategory = '"
										+ productCategory + "'";
							} else {
								queryStr = "SELECT pro.ProductID, pro.ProName, pro.ProCategory, pro.Brand, pro.Price, pro.StockQty FROM Products pro"
										+ " WHERE pro.Price > "
										+ minPrice
										+ " AND pro.Price < " + maxPrice;
							}
							SQLOut.append(queryStr + "\n\n");
							setSQLOutput(SQLOut);
							Statement stmt = conn.createStatement();
							ResultSet re = stmt.executeQuery(queryStr);
							String productID = null, proName = null, proCategory = null, brand = null;
							double price = 0;
							int stockQty = 0;
							String resultTable = "";
							String tableHead = "---------------------------------------------------------------------------------------------------------------------"
									+ "----------------------------------------------------------\n"
									+ "ProductID     Name                                         Category            Brand                      Price  StockQuantity \n"
									+ "---------------------------------------------------------------------------------------------------------------------------------"
									+ "-----------------------------------------------\n";
							while (re.next()) {
								productID = re.getString(1);
								proName = re.getString(2);
								proCategory = re.getString(3);
								brand = re.getString(4);
								price = re.getDouble(5);
								stockQty = re.getInt(6);
								resultTable += productID + "   " + proName
										+ "  " + proCategory + "   " + brand
										+ "   " + "$" + price + "   "
										+ stockQty + "\n";
							}
							resultArea.setText(tableHead + resultTable);
							frame.setVisible(false);
							ConnectDB.closeConnection(conn);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							ConnectDB.closeConnection(conn);
							return;
						}
					}
				});

			}
		});

		buttonPanel.buttons[4].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Frame2 frame = new Frame2();
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);

				frame.btn1.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						/* Fill this function */
						// Press "Order Products" button, a new window will pop
						// out.
						// Then you can enter "Product ID" and "Quantity", and
						// then you press continue,
						// the Total Price should be shown correctly.
						// Then, you can press "Place Order" to complete this
						// order.
						// This new order should be synchronized in the
						// database.
						conn = ConnectDB.openConnection();
						try {
							String proToBuy = frame.productID.getText()
									.toString(), totalPrice = null;
							int buyingQty = Integer.parseInt(frame.quantity
									.getText().toString());
							double	singleItemPrice = 0;
							String queryStr = "SELECT pro.Price FROM products pro WHERE pro.ProductID = '"
									+ proToBuy + "'";
							SQLOut.append(queryStr + "\n\n");
							setSQLOutput(SQLOut);
							Statement stmt = conn.createStatement();
							ResultSet re = stmt.executeQuery(queryStr);
							while (re.next()) {
								singleItemPrice = re.getDouble(1);
								totalPrice = "" + buyingQty * singleItemPrice;
							}
							frame.totalPrice.setText(totalPrice);
							ConnectDB.closeConnection(conn);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							ConnectDB.closeConnection(conn);
							return;
						}

					}
				});

				frame.btn2.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						/* Fill this function */
						/*
						 * Press this accept all Button, you should be able to
						 * accept all friend request and add this information
						 * into friend relationship table
						 */
						/*
						 * pop up a standard dialog box to show <succeed or
						 * failed>
						 */
						conn = ConnectDB.openConnection();
						try {
							String proToBuy = null, totalPrice = null, queryStr = null, orderID = null, customerEmail = null, shipAddr = null,
									accNum = null, expDate = null, CSC = null, addrID = null, trackingNum = null;
							int buyingQty, singleItemPrice = 0, orderCount = 0, orderNumInserted = 0;
							
							proToBuy = frame.productID.getText()
									.toString();
                            buyingQty = Integer.parseInt(frame.quantity
									.getText().toString());
                            
							queryStr = "SELECT COUNT(*) AS RS FROM Orders ";
							SQLOut.append(queryStr + "\n\n");
							setSQLOutput(SQLOut);
							Statement stmt = conn.createStatement();
							ResultSet re = stmt.executeQuery(queryStr);
							re = stmt.executeQuery(queryStr);
							re.next();
							orderCount = re.getInt("RS");
							orderNumInserted = orderCount + 1;
							orderID = "O" + orderNumInserted;
							
							queryStr = "SELECT o.TrackingNum From Orders o ";
							SQLOut.append(queryStr + "\n\n");
							setSQLOutput(SQLOut);
							re = stmt.executeQuery(queryStr);
							if(re.next()){
								trackingNum = re.getString(1);
								trackingNum = Integer.parseInt(trackingNum) + orderCount + "";
							}
							
							queryStr = "SELECT us.Accnumber, us.Expdate, us.CSC, us.AddrID FROM Users us WHERE us.UserEmail = '" + loginPanel.username.getText().toString()
									 + "'";
							SQLOut.append(queryStr + "\n\n");
							setSQLOutput(SQLOut);
							re = stmt.executeQuery(queryStr);
							if(re.next()){
								accNum = re.getString(1);
								expDate = re.getString(2);
								CSC = re.getString(3);
								addrID = re.getString(4);
							}

							Calendar c = Calendar.getInstance();
							int month = c.get(Calendar.MONTH) + 1;
							int day = c.get(Calendar.DAY_OF_MONTH);
							int year = c.get(Calendar.YEAR);
							String curDate = "0" + month + "-"+ day + "-" + year;
							/*queryStr = "INSERT INTO Orders (OrderID, ShippedTime, EstiArriTime, SignedTime, TotalPrice, "
									+ "TrackingNum, PlaceTime, AccNum, ExpDate, CSC, FillTime, CustomerEmail, ShipAddr) VALUES ('"
									+ orderID + "', null, null, null, " + Double.parseDouble(frame.totalPrice.getText())  
									+ ", '" + trackingNum + "', null, '" +accNum + "', '" + expDate + "', '" + CSC + "', to_date('"
									+ curDate + "', ' mm-dd-yyyy'), '" + loginPanel.username.getText().toString() + "', null" + ")";*/
							queryStr = "INSERT INTO Orders (OrderID, ShippedTime, EstiArriTime, SignedTime, TotalPrice, "
									+ "TrackingNum, PlaceTime, AccNum, ExpDate, CSC, FillTime, CustomerEmail, ShipAddr) VALUES ('"
									+ orderID + "', null, null, null, " + Double.parseDouble(frame.totalPrice.getText())  
									+ ", '" + trackingNum + "', null, null, null, null"  + ", to_date('"
									+ curDate + "', ' mm-dd-yyyy'), '" + loginPanel.username.getText().toString() + "', null" + ")";
							SQLOut.append(queryStr + "\n\n");
							setSQLOutput(SQLOut);
							stmt.executeQuery(queryStr);

							queryStr = "INSERT INTO OrderProductQty (OrderID, ProductID, Quantity) VALUES ('" + orderID + "', '" + frame.productID.getText()
									.toString() + "', " + Integer.parseInt(frame.quantity.getText().toString()) + ")";
							SQLOut.append(queryStr + "\n\n");
							setSQLOutput(SQLOut);
							stmt.executeQuery(queryStr);
							
							queryStr = "UPDATE Products pro set pro.StockQty = pro.StockQty - " + Integer.parseInt(frame.quantity.getText().toString())
									+ " WHERE pro.ProductID = '" + frame.productID.getText().toString() + "'";
							SQLOut.append(queryStr + "\n\n");
							setSQLOutput(SQLOut);
							stmt.executeQuery(queryStr);
							
							queryStr = "UPDATE Productseller pro set pro.quantity = pro.quantity - " + Integer.parseInt(frame.quantity.getText().toString())
									+ " WHERE pro.ProductID = '" + frame.productID.getText().toString() + "'";
							SQLOut.append(queryStr + "\n\n");
							setSQLOutput(SQLOut);
							stmt.executeQuery(queryStr);
							
							frame.setVisible(false);
							ConnectDB.closeConnection(conn);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							ConnectDB.closeConnection(conn);
							return;
						}

					}
				});

			}
		});
		buttonPanel.buttons[5].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				StringBuffer result = new StringBuffer();
				/* Fill this function */
				/*
				 * Press "Your Orders", all order history of this customer
				 * should be shown in the result panel.
				 */
				conn = ConnectDB.openConnection();
				try {
					String orderID = null, shippedTime = null, estiArriTime = null, signedTime = null, 
							trackingNum = null, placeTime = null, accNum = null, expDate = null, CSC = null, fillTime = null, customerEmail = null,
							shipAddr = null;
					int  totalPrice = 0;
					customerEmail = loginPanel.username
							.getText().toString();
					String queryStr = "SELECT * From Orders o WHERE o.CustomerEmail = '" + customerEmail + "'";
					SQLOut.append(queryStr + "\n\n");
					setSQLOutput(SQLOut);
					Statement stmt = conn.createStatement();
					ResultSet re = stmt.executeQuery(queryStr);
					String resultTable = "";
					String tableHead = "---------------------------------------------------------------------------------------------------------------------"
							+ "----------------------------------------------------------\n"
							+ "OrderID     ShippedTime    EstiArriTime    SignedTime    TotalPrice    TrackingNum     PlaceTime    AccNum           ExpDate    "
							+ "CSC    FillTime    CustomerEmail               ShipAddr \n"
							+ "---------------------------------------------------------------------------------------------------------------------------------"
							+ "-----------------------------------------------\n";
					while (re.next()) {
						orderID = re.getString(1);
						shippedTime = re.getString(2);
						estiArriTime = re.getString(3);
						signedTime = re.getString(4);
						totalPrice = re.getInt(5);
						trackingNum = re.getString(6);
						placeTime = re.getString(7);
						accNum = re.getString(8);
						expDate = re.getString(9);
						CSC = re.getString(10);
						fillTime = re.getString(11);
						shipAddr = re.getString(13);
						resultTable += orderID + "   " + shippedTime + "   " + estiArriTime + "   " + signedTime + "   " + "$" + totalPrice + "   " + trackingNum +
								"   " + placeTime + "   " + accNum + "   " + expDate +"   " +  CSC + "   " + fillTime + "   " + customerEmail + "   " + shipAddr + "\n";
					}
					resultArea.setText(tableHead + resultTable);
					ConnectDB.closeConnection(conn);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					ConnectDB.closeConnection(conn);
					return;
				}
			}
		});

		buttonPanel.buttons[6].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Frame5 frame = new Frame5("Product ID : ", "Review : ");
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);

				frame.btn1.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						/* Fill this function */
						/*
						 * Press "Review Products" button, a new window will pop
						 * out.
						 */
						/*
						 * Input product ID and review content and press the OK
						 * button, this information should be inserted into
						 * database.
						 */
						conn = ConnectDB.openConnection();
						try{
						String reviewID = null, proID = null, content = null, queryStr = null;
						int reviewCount = 0, reviewNumInserted = 0;
						proID = frame.txtfield.getText();
						content = frame.textArea.getText();
						
						queryStr = "SELECT COUNT(*) AS RS FROM Reviews ";
						SQLOut.append(queryStr + "\n\n");
						setSQLOutput(SQLOut);
						Statement stmt = conn.createStatement();
						ResultSet re = stmt.executeQuery(queryStr);
						re.next();
						reviewCount = re.getInt("RS");
						reviewNumInserted = reviewCount + 1;
						reviewID = "R" + reviewNumInserted;
						
						Calendar c = Calendar.getInstance();
						int month = c.get(Calendar.MONTH) + 1;
						int day = c
								.get(Calendar.DAY_OF_MONTH);
						int year = c.get(Calendar.YEAR);
						String curDate = "0" + month + "-"
								+ day + "-" + year;
						queryStr = "INSERT INTO Reviews (ReviewID, AuthorID, Rating, Comtent, ProductID, PostTime) VALUES ('" +
						reviewID + "', '" + loginPanel.username.getText().toString() + "', null, '" + content + "', '" + proID +
						"', to_date('" + curDate + "', ' mm-dd-yyyy'))";
						SQLOut.append(queryStr + "\n\n");
						setSQLOutput(SQLOut);
						re = stmt.executeQuery(queryStr);
						frame.setVisible(false);
						ConnectDB.closeConnection(conn);
						
						}catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							ConnectDB.closeConnection(conn);
							return;
						}
					}
				});

			}
		});
		buttonPanel.buttons[7].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Frame4 frame = new Frame4("Product ID : ", "Submit");
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);

				frame.btn1.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						/* Fill this function */
						/*
						 * Press "List All Reviews" button, a new window will
						 * pop out.
						 */
						/*
						 * Input "Product ID" and press submit, all reviews
						 * about this product should be shown in the result
						 * panel.
						 */
						conn = ConnectDB.openConnection();
						String queryStr = null, proID = null, reviewID = null, authorID = null, rating = null, content = null, postTime = null, resultTable = "";
						String tableHead = "---------------------------------------------------------------------------------------------------------------------"
								+ "----------------------------------------------------------\n"
								+ "ReviewID     AuthorID    Rating    Content                                              ProductID     PostTime    \n"
								+ "---------------------------------------------------------------------------------------------------------------------------------"
								+ "-----------------------------------------------\n";
						proID = frame.txtfield.getText();
						queryStr = "SELECT * FROM Reviews v WHERE v.ProductID = '" + proID + "'";
						SQLOut.append(queryStr + "\n\n");
						setSQLOutput(SQLOut);
						try{
						Statement stmt = conn.createStatement();
						ResultSet re = stmt.executeQuery(queryStr);
						while(re.next()){
							reviewID = re.getString(1);
							authorID = re.getString(2);
							rating = re.getString(3);
							content = re.getString(4);
							postTime = re.getString(6);
							resultTable += reviewID + "   " + authorID + "   " + rating + "   " + content + "   " + proID + "   " + postTime + "\n";	
						}
						resultArea.setText(tableHead + resultTable);
						frame.setVisible(false);
						ConnectDB.closeConnection(conn);
						}catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							ConnectDB.closeConnection(conn);
							return;
						}
						
						
					}
				});

			}
		});

		buttonPanel.buttons[8].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Frame4 frame = new Frame4("Review ID : ", "Like it");
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);

				frame.btn1.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						/* Fill this function */
						/*
						 * Press "Like Reviews" button, a new window will pop
						 * out.
						 */
						/*
						 * Input "Review ID" and press "Like it", this
						 * information should be inserted into database.
						 */
						conn = ConnectDB.openConnection();
						String queryStr = null, reviewID = null;
						reviewID = frame.txtfield.getText();
						Calendar c = Calendar.getInstance();
						int month = c.get(Calendar.MONTH) + 1;
						int day = c.get(Calendar.DAY_OF_MONTH);
						int year = c.get(Calendar.YEAR);
						String curDate = "0" + month + "-" + day + "-" + year;
						queryStr = "INSERT INTO LikeReview (CustomerEmail, ReviewID, LikeTime) VALUES ('" + loginPanel.username.getText().toString()
								+ "', '" + reviewID + "', to_date('"+ curDate + "', ' mm-dd-yyyy'))";
						SQLOut.append(queryStr + "\n\n");
						setSQLOutput(SQLOut);
						try{
							Statement stmt = conn.createStatement();
							stmt.executeQuery(queryStr);
							frame.setVisible(false);
							ConnectDB.closeConnection(conn);
						}catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							ConnectDB.closeConnection(conn);
							return;
						}
					}
				});

			}
		});

		buttonPanel.buttons[9].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				StringBuffer result = new StringBuffer();
				/* Fill this function */
				/*
				 * Press "List All Likes" button, all reviews that liked by this
				 * customer should be shown in the result panel.
				 */
				conn = ConnectDB.openConnection();
				String queryStr = null, reviewID = null, authorID = null, rating = null, content = null, proID = null, postTime = null, resultTable = "";
				queryStr = "SELECT * FROM Reviews r WHERE r.ReviewID IN (SELECT l.ReviewID FROM LikeReview l WHERE l.CustomerEmail = '" + 
						loginPanel.username.getText().toString() + "')";
				SQLOut.append(queryStr + "\n\n");
				setSQLOutput(SQLOut);
				String tableHead = "---------------------------------------------------------------------------------------------------------------------"
						+ "----------------------------------------------------------\n"
						+ "ReviewID     AuthorID    Rating    Content                                              ProductID     PostTime    \n"
						+ "---------------------------------------------------------------------------------------------------------------------------------"
						+ "-----------------------------------------------\n";
				try{
					Statement stmt = conn.createStatement();
					ResultSet re = stmt.executeQuery(queryStr);
					while(re.next()){
						reviewID = re.getString(1);
						authorID = re.getString(2);
						rating = re.getString(3);
						content = re.getString(4);
						proID = re.getString(5);
						postTime = re.getString(6);
						resultTable += reviewID + "   " + authorID + "   " + rating + "   " + content + "   " + proID + "   " + postTime + "\n";	
					}
					resultArea.setText(tableHead + resultTable);
					ConnectDB.closeConnection(conn);
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					ConnectDB.closeConnection(conn);
					return;
				}
			}
		});
		buttonPanel.buttons[10].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				StringBuffer result = new StringBuffer();
				/* Fill this function */
				/*
				 * Press "Nearest Seller" button, the nearest seller info for
				 * this customer should be shown in the result panel.
				 */
				/* This is a spatial query */
				conn = ConnectDB.openConnection();
				String queryStr = null, sellerEmail = null, resultTable = "";
				double latitude = 0, longitude = 0;
				try{

				queryStr = " select addr.Latitude, addr.Longitude from users us , addresses addr where us.useremail = '" + loginPanel.username.getText().toString() + "' "
						+ "and us.addrid = addr.addrid";
				SQLOut.append(queryStr + "\n\n");
				setSQLOutput(SQLOut);
									Statement stmt = conn.createStatement();
					ResultSet re = stmt.executeQuery(queryStr);
					if(re.next()){
						latitude = Double.parseDouble(re.getString(1));
						longitude = Double.parseDouble(re.getString(2));
					}
					
				queryStr = " SELECT se.useremail FROM sellers se , users us WHERE se.useremail = us.useremail AND "
						+ "us.addrid     IN (SELECT addr.addrid FROM addresses addr WHERE (addr.addrid IN "
						+ "(SELECT g.addrid FROM addrgeoloc g WHERE mdsys.sdo_nn(g.geoloc, SDO_GEOMETRY(2001,NULL, "
						+ "SDO_POINT_TYPE("+ latitude + ", " + longitude +", NULL), NULL, NULL)) = 'TRUE' ))) and rownum =1";
				SQLOut.append(queryStr + "\n\n");
				setSQLOutput(SQLOut);
				String tableHead = "---------------------------------------------------------------------------------------------------------------------"
						+ "----------------------------------------------------------\n"
						+ "SellerEmail    \n"
						+ "---------------------------------------------------------------------------------------------------------------------------------"
						+ "-----------------------------------------------\n";
			

					re = stmt.executeQuery(queryStr);
					while(re.next()){
						sellerEmail = re.getString(1);
						resultTable += sellerEmail + "\n";	
					}
					resultArea.setText(tableHead + resultTable);
			
					ConnectDB.closeConnection(conn);
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					ConnectDB.closeConnection(conn);
					return;
				}
			}
		});

		buttonPanel.buttons[11].addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final Frame3 frame = new Frame3("Please input coordinate: ");
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);

				frame.btn1.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						/* Fill this function */
						/*
						 * Press this Button, input left top corner coordinate
						 * and right down corner coordinate
						 */
						/*
						 * press ok, you should be able list the
						 * information(including address information) about
						 * seller who lives in this area. Close query window
						 */
						/* This is a spatial query */
						conn = ConnectDB.openConnection();
						String queryStr = null, customerEmail = null, resultTable = ""; 
						double topLeftX = 0, topLeftY = 0, bottomRightX = 0, bottomRightY = 0;
						topLeftX = Double.parseDouble(frame.txtfield[0].getText());
						topLeftY = Double.parseDouble(frame.txtfield[1].getText());
						bottomRightX = Double.parseDouble(frame.txtfield[2].getText());
						bottomRightY = Double.parseDouble(frame.txtfield[3].getText());
						queryStr = " SELECT cu.useremail FROM customers cu , users us WHERE cu.useremail = us.useremail AND us.addrid     IN "
								+ "(SELECT addr.addrid FROM addresses addr WHERE (addr.addrid IN (SELECT g.addrid FROM addrgeoloc g WHERE "
								+ "mdsys.sdo_filter(g.geoloc, (mdsys.sdo_geometry(2003, NULL,NULL,sdo_elem_info_array(1,1003,3), sdo_ordinate_array("
								+ topLeftX + ", " + topLeftY + ", " + bottomRightX + ", " + bottomRightY +")))) = 'TRUE' )))";
						SQLOut.append(queryStr + "\n\n");
						setSQLOutput(SQLOut);
						String tableHead = "---------------------------------------------------------------------------------------------------------------------"
								+ "----------------------------------------------------------\n"
								+ "CustomerEmail    \n"
								+ "---------------------------------------------------------------------------------------------------------------------------------"
								+ "-----------------------------------------------\n";
						try{
							Statement stmt = conn.createStatement();
							ResultSet re = stmt.executeQuery(queryStr);
							while(re.next()){
								customerEmail = re.getString(1);
								resultTable += customerEmail + "\n";	
							}
							resultArea.setText(tableHead + resultTable);
							frame.setVisible(false);
							ConnectDB.closeConnection(conn);
						}catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							ConnectDB.closeConnection(conn);
							return;
						}
					}
				});
			}
		});

	}

	public void setSQLOutput(StringBuffer sb) {
		sqlPanel.SQLArea.setText(sb.toString());
		sqlPanel.SQLArea.setEnabled(true);
	}

	public void setSqlPanel() {
		sqlPanel = new SqlPanel();
		showLabel = new JLabel("The corresponding SQL sentence:");
		showLabel.setBounds(30, 490, 400, 20);
		sqlPanel.setBounds(5, 515, 790, 150);
		this.add(sqlPanel);
		this.add(showLabel);
	}

	public void setLoginPanel() {
		loginPanel = new LoginPanel();
		this.add(loginPanel);

		loginPanel.signup.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				signUpPanel.enablePanel();
			}
		});
		loginPanel.login.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// buttonPanel.enablePanel();
				/* Fill this function */
				/*
				 * Press this Button, you should be able match the user
				 * information. If valid, keep the user email information(but
				 * can't modified) and clear the password
				 */
				/*
				 * If invalid, you should pop up a dialog box to notify user,
				 * then enable signup panel for user to register
				 */
				/*
				 * After logged in, you should change this button's function as
				 * logout which means disable all the panel, return to the
				 * original state
				 */
				if (trigger == 0) {
					// match account
					conn = ConnectDB.openConnection();
					String QueryStr = "select UserEmail from customers where UserEmail='"
							+ loginPanel.username.getText()
							+ "' and custPassword = '"
							+ loginPanel.password.getText() + "'";
					SQLOut.append(QueryStr + "\n\n");
					try {
						Statement stmt = conn.createStatement();
						ResultSet re = stmt.executeQuery(QueryStr);
						if (re.next()) {
							loginPanel.setUserName(loginPanel.username
									.getText().toString());
							loginPanel.disablePanel();
							loginPanel.password.setText("");
							trigger = 1;
							loginPanel.login.setText("logout");
							signUpPanel.disablePanel();
							buttonPanel.enablePanel();
							loginPanel.signup.setEnabled(false);

						} else {
							JOptionPane.showMessageDialog(null,
									"No ... please signup");
							signUpPanel.enablePanel();
							// loginPanel.disablePanel();
						}
						ConnectDB.closeConnection(conn);

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						ConnectDB.closeConnection(conn);
						return;
					}

					// getnotification

				} else {
					loginPanel.login.setText("login");
					loginPanel.enablePanel();
					loginPanel.signup.setEnabled(true);
					loginPanel.password.setText("");
					loginPanel.username.setText("");
					disableResult();
					trigger = 0;
					buttonPanel.disablePanel();
				}
				setSQLOutput(SQLOut);
			}

		});

	}

	public void setSignupPanel() {

		signUpPanel = new SignupPanel();
		this.add(signUpPanel);
		signUpPanel.signup.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				/* Fill this function */
				/*
				 * Press this signup button, you should be able check whether
				 * current account is existed. If existed, pop up an error, if
				 * not check input validation(You can design this part according
				 * to your database table's restriction) create the new account
				 * information
				 */
				/* pop up a standard dialog box to show <succeed or failed> */

				conn = ConnectDB.openConnection();
				String userEmail = signUpPanel.email.getText();
				String custPassword = signUpPanel.password.getText();
				String reenterPassword = signUpPanel.password2.getText();
				String firstName = signUpPanel.fname.getText();
				String lastName = signUpPanel.lname.getText();
				String birthday = signUpPanel.birthday.getText();
				String streetNum = signUpPanel.str_no.getText();
				String streetAddr = signUpPanel.str_address.getText();
				String city = signUpPanel.city.getText();
				String state = signUpPanel.state.getText();
				String zip = signUpPanel.zip.getText();
				if ("".equals(userEmail) || "".equals(custPassword)
						|| "".equals(reenterPassword) || "".equals(firstName)
						|| "".equals(lastName) || "".equals(birthday)
						|| "".equals(streetNum) || "".equals(streetAddr)
						|| "".equals(city) || "".equals(state)
						|| "".equals(zip)) {
					JOptionPane.showMessageDialog(null,
							"Sign up failed. No blank can be empty.");
				} else {
					String queryStr = "SELECT UserEmail FROM Customers C WHERE C.UserEmail = '"
							+ userEmail + "'";
					SQLOut.append(queryStr + "\n\n");
					setSQLOutput(SQLOut);
					try {
						Statement stmt = conn.createStatement();
						ResultSet re = stmt.executeQuery(queryStr);
						if (re.next() == false) {
							if ((signUpPanel.password.getText())
									.equals(signUpPanel.password2.getText()) == false) {
								JOptionPane
										.showMessageDialog(null,
												"Sign up failed.Passwords do not match.");
							} else {
								String pattern1 = "\\d{2}-\\d{2}-\\d{4}";
								if (Pattern.matches(pattern1, birthday) == false) {
									JOptionPane
											.showMessageDialog(null,
													"Sign up failed. Date format wrong. Should be mm-dd-yyyy.");
								} else {
									String pattern2 = "\\d+";
									if (Pattern.matches(pattern2, streetNum) == false) {
										JOptionPane
												.showMessageDialog(null,
														"Sign up failed. StreetNumber should be a sequence of number.");
									} else {
										String pattern3 = "\\d+";
										if (Pattern.matches(pattern3, zip) == false) {
											JOptionPane
													.showMessageDialog(null,
															"Sign up failed. Zip should be a sequence of number.");
										} else {
											Calendar c = Calendar.getInstance();
											int month = c.get(Calendar.MONTH) + 1;
											int day = c
													.get(Calendar.DAY_OF_MONTH);
											int year = c.get(Calendar.YEAR);
											String curDate = "0" + month + "-"
													+ day + "-" + year;
											queryStr = "SELECT COUNT(*) AS RS FROM Addresses ";
											re = stmt.executeQuery(queryStr);
											re.next();
											int addrCount = re.getInt("RS");
											int addrNumInserted = addrCount + 1;
											SQLOut.append(queryStr + addrCount
													+ "\n\n");
											setSQLOutput(SQLOut);
											String addrID = "A"
													+ addrNumInserted;
											queryStr = "INSERT INTO Addresses (AddrID, StreetAddr, AptNumber, City, Zip, State, Latitude, Longitude, MobilePhone) "
													+ "VALUES ('"
													+ addrID
													+ "', '"
													+ streetAddr
													+ "', '"
													+ streetNum
													+ "', '"
													+ city
													+ "', '"
													+ zip
													+ "', '"
													+ state
													+ "', "
													+ "null"
													+ ", "
													+ "null"
													+ ", "
													+ "null"
													+ ")";
											SQLOut.append(queryStr + "\n\n");
											setSQLOutput(SQLOut);
											stmt.executeQuery(queryStr);
											queryStr = "INSERT INTO Users (UserEmail, BirthDate, BirthMonth, Age, FName, LName, AccNumber, ExpDate, "
													+ "CSC, NickName, Gender,JoinTime, PhotoID, AddrID) VALUES ('"
													+ userEmail
													+ "', "
													+ "to_date('"
													+ birthday
													+ "', 'mm-dd-yyyy')"
													+ ", "
													+ "null"
													+ ", "
													+ "Null"
													+ ", '"
													+ firstName
													+ "', '"
													+ lastName
													+ "', "
													+ "null"
													+ ", "
													+ "null"
													+ ", "
													+ "null"
													+ ", "
													+ "null"
													+ ", "
													+ "null"
													+ ", "
													+ "to_date('"
													+ curDate
													+ "', ' mm-dd-yyyy')"
													+ ", "
													+ "null"
													+ ", '"
													+ addrID + "')";
											SQLOut.append(queryStr + "\n\n");
											setSQLOutput(SQLOut);
											stmt.executeQuery(queryStr);
											queryStr = "INSERT INTO Customers (UserEmail, CustPassword) VALUES ('"
													+ userEmail
													+ "', '"
													+ custPassword + "')";
											SQLOut.append(queryStr + "\n\n");
											setSQLOutput(SQLOut);
											stmt.executeQuery(queryStr);
											queryStr = "INSERT INTO AddrGeoLoc (AddrID, GeoLoc) VALUES ('"
													+ addrID
													+ "', SDO_GEOMETRY(2001,NULL, SDO_POINT_TYPE(" +"0,"+"0" +", NULL), NULL, NULL)"
													+  ")";
											SQLOut.append(queryStr + "\n\n");
											setSQLOutput(SQLOut);
											stmt.executeQuery(queryStr);
											JOptionPane.showMessageDialog(null,
													"Sign up succeed!");
											signUpPanel.email.setText("");
											signUpPanel.password.setText("");
											signUpPanel.password2.setText("");
											signUpPanel.fname.setText("");
											signUpPanel.lname.setText("");
											signUpPanel.city.setText("");
											signUpPanel.birthday.setText("");
											signUpPanel.str_no.setText("");
											signUpPanel.str_address.setText("");
											signUpPanel.zip.setText("");
											signUpPanel.state.setText("");
											signUpPanel.disablePanel();
											ConnectDB.closeConnection(conn);
										}
									}
								}
							}
						} else {
							JOptionPane
									.showMessageDialog(null,
											"Sign up failed. UserName has already existed.");

						}

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						ConnectDB.closeConnection(conn);
						return;

					}
				}
			}
		});

		signUpPanel.disablePanel();

	}

}

class ConnectDB {

	public static Connection openConnection() {
		try {
			String driverName = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driverName);

			// set the username and password for your connection.
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			String uname = "xinshi";
			String pwd = "Sx_19880625";

			return DriverManager.getConnection(url, uname, pwd);
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found");
			e.printStackTrace();
			return null;
		} catch (SQLException sqle) {
			System.out.println("Connection Failed");
			sqle.printStackTrace();
			return null;
		}

	}

	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("connection closing error ");
		}
	}
}

public class Assignment2 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
