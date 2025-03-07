package ojt.g1.ui.page;

import ojt.g1.connection.QRGenerator;
import ojt.g1.ui.components.*;
import ojt.g1.ui.components.Button;
import ojt.g1.ui.components.Image;
import ojt.g1.ui.components.Panel;
import ojt.g1.ui.core.Page;
import ojt.g1.ui.core.Window;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
//        saveButtonData();
        saveTextData();
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

    public void saveButtonData() {
        ArrayList<Button> buttons = getComponents(Button.class);

        JSONArray buttonArray = new JSONArray();

        for (Button button : buttons) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", button.getId());
                jsonObject.put("x", button.getX());
                jsonObject.put("y", button.getY());
                jsonObject.put("width", button.getWidth());
                jsonObject.put("height", button.getHeight());

                // Store an action identifier (e.g., method name, button tag)
                jsonObject.put("action", button.getTag() != null ? button.getTag().toString() : "defaultAction");

                buttonArray.put(jsonObject);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        File file = new File("buttons.json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(buttonArray.toString(4));
            writer.flush();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveTextData() {
        ArrayList<Text> texts = getComponents(Text.class);

        JSONArray buttonArray = new JSONArray();

        for (Text text : texts) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", text.getId());
                jsonObject.put("x", text.getX());
                jsonObject.put("y", text.getY());
                jsonObject.put("width", text.getWidth());
                jsonObject.put("height", text.getHeight());

                // Store an action identifier (e.g., method name, text tag)
                jsonObject.put("action", text.getTag() != null ? text.getTag().toString() : "defaultAction");

                buttonArray.put(jsonObject);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        File file = new File("texts.json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(buttonArray.toString(4));
            writer.flush();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}