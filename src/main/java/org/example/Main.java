package org.example;


import jakarta.xml.bind.JAXBException;
import org.apache.commons.cli.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws JAXBException, IOException, URISyntaxException {
        if (args.length == 0) {
            System.err.println("Expected subcommand: generateOntology");
            System.exit(1);
        }

        String subcommand = args[0];
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (subcommand.toLowerCase()) {
            case "generateontology":
                handleGenerateOntology(subArgs);
                break;
            default:
                System.err.println("Unknown subcommand: " + subcommand);
                System.exit(1);
        }
    }

    private static void handleGenerateOntology(String[] args) {
        Options options = new Options();

        options.addOption(Option.builder()
                .longOpt("xml")
                .hasArg()
                .desc("Path to the input XML file")
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("xsd")
                .hasArg()
                .desc("Path to the XSD schema file")
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("out")
                .hasArg()
                .desc("Path for the output OWL/RDF file (XML format)")
                .required()
                .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            String xmlPath = cmd.getOptionValue("xml");
            String xsdPath = cmd.getOptionValue("xsd");
            String outPath = cmd.getOptionValue("out");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XSDSchemaAndXMLToOntology.transform(
                    new File(xmlPath),
                    xsdPath,
                    outputStream
            );

            try(FileWriter generatedFileWriter = new FileWriter(outPath)){
                generatedFileWriter.write(outputStream.toString());
            }
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            formatter.printHelp("OntologyTool generateOntology", options);
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}