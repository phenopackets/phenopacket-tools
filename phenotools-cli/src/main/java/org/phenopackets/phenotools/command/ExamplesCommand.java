package org.phenopackets.phenotools.command;

import com.google.protobuf.util.JsonFormat;
import org.phenopackets.phenotools.builder.exceptions.PhenotoolsRuntimeException;
import org.phenopackets.phenotools.examples.BethlehamMyopathy;
import org.phenopackets.phenotools.examples.PhenopacketExample;
import org.phenopackets.phenotools.examples.Thrombocytopenia2;
import org.phenopackets.schema.v2.Phenopacket;
import org.phenopackets.schema.v2.core.File;
import picocli.CommandLine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@CommandLine.Command(name = "examples", aliases = {"E"},
        mixinStandardHelpOptions = true,
        description = "output example phenopackets to file")
public class ExamplesCommand implements Runnable {


    @CommandLine.Option(names = {"-o","--outdir"}, description = "path to out directory (default: current directory)")
    public String outdir=".";




    private void outputPhenopacket(String fname, Phenopacket phenopacket) {
        String path = this.outdir + java.io.File.separator + fname;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            String json =  JsonFormat.printer().print(phenopacket);
            writer.write(json);
        } catch (IOException e) {
            throw new PhenotoolsRuntimeException(e.getMessage());
        }
    }


    private void createOutdirectoryIfNeeded() {
        java.io.File directory = new java.io.File(this.outdir);
        if (! directory.exists()){
            boolean success = directory.mkdir();
            if (!success) {
                throw new PhenotoolsRuntimeException("Could not create outdir");
            }
        }
    }

    private void outputToFile(Phenopacket phenopacket, File outfile) {
        if (! outdir.equals(".")) {
            createOutdirectoryIfNeeded();
        }
    }


    @Override
    public void run() {
        PhenopacketExample bethleham = new BethlehamMyopathy();
        outputPhenopacket("bethlehamMyopathy.json", bethleham.getPhenopacket());
        PhenopacketExample thrombocytopenia2 = new Thrombocytopenia2();
        outputPhenopacket("thrombocytopenia2.json", thrombocytopenia2.getPhenopacket());
    }
}
