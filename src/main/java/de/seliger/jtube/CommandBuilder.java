package de.seliger.jtube;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

public class CommandBuilder {

    //        youtube-dl -o "%(title)s.%(ext)s" -x -k --audio-format mp3 --audio-quality 0 https://www.youtube.com/watch?v=Fm15TdaI2jY

    public String[] createCommandArray(String urlToDownload, String workdir, boolean includeVideo) {
        List<String> commands = Lists.newArrayList("youtube-dl", "-o", workdir + File.separator + "%(title)s.%(ext)s", "-x", "--audio-format", "mp3", "--audio-quality", "0", urlToDownload);
        if (includeVideo) {
            commands.add(indexBeforeUrl(commands), "-k");
        }
        return commands.toArray(new String[]{});
    }

    private int indexBeforeUrl(List<String> commands) {
        return commands.size()-1;
    }
}
