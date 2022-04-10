package org.me.gcu.trafficapptest;
// John Brown S1917384

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ArrayList<CurrentIncident> currentIncidents;
    //private ArrayAdapter<CurrentIncident> arrayAdapter;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private Button roadworksButton;
    private Button plannedRoadworksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Open Roadworks Activity
        roadworksButton = findViewById(R.id.roadworksButton);
        roadworksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RoadworksActivity.class);
                startActivity(intent);
            }
        });

        //Open Planned Roadworks Activity
        plannedRoadworksButton = findViewById(R.id.plannedRoadworksButton);
        plannedRoadworksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlannedRoadworksActivity.class);
                startActivity(intent);
            }
        });

        currentIncidents = new ArrayList<>();

        GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask();
        getDataAsyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<CurrentIncident> filteredIncidents = new ArrayList<>();

        // running a for loop to compare elements.
        for (CurrentIncident item : currentIncidents) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDesc().toLowerCase().contains(text.toLowerCase()) ||
                        item.getPubDate().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredIncidents.add(item);
            }
        }
        if (filteredIncidents.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredIncidents);
        }
    }

    private class GetDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream inputStream = getInputStream();
            try {
                initXMLPullParser(inputStream);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.setIncidents(currentIncidents);
            super.onPostExecute(aVoid);
        }
    }

    private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
        Log.d(TAG, "initPullParser: called");
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        parser.nextTag();

        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            parser.require(XmlPullParser.START_TAG, null, "channel");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                if (parser.getName().equals("item")) {
                    parser.require(XmlPullParser.START_TAG, null, "item");

                    String title = "";
                    String description = "";
                    String link = "";
                    String georss = "";
                    String pubDate = "";

                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }

                        String tagName = parser.getName();
                        if (tagName.equals("title")) {
                            //get title content
                            title = getContent(parser, "title");
                        } else if (tagName.equals("description")) {
                            //get desc content
                            description = getContent(parser, "description");
                        } else if (tagName.equals("link")) {
                            //get link content
                            link = getContent(parser, "link");
                        } else if (tagName.equals("georss:point")) {
                            //get georss content
                            georss = getContent(parser, "georss:point");
                        } else if (tagName.equals("pubDate")) {
                            //get pubDate content
                            pubDate = getContent(parser, "pubDate");
                        } else {
                            //skip the tag
                            skipTag(parser);
                        }
                    }

                    CurrentIncident incident = new CurrentIncident(title, description, link, georss, pubDate);
                    currentIncidents.add(incident);
                } else {
                    //skip the tag
                    skipTag(parser);
                }
            }
        }
    }

    private void skipTag (XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(TAG, "skipTag: skipping: " + parser.getName());
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int number = 1;
        while (number != 0) {
            switch (parser.next()) {
                case XmlPullParser.START_TAG:
                    number++;
                    break;
                case XmlPullParser.END_TAG:
                    number--;
                    break;
                default:
                    break;
            }
        }
    }

    private String getContent (XmlPullParser parser, String tagName) throws XmlPullParserException, IOException {
        Log.d(TAG, "getContent: started for tag: " + tagName);

        try {
            parser.require(XmlPullParser.START_TAG, null, tagName);

            String content = "";

            if (parser.next() == XmlPullParser.TEXT) {
                content = parser.getText();
                parser.next();
            }
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getInputStream () {
        Log.d(TAG, "getInputStream: started");
        try {
            URL url = new URL("https://trafficscotland.org/rss/feeds/currentincidents.aspx");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}