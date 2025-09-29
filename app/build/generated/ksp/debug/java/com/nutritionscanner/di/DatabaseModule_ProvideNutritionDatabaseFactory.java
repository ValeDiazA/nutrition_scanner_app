package com.nutritionscanner.di;

import android.content.Context;
import com.nutritionscanner.data.database.NutritionDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideNutritionDatabaseFactory implements Factory<NutritionDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideNutritionDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public NutritionDatabase get() {
    return provideNutritionDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideNutritionDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideNutritionDatabaseFactory(contextProvider);
  }

  public static NutritionDatabase provideNutritionDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideNutritionDatabase(context));
  }
}
