package com.nutritionscanner.data.ocr;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class MLKitTextRecognizer_Factory implements Factory<MLKitTextRecognizer> {
  @Override
  public MLKitTextRecognizer get() {
    return newInstance();
  }

  public static MLKitTextRecognizer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MLKitTextRecognizer newInstance() {
    return new MLKitTextRecognizer();
  }

  private static final class InstanceHolder {
    private static final MLKitTextRecognizer_Factory INSTANCE = new MLKitTextRecognizer_Factory();
  }
}
