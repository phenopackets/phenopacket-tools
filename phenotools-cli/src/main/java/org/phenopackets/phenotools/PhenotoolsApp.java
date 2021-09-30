package org.phenopackets.phenotools;



import org.phenopackets.phenotools.command.ExamplesCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;


@CommandLine.Command(name = "Phenotools builder", version = "0.0.1", mixinStandardHelpOptions = true)
public class PhenotoolsApp implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhenotoolsApp.class);


    public static void main(String[] args) {
        if (args.length == 0) {
            // if the user doesn't pass any command or option, add -h to show help
            args = new String[]{"-h"};
        }
        CommandLine cline = new CommandLine(new PhenotoolsApp())
                .addSubcommand("examples", new ExamplesCommand());
        cline.setToggleBooleanFlags(false);
        int exitCode = cline.execute(args);
        System.exit(exitCode);
    }


    @Override
    public void run() {
        // work done in subcommands
    }
}