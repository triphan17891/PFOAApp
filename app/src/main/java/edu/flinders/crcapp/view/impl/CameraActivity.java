/*
 *  Created by Tri Phan on 7/01/19 10:22 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 10:22 PM
 */

package edu.flinders.crcapp.view.impl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.hardware.camera2.*;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.*;
import android.widget.*;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.flinders.crcapp.R;
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
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    // the rectangle size in which pixels will be selected
    private final int[] SELECTED_PIXEL_NUM = {5,5};

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

    int x_cord;
    int y_cord;

    private TextureView.SurfaceTextureListener mTextureListener;
    private View.OnTouchListener mOnTouchListener;

    private File mFile;

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

    private RelativeLayout.LayoutParams mTargetLayoutParams;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewByContext();
    }

    @Override
    public void setupViewByContext() {
        Bundle b = getIntent().getExtras();
        String title = "-";
        String instruction = "-";
        int steps = 0;
        int viewId = b.getInt(StringUtils.KEY_VIEW_ID);
        switch (viewId) {
            case R.id.btn_record_new_sample:
                title = getString(R.string.intro_second_page);
                instruction = getString(R.string.record_instruct);
                mBtnAction.setText(R.string.txt_record);
                break;
            case R.id.btn_zero_point:
                title = getString(R.string.calib_zero_point_title);
                instruction = getString(R.string.calib_zero_point_instruct);
                mBtnAction.setText(R.string.txt_finish);
                break;
            case R.id.btn_one_point:
                title = getString(R.string.calib_one_point_title);
                instruction = getString(R.string.calib_one_point_instruct_1);
                mBtnAction.setText(R.string.txt_next);
                steps = 2;
                break;
            case R.id.btn_two_point:
                title = getString(R.string.calib_two_point_title);
                instruction = getString(R.string.calib_two_point_instruct_1);
                mBtnAction.setText(R.string.txt_next);
                steps = 3;
                break;
            case R.id.btn_three_point:
                title = getString(R.string.calib_three_point_title);
                instruction = getString(R.string.calib_three_point_instruct_1);
                mBtnAction.setText(R.string.txt_next);
                steps = 4;
                break;
        }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerListeners();

        mTxvView = findViewById(R.id.txv_camera);
        mTxvView.setSurfaceTextureListener(mTextureListener);

        mTargetLayoutParams = (RelativeLayout.LayoutParams) mImgTarget.getLayoutParams();
        mImgTarget.setOnTouchListener(mOnTouchListener);

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
                        layoutParams.leftMargin = X - x_cord;
                        if(Y - y_cord > GlobalUtils.toPixels(-42, getResources().getDisplayMetrics())) {
                            layoutParams.topMargin = Y - y_cord;
                        }
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                ((RelativeLayout)view.getParent()).invalidate();
                return true;
            }
        };
    }

    @Override
    @OnClick(R.id.btn_action)
    public void performActionButton(View v) {
        takePicture(v, new ProcessCallback() {
            @Override
            public void onStart() {
                //do nothing
            }

            @Override
            public void inProcess() {
                //do nothing
            }

            @Override
            public void onSuccess() {
                // get location of the cursor
                int[] location = new int[2];
                mImgTarget.getLocationOnScreen(location);
                int[] colors = mPresenter.getColors(location, SELECTED_PIXEL_NUM);

                Log.d("colors", "RGB: " + colors[0] + "/" + colors[1] + "/" + colors[2]);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     *
     * This method is for taking picture from rear camera
     * @param view the focusing view
     * @param processCallback for retrieving process status
     */
    @Override
    public void takePicture(View view, final ProcessCallback processCallback) {
        if (mCameraDevice == null) {
            Log.e(TAG, "mCameraDevice is null");
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
                    Toast.makeText(CameraActivity.this, String.format(INFO_SUCCESS_SAVING, dir), Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                    processCallback.onSuccess();
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
}
