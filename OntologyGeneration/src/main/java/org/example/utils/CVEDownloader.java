package org.example.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Slf4j
public class CVEDownloader {
    private final Map<String, String> OLD_NAMES_TO_SEARCH_TERM = Map.ofEntries(
            Map.entry("optmemc", "isMemoryCorruption"),
            Map.entry("optsql_injection", "isSqlInjection"),
            Map.entry("optxss", "isXss"),
            Map.entry("optdir_traversal", "isDirectoryTraversal"),
            Map.entry("optfileinc", "isFileInclusion"),
            Map.entry("optcsrf", "isCsrf"),
            Map.entry("optxxe", "isXxe"),
            Map.entry("optssrf", "isSsrf"),
            Map.entry("optopenredir", "isOpenRedirect"),
            Map.entry("optinputval", "isInputValidation"),
            Map.entry("optexeccode", "isCodeExecution"),
            Map.entry("optbypass", "isBypass"),
            Map.entry("optgainpriv", "isGainPrivilege"),
            Map.entry("optdos", "isDenialOfService"),
            Map.entry("optoverflow", "isOverflow"),
            Map.entry("optinfleak", "isInformationLeak")
    );

    private final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private final String token;

    private final Path baseResourcesPath;

    public CVEDownloader(String token, Path baseResourcesPath) {
        this.token = token;
        this.baseResourcesPath = baseResourcesPath.toAbsolutePath().normalize();
    }


    public void downloadAllCVEs() throws IOException {
        int currentYear = LocalDate.now().getYear();
        DownloaderSettings settings = new DownloaderSettings();

        for (String category : OLD_NAMES_TO_SEARCH_TERM.keySet()) {
            List<Map<String, String>> allData = loadCache(category, currentYear);
            queryForYearsRange(
                    allData, token, category, 2000, currentYear, settings
            );
            storeToCache(category, allData);
        }
    }

    private void storeToCache(String category, List<Map<String, String>> allData) throws IOException {
        List<Map<String, String>> merged = storeMergedCache(allData, category);
        generateOutput(category, merged);
    }

    private List<Map<String, String>> loadCache(String category, int currentYear) {
        log.debug("Loading cache for category {} from 2000 to {}", category, currentYear);
        List<Map<String, String>> allData = new ArrayList<>();
        for (int year = 2000; year <= currentYear; year++) {
            loadCacheForYear(allData, category, year);
        }
        return allData;
    }

    public void downloadMonthCVEs() throws IOException {
        int currentYear = LocalDate.now().getYear();
        DownloaderSettings settings = new DownloaderSettings();

        for (String category : OLD_NAMES_TO_SEARCH_TERM.keySet()) {
            List<Map<String, String>> allData = loadCache(category, currentYear);
            queryForCurrentMonth(
                    allData, token, category, settings
            );
            storeToCache(category, allData);
        }
    }

    private void queryForYearsRange(
            List<Map<String, String>> allData,
            String token,
            String category,
            int startYear,
            int endYear,
            DownloaderSettings messagesPerMinute
    ) {
        for (int year = startYear; year <= endYear; year++) {
            try {
                DownloadResult result = downloadForYear(year, category, token, messagesPerMinute);
                allData.addAll(result.data());
            } catch (Exception e) {
                log.error("Failed for {} in {}: {}", category, year, e.getMessage());
            }
        }
    }

    private void queryForCurrentMonth(
            List<Map<String, String>> allData,
            String token,
            String category,
            DownloaderSettings settings
    ) {
        LocalDate today = LocalDate.now();
        String startDate = today.withDayOfMonth(1).toString();
        YearMonth ym = YearMonth.of(today.getYear(), today.getMonthValue());
        String endDate = ym.atEndOfMonth().getYear() + "-" + String.format("%02d",ym.atEndOfMonth().getMonthValue()) + "-" + String.format("%02d",ym.atEndOfMonth().getDayOfMonth());

        try {
            DownloadResult result = downloadForDateRange(
                    startDate,
                    endDate,
                    category,
                    token,
                    settings,
                    true
            );
            allData.addAll(result.data());
        } catch (Exception e) {
            log.error("Failed current month update for {}: {}", category, e.getMessage());
        }
    }

    private DownloadResult downloadForYear(
            int year,
            String category,
            String token,
            DownloaderSettings settings
    ) throws Exception {
        String start = year + "-01-01";
        String end = year + "-12-31";
        return downloadForDateRange(start, end, category, token, settings, false);
    }

