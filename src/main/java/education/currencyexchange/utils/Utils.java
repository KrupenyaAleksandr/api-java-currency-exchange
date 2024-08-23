package education.currencyexchange.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Optional;

public class Utils {

     public static Optional<HashMap<String, String>> readParametersForPatch(HttpServletRequest req) {
        HashMap<String, String> parameters = null;
        String query;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
            if ((query = br.readLine()) != null) {
                if (!query.isEmpty()) {
                    parameters = new HashMap<>();
                    String[] pairs = query.split("&");

                    for (String pair : pairs) {
                        String[] keyValuePairs = pair.split("=");
                        if (keyValuePairs.length == 2) {
                            parameters.put(keyValuePairs[0], keyValuePairs[1]);
                        }
                    }

                    return Optional.of(parameters);
                } else {
                    return Optional.empty();
                }
            }
            else {
                return Optional.empty();
            }
        }
        catch (IOException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> checkExchangeRatePath(String pathInfo) {
        if (pathInfo != null && !pathInfo.equals("/")) {
            if (pathInfo.replaceFirst("/", "").length() == 6) {
                return Optional.of(pathInfo.replaceFirst("/", "").toUpperCase());
            }
        }
        return Optional.empty();
    }

    public static Optional<String> checkCurrencyPath(String pathInfo) {
        if (pathInfo != null && !pathInfo.equals("/")) {
            return Optional.of(pathInfo.replaceFirst("/", "").toUpperCase());
        }
        return Optional.empty();
    }

}
