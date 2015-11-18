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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    private String TAG  = UserFragment.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText userNameEdt;
    private EditText namesEdt;
    private EditText  emailEdt;
    private EditText  phoneNumberEdt;
    private EditText  companyEdt;
    private ProgressDialog loading;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
