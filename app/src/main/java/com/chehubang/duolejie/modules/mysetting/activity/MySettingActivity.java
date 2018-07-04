package com.chehubang.duolejie.modules.mysetting.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.Constant;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.modules.address.activity.AddressManageActivity;
import com.chehubang.duolejie.modules.mysetting.dialog.GenderDialog;
import com.chehubang.duolejie.modules.mysetting.dialog.HeadDialog;
import com.chehubang.duolejie.modules.mysetting.presenter.MysettingPresnter;
import com.chehubang.duolejie.utils.DialogUtils;
import com.chehubang.duolejie.utils.RerequestPermission;
import com.chehubang.duolejie.utils.UploadUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import common.Utils.ToastUtils;
import common.mvp.activity.MainView;
import common.picture.PictureUtils;

/**
 * Created by Beidouht on 2017/12/21.
 */

public class MySettingActivity extends BaseActivity<MysettingPresnter> implements MainView, View.OnClickListener, GenderDialog.genderSelectedListener {

    private HeadDialog headerdialog;
    private final long currentTimeMillis = System.currentTimeMillis();
    private File tempfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + currentTimeMillis + "photo_temp.jpg");
    private File outfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + currentTimeMillis + "photo_out.jpg");
    final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    private boolean cameraSatePermission;
    private String filename = "";
    private final static int SYSTEM_CUT = 3;
    private ImageView header;
    private GenderDialog genderDialog;
    private TextView sex;
    private EditText nickname;
    private TextView maddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ImageView back = (ImageView) findViewById(R.id.iv_my_setting_back);
        LinearLayout headerSelect = (LinearLayout) findViewById(R.id.ll_setting_header);
        LinearLayout sexSelect = (LinearLayout) findViewById(R.id.ll_setting_sex);
        FrameLayout addressSelect = (FrameLayout) findViewById(R.id.ll_setting_address);
        TextView updata = (TextView) findViewById(R.id.tv_personal_data_updata);
        maddress = (TextView) findViewById(R.id.tv_personal_data_address);
        header = (ImageView) findViewById(R.id.iv_setting_header);
        TextView id = (TextView) findViewById(R.id.tv_setting_id);
        nickname = (EditText) findViewById(R.id.tv_setting_nickname);
        sex = (TextView) findViewById(R.id.tv_setting_sex);
        back.setOnClickListener(this);
        headerSelect.setOnClickListener(this);
        sexSelect.setOnClickListener(this);
        addressSelect.setOnClickListener(this);
        updata.setOnClickListener(this);
        UserInfo.getInstance().getCache(this);
        id.setText(UserInfo.getInstance().getId());
        nickname.setText(UserInfo.getInstance().getNick_name());
        nickname.setSelection(UserInfo.getInstance().getNick_name().length());
        PictureUtils.loadPicture(this, UserInfo.getInstance().getUser_header(), header, R.drawable.pictx);
        sex.setText(UserInfo.getInstance().getSex());

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_my_setting_back:
                finish();
                break;
            case R.id.ll_setting_header:
                headerdialog = new HeadDialog(this);
                headerdialog.show();
                headerdialog.setOnButtonClickListener(this);
                break;
            case R.id.tv_pic_from_album:    //从相册中选取照片
                if (Build.VERSION.SDK_INT >= 23 && !cameraSatePermission) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    selectPhotoFromAlbum();
                    if (headerdialog != null) {
                        headerdialog.dismiss();
                    }
                }
                break;
            case R.id.tv_pic_from_camera:  //从相机中选择
                if (Build.VERSION.SDK_INT >= 23 && !cameraSatePermission) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    selectPhotoFromCamrea();
                    if (headerdialog != null) {
                        headerdialog.dismiss();
                    }
                }
                break;
            case R.id.ll_setting_sex:
                genderDialog = new GenderDialog(this);
                DialogUtils.setDialogStyle(genderDialog);
                genderDialog.setOngenderSelectedListener(this);
                genderDialog.show();
                break;
            case R.id.ll_setting_address:
                Intent intent = new Intent(this, AddressManageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_personal_data_updata:
                String name = nickname.getText().toString();
                String sextext = sex.getText().toString();
                String address = maddress.getText().toString();
                String content = name + "," + sextext + "," + address;
                String filename = "nick_name,sex,user_ip_address";
                mvpPresenter.updataInfo(ACTION_DEFAULT + 1, UserInfo.getInstance().getId(), filename, content, "3");
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraSatePermission = true;
                selectPhotoFromCamrea();
            } else {
                cameraSatePermission = false;
                RerequestPermission.request(this);
            }
        } else if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPhotoFromAlbum();
            } else {
                RerequestPermission.request(this);
            }
        }
    }


    /**
     * 调起相册
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void selectPhotoFromAlbum() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, Activity.DEFAULT_KEYS_SHORTCUT);
        }
        if (headerdialog != null) {
            headerdialog.dismiss();
        }
    }

    /**
     * 调起相机
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectPhotoFromCamrea() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(this.getPackageManager()) != null) {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion < 24) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempfile));
                    startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);
                } else {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, tempfile.getAbsolutePath());
                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);

                }
            } else {
                Toast.makeText(this, "无法读取相机设备", Toast.LENGTH_SHORT).show();
            }
        }
        if (headerdialog != null) {
            headerdialog.dismiss();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mvpPresenter.loadheader(ACTION_DEFAULT + 2, msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case Activity.DEFAULT_KEYS_DIALER:
                    if (resultCode == Activity.RESULT_OK) {
                        outfile.delete();
                        startPhotoZoom(Uri.fromFile(tempfile), 180, 180);
                    }
                    break;
                case Activity.DEFAULT_KEYS_SHORTCUT:     //DEFAULT_KEYS_SHORTCUT
                    if (resultCode == Activity.RESULT_OK) {
                        if (mIsKitKat) {
                            // 4.4以上     //进来了
                            String mAlbumPicturePath = getPath(this.getApplicationContext(), data.getData());
                            startPhotoZoom(Uri.fromFile(new File(mAlbumPicturePath)), 180, 180);
                        } else {
                            Uri uri = data.getData();
                            if (uri != null) {
                                startPhotoZoom(Uri.fromFile(tempfile), 180, 180);
                            }
                        }
                    }
                    break;
                case SYSTEM_CUT:
                    if (resultCode == Activity.RESULT_OK) {
                        if (outfile.exists()) {
                            showDialog();
                            filename = outfile.getAbsolutePath();
                            Bitmap bitmap = BitmapFactory.decodeFile(filename);
                            header.setImageBitmap(bitmap);
                            new Thread() {
                                @Override
                                public void run() {
                                    String s = UploadUtil.getInstance().toAsyncUploadFile(filename);
                                    Message msg = Message.obtain();
                                    msg.what = 0;
                                    msg.obj = UploadUtil.picPath(s);
                                    mHandler.sendMessage(msg);
                                }
                            }.start();

                            /*ByteArrayOutputStream out = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);*/
                            try {
                                /*out.flush();
                                out.close();*/
                                //mvpPresenter.loadheader(ACTION_DEFAULT+2,out.toByteArray());
                                //      loadHeader(out.toByteArray());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    break;
                case 2001:
                    if (resultCode == 2002) {
                        String address = data.getStringExtra("address");
                        maddress.setText(address);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //照片剪切
    public void startPhotoZoom(Uri uri, int outputX, int outputY) {
        Uri output = Uri.fromFile(outfile);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(this, uri);
            intent.setDataAndType(getImageContentUri(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outfile)); // 直接保存到文件
        startActivityForResult(intent, SYSTEM_CUT);
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote addreCheck
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (action == ACTION_DEFAULT + 1) {
            if (model.equals(Constant.request_success)) {
                EventBus.getDefault().post(Constant.EVENT_REFRESH_USER);
                ToastUtils.showToast(this, "个人信息提交成功");
                finish();
            }
        }
        EventBus.getDefault().post(Constant.EVENT_REFRESH_USER);
        dismissDialog();
    }

    @Override
    public void getDataFail(String msg, int action) {
        dismissDialog();
    }

    @Override
    protected MysettingPresnter createPresenter() {
        return new MysettingPresnter(this);
    }


    @Override
    public void genderSelected(String gender) {
        sex.setText(gender);
    }
}
