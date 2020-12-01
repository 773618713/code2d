package utils.qrcode;

public class Test {
    public static void main(String[] args) {
        String picPath = "C:\\Users\\sun\\Desktop\\twoDimensionCode.png";
        new TwoDimensionCode().encoderQRCode("123456", picPath, "png", 1);
    }
}
