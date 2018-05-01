package com.sunday.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by admin on 2017/3/8.
 */

public class MD5Utils {

    public static boolean verifyInstallPackage(File file,String md5) {
        try {
            MessageDigest sig = MessageDigest.getInstance("MD5");
            InputStream signedData = new FileInputStream(file);
            byte[] buffer = new byte[4096];//每次检验的文件区大小
            long toRead = file.length();
            long soFar = 0;
            boolean interrupted = false;
            while (soFar < toRead) {
                interrupted = Thread.interrupted();
                if (interrupted) break;
                int read = signedData.read(buffer);
                soFar += read;
                sig.update(buffer, 0, read);
            }
            byte[] digest = sig.digest();
            String digestStr = bytesToHexString(digest);//将得到的MD5值进行移位转换
            digestStr = digestStr.toLowerCase();
            md5 = md5.toLowerCase();
            if (digestStr.equals(md5)) {//比较两个文件的MD5值，如果一样则返回true
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        int i = 0;
        while (i < src.length) {
            int v;
            String hv;
            v = (src[i] >> 4) & 0x0F;
            hv = Integer.toHexString(v);
            stringBuilder.append(hv);

            v = src[i] & 0x0F;
            hv = Integer.toHexString(v);
            stringBuilder.append(hv);
            i++;
        }
        return stringBuilder.toString();
    }

    private static Random random = new Random();
    public static String random(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<6;i++){
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }


    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
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
            result = buf.toString();
//            System.out.println("MD5(" + sourceStr + ",32) = " + result);
//            System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
}
