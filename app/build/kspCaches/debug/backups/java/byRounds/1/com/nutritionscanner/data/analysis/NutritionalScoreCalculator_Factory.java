package com.nutritionscanner.data.analysis;

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
public final class NutritionalScoreCalculator_Factory implements Factory<NutritionalScoreCalculator> {
  @Override
  public NutritionalScoreCalculator get() {
    return newInstance();
  }

  public static NutritionalScoreCalculator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NutritionalScoreCalculator newInstance() {
    return new NutritionalScoreCalculator();
  }

  private static final class InstanceHolder {
    private static final NutritionalScoreCalculator_Factory INSTANCE = new NutritionalScoreCalculator_Factory();
  }
}
