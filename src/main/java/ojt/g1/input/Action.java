package ojt.g1.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Action {

    private Robot robot;
    private boolean xScrolling = false;

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

        stopXScroll();

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
        stopXScroll();
        String[] position = code.split("%")[1].split("\\|");

        int deltaX = (int) Float.parseFloat(position[0]);
        int deltaY = (int) Float.parseFloat(position[1]);

        int currentX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        int currentY = (int) MouseInfo.getPointerInfo().getLocation().getY();
        robot.mouseMove(currentX + deltaX, currentY + deltaY);
    }

    public void scroll(String code) {
        String[] data = code.split("%");

        int scrollAmount = (int) Double.parseDouble(data[1]) > 0 ? 1 : -1;

        if (data[2].equals("y")) {
            stopXScroll();
            robot.mouseWheel(scrollAmount);
        } else if (data[2].equals("x")) {
            if (!xScrolling) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                xScrolling = true;
            }
            robot.mouseWheel(scrollAmount);
        }
    }

    public void stopXScroll() {
        if (xScrolling) {
            robot.keyRelease(KeyEvent.VK_SHIFT);
            xScrolling = false;
        }
    }

    public void zoom(String code) {
        String[] data = code.split("%");

        robot.keyPress(KeyEvent.VK_CONTROL);
        if (data[1].equals("i")) {
            robot.mouseWheel(-1);
        } else if (data[1].equals("o")) {
            robot.mouseWheel(1);
        }
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
}
