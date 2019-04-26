package com.example.cse.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cse.bakingapp.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    //String str="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private boolean mTwoPane;
    static int var;
    ArrayList<Activity2Model> arrayList;
    ArrayList<Activity1Model> activity1Models;
    TextView ingrediants;
    String variable;
    String var2;

    SharedPreferences shared;
    SharedPreferences.Editor sharededit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ingrediants=findViewById(R.id.ingrediant);
        arrayList=new ArrayList<>();
        activity1Models=new ArrayList<>();
       Intent intent=getIntent();
        var=getIntent().getIntExtra("putextra",1);
        var2=getIntent().getStringExtra("appname");
        setTitle(var2);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        new AsyncSubClass().execute();
        setupRecyclerView((RecyclerView) recyclerView);
    }
    public  class AsyncSubClass extends AsyncTask<String,Void,String>{

        private String str="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(str);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream=httpURLConnection.getInputStream();
                Scanner scanner=new Scanner(inputStream);
                scanner.useDelimiter("\\A");
                if (scanner.hasNext()){
                    return scanner.next();
                }
                else{
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = jsonArray.getJSONObject(var);
                    JSONArray jsonArray1=jsonObject.getJSONArray("ingredients");
                    for (int j=0;j<jsonArray1.length();j++){
                        JSONObject jsonObject1=jsonArray1.getJSONObject(j);
                        String quantity=jsonObject1.getString("quantity");
                        String measure=jsonObject1.getString("measure");
                        String ingredient=jsonObject1.getString("ingredient");
                        arrayList.add(new Activity2Model(quantity,measure,ingredient));

                    }

                    for (int r=0;r<arrayList.size();r++){
                        variable=arrayList.get(r).getIngredient()+"\t"+arrayList.get(r)
                                .getMeasure()+"\t"+arrayList.get(r).getQuantity()+"\n";
                        ingrediants.append(variable);
                    }

                    widgetData();
                    JSONArray steps=jsonObject.getJSONArray("steps");
                    for (int k=0;k<steps.length();k++){
                        JSONObject object=steps.getJSONObject(k);
                        String shortDescription=object.getString("shortDescription");
                        String description=object.getString("description");
                        String videoURL=object.getString("videoURL");
                        String thumbnailURL=object.getString("thumbnailURL");
                        activity1Models.add(new Activity1Model(shortDescription,description,videoURL,thumbnailURL));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, activity1Models, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        Context context;
        ArrayList<Activity2Model> activity2Models;
        ArrayList<Activity1Model> activity1Models;

        private final ItemListActivity mParentActivity;
        private final List<Activity1Model> mValues;
        private final boolean mTwoPane;
        /*private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    //arguments.putString(ItemDetailFragment.ARG_ITEM_ID,String.valueOf());
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    //intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };*/

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      ArrayList<Activity1Model> items,
                                      boolean twoPane) {
            mParentActivity = parent;
            mValues = items;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            //holder.mIdView.setText(mValues.get(position).id);
            //holder.mContentView.setText(mValues.get(position).content);
            holder.mContentView.setText(mValues.get(position).getShortDescription());
           // holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(var));

                        arguments.putString("videoUrl",mValues.get(position).getVideoURL());
                        arguments.putString("shortdes",mValues.get(position).getShortDescription());
                        arguments.putString("description",mValues.get(position).getDescription());
                        arguments.putString("thumbnail",mValues.get(position).getThumbnailURL());

                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, var);
                        intent.putExtra("videoUrl",mValues.get(position).getVideoURL());
                        intent.putExtra("shortdes",mValues.get(position).getShortDescription());
                        intent.putExtra("description",mValues.get(position).getDescription());
                        intent.putExtra("thumbnail",mValues.get(position).getThumbnailURL());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
           //final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                //mIdView = (TextView) view.findViewById(R.id.);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
    public void widgetData(){
        shared=getSharedPreferences("getstr",MODE_PRIVATE);
        sharededit=shared.edit();
        StringBuffer stringBuffer=new StringBuffer();
       // StringBuffer stringBuffer1=new StringBuffer();
        stringBuffer.append(ingrediants.getText().toString());


       // String data=ingrediants.getText().toString();
        /*for (int i=0;i<arrayList.size();i++){
            stringBuffer.append(arrayList.get(i).getIngredient()+"\t"+arrayList.get(i).getMeasure()+"\t"+arrayList.get(i).getQuantity()+"\n");
        }*/
        sharededit.putString("widgettext",stringBuffer.toString());
        sharededit.apply();

        Intent intent=new Intent(ItemListActivity.this,BakeWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ar=AppWidgetManager.getInstance(ItemListActivity.this).getAppWidgetIds(new ComponentName(getApplicationContext(),BakeWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ar);
        sendBroadcast(intent);
    }
}
