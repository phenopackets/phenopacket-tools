package org.phenopackets.phenopackettools.cli.command;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.phenopackets.phenopackettools.cli.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Callable;

public abstract class BaseCommand implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCommand.class);

    protected static final Properties APPLICATION_PROPERTIES = readApplicationProperties();

    protected static final String PHENOPACKET_TOOLS_VERSION = APPLICATION_PROPERTIES.getProperty("phenopacket-tools.version", "UNKNOWN-version");

    @CommandLine.Option(names = {"-v"}, description = {"Specify multiple -v options to increase verbosity.",
            "For example, `-v -v -v` or `-vvv`"})
    public boolean[] verbosity = {};

    @Override
    public Integer call() {
        // (0) Setup verbosity and print banner.
        setupLoggingAndPrintBanner();

        // (1) Run the command functionality.
        return execute();
    }

    protected abstract Integer execute();

    private void setupLoggingAndPrintBanner() {
        Level level = parseVerbosityLevel();

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(level);

        if (!(level.equals(Level.WARN) || level.equals(Level.ERROR)))
            printBanner();
    }

    private Level parseVerbosityLevel() {
        int verbosity = 0;
        for (boolean a : this.verbosity) {
            if (a) verbosity++;
        }

        return switch (verbosity) {
            case 0 -> Level.WARN;
            case 1 -> Level.INFO;
            case 2 -> Level.DEBUG;
            case 3 -> Level.TRACE;
            default -> Level.ALL;
        };
    }

    private static Properties readApplicationProperties() {
        Properties properties = new Properties();
        try (InputStream is = Main.class.getResourceAsStream("application.properties")) {
            properties.load(is);
        } catch (IOException e) {
            // Complain and swallow. We are not stopping the entire app just for this.
            LOGGER.error("Unable to read the application.properties file. Please report to the developers: {}", e.getMessage(), e);
        }
        return properties;
    }

    private static void printBanner() {
        System.err.println(readBanner());
    }

    private static String readBanner() {
        try (InputStream is = Main.class.getResourceAsStream("banner.txt")) {
            return is == null ? "" : new String(is.readAllBytes());
        } catch (IOException e) {
            LOGGER.error("Unable to read banner. Please report to the developers: {}", e.getMessage(), e);
            return "";
        }
    }

}
