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

    public static void main(String[] args) {
        Moution moution = new Moution();

        networkHelper = new NetworkHelper();
        networkHelper.setEventOnDeviceConnect(() -> {
            moution.home.onDeviceConnect(networkHelper.getDevice());
        });
        networkHelper.setEventOnDeviceDisconnect(() -> {
            moution.home.onDeviceDisconnect();
        });
    }
}
