package project.utils;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Random;

/**
 * 一些简单的加密（本地sp加密）,base64解密的使用，一些编码等
 * Created by zby on 2016/12/9.
 */
public class ConvertHelper {

    /**
     * 获取字符串 指定编码的BYTE[]
     *
     * @param string
     * @param Enc
     * @return
     */
    public static byte[] String2Byte(String string, String Enc) {
        byte[] RetVal;
        try {
            RetVal = string.getBytes(Enc);
        } catch (Exception ex) {
            //log.error(ExceptionHelper.ExceptionMessage(ex));
            RetVal = string.getBytes();
        }

        return RetVal;
    }

    public static String UrlEncode(String url) {
        return UrlEncode(url, PConst.DEFAULT_ENC);
    }

    /**
     * 对URL进行编码
     */
    public static String UrlEncode(String url, String Enc) {
        String RetVal = "";
        try {
            RetVal = java.net.URLEncoder.encode(url, Enc);
        } catch (Exception ex) {
            //log.error(ExceptionHelper.ExceptionMessage(ex));
            RetVal = url;
        }
        return RetVal;

    }

    public static String UrlDecode(String url) {
        return UrlDecode(url, PConst.DEFAULT_ENC);
    }

    public static String UrlDecode(String url, String Enc) {
        String RetVal = url;
        try {
            if (url != null && url.indexOf('%') >= 0) {
                RetVal = java.net.URLDecoder.decode(url, Enc);
            }
        } catch (Exception ex) {
            //log.error(ExceptionHelper.ExceptionMessage(ex));
            RetVal = url;
        }
        return RetVal;
    }

    /**
     * 获取字符串
     *
     * @param arrByte
     * @param Enc
     * @return
     */
    public static String Byte2String(byte[] arrByte, String Enc) {
        String RetVal = new String(arrByte, Charset.forName(Enc));
        return RetVal;
    }

