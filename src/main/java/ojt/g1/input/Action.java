package ojt.g1.input;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Action {

    private Robot robot;

    public Action() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void perform(Decode.Code code) {
        if (robot == null)
            return;

        switch (code.getCode()) {
            case Decode.LMB -> {
                if (code.getTag() == Decode.DOWN)
                    robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                else if (code.getTag() == Decode.UP)
                    robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
            }
            case Decode.MMB -> {
                if (code.getTag() == Decode.DOWN)
                    robot.mousePress(MouseEvent.BUTTON2_DOWN_MASK);
                else if (code.getTag() == Decode.UP)
                    robot.mouseRelease(MouseEvent.BUTTON2_DOWN_MASK);
            }
            case Decode.RMB -> {
                if (code.getTag() == Decode.DOWN)
                    robot.mousePress(MouseEvent.BUTTON3_DOWN_MASK);
                else if (code.getTag() == Decode.UP)
                    robot.mouseRelease(MouseEvent.BUTTON3_DOWN_MASK);
            }
        }
    }

    public void mouseMove(String code) {
        String[] position = code.split("%")[1].split("\\|");

        int deltaX = (int) Float.parseFloat(position[0]);
        int deltaY = (int) Float.parseFloat(position[1]);

        int currentX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        int currentY = (int) MouseInfo.getPointerInfo().getLocation().getY();
        robot.mouseMove(currentX + deltaX, currentY + deltaY);
    }

    public void scroll(String code) {
        int yScrollAmount = (int) Double.parseDouble(code.split("%")[1]) > 0 ? 1 : -1;
        System.out.println(yScrollAmount);

        robot.mouseWheel(yScrollAmount);
    }
}
