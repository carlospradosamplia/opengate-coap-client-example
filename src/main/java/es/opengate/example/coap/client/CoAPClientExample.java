package es.opengate.example.coap.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;

public class CoAPClientExample {

    private static final String COAP_SCHEME = "coap";
    private static final int SECONDS = 1000; // 1 second = 1000 milliseconds
    private static final int TIMEOUT = 5 * SECONDS;
    private static final int API_KEY_OPTION_NUMBER = 2502;
    private static final int DEVICE_ID_OPTION_NUMBER = 2503;


    public static void main(String[] args) throws Exception {

        // Read configuration
        Properties configuration = new Properties();
        FileInputStream propertiesInputStream = new FileInputStream("config.properties");
        configuration.load(propertiesInputStream);
        propertiesInputStream.close();

        String host = configuration.getProperty("opengate.coap.host");
        int port = Integer.parseInt(configuration.getProperty("opengate.coap.port"));
        String apiKey = configuration.getProperty("opengate.apikey");
        String deviceId = configuration.getProperty("opengate.deviceId");
        String path = configuration.getProperty("opengate.coap.path");

        // Create CoAP client

        CoapClient client = new CoapClient.Builder(host, port)
                .scheme(COAP_SCHEME)
                .path(path)
                .query()
                .create();
        client.setTimeout(TIMEOUT);

        // Load payload
        File jsonFile = new File("datapoints.json");
        int jsonFileLength = (int) jsonFile.length();
        byte[] payload = new byte[jsonFileLength];
        FileInputStream jsonInputStream = new FileInputStream("datapoints.json");
        jsonInputStream.read(payload, 0, jsonFileLength);
        jsonInputStream.close();


        OptionSet optionSet = new OptionSet()
                .addOption(new Option(API_KEY_OPTION_NUMBER, apiKey))
                .addOption(new Option(DEVICE_ID_OPTION_NUMBER, deviceId));

        // Request
        Request request = (Request) Request.newPost().setPayload(payload).setOptions(optionSet);
        CoapResponse response = client.advanced(request);
        System.out.printf("Response code %s\n", response.getCode());





    }

}
