package auca.ac.rw.restfullApiAssignment.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfExtractor {
    public static void main(String[] args) throws IOException {
        String projectRoot = System.getProperty("user.dir");
        String defaultPath = projectRoot + File.separator + "Spring_Boot_Practical_Questions.pdf";
        String pdfPath = args.length > 0 ? args[0] : defaultPath;
        File pdf = new File(pdfPath);
        if (!pdf.exists()) {
            System.err.println("PDF not found: " + pdf.getAbsolutePath());
            System.exit(2);
        }

        try (PDDocument document = PDDocument.load(pdf)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            Path out = Path.of(projectRoot, "target", "Spring_Boot_Practical_Questions.txt");
            Files.createDirectories(out.getParent());
            Files.writeString(out, text, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Extracted text written to: " + out.toString());
        } catch (IOException e) {
            System.err.println("Failed to extract PDF: " + e.getMessage());
            throw e;
        }
    }
}
