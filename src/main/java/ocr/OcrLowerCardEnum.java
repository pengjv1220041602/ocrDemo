package ocr;

/**
 * @Author ZhPJ
 * @Date 2018/12/29
 * @Version 1.0
 * @Description: 用于返回的ocr识别律师的执业证内容，主要有两个，一个是持证人，一个是执业证号
 */
public enum OcrLowerCardEnum {
    CZR("czr"),ZYZH("zyzh");

    private final String name;

    OcrLowerCardEnum(String name) {
        this.name = name;
    }

    public String ofValue() {
        return this.name;
    }
}
