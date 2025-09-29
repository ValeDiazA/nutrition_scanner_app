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
public final class IngredientAnalyzer_Factory implements Factory<IngredientAnalyzer> {
  @Override
  public IngredientAnalyzer get() {
    return newInstance();
  }

  public static IngredientAnalyzer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static IngredientAnalyzer newInstance() {
    return new IngredientAnalyzer();
  }

  private static final class InstanceHolder {
    private static final IngredientAnalyzer_Factory INSTANCE = new IngredientAnalyzer_Factory();
  }
}
