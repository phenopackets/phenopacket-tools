package org.phenopackets.phenopackettools.cli.command;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class BaseCommandTest {

    @Test
    public void markIsSupportedForStdin() {
        // We need this functionality when sniffing format from STDIN.
        assertThat(System.in.markSupported(), equalTo(true));
    }

}