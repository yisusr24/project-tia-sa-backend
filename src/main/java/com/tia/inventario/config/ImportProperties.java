package com.tia.inventario.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.import")
public class ImportProperties {
    private Long maxFileSize;
    private Integer maxRecords;
    private Integer batchSize;
    private Integer timeoutSeconds;
    private String[] allowedFormats;
    private Character delimiter;
    private String encoding;
}
