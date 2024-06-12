package netapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginScreen extends JFrame implements ActionListener {
	private static final String DB_URL = "localhost";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Covid-19";
    private static final String DB_Name = "vaseis2";
    
    private JTextField emailField;
    private JTextField passField;
    private JButton loginButton;
    
    private Connection conn;

    public LoginScreen() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        emailField = new JTextField(20);
        
        passField = new JPasswordField();
        
        loginButton = new JButton("Login");

        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(this);

        checkJDBCdriver();
        dbConnect();
    }
    
    public void showMessage(String msg) {
    	JOptionPane.showMessageDialog(null, msg);
    }
    
    private void checkJDBCdriver() {
    	//Check if a valid driver is available
    	try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    private void dbConnect() {
    	//Connect to professional network database
    	try {
			conn = DriverManager.getConnection("jdbc:postgresql://"+DB_URL+":5432/"+DB_Name, DB_USER, DB_PASSWORD);
			System.out.println("Connection established : "+conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    

    private boolean authenticateUser(String email) {
    	//Check the user and password
    	//String emailTxt = emailField.getText();
    	//String passTxt = passField.getText();
    	
    	PreparedStatement st;
		try {
			st = conn.prepareStatement("select email,\"thePassword\" from member where email = ?");
			st.setString(1,email);
	    	ResultSet res = st.executeQuery();
	    	if(!res.next()){
	    		//check if this email exist
	    		showMessage("Wrong email or password \nTry again");
	    		System.out.println("wrong email");
	    	}else {
	    		//if exist check if the pass is correct
		    	if(res.getString("thePassword").equals(passField.getText())) {
		    		System.out.println("correct");
		    		MainScreen ms = new MainScreen(conn, email);	//create the mainScreen
		    		this.setVisible(false);		//make the loginScreen invisible
		    		ms.setVisible(true);		//make the mainScreen visible
		    	}else {
		    		showMessage("Wrong email or password \nTry again");
		    		System.out.println("wrong pass");
		    	}
	    	}
	    	res.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return false;
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		//Perform necessary actions when the login button is pressed
		authenticateUser(emailField.getText());
	}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });
    }


}
