package de.seliger.jtube;

import static java.io.File.separator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.google.common.base.Joiner;

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

    @FXML
    private void doDownload(MouseEvent event) {
        LOGGER.debug("starting Download ...");
        btnDownload.setDisable(true);
        btnDownload.setVisible(false);
        try {
            File tempFile = createTempFileName();
            doDownloadToTempFile(tempFile, urlToSave.getText());
            extractAudioFrom(tempFile, filename.getText(), targetDirectory.getText());
            if (deleteVideo.isSelected()) {
                deleteTempFile(tempFile);
            }
        } finally {
            btnDownload.setDisable(false);
            btnDownload.setVisible(true);
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

    private void extractAudioFrom(File tempFile, String filenameToSave, String targetDir) {
//        String command = "ffmpeg -i $x -acodec libmp3lame -ac 2 -ab 128k -vn -y \"$2\"";
        String finalFileName = String.format("%s.mp3", filenameToSave.trim());
        LOGGER.debug("finalFileName: " + finalFileName);
        String[] cmdarray = new String[14];
        cmdarray[0] = "ffmpeg";
        cmdarray[1] = "-i";
        cmdarray[2] = tempFile.getAbsolutePath();
        cmdarray[3] = "-acodec";
        cmdarray[4] = "libmp3lame";
        cmdarray[5] = "-ac";
        cmdarray[6] = "2";
        cmdarray[7] = "-ab";
        cmdarray[8] = "128k";
        cmdarray[9] = "-f";
        cmdarray[10] = "mp3";
        cmdarray[11] = "-vn";
        cmdarray[12] = "-y";
        cmdarray[13] = finalFileName;
        try {
            executeProcess(cmdarray);
        } catch (IOException e) {
            LOGGER.error("Fehler: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "Das Programm 'ffmpeg' wird benötigt. Bitte Papa rufen ;-) ");
        } catch (InterruptedException e) {
            LOGGER.error("Fehler: " + e.getMessage(), e);
        }
    }

    private void doDownloadToTempFile(File tempFile, String urlToFileDownload) {
//        String command = "youtube-dl --output=" + tempFile.getAbsolutePath() + " --format=18 \"" + urlToFileDownload + "\"";
        String[] cmdarray = new String[4];
        cmdarray[0] = "youtube-dl";
        cmdarray[1] = "--output=" + tempFile.getAbsolutePath();
        cmdarray[2] = "--format=18";
        cmdarray[3] = urlToFileDownload;
        try {
            executeProcess(cmdarray);
        } catch (IOException e) {
            LOGGER.error("Fehler: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "Das Programm 'youtube-dl' wird benötigt. Bitte Papa rufen ;-) ");
        } catch (InterruptedException e) {
            LOGGER.error("Fehler: " + e.getMessage(), e);
        }
    }

    private void executeProcess(String[] cmdarray) throws IOException, InterruptedException {
        String commandAsString = Joiner.on(" ").join(cmdarray);
        LOGGER.info("executing command: " + commandAsString);
        Process process = Runtime.getRuntime().exec(cmdarray, null, new File(workdir));

        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        while ((line = input.readLine()) != null) {
            LOGGER.info(line);
        }

        BufferedReader inputError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String lineError = null;
        while ((lineError = inputError.readLine()) != null) {
            LOGGER.warn(lineError);
        }

        int exitVal = process.waitFor();
        LOGGER.info("Exited with error code " + exitVal);
        if (exitVal > 0) {
            String message = "Der Aufruf des Prozesses '%s' ist fehlgeschlagen. Bitte Papa rufen ;-) ";
            JOptionPane.showMessageDialog(null, String.format(message, cmdarray[0]));
        }
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