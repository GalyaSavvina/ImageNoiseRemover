import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniformNoiseRemover {
    private BufferedImage image;

    public UniformNoiseRemover(BufferedImage image) {
        this.image = deepCopy(image);
    }

    public void applyMedianFilter(int size) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage copy = deepCopy(image);

        for (int y = size / 2; y < height - size / 2; y++) {
            for (int x = size / 2; x < width - size / 2; x++) {
                List<Integer> rValues = new ArrayList<>();
                List<Integer> gValues = new ArrayList<>();
                List<Integer> bValues = new ArrayList<>();
                List<Integer> aValues = new ArrayList();

                for (int i = -size / 2; i <= size / 2; i++) {
                    for (int j = -size / 2; j <= size / 2; j++) {
                        int pixel = getPixel(copy, x + j, y + i);
                        int rr = (pixel >> 16) & 0xFF;
                        int gg = (pixel >> 8) & 0xFF;
                        int bb = pixel & 0xFF;
                        int aa = (pixel >> 24) & 0xFF;

                        rValues.add(rr);
                        gValues.add(gg);
                        bValues.add(bb);
                        aValues.add(aa);
                    }
                }

                Collections.sort(rValues);
                Collections.sort(gValues);
                Collections.sort(bValues);
                Collections.sort(aValues);

                int medianPixel = ((int) aValues.get(aValues.size() / 2) << 24) |
                        ((int) rValues.get(rValues.size() / 2) << 16) |
                        ((int) gValues.get(gValues.size() / 2) << 8) |
                        (int) bValues.get(bValues.size() / 2);

                setPixel(copy, x, y, medianPixel);
            }
        }

        this.image = copy;
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
}
