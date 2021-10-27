package org.phenopackets.phenotools;


import org.phenopackets.phenotools.command.ConvertCommand;
import org.phenopackets.phenotools.command.ExamplesCommand;
import picocli.AutoComplete.GenerateCompletion;
import picocli.CommandLine;

import static picocli.CommandLine.*;


@Command(name = "phenotools",
        version = "0.0.1",
        mixinStandardHelpOptions = true,
        subcommands = {
            // see https://picocli.info/autocomplete.html
            GenerateCompletion.class,
            ExamplesCommand.class,
            ConvertCommand.class
        }
)
public class PhenotoolsApp implements Runnable {

    public static void main(String[] args) {
        if (args.length == 0) {
            // if the user doesn't pass any command or option, add -h to show help
            args = new String[]{"-h"};
        }
        CommandLine cline = new CommandLine(new PhenotoolsApp());
        cline.getSubcommands().get("generate-completion").getCommandSpec().usageMessage().hidden(true);
        int exitCode = cline.execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        // work done in subcommands
    }
}