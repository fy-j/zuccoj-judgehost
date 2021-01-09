package top.kealine.judgehost.testcase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.kealine.judgehost.config.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class TestcasePreparer {
    private static final Log logger = LogFactory.getLog(TestcasePreparer.class);

    private static String checkTestcaseVersion(int testcaseId, boolean isInput) throws Exception{
        Map.Entry<String, String> fileInfo = TestcaseLocalTemp.getTestcase(testcaseId, isInput);
        String md5 = fileInfo == null ? "" : fileInfo.getValue();
        int tryTimes = 10;
        while(tryTimes-->0) {
            if (TestcaseDownloader.download(testcaseId, isInput, md5)) {
                tryTimes = 100;
                break;
            }
        }
        if (tryTimes != 100) {
            throw new Exception("check testcase version error");
        } else {
            fileInfo = TestcaseLocalTemp.getTestcase(testcaseId, isInput);
            assert fileInfo != null;
            return fileInfo.getKey();
        }
    }

    private static void copyTestcase(String filename, String judgePath, boolean isInput) throws IOException {
        File file = new File(Config.JUDGEHOST_TESTCASE_DIR + filename);
        File target = new File(judgePath + (isInput?"in.in":"out.out"));
        Files.copy(file.toPath(), target.toPath());
    }

    public static void prepareTestcase(int testcaseId, String judgePath) throws Exception {
        if (!judgePath.endsWith("/")) {
            judgePath += "/";
        }
        logger.info(String.format("Preparing testcase to %s", judgePath));
        copyTestcase(checkTestcaseVersion(testcaseId, true), judgePath, true);
        copyTestcase(checkTestcaseVersion(testcaseId, false), judgePath, false);
    }
}
