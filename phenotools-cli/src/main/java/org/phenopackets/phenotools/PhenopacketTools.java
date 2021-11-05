package org.phenopackets.phenotools;

import org.phenopackets.phenotools.command.ConvertCommand;
import org.phenopackets.phenotools.command.ExamplesCommand;
import org.phenopackets.phenotools.command.ValidateCommand;
import picocli.AutoComplete;

import static picocli.CommandLine.*;

@Command(name = "phenotools",
        version = "0.0.2",
        mixinStandardHelpOptions = true,
        subcommands = {
                // see https://picocli.info/autocomplete.html
                AutoComplete.GenerateCompletion.class,
                ExamplesCommand.class,
                ConvertCommand.class,
                ValidateCommand.class
        }
)
public class PhenopacketTools {
}
