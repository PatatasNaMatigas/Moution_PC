package ojt.g1.ui.page;

import ojt.g1.connection.QRGenerator;
import ojt.g1.ui.components.*;
import ojt.g1.ui.components.Image;
import ojt.g1.ui.components.Panel;
import ojt.g1.ui.core.Page;
import ojt.g1.ui.core.Window;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Home extends Page {

    private Panel header = new Panel(0, 0, 0, 20);
    private Text scan = new Text(0, 0, 300, 0);
    private Image qrCode = new Image(
            0,
            40,
            300,
            300
    );
    private Text connectedDevicesTitle = new Text(0, 0, 300, 0);

    public Home(Window window) {
        super("HOME", window);

        create();
    }

    @Override
    public void onCreate() {
        BufferedImage generate = null;
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            generate = QRGenerator.generate(hostAddress);
            System.out.println(hostAddress);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        header.setLayoutConstraints(ConstraintType.TOP_TO_TOP, getParent());
        header.setLayoutConstraints(ConstraintType.START_TO_START, getParent());
        header.setLayoutConstraints(ConstraintType.END_TO_END, getParent());
        addComponent(header);

        scan.setLayoutConstraints(ConstraintType.START_TO_START, getParent());
        scan.setLayoutConstraints(ConstraintType.TOP_TO_BOTTOM, header);
        scan.setText("Scan the QR code to pair");
        scan.setFont(Resource.getFont("citrus.ttf", 16), Color.BLACK);
        scan.setAlignment(TextAlignment.CENTER);
        addComponent(scan);

        qrCode.setImage(generate);
        addComponent(qrCode);

        connectedDevicesTitle.setLayoutConstraints(ConstraintType.START_TO_START, getParent());
        connectedDevicesTitle.setLayoutConstraints(ConstraintType.TOP_TO_BOTTOM, qrCode);
        connectedDevicesTitle.setText("Connected Device: none");
        connectedDevicesTitle.setFont(Resource.getFont("citrus.ttf", 16), Color.BLACK);
        connectedDevicesTitle.setAlignment(TextAlignment.CENTER);
        addComponent(connectedDevicesTitle);
    }

    public void onDeviceConnect(String deviceName) {
        connectedDevicesTitle.setLayoutConstraints(ConstraintType.START_TO_START, getParent());
        connectedDevicesTitle.setLayoutConstraints(ConstraintType.TOP_TO_BOTTOM, qrCode);
        connectedDevicesTitle.setText("Connected Device: " + deviceName);
        connectedDevicesTitle.setFont(Resource.getFont("citrus.ttf", 16), Color.BLACK);
        connectedDevicesTitle.setAlignment(TextAlignment.CENTER);
        addComponent(connectedDevicesTitle);
    }

    public void onDeviceDisconnect() {
        connectedDevicesTitle.setLayoutConstraints(ConstraintType.START_TO_START, getParent());
        connectedDevicesTitle.setLayoutConstraints(ConstraintType.TOP_TO_BOTTOM, qrCode);
        connectedDevicesTitle.setText("Connected Device: None");
        connectedDevicesTitle.setFont(Resource.getFont("citrus.ttf", 16), Color.BLACK);
        connectedDevicesTitle.setAlignment(TextAlignment.CENTER);
        addComponent(connectedDevicesTitle);
    }
}