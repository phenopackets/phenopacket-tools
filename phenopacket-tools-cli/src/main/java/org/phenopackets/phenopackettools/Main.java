package org.phenopackets.phenopackettools;

import picocli.CommandLine;

public class Main {

    // Maximum number of characters in line of the usage message.
    public static final int USAGE_WIDTH = 120;

    public static void main(String[] args) {
        if (args.length == 0) {
            // if the user doesn't pass any command or option, add -h to show help
            args = new String[]{"-h"};
        }
        CommandLine cline = new CommandLine(new PhenopacketTools());
        cline.getSubcommands().get("generate-completion").getCommandSpec().usageMessage().hidden(true);
        int exitCode = cline.execute(args);
        System.exit(exitCode);
    }
}