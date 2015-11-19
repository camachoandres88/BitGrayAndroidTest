package co.bitgray.bitgraytest.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.bitgray.bitgraytest.R;
import co.bitgray.bitgraytest.adapters.AlbumAdapter;
import co.bitgray.bitgraytest.models.Album;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView reciclerViewAlbum;

    private ProgressDialog loading;
    private  AlbumAdapter albumAdapter;

    private OnFragmentInteractionListener mListener;

    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        reciclerViewAlbum = (RecyclerView) getActivity().findViewById(R.id.reciclerViewAlbum);
        reciclerViewAlbum.setHasFixedSize(true);
        reciclerViewAlbum.setLayoutManager(new LinearLayoutManager(getActivity()));
        reciclerViewAlbum.setItemAnimator(new DefaultItemAnimator());
        reciclerViewAlbum.stopScroll();
        loading =  new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_Dialog);
        loading.setIndeterminate(true);
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(false);

        new AlbumTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class AlbumTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setMessage(getResources().getString(R.string.loading));
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Album album = new Album(getActivity());
            List<Album> allDatedAlbums = album.getAllAlbum();
            albumAdapter= new AlbumAdapter(getActivity(),allDatedAlbums,R.layout.item_album);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reciclerViewAlbum.setAdapter(albumAdapter);
            loading.hide();
        }
    }

}
