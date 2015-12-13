package de.seliger.jtube;

import static java.io.File.separator;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import de.seliger.jtube.notify.DownloadProgress;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FormController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(FormController.class);

    public static final DateTimeFormatter MILLIS_FORMATTER = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS").withLocale(Locale.GERMAN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd").withLocale(Locale.GERMAN);

    @FXML
    private TextField targetDirectory;
    @FXML
    private TextField urlToSave;
    @FXML
    private CheckBox includeVideo;
    @FXML
    private Button btnDownload;
    @FXML
    private TextArea downloadStatus;

    private ProcessExecutor processExecutor = new ProcessExecutor();

    @FXML
    private void doDownload(MouseEvent event) {
        int clickCount = event.getClickCount();
        if (clickCount == 1) {
            LOGGER.debug(String.format("clickcount = %d, starting Download ...", clickCount));
            disableDownloadButton(true);
            try {
                processExecutor.downloadFrom(urlToSave.getText(), targetDirectory.getText(), includeVideo.isSelected());
            } finally {
                disableDownloadButton(false);
                LOGGER.debug("end Download.");
            }
        } else {
            LOGGER.debug(String.format("clickcount = %d, ignoring ...", clickCount));
        }
    }

    private void disableDownloadButton(boolean disable) {
        btnDownload.setDisable(disable);
        btnDownload.setVisible(!disable);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        targetDirectory.setText(createTargetDirectory(getUserHome()));
        includeVideo.setSelected(false);
        processExecutor.addOuputChangedListener(new DownloadProgress(downloadStatus));
    }

    private String getUserHome() {
        return System.getProperty("user.home");
    }

    private String createTargetDirectory(String userHome) {
        String dayString = DATE_FORMATTER.print(new LocalDate());
        String path = userHome + separator + "Musik" + separator + dayString;
        File target = new File(path);
        target.mkdirs();
        return target.getAbsolutePath();
    }

    @FXML
    public void inputChanged(Event event) {
        LOGGER.debug("inputChanged ..");
    }

    @FXML
    public void dragExited(Event event) {
        LOGGER.debug("dragExited ..");
    }

    @FXML
    public void onAction(ActionEvent actionEvent) {
        LOGGER.debug("onAction ..");
    }

    @FXML
    public void dragDone(Event event) {
        LOGGER.debug("dragDone ..");
    }

    @FXML
    public void textChanged(Event event) {
        LOGGER.debug("textChanged ..");
    }

    @FXML
    public void keyTyped(Event event) {
        LOGGER.debug("Download-Status zurücksetzen";
        downloadStatus.setText("");
    }

    @FXML
    public void dragReleased(Event event) {
        LOGGER.debug("dragReleased ..");
    }
}

/*
#!/bin/bash
x=~/tmp/youtube/youtube-dl-$RANDOM-$RANDOM.flv
youtube-dl --output=$x --format=18 "$1"
ffmpeg -i $x -acodec libmp3lame -ac 2 -ab 128k -vn -y "$2"
rm $x

http://www.youtube.com/watch?v=COHQuu9Flho
BROILERS - Ist Da Jemand?

 */