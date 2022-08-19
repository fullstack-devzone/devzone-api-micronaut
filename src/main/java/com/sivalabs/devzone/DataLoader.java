package com.sivalabs.devzone;

import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.links.services.LinksImportService;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationEventListener<ServerStartupEvent> {
    private final LinksImportService linksImportService;

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        log.info("Loading data at startup");
        try {
            linksImportService.importLinks("/data/links.csv");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
