package com.sivalabs.devzone.links.services;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.links.models.LinkDTO;
import com.sivalabs.devzone.users.services.UserService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;

@Singleton
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinksImportService {
    public static final String SYSTEM_USER_EMAIL = "admin@gmail.com";
    private final LinkService linkService;
    private final UserService userService;

    public void importLinks(String fileName) throws IOException, CsvValidationException {
        log.info("Importing links from file: {}", fileName);
        long count = 0L;

        InputStream inputStream = this.getClass().getResourceAsStream(fileName);
        try (InputStreamReader inputStreamReader =
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                CSVReader csvReader = new CSVReader(inputStreamReader)) {
            csvReader.skip(1);
            CSVIterator iterator = new CSVIterator(csvReader);

            while (iterator.hasNext()) {
                String[] nextLine = iterator.next();
                LinkDTO linkDTO = new LinkDTO();
                linkDTO.setUrl(nextLine[0]);
                linkDTO.setTitle(nextLine[1]);
                linkDTO.setCreatedUserId(
                        userService.getUserByEmail(SYSTEM_USER_EMAIL).orElseThrow().getId());
                linkDTO.setCreatedAt(Instant.now());
                if (nextLine.length > 2 && StringUtils.trimToNull(nextLine[2]) != null) {
                    linkDTO.setTags(
                            Arrays.asList(StringUtils.trimToEmpty(nextLine[2]).split("\\|")));
                }
                linkService.createLink(linkDTO);
                count++;
            }
        }
        log.info("Imported {} links from file {}", count, fileName);
    }
}
