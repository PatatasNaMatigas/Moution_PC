package ojt.g1.input;

public class Decode {

    // Message Type
    public static final int DATA = 0;
    public static final int INPUT = DATA + 1;
    public static final int MOUSE_MOVE = INPUT + 1;

    // Input Tag
    public static final int UP = MOUSE_MOVE + 1;
    public static final int DOWN = UP + 1;

    // Buttons/Inputs
    public static final int LMB = DOWN + 1;
    public static final int MMB = LMB + 1;
    public static final int RMB = MMB + 1;

    public static boolean isInputType(String code) {
        return code.split("%")[0].equals("a");
    }

    public static boolean isMouseMove(String code) {
        return code.split("%")[0].equals("mm");
    }

    public static Code decode(String code) {
        String[] decoded = code.split("%");
        return switch (decoded[1]) {
            case "lmb" -> new Code(LMB, getTag(decoded[2]));
            case "mmb" -> new Code(MMB, getTag(decoded[2]));
            case "rmb" -> new Code(RMB, getTag(decoded[2]));
            default -> new Code(-1, -1);
        };
    }

    public static int getTag(String tag) {
        return switch (tag) {
            case "u" -> UP;
            case "d" -> DOWN;
            default -> -1;
        };
    }

    public static String translate(int code) {
        return switch (code) {
            case UP -> "Up";
            case DOWN -> "Down";
            case LMB -> "Left Mouse Button";
            case MMB -> "Middle Mouse Button";
            case RMB -> "Right Mouse Button";
            default -> "Null";
        };
    }

    public static boolean isMouseScroll(String code) {
        return code.split("%")[0].equals("sc");
    }

    public static boolean isZoom(String code) {
        return code.split("%")[0].equals("zm");
    }

    public static boolean isShortCut(String code) {
        return code.split("%")[0].equals("shc");
    }

    public static class Code {
        private int code;
        private int tag;

        public Code(int code, int tag) {
            this.code = code;
            this.tag = tag;
        }

        public int getCode() {
            return code;
        }

        public int getTag() {
            return tag;
        }
    }
}
