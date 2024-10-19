package ee.ut.math.tvt.salessystem.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class TeamTabController implements Initializable {
    @FXML
    private Label teamName;

    @FXML
    private Label contactPersonLabel;
    @FXML
    private Label contactPersonEmail;

    @FXML
    private Label teamMembers;

    @FXML
    private ImageView teamLogo;
    private Properties teamProperties = new Properties();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try(InputStream stream = TeamTabController.class.getClassLoader().getResourceAsStream("application.properties")){
            if (stream != null) {
                teamProperties.load(stream);
            }
            // Set the team information
            teamName.setText(teamProperties.getProperty("team.name", "Unknown Team"));
            contactPersonLabel.setText(teamProperties.getProperty("team.contactPerson", "Unknown"));
            contactPersonEmail.setText(teamProperties.getProperty("team.contactEmail", "Unknown"));

            // Load the team members into the ListView

            teamMembers.setText(teamProperties.getProperty("team.members", "Unknown"));

            // Load the team logo image
            String logoPath = teamProperties.getProperty("team.image", "/logo.png");

            // Retrieve the image from the resources folder
            URL imageURL = getClass().getResource(logoPath);
            if (imageURL != null) {
                Image image = new Image(imageURL.toExternalForm());
                teamLogo.setImage(image);
            } else {
                System.out.println("Image file not found: " + logoPath);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
}