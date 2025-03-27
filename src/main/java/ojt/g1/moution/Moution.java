package ojt.g1.moution;

import ojt.g1.connection.NetworkHelper;
import ojt.g1.input.Action;
import ojt.g1.input.Decode;
import ojt.g1.ui.core.PageHandler;
import ojt.g1.ui.core.Window;
import ojt.g1.ui.core.WindowParams;
import ojt.g1.ui.page.Home;

import java.net.UnknownHostException;


public class Moution {

    private Home home;
    public static NetworkHelper networkHelper;

    public Moution() {
        PageHandler pageHandler = new PageHandler();
        Window window = new Window(pageHandler, 800, 500);
        window.setWindowParams(
                new WindowParams.Builder()
                        .title("Moution")
                        .build()
        );
        home = new Home(window);
        pageHandler.addPage(home);
        window.create();
    }

    public static void main(String[] args) throws UnknownHostException {
        Moution moution = new Moution();

        Action action = new Action();

        networkHelper = new NetworkHelper();
        networkHelper.setEventOnDeviceConnect(() -> {
            moution.home.onDeviceConnect(networkHelper.getDevice());
        });
        networkHelper.setEventOnDeviceDisconnect(() -> {
            moution.home.onDeviceDisconnect();
        });
        networkHelper.setEventOnMessagesEmpty(() -> {
            action.stopXScroll();
            System.out.println("Waiting for messages...");
        });
        networkHelper.startServer(25135, message -> {
            if (Decode.isInputType(message)) {
                Decode.Code code = Decode.decode(message);
                action.perform(Decode.decode(message));
                System.out.println("Received: " + message + " Code: " + Decode.translate(code.getCode()) + " Tag: " + Decode.translate(code.getTag()));
            } else if (Decode.isMouseMove(message)) {
                action.mouseMove(message);
                System.out.println("Received: " + message);
            } else if (Decode.isMouseScroll(message)) {
                action.scroll(message);
                System.out.println("Received: " + message);
            } else if (Decode.isZoom(message)) {
                action.zoom(message);
                System.out.println("Received: " + message);
            }
        });
    }
}
