package de.seliger.jtube;

import java.io.File;

public class CommandBuilder {

    //        youtube-dl -o "%(title)s.%(ext)s" -x -k --audio-format mp3 --audio-quality 0 https://www.youtube.com/watch?v=Fm15TdaI2jY

    public String[] createCommandArray(String urlToDownload, String workdir, boolean includeVideo) {
        String[] cmdarray = new String[9];
        cmdarray[0] = "youtube-dl";
        cmdarray[1] = "-o";
        cmdarray[2] = workdir + File.separator + "%(title)s.%(ext)s";
        cmdarray[3] = "-x";
        cmdarray[4] = "--audio-format";
        cmdarray[5] = "mp3";
        cmdarray[6] = "--audio-quality";
        cmdarray[7] = "0";
        cmdarray[8] = urlToDownload;
        return cmdarray;
    }
}
