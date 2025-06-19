package com.eof.asset_control_system_backend.service;

import com.eof.asset_control_system_backend.model.Movement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    public byte[] generateTermPdf(Movement movement) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Termo de Responsabilidade de Equipamento");
            contentStream.newLine();
            contentStream.newLine();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.showText("Eu, " + movement.getAppUser().getName() + ",");
            contentStream.newLine();
            contentStream.showText("do departamento " + movement.getAppUser().getDepartment() + ", declaro ter recebido o(s) seguinte(s) equipamento(s):");
            contentStream.newLine();
            contentStream.newLine();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.showText("Equipamento: " + movement.getEquipment().getType() + " " + movement.getEquipment().getBrand() + " " + movement.getEquipment().getModel());
            contentStream.newLine();
            contentStream.showText("Número de Patrimônio: " + movement.getEquipment().getAssetNumber());
            contentStream.newLine();
            contentStream.newLine();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.showText("Tipo de Movimentação: " + movement.getType());
            contentStream.newLine();
            contentStream.showText("Data da Movimentação: " + movement.getMovementDate().format(formatter));
            contentStream.newLine();
            contentStream.newLine();
            contentStream.newLine();
            contentStream.showText("Declaro que o equipamento acima foi recebido em perfeitas condições de uso e funcionamento,");
            contentStream.newLine();
            contentStream.showText("comprometendo-me a zelar pela sua integridade e bom uso.");
            contentStream.newLine();
            contentStream.newLine();
            contentStream.newLine();
            contentStream.newLine();
            contentStream.newLine();

            contentStream.showText("_________________________________________");
            contentStream.newLine();
            contentStream.showText(movement.getAppUser().getName());
            contentStream.newLine();
            contentStream.newLine();

            contentStream.showText("_________________________________________");
            contentStream.newLine();
            contentStream.showText("Responsável de TI");
            contentStream.endText();

            contentStream.close();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}