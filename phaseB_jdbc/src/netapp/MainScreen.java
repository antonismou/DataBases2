package netapp;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends JFrame implements ActionListener{
    private Connection conn;

    private String userEmail;
    
    private JPanel personalDetailsPanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField dobField;
    private JTextField countryField;
    private JButton updateButton;
    
    private JList<Member> networkList;
    private DefaultListModel<Member> networkListModel;
    
    

    public MainScreen(Connection conn, String userEmail) {
        this.conn = conn;
        this.userEmail = userEmail;

        setTitle("Home");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
        personalDetailsPanel = new JPanel(new GridLayout(5, 2,10,10));
        personalDetailsPanel.setMaximumSize(new Dimension(600,200));
        personalDetailsPanel.setBorder(new EmptyBorder(10,10,10,10));
        personalDetailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        personalDetailsPanel.add(new JPanel(new FlowLayout(FlowLayout.TRAILING)).add(new JLabel("First Name:")));
        firstNameField = new JTextField();
        personalDetailsPanel.add(firstNameField);

        personalDetailsPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        personalDetailsPanel.add(lastNameField);

        personalDetailsPanel.add(new JLabel("Date of Birth:"));
        dobField = new JTextField();
        personalDetailsPanel.add(dobField);
        
        personalDetailsPanel.add(new JLabel("Country:"));
        countryField = new JTextField();
        personalDetailsPanel.add(countryField);

        personalDetailsPanel.add(new JLabel());
        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        personalDetailsPanel.add(updateButton);

        networkListModel = new DefaultListModel<>();
        networkList = new JList<>(networkListModel);
        networkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
        add(personalDetailsPanel);
        JLabel nusers = new JLabel("My Network");
        nusers.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(nusers);
        JScrollPane jpane = new JScrollPane(networkList);
        jpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        jpane.setBorder(new EmptyBorder(10,10,10,10));
        add(jpane);

        fetchPersonalDetails();
        fetchNetwork();
    }
    
    public void showMessage(String msg) {
    	JOptionPane.showMessageDialog(null, msg);
    }
    
    private void fetchPersonalDetails() {
    	// Write code to show the appropriate values in the JTextFields
    	PreparedStatement st;
		try {
			st = conn.prepareStatement("select \"firstName\",\"secondName\",\"dateOfBirth\",country from member where email = ?");
			st.setString(1,userEmail);
	    	ResultSet res = st.executeQuery();
	    	res.next();
	    	firstNameField.setText(res.getString("firstName"));
	    	lastNameField.setText(res.getString("secondName"));
	    	dobField.setText(res.getString("dateOfBirth"));
	    	countryField.setText(res.getString("country"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void updatePersonalDetails() {
    	// Write code to update the database with new values in the JTextFields
    }

    private void fetchNetwork() {
    	// Write code to fill the list with connected members
    	PreparedStatement st;
		try {
			st = conn.prepareStatement("SELECT \"firstName\",\"secondName\", m.email FROM connects c JOIN member m ON c.\"connectedWithEmail\" = m.email WHERE c.email = ?");
			st.setString(1,userEmail);
	    	ResultSet res = st.executeQuery();
	    	while(res.next()) {
	    		networkListModel.addElement(new Member(res.getString(1),res.getString(2),res.getString(3)));
	    		System.out.println(res.getString(1) + res.getString(2) + res.getString(3));
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		//Perform necessary actions when the Update button is pressed
		
	}
}
