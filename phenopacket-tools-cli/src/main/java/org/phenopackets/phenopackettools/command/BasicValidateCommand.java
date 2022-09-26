package org.phenopackets.phenopackettools.command;


import org.phenopackets.phenopackettools.command.validate.ValidateCohortCommand;
import org.phenopackets.phenopackettools.command.validate.ValidateFamilyCommand;
import org.phenopackets.phenopackettools.command.validate.ValidatePhenopacketCommand;
import picocli.CommandLine.Command;

@Command(name = "validate",
        description = "Validate top-level elements of the Phenopacket schema.",
        mixinStandardHelpOptions = true,
        subcommands = {
                ValidatePhenopacketCommand.class,
                ValidateFamilyCommand.class,
                ValidateCohortCommand.class
        })
public class BasicValidateCommand {

}