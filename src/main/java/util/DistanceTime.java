/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Aleksandar
 */
import com.google.maps.errors.ApiException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DistanceTime {

    private static final String API_KEY = "AIzaSyAQ9l03G5RQmNL-kYajXTmBoodhwO9gqEA";

    public static long getDriveDist(String source, String destination) throws ApiException, InterruptedException, IOException, NoSuchAlgorithmException, KeyManagementException, ParseException {
        /*
     *  fix for
     *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
     *       sun.security.validator.ValidatorException:
     *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
     *               unable to find valid certification path to requested target
         */
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /*
     * end of the fix
         */
        String url_string = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + source + "&destinations=" + destination + "&key=" + API_KEY;
        url_string = url_string.replaceAll(" ", "%20");
        URL url = new URL(url_string);
        URLConnection con = url.openConnection();
        Reader reader = new InputStreamReader(con.getInputStream());

        StringBuilder sb = new StringBuilder();
        while (true) {
            int ch = reader.read();
            if (ch == -1) {
                break;
            }
            sb.append((char) ch);
        }

        String result = sb.toString();

        
        //System.out.println(url_string);
        //System.out.println(result);

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(result);
        JSONObject jsonobj = (JSONObject) obj;

        JSONArray dist = (JSONArray) jsonobj.get("rows");
        JSONObject obj2 = (JSONObject) dist.get(0);
        JSONArray disting = (JSONArray) obj2.get("elements");
        JSONObject obj3 = (JSONObject) disting.get(0);
        JSONObject obj4 = (JSONObject) obj3.get("distance");
        JSONObject obj5 = (JSONObject) obj3.get("duration");
        //System.out.println(obj4.get("text"));
        //System.out.println(obj5.get("text"));

        long res = (long) obj5.get("value");

        return res;
    }
    /*private static final String API_KEY = "AIzaSyAQ9l03G5RQmNL-kYajXTmBoodhwO9gqEA";
    OkHttpClient client = new OkHttpClient();
    private static final GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);

    public DistanceMatrix estimateRouteTime(DateTime time, Boolean isForCalculateArrivalTime, DirectionsApi.RouteRestriction routeRestriction, LatLng departure, LatLng... arrivals) {
        try {
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
            if (isForCalculateArrivalTime) {
                req.departureTime(time);
            } else {
                req.arrivalTime(time);
            }
            if (routeRestriction == null) {
                routeRestriction = DirectionsApi.RouteRestriction.TOLLS;
            }
            DistanceMatrix trix = req.origins(departure)
                    .destinations(arrivals)
                    .mode(TravelMode.DRIVING)
                    .avoid(routeRestriction)
                    .language("fr-FR")
                    .await();
            return trix;

        } catch (ApiException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void calculate(String source, String destination) throws IOException {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        try {
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + source + "&destinations=" + destination + "&key=" + API_KEY;
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            String response_text = response.body().string();

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(response_text);
            JSONObject jsonobj = (JSONObject) obj;

            JSONArray dist = (JSONArray) jsonobj.get("rows");
            JSONObject obj2 = (JSONObject) dist.get(0);
            JSONArray disting = (JSONArray) obj2.get("elements");
            JSONObject obj3 = (JSONObject) disting.get(0);
            JSONObject obj4 = (JSONObject) obj3.get("distance");
            JSONObject obj5 = (JSONObject) obj3.get("duration");
            System.out.println(obj4.get("text"));
            System.out.println(obj5.get("text"));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }*/

}
