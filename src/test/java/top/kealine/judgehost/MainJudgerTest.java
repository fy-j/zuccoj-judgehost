package top.kealine.judgehost;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import top.kealine.judgehost.config.Config;
import top.kealine.judgehost.constant.SupportedLanguage;
import top.kealine.judgehost.entity.CompileResult;
import top.kealine.judgehost.entity.JudgeTask;
import top.kealine.judgehost.worker.MainJudger;

import java.io.File;

public class MainJudgerTest {
    @Test
    public void runCaseTaskTest() {
        JudgehostInitializer.loadConfig();
        JudgeTask fakeJudgeTask = new JudgeTask();
        fakeJudgeTask.setSolutionId(3);
        fakeJudgeTask.setProblemId(1);
        fakeJudgeTask.setCode(
                "#include<bits/stdc++.h>\n" +
                "using namespace std;\n" +
                "\n" +
                "int main(){\n" +
                "    int a,b;\n" +
                "    cin>>a>>b;\n" +
                "    cout<<a+b;\n" +
                "}"
        );
        fakeJudgeTask.setLang(SupportedLanguage.CPP);
        fakeJudgeTask.setTimeLimit(1000);
        fakeJudgeTask.setMemoryLimit(65535);
        fakeJudgeTask.setTestcaseList(ImmutableList.of(14));
        MainJudger.runJudgeTask(fakeJudgeTask);
    }

    // cpp, right
    @Test
    public void cppCompileTest1() {
        JudgehostInitializer.loadConfig();
        CompileResult result = MainJudger.compileUserCode(new File(Config.JUDGEHOST_TEST_DIR),
                "#include<bits/stdc++.h>\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int main(){\n" +
                        "    int a,b;\n" +
                        "    cin>>a>>b;\n" +
                        "    cout<<a+b;\n" +
                        "}",
                SupportedLanguage.CPP);
        assert result!=null;
        assert result.getStatus() == 1;
        assert "".equals(result.getCompilerOutput().trim());
    }


    // cpp, right, c++11
    @Test
    public void cppCompileTest2() {
        JudgehostInitializer.loadConfig();
        CompileResult result = MainJudger.compileUserCode(new File(Config.JUDGEHOST_TEST_DIR),
                "#include<bits/stdc++.h>\n" +
                        "using namespace std;\n" +
                        "\n" +
                        "int main(){\n" +
                        "    int a,b;\n" +
                        "    cin>>a>>b;\n" +
                        "    auto c=a+b;\n" +
                        "    cout<<c;\n" +
                        "}",
                SupportedLanguage.CPP);
        assert result!=null;
        assert result.getStatus() == 1;
        assert "".equals(result.getCompilerOutput().trim());
    }
}
