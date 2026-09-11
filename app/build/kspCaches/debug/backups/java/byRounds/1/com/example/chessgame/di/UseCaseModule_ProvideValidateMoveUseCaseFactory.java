package com.example.chessgame.di;

import com.example.chessgame.domain.usecase.GenerateLegalMovesUseCase;
import com.example.chessgame.domain.usecase.ValidateMoveUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class UseCaseModule_ProvideValidateMoveUseCaseFactory implements Factory<ValidateMoveUseCase> {
  private final Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider;

  public UseCaseModule_ProvideValidateMoveUseCaseFactory(
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider) {
    this.generateLegalMovesUseCaseProvider = generateLegalMovesUseCaseProvider;
  }

  @Override
  public ValidateMoveUseCase get() {
    return provideValidateMoveUseCase(generateLegalMovesUseCaseProvider.get());
  }

  public static UseCaseModule_ProvideValidateMoveUseCaseFactory create(
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider) {
    return new UseCaseModule_ProvideValidateMoveUseCaseFactory(generateLegalMovesUseCaseProvider);
  }

  public static ValidateMoveUseCase provideValidateMoveUseCase(
      GenerateLegalMovesUseCase generateLegalMovesUseCase) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideValidateMoveUseCase(generateLegalMovesUseCase));
  }
}
