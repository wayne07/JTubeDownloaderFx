package de.seliger.jtube;

import junit.framework.TestCase;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommandBuilderTest {

    private final CommandBuilder commandBuilder = new CommandBuilder();
    private final String url = "http://iwas.de/bla";
    private final String workdir = "/tmp/iwo";

    @Test
    public void commandWithoutVideo() throws Exception {
        String[] expectedCommand = new String[]{"youtube-dl", "-o", workdir + "/%(title)s.%(ext)s", "-x", "--audio-format", "mp3", "--audio-quality", "0", url};

        assertThat(commandBuilder.createCommandArray(url, workdir, false), is(expectedCommand));
    }

    @Test
    public void commandWithVideo() throws Exception {
        String[] expectedCommand = new String[]{"youtube-dl", "-o", workdir + "/%(title)s.%(ext)s", "-x", "--audio-format", "mp3", "--audio-quality", "0", "-k", url};

        assertThat(commandBuilder.createCommandArray(url, workdir, true), is(expectedCommand));
    }
}