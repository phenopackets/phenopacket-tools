package org.phenopackets.phenopackettools.command;

import org.phenopackets.phenopackettools.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Callable;

public abstract class BaseCommand implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCommand.class);

    protected static final String BANNER = readBanner();

    protected static final Properties APPLICATION_PROPERTIES = readApplicationProperties();

    protected static final String PHENOPACKET_TOOLS_VERSION = APPLICATION_PROPERTIES.getProperty("phenopacket-tools.version", "UNKNOWN-version");

    private static String readBanner() {
        try (InputStream is = Main.class.getResourceAsStream("banner.txt")) {
            return is == null ? "" : new String(is.readAllBytes());
        } catch (IOException e) {
            LOGGER.error("Unable to read banner. Please report to the developers: {}", e.getMessage(), e);
            return "";
        }
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

    protected static void printBanner() {
        System.err.println(BANNER);
    }

}
