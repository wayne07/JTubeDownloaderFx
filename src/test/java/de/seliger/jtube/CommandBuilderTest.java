package de.seliger.jtube;

import junit.framework.TestCase;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommandBuilderTest extends TestCase {

    private final CommandBuilder commandBuilder = new CommandBuilder();

    @Test
    public void commandWithoutVideo() throws Exception {
        String url = "http://iwas.de/bla";
        String workdir ="/tmp/iwo";
        String[] expectedCommand = new String[]{"youtube-dl", "-o", "%(title)s.%(ext)s", "-x -k --audio-format mp3 --audio-quality 0 https://www.youtube.com/watch?v=Fm15TdaI2jY"};

        assertThat(commandBuilder.createCommandArray(url, workdir, false), is(expectedCommand));

    }
}