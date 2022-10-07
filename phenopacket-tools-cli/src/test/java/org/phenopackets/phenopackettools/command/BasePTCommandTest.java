package org.phenopackets.phenopackettools.command;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class BasePTCommandTest {

    @Test
    public void markIsSupportedForStdin() {
        // We need this functionality when sniffing format from STDIN.
        assertThat(System.in.markSupported(), equalTo(true));
    }

}