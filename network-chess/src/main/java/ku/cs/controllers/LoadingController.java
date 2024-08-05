package ku.cs.controllers;

import ku.cs.net.Client;
import ku.cs.services.LoadService;
import ku.cs.services.ProgressSetter;
import ku.cs.services.RootService;
import ku.cs.services.Data;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class LoadingController {
    @FXML
    public Label titleLabel;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public Label descriptionLabel;
    @FXML
    public ImageView loadIcon;

    @FXML
    public void initialize() {
        URL url = getClass().getResource("/ku/cs/images/ku_logo.png");
        if (url != null) {
            loadIcon.setImage(new Image(url.toExternalForm()));
        }
        RotateTransition rotator = new RotateTransition(Duration.millis(2000), loadIcon);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(360);
        rotator.setInterpolator(Interpolator.EASE_BOTH);
        rotator.setCycleCount(Animation.INDEFINITE);
        rotator.play();

        startLoading();
    }

    private void startLoading() {
        titleLabel.setText("Network Projects");
        descriptionLabel.setText("");
        LoadService.init(this, new ProgressSetter(0));
        new Thread(() -> {
            Font.loadFont(getClass().getResourceAsStream("/ku/cs/fonts/DB Ozone X v3.2.ttf"), 20);
            Font.loadFont(getClass().getResourceAsStream("/ku/cs/fonts/DB Ozone X Bd v3.2.ttf"), 20);
            Data data = Data.getInstance();

            // Fake Loading
            ProgressSetter progressSetter = new ProgressSetter(100);
            LoadService.getLoader().addProgressSetter(progressSetter);

            int n = 100;
            for (int i = 0; i < n / 2; i++) {
                progressSetter.setPercentage((double) i / n);
                LoadService.getLoader().updateBar();
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Client.init("127.0.0.1", (short) 25565);
            } catch (IOException e) {
                Platform.runLater(() -> descriptionLabel.setText(e.getMessage()));

                throw new RuntimeException(e);
            }
            for (int i = 50; i < n; i++) {
                progressSetter.setPercentage((double) i / n);
                LoadService.getLoader().updateBar();
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            RootService.getController().setData(data);
            LoadService.getLoader().close();
            Platform.runLater(() -> RootService.open("mainGameScreen.fxml"));
        }).start();
    }
}
