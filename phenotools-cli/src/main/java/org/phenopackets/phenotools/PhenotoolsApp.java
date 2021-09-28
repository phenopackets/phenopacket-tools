package org.phenopackets.phenotools;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;


@CommandLine.Command(name = "Phenotools builder", version = "0.0.1", mixinStandardHelpOptions = true)
public class PhenotoolsApp implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhenotoolsApp.class);

    @CommandLine.Option(names = "--hp", required = true, description = "check with HPO (hp.json)")
    public String hpoJsonPath;

    @CommandLine.Option(names = {"-p", "--phenopacket"}, required = true, description = "Phenopacket file to be validated")
    public String phenopacket;

    @CommandLine.Option(names = {"-o", "--out"}, description = "name of output file (default ${DEFAULT_VALUE})")
    public String outfileName = "phenotools-builder.json";


    public static void main(String[] args) {
        LOGGER.info("STARTING THE APPLICATION");
        int exitCode = new CommandLine(new PhenotoolsApp()).execute(args);
        System.exit(exitCode);
        LOGGER.info("APPLICATION FINISHED");
    }


    @Override
    public void run() {

    }
}