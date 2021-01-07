package top.kealine.judgehost.constant;

public class JudgeResult {
    // Full Name
    public static final int PENDING = -1;
    public static final int COMPILE_ERROR = 1;
    public static final int TIME_LIMIT_EXCEED = 2;
    public static final int MEMORY_LIMIT_EXCEED = 3;
    public static final int OUTPUT_LIMIT_EXCEED = 4;
    public static final int RUNTIME_ERROR = 5;
    public static final int WRONG_ANSWER = 6;
    public static final int ACCEPTED = 7;
    public static final int PRESENTATION_ERROR = 8;
    public static final int SYSTEM_ERROR = 9;


    // Short Name
    public static final int PD = PENDING;
    public static final int CE = COMPILE_ERROR;
    public static final int TLE = TIME_LIMIT_EXCEED;
    public static final int MLE = MEMORY_LIMIT_EXCEED;
    public static final int OLE = OUTPUT_LIMIT_EXCEED;
    public static final int RE = RUNTIME_ERROR;
    public static final int WA = WRONG_ANSWER;
    public static final int AC = ACCEPTED;
    public static final int PE = PRESENTATION_ERROR;
    public static final int SE = SYSTEM_ERROR;
}
