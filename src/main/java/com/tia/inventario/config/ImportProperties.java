package com.tia.inventario.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.import")
public class ImportProperties {
    private Long maxFileSize = 10485760L;
    private Integer maxRecords = 10000;
    private Integer batchSize = 100;
    private Integer timeoutSeconds = 300;
    private String[] allowedFormats = {"csv"};
    private Character delimiter = ';';
    private String encoding = "UTF-8";
}
