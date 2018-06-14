package br.lauriavictor.wmb.model;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.lauriavictor.wmb.fragment.RatingFragment;

public class FetchData extends AsyncTask<Void, Void, Void> {

    String data = "";
    String dataParsed = "";
    String singleParsed = "";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.myjson.com/bins/1deeri");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            while(line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                singleParsed = "" + jsonObject.optString("name") + "\n" +
                               "Estilo: " + jsonObject.optString("estilo") + "\n" +
                               "Teor: " + jsonObject.optString("alcool") + "\n" +
                               "Classificação: " + jsonObject.optString("classificacao") + "\n" +
                               "Origem: " + jsonObject.optString("origem") + "\n\n";
                dataParsed = dataParsed + singleParsed;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        RatingFragment.mContent.setText(this.dataParsed);
    }
}