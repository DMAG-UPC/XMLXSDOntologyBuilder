package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.CVEDownloader;
import org.example.utils.CommonVulnerabilityAndExposureToRequirementLinker;
import org.example.utils.WithCommonVulnerabilitiesAndExposuresOntologyExtender;
import org.example.utils.WithSpyderiskExtender;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.example.utils.WithCommonVulnerabilitiesAndExposuresOntologyExtender.populateOntology;

public class Main {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CliCommand()).execute(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    enum CveDownloadMode {
        NONE,
        MONTH,
        ALL
    }

    @Slf4j
    @Command(
            name = "ontology-cli",
            mixinStandardHelpOptions = true,
            description = "CLI to generate ontology with CVE data"
    )
    static class CliCommand implements Runnable {

        @Option(
                names = "--cvedetails-api-token",
                required = false,
                description = "CVEDetails' API token (required unless --cve-download-mode=NONE)"
        )
        private String cvedetailsApiToken;

        @Option(
                names = "--cve-workdir-base-path",
                required = true,
                description = "CVEs workdir base path"
        )
        private String cvesWorkdirBasePath;

        @Option(
                names = "--cve-download-mode",
                description = "CVE download mode: NONE (no download), MONTH (download current month), ALL (download everything)",
                required = true
        )
        private CveDownloadMode downloadMode;

        @Option(
                names = "--output-path",
                required = true,
                description = "output path of the generated ontology file"
        )
        private String outputPath;

        @Override
        public void run() {
            log.info("Starting execution");
            log.debug("CVE resources dictionary: {}", cvesWorkdirBasePath);
            log.debug("CVE download mode: {}", downloadMode);
            log.debug("Output path: {}", outputPath);

            if (downloadMode != CveDownloadMode.NONE && (cvedetailsApiToken == null || cvedetailsApiToken.isBlank())) {
                String msg = "The --cvedetails-api-token option is required unless --cve-download-mode=NONE";
                log.error(msg);
                throw new CommandLine.ParameterException(new CommandLine(this), msg);
            }

            try {
                runApplication();
            } catch (Exception e) {
                log.error("Error during execution", e);
                throw new CommandLine.ExecutionException(new CommandLine(this), "Execution error", e);
            }

            log.info("Successful execution.");
        }

        private void runApplication() throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
            Path cveWorkdirFolderPath = Paths.get(this.cvesWorkdirBasePath);
            CVEDownloader downloader = new CVEDownloader(cvedetailsApiToken, cveWorkdirFolderPath);

            try {
                switch (downloadMode) {
                    case ALL:
                        log.debug("Downloading all CVEs");
                        downloader.downloadAllCVEs();
                        break;
                    case MONTH:
                        log.debug("Downloading month CVEs");
                        downloader.downloadMonthCVEs();
                        break;
                    case NONE:
                        log.debug("Not downloading CVEs; using existing local CVE data only");
                        break;
                    default:
                        log.warn("Unknown download mode '{}', defaulting to MONTH", downloadMode);
                        downloader.downloadMonthCVEs();
                        break;
                }
            } catch (Exception e) {
                log.error("Will proceed with old CVEs list, as failed to download new CVEs due to ", e);
            }

            var type_to_CVEs = CommonVulnerabilityAndExposureToRequirementLinker.getCVETypeToCVEs(cveWorkdirFolderPath);
            var commonVulnerabilityAndExposureToRequirementLinker = new CommonVulnerabilityAndExposureToRequirementLinker();
            var requirementToCVETypes = commonVulnerabilityAndExposureToRequirementLinker.getRequirementToCVETypes();

            File xsdFile = resourceToFile("/input/RequirementsSchemas.xsd", "RequirementsSchemas", ".xsd");
            File xmlFile = resourceToFile("/requirements_with_dates.xml", "requirements_with_dates", ".xml");
            correct_schema_import_path(xmlFile, xsdFile);

            File ontologyWithDatesFile = generateOntologyToFile(xmlFile, xsdFile);
            File ontologyWithSpyderiskFile = extendWithSpyderiskToFile(ontologyWithDatesFile);
            extendWithCVEs(ontologyWithSpyderiskFile, requirementToCVETypes, type_to_CVEs, outputPath);

        }
    }

    private static void extendWithCVEs(File ontologyWithSpyderiskFile, Map<String, Set<String>> requirementToCVETypes, Map<String, List<String>> type_to_CVEs, String outputPath) throws IOException {
        try (
                InputStream ontologyInputStream = new FileInputStream(ontologyWithSpyderiskFile);
                InputStream originalRequirementInputStream = Main.class.getResourceAsStream("/requirements_with_dates.xml")
        ) {
            try {
                final Set<String> CVEs_to_add_to_ontology = new HashSet<>();
                CommonVulnerabilityAndExposureToRequirementLinker.getCVEsOfInterest(CVEs_to_add_to_ontology, requirementToCVETypes, type_to_CVEs, originalRequirementInputStream);
                File outputFile = new File(outputPath);
                WithCommonVulnerabilitiesAndExposuresOntologyExtender.populateOntology(CVEs_to_add_to_ontology, requirementToCVETypes, type_to_CVEs, ontologyInputStream, outputFile.getAbsolutePath());
            } catch (ParserConfigurationException | SAXException | XMLStreamException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static File extendWithSpyderiskToFile(File ontologyToExtend) throws IOException, OWLOntologyStorageException, OWLOntologyCreationException {
        try (ByteArrayOutputStream outputStreamWithSpyderisk = new ByteArrayOutputStream()) {
            var ontologyWithSpyderisk = WithSpyderiskExtender.extendOntologyWithSpyderisk(IRI.create(ontologyToExtend.toURI()));
            File ontologyWithSpyderiskFile = File.createTempFile("generated_with_dates_and_spyderisk", ".owl");
            ontologyWithSpyderisk.saveOntology(outputStreamWithSpyderisk);
            try (FileWriter generatedFileWriter = new FileWriter(ontologyWithSpyderiskFile)) {
                generatedFileWriter.write(outputStreamWithSpyderisk.toString());
            }
            return ontologyWithSpyderiskFile;
        }
    }


    private static File generateOntologyToFile(File xmlFile, File xsdFile) throws IOException {
        try (ByteArrayOutputStream ontologyWithDatesOutputStream = new ByteArrayOutputStream()) {
            XSDSchemaAndXMLToOntology.transform(xmlFile.getPath(), xsdFile.getPath(), ontologyWithDatesOutputStream);

            File generatedOntologyFile = File.createTempFile("generated_populated_requirements_with_dates", ".owl");
            try (FileWriter generatedFileWriter = new FileWriter(generatedOntologyFile)) {
                generatedFileWriter.write(ontologyWithDatesOutputStream.toString());
            }
            return generatedOntologyFile;
        }
    }


    private static void correct_schema_import_path(File xmlFile, File xsdFile) throws IOException {
        String xmlContent = new String(java.nio.file.Files.readAllBytes(xmlFile.toPath()));
        xmlContent = xmlContent.replaceAll(
            "(xsi:schemaLocation\\s*=\\s*['\"][^'\"]*edu\\.upc\\.dmag[^'\"\\s]*\\s+)([^'\"\\s]+)",
            "$1" + xsdFile.toURI()
        );
        java.nio.file.Files.write(xmlFile.toPath(), xmlContent.getBytes());
    }

    private static File resourceToFile(String name, String prefix, String suffix) throws IOException {
        InputStream requirementSchemaInputStream = Main.class.getResourceAsStream(name);
        File xsdFile = File.createTempFile(prefix, suffix);
        try (OutputStream writer = new FileOutputStream(xsdFile)) {
            requirementSchemaInputStream.transferTo(writer);
        }
        return xsdFile;
    }

    private static File testResourceToFile(String name, String prefix, String suffix) throws IOException {
        InputStream requirementSchemaInputStream = Main.class.getResourceAsStream(name);
        File xsdFile = File.createTempFile(prefix, suffix);
        try (OutputStream writer = new FileOutputStream(xsdFile)) {
            requirementSchemaInputStream.transferTo(writer);
        }
        return xsdFile;
    }
}