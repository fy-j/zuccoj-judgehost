package top.kealine.judgehost.util;

import org.apache.commons.codec.digest.DigestUtils;
import top.kealine.judgehost.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public static String md5Hex(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return md5Hex(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String md5Hex(String filename) {
        File file = new File(Config.JUDGEHOST_TESTCASE_DIR + filename);
        return md5Hex(file);
    }
    public static String md5Hex(InputStream inputStream) throws IOException {
        return DigestUtils.md5Hex(inputStream);
    }
    public static boolean save(InputStream inputStream, String filename) {
        try {
            File file = new File(Config.JUDGEHOST_TESTCASE_DIR + filename);
            if (file.exists()) {
                if (!file.delete()) {
                    return false;
                }
            }
            OutputStream outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while((len = inputStream.read(buf)) != -1){
                outputStream.write(buf,0,len);
            }
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(String filename) {
        File file = new File(Config.JUDGEHOST_TESTCASE_DIR + filename);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
}
