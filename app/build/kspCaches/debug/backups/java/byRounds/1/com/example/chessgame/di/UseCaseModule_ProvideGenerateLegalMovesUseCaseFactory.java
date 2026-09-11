package com.example.chessgame.di;

import com.example.chessgame.domain.usecase.GenerateLegalMovesUseCase;
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
public final class UseCaseModule_ProvideGenerateLegalMovesUseCaseFactory implements Factory<GenerateLegalMovesUseCase> {
  @Override
  public GenerateLegalMovesUseCase get() {
    return provideGenerateLegalMovesUseCase();
  }

  public static UseCaseModule_ProvideGenerateLegalMovesUseCaseFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GenerateLegalMovesUseCase provideGenerateLegalMovesUseCase() {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGenerateLegalMovesUseCase());
  }

  private static final class InstanceHolder {
    private static final UseCaseModule_ProvideGenerateLegalMovesUseCaseFactory INSTANCE = new UseCaseModule_ProvideGenerateLegalMovesUseCaseFactory();
  }
}
