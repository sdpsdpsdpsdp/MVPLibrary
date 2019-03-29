package com.laisontech.mvp.utils;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * ..................................................................
 * .         The Buddha said: I guarantee you have no bug!          .
 * .                                                                .
 * .                            _ooOoo_                             .
 * .                           o8888888o                            .
 * .                           88" . "88                            .
 * .                           (| -_- |)                            .
 * .                            O\ = /O                             .
 * .                        ____/`---'\____                         .
 * .                      .   ' \\| |// `.                          .
 * .                       / \\||| : |||// \                        .
 * .                     / _||||| -:- |||||- \                      .
 * .                       | | \\\ - /// | |                        .
 * .                     | \_| ''\---/'' | |                        .
 * .                      \ .-\__ `-` ___/-. /                      .
 * .                   ___`. .' /--.--\ `. . __                     .
 * .                ."" '< `.___\_<|>_/___.' >'"".                  .
 * .               | | : `- \`.;`\ _ /`;.`/ - ` : | |               .
 * .                 \ \ `-. \_ __\ /__ _/ .-` / /                  .
 * .         ======`-.____`-.___\_____/___.-`____.-'======          .
 * .                            `=---='                             .
 * ..................................................................
 * Created by SDP on 2018/5/29.
 */

public class GzipUtils {
    private static final int BUFFER_SIZE = 256;

    /**
     * GZIP压缩
     *
     * @param bytes 待压缩的字符数组
     * @return 压缩后的字符数组
     */
    public static byte[] compress_GZIP(byte[] bytes) {
        if (bytes == null || bytes.length < 1) {
            return null;
        }
        byte[] compressbytes;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
            gzip.flush();
            gzip.close();
            compressbytes = out.toByteArray();
            out.close();
            return compressbytes;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * GZIP解压
     *
     * @param gzipStr 待解压的字符串
     * @return 解压后的字符串
     */
    public static String decompress_GZIP(String gzipStr) {
        if (gzipStr == null || gzipStr.isEmpty()) {
            return "";
        }
        byte[] bytes = hexStringToBytes(gzipStr, false);
        String retstr;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            gunzip.close();
            in.close();
            retstr = out.toString("UTF-8");
            out.close();
            return retstr;
        } catch (IOException ex) {
            return "";
        }
    }

    private static byte[] hexStringToBytes(String hexstr, boolean lowbytesfirst) {
        //先过滤无效字符
        hexstr = filterHexString(hexstr);
        if ((hexstr == null) || (hexstr.length() < 2) || (hexstr.length() % 2 == 1)) {
            return null;
        }
        hexstr = hexstr.toUpperCase();
        int bytelen = hexstr.length() / 2;
        byte[] bytes = new byte[bytelen];

        char[] hexChars = hexstr.toCharArray();
        int pos = 0;
        for (int i = 0; i < bytelen; i++) {
            pos = i * 2;
            bytes[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        //输入为 "00 11 22 33"
        //目前bytes里保存的是 0x33221100
        //如果传输的是先低字节,则期待转换为 0x33221100
        //如果传输的是先高字节,则期待转换为 0x00112233
        //因此,如果是 先高字节模式,还需要进行高低字节翻转
        if (!lowbytesfirst) {
            return bytes;
        }
        byte[] retarry = new byte[bytelen];
        for (int idx = 0; idx < bytelen; idx++) {
            retarry[idx] = bytes[bytelen - idx - 1];
        }
        return retarry;
    }

    /**
     * 从字符串里过滤无效字符,得到hex字符串
     *
     * @param rawstring 字符串
     * @return 过滤后的字符串
     */
    private static String filterHexString(String rawstring) {
        if (rawstring == null || rawstring.length() < 1) {
            return "";
        }

        StringBuilder strb = new StringBuilder(rawstring.length());
        for (int charidx = 0; charidx < rawstring.length(); ++charidx) {
            if ((rawstring.charAt(charidx) >= '0' && rawstring.charAt(charidx) <= '9') ||
                    (rawstring.charAt(charidx) >= 'a' && rawstring.charAt(charidx) <= 'f') ||
                    (rawstring.charAt(charidx) >= 'A' && rawstring.charAt(charidx) <= 'F')) {
                strb.append(rawstring.charAt(charidx));
            }
        }

        String ret = strb.toString();
        strb = null;
        return ret;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
