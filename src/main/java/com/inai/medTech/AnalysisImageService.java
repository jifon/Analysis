package com.inai.medTech;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisImageService {

    private final AnalysisImageRepository imageRepository;

    public String changeColor(MultipartFile multipartFile) throws IOException {
        File file = Files.createTempFile(
                        System.currentTimeMillis() + "",
                        Objects.requireNonNull
                                (multipartFile.getOriginalFilename(), "File must have an extension")
                )
                .toFile();

        multipartFile.transferTo(file);

        BufferedImage image = ImageIO.read(file);

        BufferedImage grayscaleImage = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int k = 0; k < image.getHeight(); k++) {

                Color color = new Color(image.getRGB(i, k));

                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int a = color.getAlpha();

                int grayScaling = (r + g + b) / 3;

                Color gColor = new Color(grayScaling, grayScaling, grayScaling, a);

                grayscaleImage.setRGB(i, k, gColor.getRGB());
            }
        }

        File saveFile = new File("/home/tilek/Downloads/image/images/"
                + multipartFile.getOriginalFilename());

        ImageIO.write(grayscaleImage, "PNG", saveFile);

        AnalysisImageEntity imageEntity = new AnalysisImageEntity();

        imageEntity.setUrl(saveFile.getAbsolutePath());

        imageRepository.save(imageEntity);

        return saveFile.getAbsolutePath();
    }

    public List<String> getUrls() {
        return imageRepository.findAll().stream()
                .map(AnalysisImageEntity::getUrl)
                .collect(Collectors.toList());
    }

}
