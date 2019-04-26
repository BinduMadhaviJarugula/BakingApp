package com.example.cse.bakingapp;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse.bakingapp.dummy.DummyContent;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    static boolean whenReady;
    static long crp;
    TextView descrip;
    String vURL,thumbnail,descr,sdesc;

    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            crp=savedInstanceState.getLong("curpos",0);
            whenReady=savedInstanceState.getBoolean("whenready",true);
        }
        else{
            crp=0;
            whenReady=true;
        }
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
           // mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            vURL=getArguments().getString("videoUrl");
            sdesc=getArguments().getString("shortdes");
            descr=getArguments().getString("description");
            thumbnail=getArguments().getString("thumbnail");


            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(sdesc);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        descrip=rootView.findViewById(R.id.item_detail);
        simpleExoPlayerView=rootView.findViewById(R.id.exoplayer);
        descrip.setText(descr);


        // Show the dummy content as text in a TextView.
        /*if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
        }
*/
        playVideo();
        return rootView;
    }
    public void playVideo(){
        Uri urlvideo = null;
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            if (!vURL.isEmpty()){
                urlvideo=Uri.parse(vURL);
            }
            else if(!thumbnail.isEmpty()){
                urlvideo=Uri.parse(thumbnail);
            }
            else{
                Toast.makeText(getContext(),"Video Not Available",Toast.LENGTH_SHORT).show();
                simpleExoPlayerView.setVisibility(View.GONE);
                urlvideo=Uri.parse("");
            }
            DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("player");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(urlvideo, factory, extractorsFactory, null, null);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
            if (crp!=0){
                simpleExoPlayer.seekTo(crp);
                simpleExoPlayer.setPlayWhenReady(whenReady);
            }
            else{
                simpleExoPlayer.setPlayWhenReady(true);
            }
        }
        catch (Exception e){
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("curpos",simpleExoPlayer.getCurrentPosition());
        outState.putBoolean("whenready",simpleExoPlayer.getPlayWhenReady());
    }
    public void stopVideo(){
        if (simpleExoPlayer!=null){
            crp=simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            playVideo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopVideo();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopVideo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVideo();
    }
}
