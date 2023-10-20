import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {
        try {
            // Обработка изображения с гауссовским шумом
            File inputGaussianImageFile = new File("D:\\Image\\src\\test\\main\\java\\org\\example\\noisy_image.png");
            BufferedImage originalGaussianImage = ImageIO.read(inputGaussianImageFile);

            GaussianNoiseRemover gaussianNoiseRemover = new GaussianNoiseRemover(originalGaussianImage);
            gaussianNoiseRemover.applyGaussianFilter(3, 1.0);
            BufferedImage processedGaussianImage = gaussianNoiseRemover.getImage();

            File outputProcessedGaussianImageFile = new File("processed_gaussian_image.png");
            ImageIO.write(processedGaussianImage, "png", outputProcessedGaussianImageFile);

            // Создание разности между исходным и обработанным изображениями с гауссовским шумом
            BufferedImage diffGaussianImage = getDifferenceImage(originalGaussianImage, processedGaussianImage);
            File outputDiffGaussianImageFile = new File("diff_gaussian_image.png");
            ImageIO.write(diffGaussianImage, "png", outputDiffGaussianImageFile);

            System.out.println("Обработка изображения с гауссовским шумом завершена. Результат сохранен в " + outputProcessedGaussianImageFile.getAbsolutePath());
            System.out.println("Разность между изображениями сохранена в " + outputDiffGaussianImageFile.getAbsolutePath());

            // Обработка изображения с равномерным шумом
            File inputUniformImageFile = new File("D:\\Image\\src\\test\\main\\java\\org\\example\\noisy_image2.png");
            BufferedImage originalUniformImage = ImageIO.read(inputUniformImageFile);

            UniformNoiseRemover uniformNoiseRemover = new UniformNoiseRemover(originalUniformImage);
            uniformNoiseRemover.applyMedianFilter(3);
            BufferedImage processedUniformImage = uniformNoiseRemover.getImage();

            File outputProcessedUniformImageFile = new File("processed_uniform_image.png");
            ImageIO.write(processedUniformImage, "png", outputProcessedUniformImageFile);

            // Создание разности между исходным и обработанным изображениями с равномерным шумом
            BufferedImage diffUniformImage = getDifferenceImage(originalUniformImage, processedUniformImage);
            File outputDiffUniformImageFile = new File("diff_uniform_image.png");
            ImageIO.write(diffUniformImage, "png", outputDiffUniformImageFile);

            System.out.println("Обработка изображения с равномерным шумом завершена. Результат сохранен в " + outputProcessedUniformImageFile.getAbsolutePath());
            System.out.println("Разность между изображениями сохранена в " + outputDiffUniformImageFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для извлечения разности между двумя изображениями
    public static BufferedImage getDifferenceImage(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();

        BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);
                int diffRGB = rgb1 - rgb2;

                diffImage.setRGB(x, y, diffRGB);
            }
        }

        return diffImage;
    }
}
