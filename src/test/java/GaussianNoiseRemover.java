import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class GaussianNoiseRemover {
    private BufferedImage image;

    public GaussianNoiseRemover(BufferedImage image) {
        this.image = deepCopy(image);
    }

    public void applyGaussianFilter(int size, double sigma) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage copy = deepCopy(image);

        int start = size / 2;
        double[][] filter = createGaussianFilter(size, sigma);

        for (int y = start; y < height - start; y++) {
            for (int x = start; x < width - start; x++) {
                double r = 0, g = 0, b = 0, a = 0;

                for (int i = -start; i <= start; i++) {
                    for (int j = -start; j <= start; j++) {
                        int pixel = getPixel(copy, x + j, y + i);
                        int rr = (pixel >> 16) & 0xFF;
                        int gg = (pixel >> 8) & 0xFF;
                        int bb = pixel & 0xFF;
                        int aa = (pixel >> 24) & 0xFF;

                        r += rr * filter[i + start][j + start];
                        g += gg * filter[i + start][j + start];
                        b += bb * filter[i + start][j + start];
                        a += aa * filter[i + start][j + start];
                    }
                }

                int newPixel = ((int) a << 24) | ((int) r << 16) | ((int) g << 8) | (int) b;
                setPixel(copy, x, y, newPixel);
            }
        }

        this.image = copy;
    }


    private double[][] createGaussianFilter(int size, double sigma) {
        double[][] filter = new double[size][size];
        double sum = 0.0;
        int start = size / 2;

        for (int x = -start; x <= start; x++) {
            for (int y = -start; y <= start; y++) {
                filter[x + start][y + start] = Math.exp(-(x * x + y * y) / (2 * sigma * sigma)) / (2 * Math.PI * sigma * sigma);
                sum += filter[x + start][y + start];
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                filter[i][j] /= sum;
            }
        }

        return filter;
    }
    private BufferedImage deepCopy(BufferedImage original) {
        WritableRaster raster = original.copyData(null);
        return new BufferedImage(original.getColorModel(), raster, original.isAlphaPremultiplied(), null);
    }


    private int getPixel(BufferedImage image, int x, int y) {
        return image.getRGB(x, y);
    }


    private void setPixel(BufferedImage image, int x, int y, int pixel) {
        image.setRGB(x, y, pixel);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

