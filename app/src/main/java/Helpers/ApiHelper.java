package Helpers;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiHelper extends AsyncTask<String, Void, String> {

    private final Callback callback;
    private final ProgressBar progressBar;

    public interface Callback {
        void onTaskComplete(String result);
    }

    // Constructor to set the callback
    public ApiHelper(Callback callback,ProgressBar progressBar) {

        this.callback = callback;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (callback != null) {
            callback.onTaskComplete(result);
        }

        // Hide the ProgressBar
        if (progressBar != null) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String apiUrl = "https://stuiis.cms.gre.ac.uk/COMP1424CoreWS/comp1424cw/SubmitClasses";
        String jsonData = params[0];

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            urlConnection.setRequestMethod("POST");

            // Set the content type and length
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            // Enable input/output streams
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            String encodedParams = encodeParams(jsonData);
            byte[] postData = encodedParams.getBytes("UTF-8");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(postData.length));

            // Write the JSON data to the output stream
            try (OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream())) {
                os.write(postData);
                os.flush();
            }

            // Get the response from the server
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    // Print or return the API response
                    System.out.println("API Response: " + response.toString());
                    return "Upload successful";
                }
            } else {


                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    // Print or return the error response
                    System.out.println("Error Response: " + errorResponse.toString());
                    return "Error: " + responseCode;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    private String encodeParams(String jsonPayload) {
        try {
            return "jsonpayload=" + URLEncoder.encode(jsonPayload, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
