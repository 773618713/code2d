package utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ZxingUtil {
    private static int margin = 0;               //白边大小，取值范围0~4

    /**
     * 解码，需要javase包。 文件方式解码
     *
     * @param file
     * @return
     */
    public static String decode(File file) {
        BufferedImage image;
        try {
            if (file == null || file.exists() == false) {
                throw new Exception(" File not found:" + file.getPath());
            }
            image = ImageIO.read(file);
            return decode(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解码
     *
     * @param image
     * @return
     * @throws NotFoundException
     */
    public static String decode(BufferedImage image) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        // 解码设置编码方式为：utf-8，
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 生成二维码
     * 保存为文件
     *
     * @param content  内容
     * @param filepath 路径
     * @param width    宽度
     * @param height   高度
     * @throws Exception
     */
    public static void encode(String content, String filepath, int width, int height) throws Exception {
        Map<EncodeHintType, Object> encodeHints = new HashMap<EncodeHintType, Object>();
        encodeHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        encodeHints.put(EncodeHintType.MARGIN, margin);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,
                encodeHints);
        Path path = FileSystems.getDefault().getPath(filepath);
        MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
    }

    /**
     * 生成二维码
     * 返回BufferedImage
     *
     * @param content 内容
     * @param width   宽度
     * @param height  高度
     * @return BufferedImage
     * @throws Exception
     */
    public static BufferedImage encode(String content, int width, int height) throws Exception {
        Map<EncodeHintType, Object> encodeHints = new HashMap<EncodeHintType, Object>();
        encodeHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        encodeHints.put(EncodeHintType.MARGIN, margin);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,
                encodeHints);
        BufferedImage bi = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bi.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bi;
    }

    /**
     * 生成二维码
     *
     * @param content  内容
     * @param filepath 路径
     * @throws Exception
     */
    public static void encode(String content, String filepath) throws Exception {
        int width = 100;
        int height = 100;
        encode(content, filepath, width, height);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @return
     * @throws Exception
     */
    public static BufferedImage encode(String content) throws Exception {
        int width = 512;
        int height = 512;
        BufferedImage bi = encode(content, width, height);
        return bi;
    }


    /**
     * 生成条形码
     *
     * @param content 内容
     * @param width   宽度
     * @param height  高度
     * @return
     * @throws Exception
     */
    public static BufferedImage encodeCode128(String content, int width, int height) throws Exception {
        Map<EncodeHintType, Object> encodeHints = new HashMap<EncodeHintType, Object>();
        encodeHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        encodeHints.put(EncodeHintType.MARGIN, margin);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, width, height,
                encodeHints);
        // Path path = FileSystems.getDefault().getPath(filepath);
        // MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
        BufferedImage bi = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bi.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bi;
    }

    //测试方法
    public static void main(String[] args) {
        try {
            BufferedImage bi = encodeCode128("123456789", 500, 100);
            FileOutputStream fos = new FileOutputStream("C:\\Users\\sun\\Desktop\\code128.png");
            ImageIO.write(bi, "png", fos);
            FileInputStream fis = new FileInputStream("C:\\Users\\sun\\Desktop\\code128.png");
            bi = ImageIO.read(fis);
            System.out.println(decode(bi));

            String code2dPath = "C:\\Users\\sun\\Desktop\\code2d.png";

            BufferedImage bi2 = encode("123456", 100, 100);
            System.out.println(decode(bi2));

            encode("admin", code2dPath);
            System.out.println(decode(new File(code2dPath)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
