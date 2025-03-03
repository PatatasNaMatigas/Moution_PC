package ojt.g1.moution;

import ojt.g1.connection.NetworkHelper;
import ojt.g1.connection.QRGenerator;
import ojt.g1.input.Action;
import ojt.g1.input.Decode;
import ojt.g1.ui.core.PageHandler;
import ojt.g1.ui.core.Window;
import ojt.g1.ui.page.Home;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Moution {

    private Home home;

    public Moution() {
        PageHandler pageHandler = new PageHandler();
        Window window = new Window(pageHandler, 800, 500);
        home = new Home(window);
        pageHandler.addPage(home);
        window.create();
    }

    public static void main(String[] args) throws UnknownHostException {
        Moution moution = new Moution();

        Action action = new Action();

        NetworkHelper networkHelper = new NetworkHelper();
        networkHelper.setEventOnDeviceConnect(() -> {
            moution.home.onDeviceConnect(networkHelper.getDevice());
        });
        networkHelper.setEventOnDeviceDisconnect(() -> {
            moution.home.onDeviceDisconnect();
        });
        networkHelper.startServer(25135, message -> {
            if (Decode.isInputType(message)) {
                Decode.Code code = Decode.decode(message);
                System.out.println("Received: " + message + " Code: " + Decode.translate(code.getCode()) + " Tag: " + Decode.translate(code.getTag()));
                action.perform(Decode.decode(message));
            } else if (Decode.isMouseMove(message)) {
                System.out.println("Received: " + message);
                action.mouseMove(message);
            } else if (Decode.isMouseScroll(message)) {
                System.out.println("Received: " + message);
                action.scroll(message);
            }
        });
    }
}
