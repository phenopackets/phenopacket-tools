package org.phenopackets.phenopackettools.command;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenopackettools.util.format.PhenopacketFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * {@link SingleItemIOCommand} adds a writing routine to {@link SingleItemInputCommand}.
 */
public abstract class SingleItemIOCommand extends SingleItemInputCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleItemIOCommand.class);

    /**
     * Write the {@code message} in an appropriate {@code format} into the provided {@link OutputStream} {@code os}.
     * <p>
     * Uses {@link }
     * @param message message to be written out.
     * @param format format to write out
     * @param os where to write
     * @throws IOException in case of I/O errors during the output
     */
    protected static void writeMessage(Message message, PhenopacketFormat format, OutputStream os) throws IOException {
        switch (format) {
            case PROTOBUF -> {
                LOGGER.debug("Writing protobuf message");
                message.writeTo(os);
            }
            case JSON -> {
                LOGGER.debug("Writing JSON message");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                JsonFormat.printer().appendTo(message, writer);
                writer.flush();
            }
            case YAML -> {
                // TODO - implement
                throw new RuntimeException("YAML printer is not yet implemented");
            }
        }
    }

}
