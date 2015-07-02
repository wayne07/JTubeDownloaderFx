package de.seliger.jtube.notify;

import javafx.scene.control.TextArea;

public class DownloadProgress implements OutputChangedListener {

    private TextArea downloadStatus;

    public DownloadProgress(TextArea downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    @Override
    public void lineAdded(final String text) {
//        javafx.application.Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                downloadStatus.appendText(text + "\n");
//            }
//        });
        downloadStatus.appendText(text + "\n");
    }

}
