package vn.app.tintocshipper.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.app.tintocshipper.R;
import vn.app.tintocshipper.config.Config;
import vn.app.tintocshipper.config.GlobalClass;
import vn.app.tintocshipper.control.Async;
import vn.app.tintocshipper.control.CmmFunc;
import vn.app.tintocshipper.helper.APIHelper;
import vn.app.tintocshipper.helper.ErrorHelper;
import vn.app.tintocshipper.helper.HttpHelper;
import vn.app.tintocshipper.helper.StorageHelper;
import vn.app.tintocshipper.utils.CircleTransform;
import vn.app.tintocshipper.utils.Utility;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ShipperInfoFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    ImageView ivBack, ivHeaderPhoto;
    EditText edt_shipper_name, edt_shipper_phone, edt_shipper_cmnd, edt_shipper_address, edt_shipper_country, edt_shipper_bike;
    TextView txt_shipper_birthday, txtFullName,txtChangePass;
    LinearLayout llBirthday;
    String phone, fullName, address, cmnd, home_town, vehicle_brand, urlNavHeaderBg;
    DateTime dateTime = new DateTime();
    String userName = StorageHelper.get(StorageHelper.USERNAME);
    ImageView iv_header_photo;
    //endregion

    //region Constructor
    public ShipperInfoFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Instance
    public static ShipperInfoFragment newInstance() {
        ShipperInfoFragment fragment = new ShipperInfoFragment();
        Bundle args = new Bundle();
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
            view = inflater.inflate(R.layout.fragment_shipper_info, container, false);
        }
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivBack = view.findViewById(R.id.iv_back);
            ivHeaderPhoto = view.findViewById(R.id.avatar);
            txtFullName = view.findViewById(R.id.full_name);
            edt_shipper_name = view.findViewById(R.id.edt_shipper_name);
            edt_shipper_phone = view.findViewById(R.id.edt_shipper_phone);
            edt_shipper_cmnd = view.findViewById(R.id.edt_shipper_cmnd);
            edt_shipper_address = view.findViewById(R.id.edt_shipper_address);
            edt_shipper_country = view.findViewById(R.id.edt_shipper_country);
            llBirthday = view.findViewById(R.id.ll_birthday);
            iv_header_photo = view.findViewById(R.id.iv_header_photo);
            txt_shipper_birthday = view.findViewById(R.id.txt_shipper_birthday);
            txt_shipper_birthday.setText(getDate(dateTime));
            edt_shipper_bike = view.findViewById(R.id.edt_shipper_bike);
            txtChangePass = view.findViewById(R.id.txtv_changepass);

            edt_shipper_name.setEnabled(false);
            edt_shipper_phone.setEnabled(false);
            edt_shipper_cmnd.setEnabled(false);
            edt_shipper_address.setEnabled(false);
            edt_shipper_country.setEnabled(false);
            txt_shipper_birthday.setEnabled(false);
            edt_shipper_bike.setEnabled(false);

            new GetShipperInfo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            ivBack.setOnClickListener(this);
            getView().findViewById(R.id.ll_comfirm).setOnClickListener(this);
            getView().findViewById(R.id.txt_shipper_birthday).setOnClickListener(this);
            getView().findViewById(R.id.txtv_changepass).setOnClickListener(this);

            //region setting font edittext
            edt_shipper_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = edt_shipper_name.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edt_shipper_name.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edt_shipper_name.setTypeface(customFont);
                    }


                }
            });

            edt_shipper_phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = edt_shipper_phone.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edt_shipper_phone.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edt_shipper_phone.setTypeface(customFont);
                    }


                }
            });

            edt_shipper_country.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = edt_shipper_country.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edt_shipper_country.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edt_shipper_country.setTypeface(customFont);
                    }


                }
            });

            edt_shipper_bike.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = edt_shipper_bike.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edt_shipper_bike.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edt_shipper_bike.setTypeface(customFont);
                    }


                }
            });

            edt_shipper_cmnd.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = edt_shipper_cmnd.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edt_shipper_cmnd.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edt_shipper_cmnd.setTypeface(customFont);
                    }


                }
            });

            edt_shipper_address.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = edt_shipper_address.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edt_shipper_address.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edt_shipper_address.setTypeface(customFont);
                    }


                }
            });

            txt_shipper_birthday.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = txt_shipper_birthday.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        txt_shipper_birthday.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        txt_shipper_birthday.setTypeface(customFont);
                    }
                }
            });
            //endregion

            isLoad = true;
        }
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
//                ivBack.setOnClickListener(null);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                            ivBack.setOnClickListener(ShipperInfoFragment.this);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.txt_shipper_birthday:
                getBirthday();
                break;
            case R.id.ll_comfirm:
                CmmFunc.replaceFragment(getActivity(),R.id.main_container, ChangePasswordFragment.newInstance());
                break;
            case R.id.avatar:
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, 1999);
                break;
        }
    }
    //endregion

    //region Methods

    //region Get Birthday
    private void getBirthday() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
                        txt_shipper_birthday.setText(dateTime.toString("dd - MM - yyyy"));

                    }
                }, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
        datePickerDialog.show();
    }
    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1999 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                Bitmap bmp = null;
                Uri uri = Uri.parse(data.getDataString());
                if (uri != null) {
                    String realPath = CmmFunc.getPathFromUri(getActivity(), uri);
                    if (realPath != null) {
                        ExifInterface exif = new ExifInterface(realPath);
                        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        switch (rotation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotation = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotation = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotation = 270;
                                break;
                            default:
                                rotation = 0;
                                break;
                        }
                        Matrix matrix = new Matrix();
                        if (rotation != 0f) {
                            matrix.preRotate(rotation);
                        }
                        bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    } else {
                        if (data.getData() == null) {
                            bmp = (Bitmap) data.getExtras().get("data");
                        } else {
                            InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            bmp = BitmapFactory.decodeStream(bufferedInputStream);
                        }
                    }
                } else {
                    if (data.getData() == null) {
                        bmp = (Bitmap) data.getExtras().get("data");
                    } else {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    }
                }

                bmp = CmmFunc.scaleDown(bmp, 720, true);
                File file = CmmFunc.bitmapToFile(getActivity(), bmp);
                updateAvatar(file);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateAvatar(final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map.Entry<String, String>> params = new ArrayList<>();
                    params.add(new AbstractMap.SimpleEntry("country_code", Config.COUNTRY_CODE));
                    params.add(new AbstractMap.SimpleEntry("deviceid", Utility.getDeviceId(getContext())));
                    params.add(new AbstractMap.SimpleEntry("username", StorageHelper.get(StorageHelper.USERNAME)));
                    params.add(new AbstractMap.SimpleEntry("token", StorageHelper.get(StorageHelper.TOKEN)));
                    String response = HttpHelper.postFile(Config.BASE_URL + Config.DOMAIN_UPDATE_AVATAR, params, "avatar", file);
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(getActivity()).load(file).transform(new CircleTransform(getActivity())).into(ivHeaderPhoto);
                                ImageView avatar = getActivity().findViewById(R.id.iv_header_photo);
                                Glide.with(getActivity()).load(file).transform(new CircleTransform(getActivity())).into(avatar);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateProfile(final String phone, final String fullName, final String address, final String cmnd, final String home_town, final String birthday,
                               final String vehicle_brand) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map.Entry<String, String>> params = new ArrayList<>();
                    params.add(new AbstractMap.SimpleEntry("country_code", Config.COUNTRY_CODE));
                    params.add(new AbstractMap.SimpleEntry("deviceid", Utility.getDeviceId(getContext())));
                    params.add(new AbstractMap.SimpleEntry("username", StorageHelper.get(StorageHelper.USERNAME)));
                    params.add(new AbstractMap.SimpleEntry("token", StorageHelper.get(StorageHelper.TOKEN)));
                    params.add(new AbstractMap.SimpleEntry("phone", phone));
                    params.add(new AbstractMap.SimpleEntry("fullname", fullName));
                    params.add(new AbstractMap.SimpleEntry("address", address));
                    params.add(new AbstractMap.SimpleEntry("cmnd", cmnd));
                    params.add(new AbstractMap.SimpleEntry("home_town", home_town));
                    params.add(new AbstractMap.SimpleEntry("birthday", birthday));
                    params.add(new AbstractMap.SimpleEntry("vehicle_brand", vehicle_brand));
                    String updateProfileShipperResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_UPDATE_PROFILE_SHIPPER, params);
                    JSONObject jsonObject = new JSONObject(updateProfileShipperResponse);
                    final int code = jsonObject.getInt("code");
                    if (code == 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), getString(R.string.success_update), Toast.LENGTH_SHORT).show();
                                new GetShipperInfo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                //Cập nhận lên left menu
                                TextView txtFullName = getActivity().findViewById(R.id.txt_full_name);
                                txtFullName.setText(fullName);
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GlobalClass.getActivity(), ErrorHelper.getValueByKey(code), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //region Format date/time
    private String BirthdayFormat(DateTime date) {
        if (date == null) {
            return null;
        }
        return date.getYear() + "-" + StringUtils.leftPad(date.getMonthOfYear() + "", 2, "0") + "-"
                + StringUtils.leftPad(date.getDayOfMonth() + "", 2, "0");
    }

    private String getDate(DateTime dateTime) {
        if (dateTime == null) {
            return StringUtils.EMPTY;
        }
        return StringUtils.leftPad(dateTime.getDayOfMonth() + "", 2, "0") + " - " +
                StringUtils.leftPad(dateTime.getMonthOfYear() + "", 2, "0") + " - " + dateTime.getYear();
    }
    //endregion

    //endregion

    //region Request API

    //region Get Shipper Info
    class GetShipperInfo extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getProfileShipper(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        userName, StorageHelper.get(StorageHelper.TOKEN));
                jsonObject = new JSONObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                int code = jsonObject.getInt("code");
                if (code == 1) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    txtFullName.setText(data.getString("fullname"));
                    edt_shipper_name.setText(data.getString("fullname"));
                    edt_shipper_phone.setText(data.getString("phone"));
                    edt_shipper_cmnd.setText(data.getString("cmnd"));
                    edt_shipper_address.setText(data.getString("address"));
                    edt_shipper_country.setText(data.getString("home_town"));
                    txt_shipper_birthday.setText(data.getString("birthday"));
                    if (!data.getString("birthday").equals("")) {
                        dateTime = DateTime.parse(data.getString("birthday"), DateTimeFormat.forPattern("dd-MM-yyyy"));
                    }
                    edt_shipper_bike.setText(data.getString("vehicle_brand"));
                    urlNavHeaderBg = data.getString("avatar");

                    Glide.with(getContext()).load(urlNavHeaderBg)
                            .crossFade().bitmapTransform(new CircleTransform(getApplicationContext()))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivHeaderPhoto);
                    ivHeaderPhoto.setOnClickListener(ShipperInfoFragment.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Update Shipper Info
    class UpdateShipperInfo extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String birthday = BirthdayFormat(dateTime);
                String value = APIHelper.updateProfileShipper(Config.COUNTRY_CODE, Utility.getDeviceId(getContext()),
                        userName, StorageHelper.get(StorageHelper.TOKEN), phone, fullName, address, cmnd, home_town, birthday, vehicle_brand);
                jsonObject = new JSONObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                int code = jsonObject.getInt("code");
                if (code == 1) {
                    Toast.makeText(getActivity(), getString(R.string.success_update),
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion

}