package ocr;

import com.qcloud.image.exception.AbstractImageException;

import java.io.File;
import java.util.Map;

/**
 * @Author ZhPJ
 * @Date 2018/12/29 00299:35
 * @Version 1.0
 * @Description:
 */
public class test1 {
    public static void main(String[] args) throws AbstractImageException {
        final Map<OcrLowerCardEnum, String> map = OcrLowerCard.ocrLowerCardInfo(new File("G:\\" + 9 + ".jpg"));
        System.out.println(map.get(OcrLowerCardEnum.CZR));
        System.out.println(map.get(OcrLowerCardEnum.ZYZH));
    }
}
