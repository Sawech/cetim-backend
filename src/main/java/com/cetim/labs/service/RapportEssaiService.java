package com.cetim.labs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import com.cetim.labs.model.RapportEssai;
import com.cetim.labs.model.Test;
import com.cetim.labs.model.TestVariable;

@Service
public class RapportEssaiService {

    public File generateDocument(RapportEssai rapport) throws IOException {
        Test test = rapport.getTest();
        Map<String, String> data = new HashMap<>();

        // Prepare variable values
        for (TestVariable var : test.getVariables()) {
            data.put(var.getName(), String.valueOf(var.getComputedValue()));
        }

        data.put("OS", String.valueOf(rapport.getRapportPartiel().getRapportFinal().getOS().getServiceOrderID()));
        data.put("dateOS", String.valueOf(rapport.getRapportPartiel().getRapportFinal().getOS().getDate()));
        data.put("testcode", test.getTestCode());
        data.put("RapportFinalId", String.valueOf(rapport.getRapportPartiel().getRapportFinal().getId()));
        data.put("codeEchantillon", String.valueOf(rapport.getRapportPartiel().getFicheEssai().getOrder().getEchantillon().getEchantillonCode()));
        data.put("typeEchantillon", String.valueOf(rapport.getRapportPartiel().getFicheEssai().getOrder().getEchantillon().getEchantillonType()));

        // Ensure the rapports directory exists
        File rapportsDir = new File("rapports");
        if (!rapportsDir.exists()) {
            rapportsDir.mkdirs();
        }

        File output = new File(rapportsDir, "rapport_essai_" + rapport.getId() + ".docx");
        // If the file exists, delete it first
        if (output.exists()) {
            output.delete();
        }

        // Load template from classpath
        try (FileInputStream templateStream = new FileInputStream(getClass().getClassLoader().getResource("templates/"+ test.getTestCode() +"template.docx").getFile())) {
            fillTemplate(templateStream, output, data);
        }
        return output;
    }

    // Overloaded fillTemplate to accept InputStream
    private void fillTemplate(FileInputStream templateStream, File output, Map<String, String> variables) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(templateStream)) {
            // Replace in normal paragraphs
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                replaceInParagraph(paragraph, variables);
            }
            // Replace in table cells
            doc.getTables().forEach(table ->
                table.getRows().forEach(row ->
                    row.getTableCells().forEach(cell ->
                        cell.getParagraphs().forEach(paragraph ->
                            replaceInParagraph(paragraph, variables)
                        )
                    )
                )
            );
            try (FileOutputStream out = new FileOutputStream(output)) {
                doc.write(out);
            }
        }
    }

    // Helper method to replace placeholders in a paragraph (preserves style, handles split placeholders)
    private void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> variables) {
        // Concatenate all run texts
        StringBuilder fullText = new StringBuilder();
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                fullText.append(text);
            }
        }
        String replacedText = fullText.toString();
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            replacedText = replacedText.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        // If no change, do nothing
        if (replacedText.equals(fullText.toString())) {
            return;
        }
        // Remove all runs
        int numRuns = paragraph.getRuns().size();
        for (int i = numRuns - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }
        // Create a new run with the replaced text and copy style from the first run if available
        if (!replacedText.isEmpty()) {
            XWPFRun newRun = paragraph.createRun();
            if (numRuns > 0) {
                XWPFRun firstRun = paragraph.createRun();
                firstRun = paragraph.insertNewRun(0);
                XWPFRun styleSource = paragraph.getRuns().size() > 0 ? paragraph.getRuns().get(0) : null;
                if (styleSource != null) {
                    newRun.getCTR().setRPr(styleSource.getCTR().getRPr());
                }
            }
            newRun.setText(replacedText);
        }
    }
}
