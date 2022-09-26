package org.phenopackets.phenopackettools;

import org.phenopackets.phenopackettools.command.ConvertCommand;
import org.phenopackets.phenopackettools.command.ExamplesCommand;
import org.phenopackets.phenopackettools.command.HtmlOutputCommand;
import org.phenopackets.phenopackettools.command.BasicValidateCommand;
import picocli.AutoComplete;

import static picocli.CommandLine.*;

@Command(name = "phenopacket-tools",
        version = "0.4.6-SNAPSHOT",
        mixinStandardHelpOptions = true,
        usageHelpWidth = Main.USAGE_WIDTH,
        subcommands = {
                // see https://picocli.info/autocomplete.html
                AutoComplete.GenerateCompletion.class,
                ExamplesCommand.class,
                ConvertCommand.class,
                HtmlOutputCommand.class,
                BasicValidateCommand.class
        }
)
public class PhenopacketTools {
}
