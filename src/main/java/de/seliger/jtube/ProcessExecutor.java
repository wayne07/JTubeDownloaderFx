package de.seliger.jtube;

import com.google.common.base.Joiner;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessExecutor {

    private static final Logger LOGGER = Logger.getLogger(ProcessExecutor.class);

    private final CommandBuilder commandBuilder = new CommandBuilder();

    public void downloadFrom(String urlToDownload, String workdir, boolean includeVideo) {
        String[] cmdarray = commandBuilder.createCommandArray(urlToDownload, workdir, includeVideo);
        try {
            executeProcess(cmdarray);
        } catch (IOException e) {
            LOGGER.error("Fehler: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(null, String.format("Das Programm '%s' wird benötigt. Bitte Papa rufen ;-) ", cmdarray[0]));
        } catch (Exception e) {
            LOGGER.error("Fehler: " + e.getMessage(), e);
        }
    }

    private void executeProcess(String[] cmdarray) throws IOException, InterruptedException {
        String commandAsString = Joiner.on(" ").join(cmdarray);
        LOGGER.info("executing command: " + commandAsString);
        Process process = Runtime.getRuntime().exec(cmdarray);

        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            LOGGER.info(line);
        }

        BufferedReader inputError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String lineError;
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
