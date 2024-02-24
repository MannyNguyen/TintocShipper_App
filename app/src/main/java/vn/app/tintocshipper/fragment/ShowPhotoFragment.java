package vn.app.tintocshipper.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import vn.app.tintocshipper.R;

public class ShowPhotoFragment extends BaseFragment {
    ImageView ivPhoto;
    private float xCoOrdinate, yCoOrdinate;
    //endregion

    //region Constructor
    public ShowPhotoFragment() {
    }
    //endregion

    //region NewInstance
    // TODO: Rename and change types and number of parameters
    public static ShowPhotoFragment newInstance(byte[] bitmap) {
        ShowPhotoFragment fragment = new ShowPhotoFragment();
        Bundle args = new Bundle();
        args.putByteArray("bitmap", bitmap);
        fragment.setArguments(args);
        return fragment;
    }
    public static ShowPhotoFragment newInstance(String photoUrl) {
        ShowPhotoFragment fragment = new ShowPhotoFragment();
        Bundle args = new Bundle();
        args.putString("photoUrl", photoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    //endregion

    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_show_photo, container, false);
        }
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad){
            ivPhoto = view.findViewById(R.id.iv_photo);
            final int windowwidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
            final int windowheight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
            final View backGround = view.findViewById(R.id.bg);
            if(getArguments().containsKey("bitmap")){
                byte[] byteArray = getArguments().getByteArray("bitmap");
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ivPhoto.setImageBitmap(bmp);
            }else if(getArguments().containsKey("photoUrl")){
                Glide.with(getActivity()).load(getArguments().getString("photoUrl")).into((ImageView) view.findViewById(R.id.iv_photo));
            }

            ivPhoto.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            //xCoOrdinate = image.getX() - event.getRawX();
                            yCoOrdinate = ivPhoto.getY() - event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            ivPhoto.animate().y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                            float alpha = (windowheight / 2) - event.getRawY();
                            if (alpha > 0) {
                                alpha = event.getRawY() - (windowheight / 2);
                            }
                            alpha = (alpha / (windowheight / 2)) * (-1);
                            backGround.setAlpha(0.8f - alpha);
                            Log.d("YYY", event.getRawY() + "");

                            break;
                        case MotionEvent.ACTION_UP:
                            if (event.getRawY() < windowheight / 4) {
                                ivPhoto.animate().y(0 - ivPhoto.getHeight()).setDuration(200).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }
                                }).start();
                            } else if (event.getRawY() > windowheight - windowheight / 4) {
                                ivPhoto.animate().y(windowheight + ivPhoto.getHeight()).setDuration(200).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }
                                }).start();
                            } else {
                                ivPhoto.animate().y(windowheight / 2 - ivPhoto.getHeight() / 2).setDuration(200).start();
                                backGround.setAlpha(0.9f);
                            }

                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });

            isLoad=true;
        }
    }



//region cmt
    //region Constructor
//    public ShowPhotoFragment() {
//        // Required empty public constructor
//    }
//    //endregion
//
//    //region NewInstance
//    // TODO: Rename and change types and number of parameters
//    public static ShowPhotoFragment newInstance(String photoUrl) {
//        ShowPhotoFragment fragment = new ShowPhotoFragment();
//        Bundle args = new Bundle();
//        args.putString("photoUrl", photoUrl);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    //region OnCreateView
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        if (view == null) {
//            view = inflater.inflate(R.layout.fragment_show_photo, container, false);
//        }
//        return view;
//    }
//    //endregion
//
//    //region OnViewCreated
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        if (!isLoad) {
//            Glide.with(getActivity()).load(getArguments().getString("photoUrl")).into((ImageView) view.findViewById(R.id.iv_photo));
//            isLoad = true;
//        }
//    }
//endregion
}
