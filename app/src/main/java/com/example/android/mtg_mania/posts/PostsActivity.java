package com.example.android.mtg_mania.posts;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.mtg_mania.R;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {

    public static final String LOG_TAG = PostsActivity.class.getName();

    /**
     * The URL regarding the selected subreddit from the spinner
     */
    public static String BASE_JSON_REQUEST_URL = "https://www.reddit.com/r/";

    public static String JSON_REQUEST_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        ArrayList<String> subreddits = new ArrayList<>();
        subreddits.add("modernmagic");
        subreddits.add("magicTCG");
        subreddits.add("MagicArena");
        subreddits.add("spikes");
        subreddits.add("MTGLegacy");
        subreddits.add("Magicdeckbuilding");
        subreddits.add("EDH");
        subreddits.add("budgetdecks");


        final Spinner subredditsSpinner = (Spinner) findViewById(R.id.subreddits_spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(PostsActivity.this, R.layout.spinner_item, subreddits);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subredditsSpinner.setAdapter(spinnerAdapter);

        subredditsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubreddit = subredditsSpinner.getSelectedItem().toString();

                JSON_REQUEST_URL = BASE_JSON_REQUEST_URL + selectedSubreddit + "/hot.json";

                // Kick off an {@link AsyncTask} to perform the network request
                PostsAsyncTask task = new PostsAsyncTask();
                task.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

    }

    /**
     * Update the screen to display information from the given {@link ArrayList}.
     */
    public void updateUi(ArrayList posts) {
        // Find a reference to the {@link ListView} in the layout
        ListView postListView = (ListView) findViewById(R.id.posts_list);

        // Create a new {@link ArrayAdapter} of posts
        final PostsAdapter adapter = new PostsAdapter(this, R.layout.item_post, posts);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        postListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open the selected Reddit post.
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current post that was clicked
                Posts currentPost = adapter.getItem(position);

                if( !currentPost.getUrl().equals("error")) {
                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri postURI = Uri.parse(currentPost.getUrl());

                    // Create a new intent to view the post URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, postURI);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            }
        });
    }

    /**
     * {@link PostsAsyncTask} to perform the network request on a background thread, and then
     * update the UI with the posts list in the response.
     */
    private class PostsAsyncTask extends AsyncTask<URL, Void, ArrayList> {

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e("createURL", "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("makeHttpRequest", "Problem parsing the posts JSON results", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream == null) {
                    // function must handle java.io.IOException here
                    return "";
                }
            }
            return jsonResponse;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Posts> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(PostsActivity.JSON_REQUEST_URL);

            PostsActivity.JSON_REQUEST_URL = "";

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                System.err.println("Caught IOException: " + e.getMessage());
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            // Return the {@link Event} object as the result fo the {@link PostsAsyncTask}
            return extractPosts(jsonResponse);
        }

        /**
         * Update the screen with the given post (which was the result of the
         * {@link PostsAsyncTask}).
         */
        @Override
        protected void onPostExecute(ArrayList posts) {

            if (posts == null) {

                ArrayList<Posts> errorMessage = new ArrayList<Posts>();
                // Output an error message for the user
                String content = "It seems that you are not connected to the internet or that something wrong happened. " +
                                "Please select another subreddit or restart the MTGM app.";

                Posts post = new Posts(null, null, "error", content, null);
                errorMessage.add(post);
                posts = errorMessage;
            }

            updateUi(posts);
        }

        /**
         * Return a list of {@link Posts} objects that has been built up from
         * parsing a JSON response.
         */
        private ArrayList<Posts> extractPosts(String jsonResponse) {

            // Create an empty ArrayList that we can start adding news to
            ArrayList<Posts> posts = new ArrayList<>();

            // Try to parse the jsonResponse. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {

                JSONObject baseJsonResponse = new JSONObject(jsonResponse);
                JSONObject postsData = baseJsonResponse.optJSONObject("data");
                JSONArray postsArray = postsData.getJSONArray("children");

                for (int i = 0; i < postsArray.length(); i++) {
                    JSONObject currentPost = postsArray.getJSONObject(i);
                    JSONObject data = currentPost.getJSONObject("data");

                    // Extract the value for the key called "title"
                    String title = data.getString("title");

                    // Extract the value for the key called "author"
                    String author = data.getString("author");

                    // Extract the value for the key called "url"
                    String url = data.getString("url");

                    // Extract the value for the key called "selftext"
                    String content = data.getString("selftext");

                    // Extract the value for the key called "num_comments"
                    Integer comments_number = data.getInt("num_comments");

                    Posts post = new Posts(title, author, url, content, comments_number);

                    posts.add(post);
                }

                return posts;

            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("extractPosts", "Problem parsing the posts JSON results", e);
                System.out.println("Something bad happened, please try again!");
            }

            // Return null if it failed.
            return null;
        }
    }
}
