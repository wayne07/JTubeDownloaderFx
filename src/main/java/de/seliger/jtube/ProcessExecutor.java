package de.seliger.jtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import de.seliger.jtube.notify.OutputChangedListener;

public class ProcessExecutor {

    private static final Logger LOGGER = Logger.getLogger(ProcessExecutor.class);

    private final CommandBuilder commandBuilder = new CommandBuilder();
    private final List<OutputChangedListener> outputChangedListeners = Lists.newArrayList();

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

    public void addOuputChangedListener(OutputChangedListener listener) {
        outputChangedListeners.add(listener);
    }

    private void executeProcess(final String[] cmdarray) throws IOException, InterruptedException {
        String commandAsString = Joiner.on(" ").join(cmdarray);
        LOGGER.info("executing command: " + commandAsString);

        new Thread(new BackgroundProcess(cmdarray, this)).start();
    }

    public static class BackgroundProcess implements Runnable {

        private final String[] cmdarray;
        private final ProcessExecutor processExecutor;

        public BackgroundProcess(String[] cmdarray, ProcessExecutor processExecutor) {
            this.cmdarray = cmdarray;
            this.processExecutor = processExecutor;
        }

        @Override
        public void run() {
            try {
                runProcess();
            } catch (Exception e) {
                throw new RuntimeException("Fehler beim Download", e);
            }
        }

        public void runProcess() throws IOException, InterruptedException {
            Process process = Runtime.getRuntime().exec(cmdarray);

            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                processExecutor.informListener(line);
                LOGGER.info(line);
            }

            BufferedReader inputError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String lineError;
            while ((lineError = inputError.readLine()) != null) {
                processExecutor.informListener(lineError);
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

    private void informListener(String line) {
        for (OutputChangedListener listener : outputChangedListeners) {
            listener.lineAdded(line);
        }
    }

}
