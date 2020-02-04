package org.tensorflow.lite.examples.transfer.databinding;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.lang.Deprecated;
import java.lang.Object;
import org.tensorflow.lite.examples.transfer.CameraFragmentViewModel;

public abstract class CameraFragmentBinding extends ViewDataBinding {
  @NonNull
  public final Chip captureModeChip;

  @NonNull
  public final LinearLayout classBtn1;

  @NonNull
  public final LinearLayout classBtn2;

  @NonNull
  public final LinearLayout classBtn3;

  @NonNull
  public final LinearLayout classBtn4;

  @NonNull
  public final LinearLayout classesBar;

  @NonNull
  public final Chip inferenceModeChip;

  @NonNull
  public final ChipGroup modeChipGroup;

  @NonNull
  public final TextureView viewFinder;

  @Bindable
  protected CameraFragmentViewModel mVm;

  protected CameraFragmentBinding(Object _bindingComponent, View _root, int _localFieldCount,
      Chip captureModeChip, LinearLayout classBtn1, LinearLayout classBtn2, LinearLayout classBtn3,
      LinearLayout classBtn4, LinearLayout classesBar, Chip inferenceModeChip,
      ChipGroup modeChipGroup, TextureView viewFinder) {
    super(_bindingComponent, _root, _localFieldCount);
    this.captureModeChip = captureModeChip;
    this.classBtn1 = classBtn1;
    this.classBtn2 = classBtn2;
    this.classBtn3 = classBtn3;
    this.classBtn4 = classBtn4;
    this.classesBar = classesBar;
    this.inferenceModeChip = inferenceModeChip;
    this.modeChipGroup = modeChipGroup;
    this.viewFinder = viewFinder;
  }

  public abstract void setVm(@Nullable CameraFragmentViewModel vm);

  @Nullable
  public CameraFragmentViewModel getVm() {
    return mVm;
  }

  @NonNull
  public static CameraFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.camera_fragment, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static CameraFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<CameraFragmentBinding>inflateInternal(inflater, org.tensorflow.lite.examples.transfer.R.layout.camera_fragment, root, attachToRoot, component);
  }

  @NonNull
  public static CameraFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.camera_fragment, null, false, component)
   */
  @NonNull
  @Deprecated
  public static CameraFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<CameraFragmentBinding>inflateInternal(inflater, org.tensorflow.lite.examples.transfer.R.layout.camera_fragment, null, false, component);
  }

  public static CameraFragmentBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static CameraFragmentBinding bind(@NonNull View view, @Nullable Object component) {
    return (CameraFragmentBinding)bind(component, view, org.tensorflow.lite.examples.transfer.R.layout.camera_fragment);
  }
}
