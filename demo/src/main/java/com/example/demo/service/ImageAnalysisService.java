package com.example.demo.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ImageAnalysisService {

    /**
     * Verify if the image is predominantly green (indicating safe/clean status in
     * cloud blacklist apps).
     */
    public boolean verifyImageColor(File imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            if (image == null)
                return false;

            int width = image.getWidth();
            int height = image.getHeight();
            long greenPixels = 0;
            long totalPixels = 0;

            // Simple sampling or full scan. Let's do a skip scan for performance.
            // 100x100 points
            int stepX = Math.max(1, width / 100);
            int stepY = Math.max(1, height / 100);

            for (int x = 0; x < width; x += stepX) {
                for (int y = 0; y < height; y += stepY) {
                    int rgb = image.getRGB(x, y);
                    // int alpha = (rgb >> 24) & 0xFF;
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    // Definition of "Greenish"
                    // Green should be significantly higher than Red and Blue
                    if (green > red * 1.2 && green > blue * 1.2 && green > 50) {
                        greenPixels++;
                    }
                    totalPixels++;
                }
            }

            // If more than 20% is green, we assume it's the green success screen.
            // This threshold might need tuning based on actual screenshots.
            return (double) greenPixels / totalPixels > 0.1;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extract QQ number from image using OCR.
     */
    public String extractQQNumber(File imageFile) {
        ITesseract instance = new Tesseract();
        // Set tessdata path if needed. For now assume default installation or set env
        // var.
        // instance.setDatapath("...");
        instance.setLanguage("eng"); // Numbers are usually in english mode

        try {
            String result = instance.doOCR(imageFile);
            System.out.println("OCR Result: " + result);
            return result;
        } catch (TesseractException e) {
            System.err.println("OCR Error: " + e.getMessage());
            return "";
        }
    }
}
