package functional.reddit.com.functional_reddit;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple main activity that on the click of a button gets data from a given sub reddit.
 */
public class MainActivity extends AppCompatActivity {
    private static final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Singleton singleton = Singleton.singleton;

        // Change this to make Async requests.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Button button = (Button) findViewById(R.id.subReddit_button);
        final EditText editText = (EditText) findViewById(R.id.subreddit_editText);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Im here");
                if(editText.getText()!=null) {
                    sendGET(editText.getText().toString());
                }else {
                    // send a default sub reddit.
                    sendGET("coys");
                }
            }
        });
    }

    private void sendGET(String subreddit) {
        try {
            System.out.println("Im here in sendGet");
            String GET_URL = "http://www.reddit.com/r/" +subreddit+ "/new.json?sort=new";
            URL obj = new URL(GET_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());

                // Open a new full screen activity.
                Intent intent = new Intent(this, SubRedditViewerActivity.class);
//                TextView textView = (TextView) findViewById(R.id.textView);
//                String message = textView.getText().toString();
                intent.putExtra("functional.reddit.com.functional_reddit",response.toString());
                startActivity(intent);
            } else {
                System.out.println("GET request not worked");
            }

        } catch (MalformedURLException me) {
            me.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
