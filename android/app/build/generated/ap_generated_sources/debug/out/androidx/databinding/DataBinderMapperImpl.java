package androidx.databinding;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new org.tensorflow.lite.examples.transfer.DataBinderMapperImpl());
  }
}
