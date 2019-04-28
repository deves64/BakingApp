package com.udacity.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.bakingapp.util.AppExecutors;
import com.udacity.bakingapp.vo.Step;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener{
    private static final String PLAYER_POSITION = "mPlayerPosition";
    private static final String PLAYER_STATE = "playerState";

    private long mPlayerPosition;
    private boolean mPlayState;

    private Step mStep;
    private OnFragmentInteractionListener mListener;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private TextView mDescription;
    private ImageView mThumbnail;

    private AppExecutors mAppExecutors;

    public RecipeStepFragment() {
        // Required empty public constructor
    }

    public RecipeStepFragment setStep(Step step) {
        mStep = step;
        return this;
    }

    public RecipeStepFragment setAppExecutors(AppExecutors mAppExecutors) {
        this.mAppExecutors = mAppExecutors;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_main, container, false);

        if (savedInstanceState != null) {
            mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            mPlayState = savedInstanceState.getBoolean(PLAYER_STATE);
        }

        if (view.findViewById(R.id.description_as_text) != null) {
            mDescription = view.findViewById(R.id.description_as_text);
        }

        if (view.findViewById(R.id.description_as_image) != null) {
            mThumbnail = view.findViewById(R.id.description_as_image);
            mThumbnail.setVisibility(View.GONE);
        }

        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.description_as_video);
        mPlayerView.setVisibility(View.GONE);

        return view;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            if (mThumbnail != null) {
                mThumbnail.setVisibility(View.GONE);
            }
            mPlayerView.setVisibility(View.VISIBLE);
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mPlayState);
            mExoPlayer.seekTo(mPlayerPosition);
        }
    }

    private void initializeThumbnail(Uri mediaUri) {
        Log.v("initializeThumbnail", "IMAGE");
        mPlayerView.setVisibility(View.GONE);

        if (mThumbnail != null) {
            mThumbnail.setVisibility(View.VISIBLE);
        }

        Glide.with(getContext())
                .load(mediaUri)
                .into(mThumbnail);
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mPlayState = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
            Log.v("releasePlayer", "TRUE");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("onPause", "TRUE");
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYER_POSITION, mPlayerPosition);
        outState.putBoolean(PLAYER_STATE, mPlayState);
        Log.v("onSaveInstanceState", "TRUE");
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == SimpleExoPlayer.STATE_ENDED && mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.seekToDefaultPosition();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        mAppExecutors.getmNetworkIo().execute(new Runnable() {
            @Override
            public void run() {
                if (!isInternetConnectionAvailable()) {
                    return;
                }

                if (mStep == null) {
                    return;
                }

                if (mDescription != null) {
                    mAppExecutors.getmMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDescription.setText(mStep.getDescription());
                        }
                    });
                }

                if (mStep.getVideoUrl() != null) {
                    if (!mStep.getVideoUrl().isEmpty()) {
                        if (isVideo(mStep.getVideoUrl())) {
                            mAppExecutors.getmMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    initializePlayer(Uri.parse(mStep.getVideoUrl()));
                                }
                            });

                            return;
                        }
                    }
                }

                if (mStep.getThumbnailUrl() != null) {
                    if (mStep.getThumbnailUrl().isEmpty()) {
                        return;
                    }

                    if (isVideo(mStep.getThumbnailUrl())) {
                        mAppExecutors.getmMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                initializePlayer(Uri.parse(mStep.getThumbnailUrl()));
                            }
                        });
                    }
                    else {
                        mAppExecutors.getmMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                initializeThumbnail(Uri.parse(mStep.getThumbnailUrl()));
                            }
                        });
                    }
                }
            }
        });
    }

    private Boolean isVideo(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection u = url.openConnection();
            long length = Long.parseLong(u.getHeaderField("Content-Length"));
            String type = u.getHeaderField("Content-Type");
            Log.v("isVideo", type);

            if(type.equals("video/mp4")) {
                return true;
            }
            else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    private Boolean isInternetConnectionAvailable() {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
