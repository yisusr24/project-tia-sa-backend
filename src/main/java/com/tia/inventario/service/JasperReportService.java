package com.tia.inventario.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JasperReportService {

    public byte[] generarReportePDF(String reporteName, List<?> datos, Map<String, Object> parametros) {
        log.info("Generando reporte PDF: {} con {} registros", reporteName, datos.size());
        
        try {
            InputStream reportStream = getClass()
                .getResourceAsStream("/reportes/" + reporteName + ".jasper");
            
            if (reportStream == null) {
                throw new RuntimeException("Reporte no encontrado: " + reporteName);
            }
            
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                reportStream, 
                parametros, 
                dataSource
            );
            
            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            
            log.info("Reporte PDF generado exitosamente: {} ({} bytes)", reporteName, pdf.length);
            return pdf;
            
        } catch (JRException e) {
            log.error("Error generando reporte PDF: {}", reporteName, e);
            throw new RuntimeException("Error generando reporte PDF: " + e.getMessage(), e);
        }
    }

    public byte[] generarReporteExcel(String reporteName, List<?> datos, Map<String, Object> parametros) {
        log.info("Generando reporte Excel: {} con {} registros", reporteName, datos.size());
        
        try {
            InputStream reportStream = getClass()
                .getResourceAsStream("/reportes/" + reporteName + ".jasper");
            
            if (reportStream == null) {
                throw new RuntimeException("Reporte no encontrado: " + reporteName);
            }
            
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                reportStream, 
                parametros, 
                dataSource
            );
            
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setCollapseRowSpan(false);
            exporter.setConfiguration(configuration);
            
            exporter.exportReport();
            
            byte[] excel = out.toByteArray();
            log.info("Reporte Excel generado exitosamente: {} ({} bytes)", reporteName, excel.length);
            return excel;
            
        } catch (JRException e) {
            log.error("Error generando reporte Excel: {}", reporteName, e);
            throw new RuntimeException("Error generando reporte Excel: " + e.getMessage(), e);
        }
    }
}
