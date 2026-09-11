package com.example.chessgame.di;

import com.example.chessgame.domain.usecase.DetectCheckUseCase;
import com.example.chessgame.domain.usecase.DetectCheckmateUseCase;
import com.example.chessgame.domain.usecase.GenerateLegalMovesUseCase;
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
public final class UseCaseModule_ProvideDetectCheckmateUseCaseFactory implements Factory<DetectCheckmateUseCase> {
  private final Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider;

  private final Provider<DetectCheckUseCase> detectCheckUseCaseProvider;

  public UseCaseModule_ProvideDetectCheckmateUseCaseFactory(
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider,
      Provider<DetectCheckUseCase> detectCheckUseCaseProvider) {
    this.generateLegalMovesUseCaseProvider = generateLegalMovesUseCaseProvider;
    this.detectCheckUseCaseProvider = detectCheckUseCaseProvider;
  }

  @Override
  public DetectCheckmateUseCase get() {
    return provideDetectCheckmateUseCase(generateLegalMovesUseCaseProvider.get(), detectCheckUseCaseProvider.get());
  }

  public static UseCaseModule_ProvideDetectCheckmateUseCaseFactory create(
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider,
      Provider<DetectCheckUseCase> detectCheckUseCaseProvider) {
    return new UseCaseModule_ProvideDetectCheckmateUseCaseFactory(generateLegalMovesUseCaseProvider, detectCheckUseCaseProvider);
  }

  public static DetectCheckmateUseCase provideDetectCheckmateUseCase(
      GenerateLegalMovesUseCase generateLegalMovesUseCase, DetectCheckUseCase detectCheckUseCase) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideDetectCheckmateUseCase(generateLegalMovesUseCase, detectCheckUseCase));
  }
}
