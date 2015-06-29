package de.seliger.jtube;

import static java.io.File.separator;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FormController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(FormController.class);

    public static final DateTimeFormatter MILLIS_FORMATTER = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS").withLocale(Locale.GERMAN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyyMMdd").withLocale(Locale.GERMAN);

    @FXML
    private CheckBox deleteVideo;
    @FXML
    private TextField targetDirectory;
    @FXML
    private TextField urlToSave;
    @FXML
    private TextField filename;
    @FXML
    private Button btnDownload;

    private String workdir;

    private ProcessExecutor processExecutor = new ProcessExecutor();

    @FXML
    private void doDownload(MouseEvent event) {
        LOGGER.debug("starting Download ...");
        btnDownload.setDisable(true);
        btnDownload.setVisible(false);
        try {
            processExecutor.downloadFrom(urlToSave.getText(), workdir);

//            File tempFile = createTempFileName();
//            doDownloadToTempFile(tempFile, urlToSave.getText());
//            extractAudioFrom(tempFile, filename.getText(), targetDirectory.getText());
//            if (deleteVideo.isSelected()) {
//                deleteTempFile(tempFile);
//            }
        } finally {
            btnDownload.setDisable(false);
            btnDownload.setVisible(true);
            LOGGER.debug("end Download.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.workdir = createTargetDirectory(getUserHome());

        targetDirectory.setText(workdir);
        deleteVideo.setSelected(true);
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

    private void deleteTempFile(File tempFile) {
        tempFile.delete();
    }

    private File createTempFileName() {
        String systemTempPath = System.getProperty("java.io.tmpdir");
        String randomTimeString = MILLIS_FORMATTER.print(new DateTime());
        String name = systemTempPath + separator + "youtube" + separator + "youtube-dl-" + randomTimeString + ".flv";
        LOGGER.info("Temp-File-Name: " + name);
        return new File(name);
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