package org.phenopackets.phenotools;


import org.phenopackets.phenotools.command.ConvertCommand;
import org.phenopackets.phenotools.command.ExamplesCommand;
import picocli.CommandLine;


@CommandLine.Command(name = "phenotools", version = "0.0.1", mixinStandardHelpOptions = true)
public class PhenotoolsApp implements Runnable {

    public static void main(String[] args) {
        if (args.length == 0) {
            // if the user doesn't pass any command or option, add -h to show help
            args = new String[]{"-h"};
        }
        CommandLine cline = new CommandLine(new PhenotoolsApp())
                .addSubcommand("examples", new ExamplesCommand())
                .addSubcommand("convert", new ConvertCommand())
                .setExpandAtFiles(true)
                .setToggleBooleanFlags(false);
        int exitCode = cline.execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        // work done in subcommands
    }
}