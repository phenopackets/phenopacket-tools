package org.phenopackets.phenopackettools;

import org.phenopackets.phenopackettools.command.ConvertCommand;
import org.phenopackets.phenopackettools.command.ExamplesCommand;
import org.phenopackets.phenopackettools.command.HtmlOutputCommand;
import org.phenopackets.phenopackettools.command.ValidateCommand;
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
                HtmlOutputCommand.class,
                ValidateCommand.class
        }
)
public class PhenopacketTools {
}
