package com.example.chessgame.di;

import com.example.chessgame.domain.usecase.DetectCheckUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class UseCaseModule_ProvideDetectCheckUseCaseFactory implements Factory<DetectCheckUseCase> {
  @Override
  public DetectCheckUseCase get() {
    return provideDetectCheckUseCase();
  }

  public static UseCaseModule_ProvideDetectCheckUseCaseFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DetectCheckUseCase provideDetectCheckUseCase() {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideDetectCheckUseCase());
  }

  private static final class InstanceHolder {
    private static final UseCaseModule_ProvideDetectCheckUseCaseFactory INSTANCE = new UseCaseModule_ProvideDetectCheckUseCaseFactory();
  }
}
