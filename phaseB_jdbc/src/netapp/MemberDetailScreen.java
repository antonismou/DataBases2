package netapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDetailScreen extends JFrame {
    private Connection conn;
    private String memberEmail;
    private JTextArea detailsArea;
    private JTextArea messagesArea;
    private JTextArea educationArea;
    private JTextArea experienceArea;

    public MemberDetailScreen(Connection conn, String memberEmail) {
        this.conn = conn;
        this.memberEmail = memberEmail;

        setTitle("Details for :"+memberEmail);
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        detailsArea = new JTextArea(5, 40);
        detailsArea.setEditable(false);
        messagesArea = new JTextArea(10, 40);
        messagesArea.setEditable(false);
        educationArea = new JTextArea(10, 40);
        educationArea.setEditable(false);
        experienceArea = new JTextArea(10, 40);
        experienceArea.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(new EmptyBorder(20,20,20,20));
        
        panel.add(new JLabel("Personal Details"));
        panel.add(new JScrollPane(detailsArea));
        panel.add(new JLabel("Messages"));
        panel.add(new JScrollPane(messagesArea));
        panel.add(new JLabel("Education"));
        panel.add(new JScrollPane(educationArea));
        panel.add(new JLabel("Experience"));
        panel.add(new JScrollPane(experienceArea));

        add(panel);

        fetchMemberDetails();
        fetchMessages();
        fetchEducation();
        fetchExperience();
    }

    private void fetchMemberDetails() {
    	// Write code to show member's details to the appropriate JTextArea
    	PreparedStatement stmt;
    	String query = "SELECT \"firstName\", \"secondName\", \"dateOfBirth\", gender, country FROM member WHERE email = ?";
    	try {
    			stmt = conn.prepareStatement(query);
    			stmt.setString(1, memberEmail);
    			ResultSet rs = stmt.executeQuery();
    			if(rs.next()) {
    				String details = "Name: " + rs.getString("firstName") + "\n" +
    								 "Second Name: " + rs.getString("secondName") + "\n" +
    								 "Date of Birth: " + rs.getString("dateOfBirth") + "\n" +
    								 "Gender: " + rs.getString("gender") + "\n" +
    								 "Country: " + rs.getString("country");
    				detailsArea.setText(details);
    			}
    	} catch(SQLException e) {
    		e.printStackTrace();
    			
    	}
    		
    }

    private void fetchMessages() {
    	// Write code to show messages from member to the appropriate JTextArea
    	String query = "SELECT \"msgID\", \"dateSent\", \"theSubject\", \"theText\", \"senderEmail\", \"receiverEmail\" FROM msg WHERE \"senderEmail\" = ?";
    	PreparedStatement stmt;
    	try {
    		stmt = conn.prepareStatement(query);
    		stmt.setString(1, memberEmail);
    		ResultSet rs = stmt.executeQuery();
    		StringBuilder messages = new StringBuilder();
    		while (rs.next()) {
    			messages.append("To: ").append(rs.getString("receiverEmail")).append("\n")
    			.append("Message: ").append(rs.getString("theText")).append("\n\n");
    		}
    		messagesArea.setText(messages.toString());
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    }

    private void fetchEducation() {
    	// Write code to show member's details about education the appropriate JTextArea
    	String query = "SELECT \"educationID\", country, school, \"eduLevel\", \"categoryID\", \"fromYear\", \"toYear\" FROM education WHERE email = ?";
    	PreparedStatement stmt;
    	try {
    		stmt = conn.prepareStatement(query);
    		stmt.setString(1, memberEmail);
    		ResultSet rs = stmt.executeQuery();
    		StringBuilder education = new StringBuilder();
    		while(rs.next()) {
    			education.append("Country: ").append(rs.getString("country")).append("\n")
    					  .append("School: ").append(rs.getString("school")).append("\n")
    					  .append("Level: ").append(rs.getString("eduLevel")).append("\n")
    					  .append("Category: ").append(rs.getString("categoryID")).append("\n")
    					  .append("From: ").append(rs.getString("fromYear")).append("\n")
    					  .append("To: ").append(rs.getString("toYear")).append("\n");
    		}
    		educationArea.setText(education.toString());
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    }

    private void fetchExperience() {
    	// Write code to show member's professional experience to the appropriate JTextArea
    	String query = "SELECT \"experienceID\", company, \"workStatus\", title, description, \"fromYear\", \"toYear\" FROM experience WHERE email = ?";
    	PreparedStatement stmt;
    	try {
    		stmt = conn.prepareStatement(query);
    		stmt.setString(1, memberEmail);
    		ResultSet rs = stmt.executeQuery();
    		StringBuilder experience = new StringBuilder();
    		while(rs.next()) {
    			experience.append("Company: ").append(rs.getString("company")).append("\n")
    					  .append("Status: ").append(rs.getString("workStatus")).append("\n")
    					  .append("Title: ").append(rs.getString("title")).append("\n")
    					  .append("Description: ").append(rs.getString("description")).append("\n")
    					  .append("From: ").append(rs.getString("fromYear")).append("\n")
    					  .append("To: ").append(rs.getString("toYear")).append("\n");
    		}
    		experienceArea.setText(experience.toString());
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    	}
    }
}