    /**
     * @param s
     * @return
     */
    public final static String MD5(final String s) {

        String RetVal = "";

        if (TextUtils.isEmpty(s)) {
            return RetVal;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {

            java.security.MessageDigest mdTemp = java.security.MessageDigest
                    .getInstance("MD5");

            mdTemp.update(s.getBytes());

            byte[] md = mdTemp.digest();
            char str[] = new char[md.length * 2];

            int k = 0;
            for (int i = 0; i != md.length; ++i) {
                byte byte0 = md[i];

                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }

            RetVal = new String(str);

        } catch (Exception ex) {
            //log.error(ExceptionHelper.ExceptionMessage(ex));
        }

        return RetVal;
    }

    /**
     * 加密字符串
     *
     * @param strMsg 需要加密的内容
     * @return 截取后的MD5摘要信息
     */
    public static String encode(String strMsg) {

        String result = "";
        // 字符串为空
        if (TextUtils.isEmpty(strMsg))
            return result;

        try {
            byte b[] = md5(strMsg.getBytes("utf-8"));
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            // 截取中间16位
            result = buf.toString().toUpperCase().substring(8, 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取byte[]的md5值
     *
     * @param bytes byte[]
     * @return md5
     * @throws Exception
     */
    public static byte[] md5(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        return md.digest();
    }


    public static double Byte2GB(double byteSize) {
        double RetVal = byteSize / (1024 * 1024 * 1024);
        return RetVal;
    }

    public static double Byte2MB(double byteSize) {
        double RetVal = byteSize / (1024 * 1024);
        return RetVal;
    }

    public static double Byte2KB(double byteSize) {
        double RetVal = byteSize / (1024);
        return RetVal;
    }

    public static String Base64Enc(byte[] data) {
        String RetVal = "";
        if (data != null && data.length >= 0) {
            RetVal = Base64.encode(data);
            RetVal = RetVal.replace("\r\n", "");
        }
        return RetVal;
    }

    public static byte[] Base64Dec(String base64) {
        byte[] Data = new byte[0];
        try {

            Data = Base64.decode(base64);
        } catch (Exception e) {
            //log.error(ExceptionHelper.ExceptionMessage(e));
            Data = new byte[0];
        }
        return Data;
    }

    /**
     * Base64加密字符串
     *
     * @param encodeStr
     * @return
     * @throws Exception
     */
    public static String ToBASE64(String encodeStr) {
        byte[] data = String2Byte(encodeStr, PConst.DEFAULT_ENC);
        return Base64Enc(data);
    }

    /**
     * Base64解密
     *
     * @param base64
     * @return
     */
    public static String FromBASE64(String base64) {
        byte[] b = null;
        if (base64 != null) {
            b = Base64Dec(base64);
        }

        String decodeStr = Byte2String(b, PConst.DEFAULT_ENC);
        return decodeStr;
    }

    /**
     * 简单加密(用于保存本地数据)
     *
     * @param strOriginal
     * @return
     */
    public static String doEncrypt(String strOriginal) {
        String strReturn = "";

        if (strOriginal == null || strOriginal.isEmpty()) {
            return strReturn;
        }

		/*
         * 加密算法： 1.对每个字节和产生的随机数进行按位异或 2.base64编码字符串 3.把随机数对应的字符4位连接到字符串的尾部
		 */

        // 加密随机数
        Random rd = new Random();
        byte intRnd = (byte) (rd.nextInt(128));
        rd = null;
        // byte intRnd = (byte)253;

        byte[] dataEncrypt = String2Byte(strOriginal, PConst.DEFAULT_ENC);
        if (dataEncrypt == null || dataEncrypt.length <= 0)
            return strReturn;

        for (int i = 0; i < dataEncrypt.length; ++i) {
            dataEncrypt[i] = (byte) (dataEncrypt[i] ^ intRnd);
        }

        strReturn = Base64Enc(dataEncrypt);
        dataEncrypt = null;

        int bound = intRnd;
        String strTemp = strReturn + String.format("%04d", bound);
        char[] arrTemp = strTemp.toCharArray();
        int iBeg = 0;
        int iEnd = arrTemp.length - 1;
        StringBuffer buff = new StringBuffer(arrTemp.length + 1);
        boolean isBeg = true;
        while (iBeg <= iEnd) {
            if (isBeg) {
                buff.append(arrTemp[iBeg++]);
                isBeg = false;
            } else {

                buff.append(arrTemp[iEnd--]);
                isBeg = true;
            }
        }

        strReturn = buff.toString();
        buff = null;

        // 返回加密后的字符串
        return strReturn;
    }

    /**
     * 简单解密(用于保存本地数据)
     *
     * @param strCryptograph
     * @return
     */
    public static String doDecrypt(String strCryptograph) {
        String RetVal = "";

        try {
            if (strCryptograph == null || strCryptograph.length() <= 4) {
                return RetVal;
            }

            //
            char[] arrTemp = strCryptograph.toCharArray();
            char[] arrDest = new char[arrTemp.length];

            int iBeg = 0;
            int iEnd = arrDest.length - 1;
            boolean isBeg = true;
            for (char c : arrTemp) {
                if (isBeg) {
                    arrDest[iBeg++] = c;
                    isBeg = false;
                } else {
                    arrDest[iEnd--] = c;
                    isBeg = true;
                }
            }
            arrTemp = null;

            // 校验末尾是否为4为数字
            boolean isNumChar = true;
            for (int i = 1; i <= 4; ++i) {
                char c = arrDest[arrDest.length - i];
                if (c < '0' || c > '9') {
                    isNumChar = false;
                    break;
                }
            }
            if (!isNumChar) {
                arrDest = null;
                return RetVal;
            }

            strCryptograph = new String(arrDest);
            arrDest = null;

            //
            String strBound = strCryptograph
                    .substring(strCryptograph.length() - 4);
            String strData = strCryptograph.substring(0,
                    strCryptograph.length() - 4);

            int intRnd = Integer.parseInt(strBound);

            byte[] dataDecrypt = Base64Dec(strData);

            for (int i = 0; i < dataDecrypt.length; ++i) {
                dataDecrypt[i] = (byte) (dataDecrypt[i] ^ intRnd);
            }

            RetVal = Byte2String(dataDecrypt, PConst.DEFAULT_ENC);
            dataDecrypt = null;
        } catch (Exception ex) {
            //log.error(ExceptionHelper.ExceptionMessage(ex));
            RetVal = "";
        }

        return RetVal;
    }

    /**
     * 对象序列化成二进制流字符串 与unSerialize配合使用
     *
     * @param obj
     * @return
     */
    public static String serialize2String(Object obj) {

        String retVal = "";
        // 序列化使用的输出流
        ObjectOutputStream oos = null;
        // 序列化后数据流给ByteArrayOutputStream 来保存。
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            byte[] datas = baos.toByteArray();
            retVal = ConvertHelper.Base64Enc(datas);
            baos.close();
            oos.close();
        } catch (Exception ex) {
            //log.error(ExceptionHelper.ExceptionMessage(ex));
        }

        return retVal;
    }

    /**
     * 对象字符串反序列化成对象 与serialize配合使用,对返回的对象做强转
     *
     * @param strObj
     * @return
     */
    public static Object unSerialize2Obj(String strObj) {

        Object retVal = null;
        // ByteArrayInputStream 可接收一个字节数组 "byte[] "。供反序列化做参数
        ByteArrayInputStream bais = null;
        // 反序列化使用的输入流
        ObjectInputStream ois = null;

        try {
            byte[] datas = ConvertHelper.Base64Dec(strObj);
            bais = new ByteArrayInputStream(datas);
            ois = new ObjectInputStream(bais);
            retVal = ois.readObject();
            bais.close();
            ois.close();
        } catch (Exception ex) {
            //log.error(ExceptionHelper.ExceptionMessage(ex));
        }

        return retVal;
    }

    public static void main(String[] args) {
        String regCode = "NTc2ODUzNTQ3MDY4Njc1NDY1N中文字符内容TU0OTU0N||\\jU2NTU看看看看0NTI2ODQ4NTU1MjUwNTI2NzU0NjY3MDcwNDk1_posaNTU1NTU1Ng==";

        // String regCode = "";
        System.out.println(regCode);
        String encStr = doEncrypt(regCode);
        System.out.println(encStr);
        String decStr = doDecrypt(encStr);
        System.out.println(decStr);

    }

}
