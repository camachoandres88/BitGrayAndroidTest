package co.bitgray.bitgraytest.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Random;

import co.bitgray.bitgraytest.R;
import co.bitgray.bitgraytest.models.User;
import co.bitgray.bitgraytest.services.RestApiHelper;
import co.bitgray.bitgraytest.services.RestUser;
import co.bitgray.bitgraytest.utils.*;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserFragment extends Fragment {
    private String TAG  = UserFragment.class.getName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText userNameEdt;
    private EditText namesEdt;
    private EditText  emailEdt;
    private EditText  phoneNumberEdt;
    private EditText  companyEdt;
    private ProgressDialog loading;

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserFragment() {
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
        userNameEdt = (EditText) getActivity().findViewById(R.id.userNameEdt);
        emailEdt = (EditText) getActivity().findViewById(R.id.emailEdt);
        namesEdt = (EditText) getActivity().findViewById(R.id.namesEdt);
        phoneNumberEdt = (EditText) getActivity().findViewById(R.id.phoneNumberEdt);
        companyEdt = (EditText) getActivity().findViewById(R.id.companyEdt);

        loading =  new ProgressDialog(getActivity(),
                R.style.Theme_AppCompat_Dialog);
        loading.setIndeterminate(true);
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(false);
        loadUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
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

    public void loadUser(){
        loading.setMessage(getResources().getString(R.string.loading_user));
        loading.show();

        if (NetworkUtils.haveNetworkConnection(getActivity())) {
            Random random = new Random();
            int number = random.nextInt((10 - 1) + 1) + 1;


            RestUser restUser = RestApiHelper.createService(RestUser.class, ConstantUtils.URL_MAIN_REST, getActivity());

            restUser.getUser(number, new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    userNameEdt.setText(user.getUsername());
                    namesEdt.setText(user.getName());
                    emailEdt.setText(user.getEmail());
                    phoneNumberEdt.setText(user.getPhone());
                    companyEdt.setText(user.getCompany().getName());

                    loading.hide();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getMessage());
                    loading.hide();
                }
            });
        }else{
            Toast.makeText(getActivity(),R.string.no_internet_connection,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loading.dismiss();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
}
