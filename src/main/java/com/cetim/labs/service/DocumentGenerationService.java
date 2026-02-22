package com.cetim.labs.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.Predicate;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import com.cetim.labs.model.ServiceOrder;
import com.cetim.labs.model.FicheDessai;
import com.cetim.labs.model.Order;
import com.cetim.labs.model.Test;

@Service
public class DocumentGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentGenerationService.class);
    
    private enum DocumentType {
        SERVICE_ORDER("templates/template.docx"),
        FICHE_DESSAI("templates/fichetemplate.docx");
        
        private final String templatePath;
        
        DocumentType(String templatePath) {
            this.templatePath = templatePath;
        }
        
        public String getTemplatePath() {
            return templatePath;
        }
    }

    public boolean generateDocument(ServiceOrder serviceOrder, FicheDessai fDessai) throws Exception {
        try {
            if (serviceOrder != null) {
                return generateDocument(DocumentType.SERVICE_ORDER, serviceOrder);
            } else if (fDessai != null) {
                return generateDocument(DocumentType.FICHE_DESSAI, fDessai);
            } 
            return false;
        } catch (Exception e) {
            logger.error("Error generating Word document", e);
            throw e;
        }
    }

    private boolean generateDocument(DocumentType docType, Object contextObject) throws Exception {
        XWPFDocument document;
        try (InputStream templateStream = new ClassPathResource(docType.getTemplatePath()).getInputStream()) {
            document = new XWPFDocument(templateStream);
        }
        
        try {
            // Process paragraphs
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                processParagraph(paragraph, docType, contextObject);
            }

            // Process tables
            for (XWPFTable table : document.getTables()) {
                processTable(table, docType, contextObject);
            }


            String directoryPath = "Ordres-des-Service";
            if (docType == DocumentType.SERVICE_ORDER) {
                directoryPath = "Ordres-des-Service";
            } else {
                directoryPath = "Fiches-Dessai";
            }
            
            File directory = new File(directoryPath);
            if (!directory.exists() && !directory.mkdirs()) {
                logger.error("Failed to create directory: {}", directoryPath);
                return false;
            }

            // Generate filename based on document type and current timestamp
            String filename = "";
            if (docType == DocumentType.SERVICE_ORDER) {
                ServiceOrder so = (ServiceOrder) contextObject;
                filename = String.format("OS_%d.docx", so.getServiceOrderID());
            } else {
                FicheDessai fDessai = (FicheDessai) contextObject;
                filename = String.format("FicheDessai_%d.docx", fDessai.getId());
            }

            // Save the document
            File outputFile = new File(directory, filename);
            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                document.write(out);
                return true;
            } catch (Exception e) {
                logger.error("Failed to save document: {}", e.getMessage());
                return false;
            }
        } finally {
            document.close();
        }
    }

    private void processParagraph(XWPFParagraph paragraph, DocumentType docType, Object contextObject) {
        String paragraphText = paragraph.getText();
        if (paragraphText == null) return;
    
        Map<String, String> replacements = buildReplacements(docType, contextObject);
        boolean hasReplacements = false;
    
        // Check if this paragraph needs any replacements
        for (String placeholder : replacements.keySet()) {
            if (paragraphText.contains(placeholder)) {
                hasReplacements = true;
                break;
            }
        }
    
        if (hasReplacements) {
            // Store original runs formatting
            RunFormatting originalFormatting = !paragraph.getRuns().isEmpty() ? 
                new RunFormatting(paragraph.getRuns().get(0)) : new RunFormatting();
    
            // Clear existing runs
            for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                paragraph.removeRun(i);
            }
    
            // Apply replacements
            String newText = paragraphText;
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                newText = newText.replace(entry.getKey(), entry.getValue());
            }
    
            // Create new run with original formatting
            XWPFRun newRun = paragraph.createRun();
            newRun.setText(newText);
            originalFormatting.applyTo(newRun);
        }
    }

    private Map<String, String> buildReplacements(DocumentType docType, Object contextObject) {
        Map<String, String> replacements = new HashMap<>();
        
        switch (docType) {
            case SERVICE_ORDER:
                ServiceOrder serviceOrder = (ServiceOrder) contextObject;
                replacements.put("{{dateOS}}", formatDate(serviceOrder.getDate()));
                replacements.put("{{OS}}", String.valueOf(serviceOrder.getServiceOrderID()));
                // Add more service order specific replacements
                break;
        }
        
        return replacements;
    }

    // RunFormatting class remains the same
    private static class RunFormatting {
        private boolean bold = false;
        private String fontFamily = null;
        private int fontSize = -1;
        private String color = null;
        private boolean italic = false;
        private boolean underline = false;
    
        public RunFormatting() {}

        public RunFormatting(XWPFRun run) {
            this.bold = run.isBold();
            this.fontFamily = run.getFontFamily();
            this.fontSize = run.getFontSize();
            this.color = run.getColor();
            this.italic = run.isItalic();
            this.underline = run.getUnderline() != UnderlinePatterns.NONE;
        }
    
        public void applyTo(XWPFRun run) {
            run.setBold(bold);
            if (fontFamily != null) run.setFontFamily(fontFamily);
            if (fontSize > 0) run.setFontSize(fontSize);
            if (color != null) run.setColor(color);
            run.setItalic(italic);
            if (underline) run.setUnderline(UnderlinePatterns.SINGLE);
        }

        public void copyRunFormatting(XWPFRun source, XWPFRun target) {
            target.setBold(source.isBold());
            target.setItalic(source.isItalic());
            target.setFontFamily(source.getFontFamily());
            int fontSize = source.getFontSize();
            target.setFontSize(fontSize != -1 ? fontSize : 11);
            target.setColor(source.getColor());
            target.setUnderline(source.getUnderline());
        }
    }






    /*
     * 
     * process the tables of the document
     */
    private void processTable(XWPFTable table, DocumentType docType, Object contextObject) {
        switch (docType) {
            case SERVICE_ORDER:
                processServiceOrderTable(table, (ServiceOrder) contextObject);
                break;
            case FICHE_DESSAI:
                processFicheDessaiTable(table, (FicheDessai) contextObject);
                break;
            default:
                logger.warn("No table processing defined for document type: {}", docType);
        }
    }

    private void processServiceOrderTable(XWPFTable table, ServiceOrder serviceOrder) {
        
        TableTemplate template = findTableTemplate(table, 
            text -> text.contains("{{codeEchantillon") || 
                    text.contains("{{test") || 
                    text.contains("{{delai"));

        if (template == null) {
            logger.error("Template row not found in table");
            return;
        }

        List<Order> orders = serviceOrder.getOrders();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            int orderNum = i + 1;
            
            XWPFTableRow newRow = table.createRow();
            copyRowProperties(template.row, newRow);
            
            for (int j = 0; j < template.row.getTableCells().size(); j++) {
                XWPFTableCell templateCell = template.row.getTableCells().get(j);
                XWPFTableCell newCell = newRow.getTableCells().get(j);
                copyCellProperties(templateCell, newCell);
                
                clearCellContent(newCell);
                
                String cellText = templateCell.getText();
                if (cellText != null) {
                    String modifiedText = updateNumberedPlaceholders(cellText, orderNum);
                    String newText = replaceServiceOrderPlaceholders(modifiedText, serviceOrder, order, orderNum);
                    
                    if (modifiedText.contains("{{test" + orderNum + "}}")) {
                        processAnalysisCell(newCell, order);
                    } else {
                        createCellWithText(newCell, templateCell, newText);
                    }
                }
            }
        }
        
        table.removeRow(template.index);
    }

    private void processFicheDessaiTable(XWPFTable table, FicheDessai fDessai) {

        TableTemplate singleRowTemplate = findTableTemplate(table, 
            text -> text.contains("{{RE}}") ||
                    text.contains("{{OS}}") || 
                    text.contains("{{DateOS}}"));

        if (singleRowTemplate != null) {
            XWPFTableRow row = singleRowTemplate.row;
            for (XWPFTableCell cell : row.getTableCells()) {
                String cellText = cell.getText();
                if (cellText != null) {
                    String updatedText = cellText
                        .replace("{{RE}}", String.valueOf(fDessai.getServiceOrder().getRaportFinal().getId()))
                        .replace("{{OS}}", String.valueOf(fDessai.getServiceOrder().getServiceOrderID()))
                        .replace("{{DateOS}}", formatDate(fDessai.getCreationDate()))
                        .replace("{{codeEchantillon}}", fDessai.getOrder().getEchantillon().getEchantillonCode());
                    clearCellContent(cell);
                    cell.setText(updatedText);
                }
            }
        }

        TableTemplate multiRowTemplate = findTableTemplate(table, 
            text -> text.contains("{{Test}}") || 
                    text.contains("{{testCode}}") || 
                    text.contains("{{Nombre déchantillon}}"));

        if (multiRowTemplate != null) {
            XWPFTableRow templateRow = multiRowTemplate.row;

            List<Test> testList = new ArrayList<>(fDessai.getOrder().getTests());
            for (int i = 0; i < testList.size(); i++) {
                Test test = testList.get(i);
                XWPFTableRow newRow = table.createRow();
                copyRowProperties(templateRow, newRow);

                for (int j = 0; j < templateRow.getTableCells().size(); j++) {
                    XWPFTableCell templateCell = templateRow.getTableCells().get(j);
                    XWPFTableCell newCell = newRow.getTableCells().get(j);
                    copyCellProperties(templateCell, newCell);

                    String cellText = templateCell.getText();
                    if (cellText != null) {
                        String updatedText = cellText
                            .replace("{{Test}}", test.getTestName()) // Replace with actual value
                            .replace("{{testCode}}", test.getTestCode()) // Replace with actual value
                            .replace("{{Nombre déchantillon}}", "1")
                            .replace("{{codeEchantillon}}", fDessai.getOrder().getEchantillon().getEchantillonCode());
                        clearCellContent(newCell);
                        newCell.setText(updatedText);
                    }
                }
            }

            table.removeRow(multiRowTemplate.index);
        }

    }

    private static class TableTemplate {
        XWPFTableRow row;
        int index;

        TableTemplate(XWPFTableRow row, int index) {
            this.row = row;
            this.index = index;
        }
    }

    private TableTemplate findTableTemplate(XWPFTable table, Predicate<String> placeholderMatcher) {
        for (int i = 0; i < table.getRows().size(); i++) {
            XWPFTableRow row = table.getRows().get(i);
            if (rowContainsPlaceholder(row, placeholderMatcher)) {
                return new TableTemplate(row, i);
            }
        }
        return null;
    }

    private boolean rowContainsPlaceholder(XWPFTableRow row, Predicate<String> placeholderMatcher) {
        for (XWPFTableCell cell : row.getTableCells()) {
            String cellText = cell.getText();
            if (cellText != null && placeholderMatcher.test(cellText)) {
                return true;
            }
        }
        return false;
    }

    private String updateNumberedPlaceholders(String text, int orderNum) {
        return text.replace("{{codeEchantillon1}}", "{{codeEchantillon" + orderNum + "}}")
                  .replace("{{test1}}", "{{test" + orderNum + "}}")
                  .replace("{{delai1}}", "{{delai" + orderNum + "}}");
    }

    private String replaceServiceOrderPlaceholders(String text, ServiceOrder serviceOrder, Order order, int orderNum) {
        return text.replace("{{dateOS}}", formatDate(serviceOrder.getDate()))
                  .replace("{{OS}}", String.valueOf(serviceOrder.getServiceOrderID()))
                  .replace("{{codeEchantillon" + orderNum + "}}", order.getEchantillon().getEchantillonCode())
                  .replace("{{delai" + orderNum + "}}", order.getDelai() + "J");
    }

    private void clearCellContent(XWPFTableCell cell) {
        for (int i = cell.getParagraphs().size() - 1; i >= 0; i--) {
            cell.removeParagraph(i);
        }
    }

    private void createCellWithText(XWPFTableCell cell, XWPFTableCell templateCell, String text) {
        XWPFParagraph newPara = cell.addParagraph();
        copyParagraphProperties(templateCell.getParagraphs().get(0), newPara);
        
        XWPFRun newRun = newPara.createRun();
        newRun.setText(text);
        new RunFormatting().copyRunFormatting(templateCell.getParagraphs().get(0).getRuns().get(0), newRun);
    }

    // The analysis cell processing remains largely the same, but could be made more generic
    private void processAnalysisCell(XWPFTableCell cell, Order order) {
        clearCellContent(cell);
        
        if (order.getTests().isEmpty()) return;
        
        XWPFParagraph formatPara = cell.addParagraph();
        XWPFRun formatRun = formatPara.createRun();
        
        for (Test test : order.getTests()) {
            XWPFParagraph testPara = cell.addParagraph();
            copyParagraphProperties(formatPara, testPara);
            
            XWPFRun testRun = testPara.createRun();
            testRun.setText(test.getTestName() + " ( " + test.getTestCode() + " )");
            new RunFormatting().copyRunFormatting(formatRun, testRun);
        }
        
        cell.removeParagraph(0);
    }
    

    // The copy properties methods remain the same
    private void copyRowProperties(XWPFTableRow source, XWPFTableRow target) {
        if (source.getCtRow() != null && source.getCtRow().getTrPr() != null) {
            target.getCtRow().setTrPr((CTTrPr) source.getCtRow().getTrPr().copy());
        }
    }

    private String formatDate(java.util.Date date) {
        if (date == null) return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    
    private void copyCellProperties(XWPFTableCell source, XWPFTableCell target) {
        if (source.getCTTc() != null && source.getCTTc().getTcPr() != null) {
            target.getCTTc().setTcPr((CTTcPr) source.getCTTc().getTcPr().copy());
        }
    }
    
    private void copyParagraphProperties(XWPFParagraph source, XWPFParagraph target) {
        if (source.getCTP() != null && source.getCTP().getPPr() != null) {
            target.getCTP().setPPr((CTPPr) source.getCTP().getPPr().copy());
        }
        target.setAlignment(source.getAlignment());
    }

}