    private DownloadResult downloadForDateRange(
            String startDate,
            String endDate,
            String category,
            String token,
            DownloaderSettings settings,
            boolean isUpdate
    ) throws Exception {

        String year = startDate.substring(0, 4);
        String apiCategory = OLD_NAMES_TO_SEARCH_TERM.get(category);
        Path cacheFile = baseResourcesPath
                .resolve("cache_queries")
                .resolve(apiCategory + "_" + year + ".tsv");

        if (!isUpdate && Files.exists(cacheFile)) {
            List<Map<String, String>> cached = readTSV(cacheFile);
            if (!cached.isEmpty()) {
                return new DownloadResult(cached);
            }
        }

        long delayMs = 60_000L / settings.getMessagesPerMinute();

        while (true) {
            Thread.sleep(delayMs);

            String uri = "https://www.cvedetails.com/api/v1/vulnerability/search?"
                    + apiCategory + "=1"
                    + "&publishDateStart=" + startDate
                    + "&publishDateEnd=" + endDate
                    + "&resultsPerPage=20"
                    + "&outputFormat=tsv";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response =
                    HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 429) {
                log.info("Rate limited. Sleeping 1 minute.");
                Thread.sleep(60_000);
                settings.decreaseMessagesPerMinute();
                delayMs = 60_000L / settings.getMessagesPerMinute();
            } else if (response.statusCode() != 200) {
                throw new RuntimeException("HTTP " + response.statusCode());
            } else {
                log.debug("Downloaded {} CVEs for {} between {} and {}",
                        category, year, startDate, endDate);
                List<Map<String, String>> data = parseTSV(response.body());
                Files.createDirectories(cacheFile.getParent());
                writeTSV(cacheFile, data);

                return new DownloadResult(data);
            }
        }
    }

    private void loadCacheForYear(
            List<Map<String, String>> allData,
            String category,
            int year
    ) {
        String apiCategory = OLD_NAMES_TO_SEARCH_TERM.get(category);
        Path file = baseResourcesPath
                .resolve("cache_queries")
                .resolve(apiCategory + "_" + year + ".tsv");

        if (Files.exists(file)) {
            try {
                var cache_read = readTSV(file);
                log.debug("Loaded {} CVEs from cache for {} in {}: {}", cache_read.size(), category, year, file);
                allData.addAll(cache_read);
            } catch (IOException ignored) {
                log.debug("Failed to read cache for {} in {}: {}", category, year, file);
            }
        } else {
            log.warn("Cache file not found for {} in {}: {}", category, year, file);
        }
    }

    private List<Map<String, String>> storeMergedCache(
            List<Map<String, String>> allData,
            String category
    ) throws IOException {

        Map<String, Map<String, String>> byCve = new LinkedHashMap<>();
        for (Map<String, String> row : allData) {
            byCve.put(row.get("cveId"), row);
        }

        List<Map<String, String>> merged = new ArrayList<>(byCve.values());
        Path mergedDir = baseResourcesPath.resolve("cache_merged");
        Files.createDirectories(mergedDir);
        writeCSV(mergedDir.resolve(category + ".csv"), merged);
        return merged;
    }

    /* ===================== OUTPUT ===================== */

    private void generateOutput(
            String category,
            List<Map<String, String>> data
    ) throws IOException {

        Path outputDir = baseResourcesPath.resolve("output");
        Files.createDirectories(outputDir);
        Path out = outputDir.resolve(category + ".txt");

        try (BufferedWriter w = Files.newBufferedWriter(out)) {
            for (Map<String, String> row : data) {
                w.write(row.get("cveId"));
                w.newLine();
            }
        }
    }

    /* ===================== TSV / CSV HELPERS ===================== */

    private static List<Map<String, String>> parseTSV(String content) throws IOException {
        return readTSV(new StringReader(content));
    }

    private static List<Map<String, String>> readTSV(Path path) throws IOException {
        try (Reader r = Files.newBufferedReader(path)) {
            return readTSV(r);
        }
    }

    private static List<Map<String, String>> readTSV(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        String header = br.readLine();
        if (header == null) return List.of();

        String[] cols = header.split("\t");
        List<Map<String, String>> rows = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            String[] vals = line.split("\t", -1);
            Map<String, String> row = new HashMap<>();
            for (int i = 0; i < cols.length; i++) {
                row.put(cols[i].replace("\"", ""), i < vals.length ? vals[i] : "");
            }
            rows.add(row);
        }
        return rows;
    }

    private static void writeTSV(Path path, List<Map<String, String>> data) throws IOException {
        if (data.isEmpty()){
            log.debug("No data to write for {}. Skipping TSV cache file: {}", path.getFileName(), path.toAbsolutePath());
            return;
        }

        try (BufferedWriter w = Files.newBufferedWriter(path)) {
            log.debug("Writing {} CVEs to TSV cache file: {}", data.size(), path.toAbsolutePath());
            Set<String> headers = data.get(0).keySet();
            w.write(String.join("\t", headers));
            w.newLine();

            for (Map<String, String> row : data) {
                List<String> vals = new ArrayList<>();
                for (String h : headers) {
                    vals.add(row.getOrDefault(h, ""));
                }
                w.write(String.join("\t", vals));
                w.newLine();
            }
        }
    }

    private static void writeCSV(Path path, List<Map<String, String>> data) throws IOException {
        if (data.isEmpty()) return;

        try (BufferedWriter w = Files.newBufferedWriter(path)) {
            Set<String> headers = data.get(0).keySet();
            w.write(String.join(",", headers));
            w.newLine();

            for (Map<String, String> row : data) {
                List<String> vals = new ArrayList<>();
                for (String h : headers) {
                    vals.add(row.getOrDefault(h, ""));
                }
                w.write(String.join(",", vals));
                w.newLine();
            }
        }
    }

    /* ===================== RECORD ===================== */

    private record DownloadResult(
            List<Map<String, String>> data
    ) {}
}
