package com.nutritionscanner.data.stellar;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class StellarWalletManager_Factory implements Factory<StellarWalletManager> {
  private final Provider<StellarPreferences> stellarPreferencesProvider;

  public StellarWalletManager_Factory(Provider<StellarPreferences> stellarPreferencesProvider) {
    this.stellarPreferencesProvider = stellarPreferencesProvider;
  }

  @Override
  public StellarWalletManager get() {
    return newInstance(stellarPreferencesProvider.get());
  }

  public static StellarWalletManager_Factory create(
      Provider<StellarPreferences> stellarPreferencesProvider) {
    return new StellarWalletManager_Factory(stellarPreferencesProvider);
  }

  public static StellarWalletManager newInstance(StellarPreferences stellarPreferences) {
    return new StellarWalletManager(stellarPreferences);
  }
}
