package co.bitgray.bitgraytest.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import co.bitgray.bitgraytest.R;
import co.bitgray.bitgraytest.utils.GeneralUtils;

public class PhotoFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Location lastLocation;

    private GoogleApiClient googleApiClient;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    Button btnCameraPhoto;
    FloatingActionButton btnSavePhoto;
    ImageView imageThumbnailPhoto;
    String currentPhotoPath;

    EditText latitudePhoto;
    EditText tittlePhoto;
    EditText longitudePhoto;

    TextInputLayout inputLayoutTittlePhoto;
    TextInputLayout inputLayoutLatitudePhoto;
    TextInputLayout inputLayoutLongitudePhoto;

    LinearLayout containerPhotoLocation;

    private OnFragmentInteractionListener mListener;

    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        btnCameraPhoto = (Button) getActivity().findViewById(R.id.btnCameraPhoto);
        btnSavePhoto = (FloatingActionButton) getActivity().findViewById(R.id.btnSavePhoto);
        imageThumbnailPhoto = (ImageView) getActivity().findViewById(R.id.imageThumbnailPhoto);
        latitudePhoto= (EditText) getActivity().findViewById(R.id.latitudePhoto);
        longitudePhoto= (EditText) getActivity().findViewById(R.id.longitudePhoto);
        tittlePhoto= (EditText) getActivity().findViewById(R.id.tittlePhoto);
        inputLayoutTittlePhoto= (TextInputLayout) getActivity().findViewById(R.id.inputLayoutTittlePhoto);
        inputLayoutLatitudePhoto= (TextInputLayout) getActivity().findViewById(R.id.inputLayoutLatitudePhoto);
        inputLayoutLongitudePhoto= (TextInputLayout) getActivity().findViewById(R.id.inputLayoutLongitudePhoto);
        containerPhotoLocation= (LinearLayout) getActivity().findViewById(R.id.containerPhotoLocation);

        btnCameraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        btnSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhotoInAlbum();
            }
        });
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = setUpPhotoFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void handleBigCameraPhoto() {
        if (currentPhotoPath != null) {
            setPic();
            galleryAddPic();
        }
    }

    private void setPic() {
        int targetW = imageThumbnailPhoto.getWidth();
        int targetH = imageThumbnailPhoto.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        if(lastLocation!=null){
            latitudePhoto.setText(String.valueOf(lastLocation.getLatitude()));
            longitudePhoto.setText(String.valueOf(lastLocation.getLongitude()));
        }

        imageThumbnailPhoto.setImageBitmap(bitmap);
        latitudePhoto.setVisibility(View.VISIBLE);
        longitudePhoto.setVisibility(View.VISIBLE);
        tittlePhoto.setVisibility(View.VISIBLE);
        tittlePhoto.requestFocus();
        inputLayoutTittlePhoto.setVisibility(View.VISIBLE);
        inputLayoutLongitudePhoto.setVisibility(View.VISIBLE);
        inputLayoutLatitudePhoto.setVisibility(View.VISIBLE);
        btnSavePhoto.setVisibility(View.VISIBLE);
        containerPhotoLocation.setVisibility(View.VISIBLE);
    }

    public void savePhotoInAlbum(){
        if(currentPhotoPath!= null && tittlePhoto.getText()!=null && tittlePhoto.getText().toString().trim()!=""){
            co.bitgray.bitgraytest.models.Photo photo= new co.bitgray.bitgraytest.models.Photo(getActivity());
            try {
                photo.setDate(GeneralUtils.dateToMillisecondsRemoveTime(new Date()));
                photo.setResource(currentPhotoPath);
                photo.setTitle(tittlePhoto.getText().toString());
                photo.setResourceData(GeneralUtils.getBytes(GeneralUtils.getThumbnail(currentPhotoPath)));



                photo.save();



                imageThumbnailPhoto.setImageResource(R.drawable.empty_image);
                latitudePhoto.setVisibility(View.GONE);
                longitudePhoto.setVisibility(View.GONE);
                tittlePhoto.setText("");
                tittlePhoto.setVisibility(View.GONE);
                inputLayoutTittlePhoto.setVisibility(View.GONE);
                inputLayoutLongitudePhoto.setVisibility(View.GONE);
                inputLayoutLatitudePhoto.setVisibility(View.GONE);
                btnSavePhoto.setVisibility(View.GONE);
                containerPhotoLocation.setVisibility(View.GONE);
                currentPhotoPath = null;
                GeneralUtils.hideSoftKeyboard(getActivity());
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.photo_saved),Toast.LENGTH_LONG).show();

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.photo_validation),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private File setUpPhotoFile() throws IOException {

        File f = GeneralUtils.createImageFile(getActivity().getResources().getString(R.string.app_name));
        currentPhotoPath = f.getAbsolutePath();

        return f;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            handleBigCameraPhoto();
        }
    }

}
