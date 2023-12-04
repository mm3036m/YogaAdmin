package Helpers;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.io.BufferedOutputStream;
        import java.io.OutputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;

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
        String apiUrl = "https://stuiis.cms.gre.ac.uk/COMP1424CoreWS/comp1424cw";
        String jsonData = params[0];

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            urlConnection.setRequestMethod("POST");

            // Set the content type and length
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(jsonData.length()));

            // Enable input/output streams
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            // Write the JSON data to the output stream
            try (OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream())) {
                os.write(jsonData.getBytes());
                os.flush();
            }

            // Get the response from the server
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                return "Upload successful";
            } else {
                return "Error: " + responseCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
