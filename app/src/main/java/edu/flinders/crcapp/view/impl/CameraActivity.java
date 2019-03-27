/*
 *  Created by Tri Phan on 7/01/19 10:22 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 10:22 PM
 */

package edu.flinders.crcapp.view.impl;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.hardware.camera2.*;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.net.Uri;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.*;
import android.widget.*;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import dmax.dialog.SpotsDialog;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.model.Equation;
import edu.flinders.crcapp.model.GlobalUtils;
import edu.flinders.crcapp.model.ProcessCallback;
import edu.flinders.crcapp.model.StringUtils;
import edu.flinders.crcapp.view.CameraView;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.CameraPresenter;
import edu.flinders.crcapp.injection.AppComponent;
import edu.flinders.crcapp.injection.CameraViewModule;
import edu.flinders.crcapp.injection.DaggerCameraViewComponent;

import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CameraActivity extends BaseActivity<CameraPresenter, CameraView> implements CameraView {
    private static final String PREF_NAME = "CameraPref";
    private static final String OBJ_NAME = "PointerCoord";
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    // the rectangle size in which pixels will be selected (in dp)
    private final int[] SELECTED_PIXEL_NUM = {8, 8};

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Inject
    PresenterFactory<CameraPresenter> mPresenterFactory;
    private static final String TAG = "CameraActivity";

    @BindView(R.id.btn_action)
    Button mBtnAction;
    private TextureView mTxvView;

    @BindView(R.id.rbg_step)
    RadioGroup mRbgStep;

    @BindView(R.id.tv_camera_instruct)
    TextView mTVInstruction;

    @BindView(R.id.rl_cursor)
    RelativeLayout mRlCursor;

    @BindView(R.id.img_target)
    ImageView mImgTarget;

    @BindView(R.id.img_temp)
    ImageView mImgTemp;

    @BindString(R.string.caution_granting_permission)
    String CAUTION_GRANTING_PERMISSION;
    @BindString(R.string.info_success_saving)
    String INFO_SUCCESS_SAVING;
    @BindString(R.string.info_config_changed)
    String INFO_CONFIG_CHANGED;
    @BindString(R.string.app_name)
    String APP_NAME;

    int mCurLeftMargin;
    int mCurTopMargin;
    int x_cord;
    int y_cord;

    private TextureView.SurfaceTextureListener mTextureListener;
    private View.OnTouchListener mOnTouchListener;

    private String mCameraId;
    protected CameraCaptureSession mCameraCaptureSessions;
    protected CaptureRequest mCaptureRequest;
    protected CaptureRequest.Builder mCaptureRequestBuilder;
    private Size mImageDimension;
    private ImageReader mImageReader;
    private boolean mFlashSupported;
    protected CameraDevice mCameraDevice;

    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    private Context mContext;
    private Activity mActivity;

    private int[] mColors0 = new int[]{-1, -1, -1};
    private int[] mColors1 = new int[]{-1, -1, -1};
    private int[] mColors2 = new int[]{-1, -1, -1};

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            mCameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            mCameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;

        Bundle b = getIntent().getExtras();
        int viewId = b.getInt(StringUtils.KEY_VIEW_ID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewByContext(viewId);

        // Retrieve the saved pointer position
        Integer[] pos = Equation.getSavedObjectFromPreference(this, PREF_NAME, OBJ_NAME, Integer[].class);
        if (pos != null) {
            if(pos[0] != 0 && pos[1] != 0) {
                mCurLeftMargin = pos[0];
                mCurTopMargin = pos[1];
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRlCursor
                        .getLayoutParams();
                layoutParams.leftMargin = mCurLeftMargin;
                layoutParams.topMargin = mCurTopMargin;
                mRlCursor.setLayoutParams(layoutParams);

                ((RelativeLayout) mRlCursor.getParent()).invalidate();
            }
        }
    }

    @Override
    public void setupViewByContext(int viewId) {
        String title = "-";
        String instruction = "-";
        int steps = 0;
        switch (viewId) {
            case R.id.btn_record_new_sample:
                title = getString(R.string.intro_second_page);
                instruction = getString(R.string.record_instruct);
                mBtnAction.setText(R.string.txt_record);
                break;
            case 3:
                title = getString(R.string.calib_one_point_title);
                instruction = getString(R.string.calib_one_sample_instruct_2);
                mBtnAction.setText(R.string.txt_analyze_0ppb);
                break;
            case 2:
                title = getString(R.string.calib_two_point_title);
                instruction = getString(R.string.calib_two_sample_instruct_2);
                mBtnAction.setText(R.string.txt_analyze_0ppb);
                steps = 2;
                break;
            case 1:
                title = getString(R.string.calib_three_point_title);
                instruction = getString(R.string.calib_three_sample_instruct_2);
                mBtnAction.setText(R.string.txt_analyze_0ppb);
                steps = 3;
                break;
        }

        mRbgStep.removeAllViews();
        for (int i = 0; i < steps; i++) {
            RadioButton btn = new RadioButton(this);
            btn.setBackground(getDrawable(R.drawable.custom_radio_button));
            btn.setButtonDrawable(getDrawable(R.drawable.custom_radio_button));
            btn.setId(View.generateViewId());
            btn.setEnabled(false);

            RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(
                    GlobalUtils.toPixels(15, getResources().getDisplayMetrics()), GlobalUtils.toPixels(15, getResources().getDisplayMetrics()));
            if (i == 0) {
                btn.setChecked(true);
            } else {
                btn.setChecked(false);
                rprms.setMargins(GlobalUtils.toPixels(10, getResources().getDisplayMetrics()), 0, 0, 0);
            }
            mRbgStep.addView(btn, rprms);
        }

        mTVInstruction.setText(instruction);
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerListeners();

        mTxvView = findViewById(R.id.txv_camera);
        mTxvView.setSurfaceTextureListener(mTextureListener);

        //mTargetLayoutParams = (RelativeLayout.LayoutParams) mRlCursor.getLayoutParams();
        mRlCursor.setOnTouchListener(mOnTouchListener);
    }

    @Override
    protected void onDestroy() {
        // Save current location of pointer after getting out
        Equation.saveObjectToSharedPreference(this, PREF_NAME, OBJ_NAME, new Integer[]{mCurLeftMargin, mCurTopMargin});
        super.onDestroy();
    }

    @NonNull
    @Override
    protected PresenterFactory<CameraPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

//    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
//        @Override
//        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
//            super.onCaptureCompleted(session, request, result);
//            Toast.makeText(CameraActivity.this, "Saved:" + mFile, Toast.LENGTH_SHORT).show();
//            createCameraPreview();
//        }
//    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CameraActivity.this, CAUTION_GRANTING_PERMISSION, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e(TAG, "onResume");
        startBackgroundThread();
        if (mTxvView.isAvailable()) {
            openCamera();
        } else {
            mTxvView.setSurfaceTextureListener(mTextureListener);
        }
    }

    @Override
    protected void onPause() {
        //Log.e(TAG, "onPause");
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerCameraViewComponent.builder()
                .appComponent(parentComponent)
                .cameraViewModule(new CameraViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void registerListeners() {
        // This listener is for camera review layout
        mTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                //open your camera here
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                // Transform you image captured size according to the surface width and height
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        };

        mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        x_cord = X - lParams.leftMargin;
                        y_cord = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        mCurLeftMargin = X - x_cord;
                        layoutParams.leftMargin = mCurLeftMargin;
                        if (Y - y_cord > GlobalUtils.toPixels(-42, getResources().getDisplayMetrics())) {
                            mCurTopMargin = Y - y_cord;
                            layoutParams.topMargin = mCurTopMargin;
                        }
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                ((RelativeLayout) view.getParent()).invalidate();
                return true;
            }
        };
    }

    @Override
    @OnClick(R.id.btn_action)
    public void performActionButton(View v) {
        final AlertDialog loadingDialog = new SpotsDialog(mContext, R.style.CustomLoadingDialog);
        final LovelyStandardDialog choiceDialog = new LovelyStandardDialog(this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle(R.string.dialog_confirmation);

        takePicture(v, new ProcessCallback() {
            @Override
            public void onStart() {
                //do nothing
                loadingDialog.show();
            }

            @Override
            public void inProcess() {
                //do nothing
            }

            @Override
            public void onSuccess(final String s) {
                // get location of the cursor
                int[] location = new int[2];
                mImgTarget.getLocationOnScreen(location);

                // The y axis should minus the height of status bar and action bar
                int offset = GlobalUtils.toPixels(6, getResources().getDisplayMetrics());
                location[1] = location[1] - (GlobalUtils.getActionBarHeight(mContext) + GlobalUtils.getStatusBarHeight(mContext)) + offset;
                Log.d(TAG, "actionBarH/statusBarH: " + GlobalUtils.getActionBarHeight(mContext) + "/" + GlobalUtils.getStatusBarHeight(mContext));

                int actualX = GlobalUtils.toPixels(SELECTED_PIXEL_NUM[0], getResources().getDisplayMetrics());
                int actualY = GlobalUtils.toPixels(SELECTED_PIXEL_NUM[1], getResources().getDisplayMetrics());

                Bitmap b = mPresenter.getBitmapFromCamera();
                // Get saved image location
                Uri uri = Uri.fromFile(new File(s));
                try {
                    // Do the rotation of the bitmap when it in 90 or 270 degree
                    b = GlobalUtils.rotateImageIfRequired(mContext, b, uri);
                } catch (Exception e) {
                    Log.e(TAG, "Fail to rotate the bitmap: " + e.getMessage());
                }
                mPresenter.setBitmapFromCamera(Bitmap.createScaledBitmap(b, mTxvView.getWidth(), mTxvView.getHeight(), true));
                final int[] colors = mPresenter.getColors(location, new int[]{actualX, actualY});
                Log.d(TAG, "RGB: " + colors[0] + "/" + colors[1] + "/" + colors[2]);

                // Close the loading dialog
                loadingDialog.dismiss();
                // Start updating the view
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //mImgTemp.setImageBitmap(mPresenter.getBitmapFromCamera());
                            Log.d(TAG, "w/h: " + mPresenter.getBitmapFromCamera().getWidth() + "/" + mPresenter.getBitmapFromCamera().getHeight());

                            // Get current context for giving a right action
                            Bundle bundle = getIntent().getExtras();
                            int viewId = bundle.getInt(StringUtils.KEY_VIEW_ID);
                            switch (viewId) {
                                // Get final value
                                case R.id.btn_record_new_sample:
                                    float v = mPresenter.getFinalValue(colors);
                                    String msg = String.format(getResources().getString(R.string.dialog_sample_value_success_confirm), String.valueOf(v));
                                    choiceDialog.setMessage(Html.fromHtml(msg))
                                            .setNegativeButton(android.R.string.no, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mPresenter.deleteAFile(s);
                                                }
                                            })
                                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Toast.makeText(CameraActivity.this, String.format(INFO_SUCCESS_SAVING, s), Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .show();
                                    Log.d(TAG, "Final value: " + v);
                                    break;
                                // Get one point calib
                                case 3:
                                    // Save new value for calibration. The update will be shown in IntroActivity (onResume)
                                    mPresenter.calibOneSample(colors);
                                    // close the camera
                                    mActivity.finish();
                                    break;
                                // Get two point calib
                                case 2:
                                    // Check if fulfill all needed colors. If color has value -1 meaning it's still untouched.
                                    // Based on the color objects, the layout will be changed.
                                    if (mColors0[0] == -1) {
                                        mColors0 = colors;
                                        // Change text of the action button
                                        mBtnAction.setText(R.string.txt_analyze_50ppb);
                                        ((RadioButton) mRbgStep.getChildAt(1)).setChecked(true);
                                    } else {
                                        mColors1 = colors;
                                        // Do get calib values
                                        mPresenter.calibTwoSample(mColors0, mColors1);
                                        // close the camera
                                        mActivity.finish();
                                    }
                                    break;
                                // Get three point calib
                                case 1:
                                    if (mColors0[0] == -1) {
                                        mColors0 = colors;
                                        // Change text of the action button
                                        mBtnAction.setText(R.string.txt_analyze_50ppb);
                                        ((RadioButton) mRbgStep.getChildAt(1)).setChecked(true);
                                    } else {
                                        if (mColors1[0] == -1) {
                                            mColors1 = colors;
                                            // Change text of the action button
                                            mBtnAction.setText(R.string.txt_analyze_500ppb);
                                            ((RadioButton) mRbgStep.getChildAt(2)).setChecked(true);
                                        } else {
                                            mColors2 = colors;
                                            // Do get calib values
                                            mPresenter.calibThreeSample(mColors0, mColors1, mColors2);
                                            // close the camera
                                            mActivity.finish();
                                        }
                                    }
                                    break;
                            }
                        } catch (Exception e) {
                            LovelyStandardDialog dialog = new LovelyStandardDialog(mContext)
                                    .setTopColorRes(R.color.colorPrimaryDark)
                                    .setTitle(R.string.dialog_issue)
                                    .setMessage(e.getMessage());
                            dialog.setPositiveButton(R.string.dialog_btn_restart, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // reset to the default
                                    mColors0 = new int[]{-1, -1, -1};
                                    mColors1 = new int[]{-1, -1, -1};
                                    mColors2 = new int[]{-1, -1, -1};
                                    Bundle b = getIntent().getExtras();
                                    int viewId = b.getInt(StringUtils.KEY_VIEW_ID);
                                    setupViewByContext(viewId);
                                }
                            }).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String f) {
                Log.e(TAG, "Fail to get bitmap from camera.");
                loadingDialog.dismiss();
            }
        });
    }

    /**
     * This method is for taking picture from rear camera
     *
     * @param view            the focusing view
     * @param processCallback for retrieving process status
     */
    @Override
    public void takePicture(View view, final ProcessCallback processCallback) {
        processCallback.onStart();
        if (mCameraDevice == null) {
            Log.e(TAG, "mCameraDevice is null");
            processCallback.onFailure("mCameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(mTxvView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            final String fileName = GlobalUtils.namedByTime();
            final String path = Environment.getExternalStorageDirectory() + "/" + APP_NAME + "/";
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            final String dir = path + fileName;
            ImageReader.OnImageAvailableListener readerListener = mPresenter.getImageReaderListener(dir);
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    createCameraPreview();

                    processCallback.onSuccess(dir);
                }
            };

            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createCameraPreview() {
        try {
            SurfaceTexture texture = mTxvView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mImageDimension.getWidth(), mImageDimension.getHeight());
            Surface surface = new Surface(texture);
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == mCameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    mCameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity.this, INFO_CONFIG_CHANGED, Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open rear camera
     */
    @Override
    public void openCamera() {
        CameraManager manager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        //Log.e(TAG, "Is camera open?");
        try {
            mCameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            mImageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(mCameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        //Log.e(TAG, "openCamera X");
    }

    @Override
    public void updatePreview() {
        if (null == mCameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            mCameraCaptureSessions.setRepeatingRequest(mCaptureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeCamera() {
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (null != mImageReader) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    @Override
    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    public void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LovelyInfoDialog dialog = new LovelyInfoDialog(mContext)
                .setTopColorRes(R.color.colorPrimaryDark)
                //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
                .setTitle(R.string.dialog_instruction);

        String infoMsg = "-";

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_info:
                // Get view id from prev activity to know the cases
                Bundle b = getIntent().getExtras();
                int viewId = b.getInt(StringUtils.KEY_VIEW_ID);
                switch (viewId) {
                    case R.id.btn_record_new_sample:
                        infoMsg = getResources().getString(R.string.record_instruct);
                        break;
                    case 3:
                        infoMsg = getResources().getString(R.string.calib_one_sample_desc)
                                + "\n" + getResources().getString(R.string.calib_one_sample_instruct_1)
                                + "\n" + getResources().getString(R.string.calib_one_sample_instruct_2);
                        break;
                    case 2:
                        infoMsg = getResources().getString(R.string.calib_two_sample_desc)
                                + "\n" + getResources().getString(R.string.calib_two_sample_instruct_1)
                                + "\n" + getResources().getString(R.string.calib_two_sample_instruct_2)
                                + "\n" + getResources().getString(R.string.calib_two_sample_instruct_3);
                        break;
                    case 1:
                        infoMsg = getResources().getString(R.string.calib_three_sample_desc)
                                + "\n" + getResources().getString(R.string.calib_three_sample_instruct_1)
                                + "\n" + getResources().getString(R.string.calib_three_sample_instruct_2)
                                + "\n" + getResources().getString(R.string.calib_three_sample_instruct_3)
                                + "\n" + getResources().getString(R.string.calib_three_sample_instruct_4);
                        break;
                }
                dialog.setMessage(infoMsg).show();
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }
}
