package hamdan.juniordesign.quebo.photoedit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import hamdan.juniordesign.quebo.MainActivity;
import hamdan.juniordesign.quebo.R;

/**
 * The fragment that will show the Image Cropping UI by requested preset.
 */
public class MainFragment extends Fragment
        implements CropImageView.OnSetImageUriCompleteListener,
        CropImageView.OnCropImageCompleteListener {

    private CropDemoPreset mDemoPreset;

    private CropImageView mCropImageView;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static MainFragment newInstance(CropDemoPreset demoPreset) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("DEMO_PRESET", demoPreset.name());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Set the image to show for cropping.
     */
    public void setImageUri(Uri imageUri) {
        mCropImageView.setImageUriAsync(imageUri);
        //        CropImage.activity(imageUri)
        //                .start(getContext(), this);
    }

    /**
     * Set the options of the crop image view to the given values.
     */
    public void setCropImageViewOptions(CropImageViewOptions options) {
        mCropImageView.setScaleType(options.scaleType);
        mCropImageView.setCropShape(options.cropShape);
        mCropImageView.setGuidelines(options.guidelines);
        mCropImageView.setAspectRatio(options.aspectRatio.first, options.aspectRatio.second);
        mCropImageView.setFixedAspectRatio(options.fixAspectRatio);
        mCropImageView.setMultiTouchEnabled(options.multitouch);
        mCropImageView.setShowCropOverlay(options.showCropOverlay);
        mCropImageView.setShowProgressBar(options.showProgressBar);
        mCropImageView.setAutoZoomEnabled(options.autoZoomEnabled);
        mCropImageView.setMaxZoom(options.maxZoomLevel);
        mCropImageView.setFlippedHorizontally(options.flipHorizontally);
        mCropImageView.setFlippedVertically(options.flipVertically);
    }

    /**
     * Set the initial rectangle to use.
     */
    public void setInitialCropRect() {
        mCropImageView.setCropRect(new Rect(100, 300, 500, 1200));
    }

    /**
     * Reset crop window to initial rectangle.
     */
    public void resetCropRect() {
        mCropImageView.resetCropRect();
    }

    public void updateCurrentCropViewOptions() {
        CropImageViewOptions options = new CropImageViewOptions();
        options.scaleType = mCropImageView.getScaleType();
        options.cropShape = mCropImageView.getCropShape();
        options.guidelines = mCropImageView.getGuidelines();
        options.aspectRatio = mCropImageView.getAspectRatio();
        options.fixAspectRatio = mCropImageView.isFixAspectRatio();
        options.showCropOverlay = mCropImageView.isShowCropOverlay();
        options.showProgressBar = mCropImageView.isShowProgressBar();
        options.autoZoomEnabled = mCropImageView.isAutoZoomEnabled();
        options.maxZoomLevel = mCropImageView.getMaxZoom();
        options.flipHorizontally = mCropImageView.isFlippedHorizontally();
        options.flipVertically = mCropImageView.isFlippedVertically();
        ((PhotoEditingActivity) Objects.requireNonNull(getActivity())).setCurrentOptions(options);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        switch (mDemoPreset) {
            case RECT:
                rootView = inflater.inflate(R.layout.fragment_main_rect, container, false);
                break;
            case CIRCULAR:
                rootView = inflater.inflate(R.layout.fragment_main_oval, container, false);
                break;
            case CUSTOMIZED_OVERLAY:
                rootView = inflater.inflate(R.layout.fragment_main_customized, container, false);
                break;
            case MIN_MAX_OVERRIDE:
                rootView = inflater.inflate(R.layout.fragment_main_min_max, container, false);
                break;
            case SCALE_CENTER_INSIDE:
                rootView = inflater.inflate(R.layout.fragment_main_scale_center, container, false);
                break;
            case CUSTOM:
                rootView = inflater.inflate(R.layout.fragment_main_rect, container, false);
                break;
            default:
                throw new IllegalStateException("Unknown preset: " + mDemoPreset);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCropImageView = view.findViewById(R.id.cropImageView);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);

        updateCurrentCropViewOptions();

        if (savedInstanceState == null) {
            mCropImageView.setImageBitmap(MainActivity.bitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_action_crop) {
            mCropImageView.getCroppedImageAsync();
            return true;
        } else if (item.getItemId() == R.id.main_action_rotate) {
            mCropImageView.rotateImage(90);
            return true;
        } else if (item.getItemId() == R.id.main_action_flip_horizontally) {
            mCropImageView.flipImageHorizontally();
            return true;
        } else if (item.getItemId() == R.id.main_action_flip_vertically) {
            mCropImageView.flipImageVertically();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        assert getArguments() != null;
        mDemoPreset = CropDemoPreset.valueOf(getArguments().getString("DEMO_PRESET"));
        ((PhotoEditingActivity) Objects.requireNonNull(getActivity())).setCurrentFragment(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCropImageView != null) {
            mCropImageView.setOnSetImageUriCompleteListener(null);
            mCropImageView.setOnCropImageCompleteListener(null);
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Toast.makeText(getActivity(), "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("AIC", "Failed to load image by URI", error);
            Toast.makeText(getActivity(), "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("SAMPLE_SIZE", result.getSampleSize());
            if (result.getUri() != null) {
                intent.putExtra("URI", result.getUri());
            } else {
                MainActivity.bitmap = mCropImageView.getCropShape() == CropImageView.CropShape.OVAL
                        ? CropImage.toOvalBitmap(result.getBitmap())
                        : result.getBitmap();
            }
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        } else {
            Log.e("AIC", "Failed to crop image", result.getError());
            Toast.makeText(getActivity(), "Image crop failed: " + result.getError().getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            handleCropResult(result);
        }
    }

}
