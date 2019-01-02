package ocr;

import com.alibaba.fastjson.JSON;
import com.qcloud.image.ImageClient;
import com.qcloud.image.exception.AbstractImageException;
import com.qcloud.image.request.GeneralOcrRequest;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ZhPJ
 * @Date 2018/12/29 00299:01
 * @Version 1.0
 * @Description: 用于识别律师的执业证号，此类只有一个公用的方法，且为抽象的所以直接用此类调用即可
 */
public abstract class OcrLowerCard {

    private static final String APP_ID = "";
    private static final String SECRET_ID = "";
    private static final String SECRE_KEY = "";
    private static final String BUCKET_NAME = "";
    private static final ImageClient imageClient =
            new ImageClient(APP_ID, SECRET_ID, SECRE_KEY, ImageClient.NEW_DOMAIN_recognition_image_myqcloud_com/*默认使用新域名, 如果你是老用户, 请选择旧域名*/);

    // 持证人可能包含的情况
    private static final List<String> CZR_LIST = Arrays.asList("持证人", "寺证人", "持正人", "持证入", "寺证入", "持证", "特征人");
    // 执业证号
    private static final List<String> ZYZH_LIST = Arrays.asList("执业证号", "丸业证号", "执业正号", "丸业正号", "执业证", "执止证");
    // 法律职业资格 用于排除持证人和本行同行的问题
    private static final List<String> FYLZG_LIST = Arrays.asList("法律职业资格");

    // 正则表达式 用于去除在律师证号中，可能会出现一些 识别的标点
    private static final String REG_CZR = "[`~!@#$%^&*()+=|{}':;,\\[\\]》·.<>/?~！@#￥%……（）——+|{}【】‘；：”“’。，、？ 0-9a-zA-Z]";
    // 用于去除非0-9的数字，职业证号只有数字
    private static final String REG_ZYZH = "[^0-9]";

    /**
     * @deprecation: 进行ocr识别
     *                 识别内容为律师的执业证
     * @param:imageFile 为传递的图片文件
     * @return: 返回的是一个枚举类型，使用OcrLowerCardEnum即可获取对应的值
     * @see: OcrLowerCardEnum 可查看对应的枚举类型
     */
    public static Map<OcrLowerCardEnum, String> ocrLowerCardInfo(File imageFile) throws AbstractImageException {
        Map<OcrLowerCardEnum, String> resultMap = new HashMap<OcrLowerCardEnum, String>();
        // 控制循环,用于提高效率
        boolean flagCzr = true;
        boolean flagZyzh = true;
        boolean flagFlzy = true;
        // 结果值
        // 持证人
        String czrString = "";
        // 执业证号
        String zyzhString = "";
        // 法律职业资格
        String flzyString = "";

        final GeneralOcrRequest request  = new GeneralOcrRequest(BUCKET_NAME, imageFile);
        final String sResponse = imageClient.generalOcr(request);
        final OCRResponse ocrResponse = JSON.parseObject(sResponse, OCRResponse.class);
        System.out.println(sResponse);

        final List<Item> items = ocrResponse.getData().getItems();
        // 循环遍历JSON串
        for (int i = 0; i < items.size(); i++) {
            final String itemstring = items.get(i).getItemstring().replaceAll(" ","");
            // 开始查找持证人，查到后，为了提升性能，那么将标识符置为false
            if (flagCzr) {
                czrString = startMatch(itemstring, CZR_LIST, i, items, "人", 4)
                        .replaceAll(REG_CZR,"");
                if (StringUtils.isNotBlank(czrString)) {
                    if (czrString.contains("持证")) {
                        czrString = czrString.substring(czrString.indexOf("持证") + 2);
                    }
                    flagCzr = false;
                }
            }
            // 用于判断是否持证人和律师资格证处于同行
            if (flagFlzy) {
                flzyString = startMatch(itemstring, FYLZG_LIST, i, items, "------", 999)
                        .replaceAll(REG_CZR,"");
                if (StringUtils.isNotBlank(flzyString)) {
                    flagFlzy = false;
                }
            }
            // 开始查找执业证号
            if (flagZyzh && !itemstring.contains("号类")) {
                zyzhString = startMatch(itemstring, ZYZH_LIST, i, items, "号", 7)
                        .replaceAll(REG_ZYZH,"");
                if (StringUtils.isNotBlank(zyzhString)) {
                    if (zyzhString.contains("执业证")) {
                        zyzhString = zyzhString.substring(zyzhString.indexOf("执业证") + 3);
                    }
                    flagZyzh = false;
                }
            }
        }
        czrString = czrString.contains("律师资格") ? flzyString : czrString;
        resultMap.put(OcrLowerCardEnum.CZR, czrString);
        resultMap.put(OcrLowerCardEnum.ZYZH, zyzhString);
        return resultMap;
    }

    /**
     * 在JSON串中查找对应的律师信息，先进行同行匹配，在进行索引匹配。
     * cor识别可能将 Key和Vaule识别在一行，那么就用匹配符进行匹配
     * @param itemstring 腾讯云返回JSON串中，对应的对象组合成的字符串
     * @param kwList 对应的需要查找的关键词，可在类外提前声明
     * @param i 腾讯云JSON返回的项的索引值
     * @param items 腾讯云返回对应的项
     * @param regs 匹配符，目的是如果识别在同一行，那么用最后这个字进行匹配
     * @param len 限制长度，防止识别时出现在同一行，如果大于此长度，那么我们认为他是同一行
     * @return 最终的字符串
     */
    private static String startMatch (String itemstring, List<String> kwList, int i, List<Item> items, String regs, int len) {
        int index = -1;
        String result = "";
        for (String tempStr : kwList) {
            if (itemstring.contains(tempStr)) {
                if (itemstring.length() > len) {
                    result = itemstring.substring(itemstring.indexOf(regs) + 1);
                } else {
                    index = i + 1;
                }
                break;
            }
        }
        return index > -1 && index < items.size()
                ? items.get(index).getItemstring()
                : result;
    }

}
