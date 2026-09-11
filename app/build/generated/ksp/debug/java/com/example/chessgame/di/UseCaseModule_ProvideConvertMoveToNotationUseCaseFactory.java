package com.example.chessgame.di;

import com.example.chessgame.domain.usecase.ConvertMoveToNotationUseCase;
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
public final class UseCaseModule_ProvideConvertMoveToNotationUseCaseFactory implements Factory<ConvertMoveToNotationUseCase> {
  private final Provider<DetectCheckmateUseCase> detectCheckmateUseCaseProvider;

  private final Provider<DetectCheckUseCase> detectCheckUseCaseProvider;

  private final Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider;

  public UseCaseModule_ProvideConvertMoveToNotationUseCaseFactory(
      Provider<DetectCheckmateUseCase> detectCheckmateUseCaseProvider,
      Provider<DetectCheckUseCase> detectCheckUseCaseProvider,
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider) {
    this.detectCheckmateUseCaseProvider = detectCheckmateUseCaseProvider;
    this.detectCheckUseCaseProvider = detectCheckUseCaseProvider;
    this.generateLegalMovesUseCaseProvider = generateLegalMovesUseCaseProvider;
  }

  @Override
  public ConvertMoveToNotationUseCase get() {
    return provideConvertMoveToNotationUseCase(detectCheckmateUseCaseProvider.get(), detectCheckUseCaseProvider.get(), generateLegalMovesUseCaseProvider.get());
  }

  public static UseCaseModule_ProvideConvertMoveToNotationUseCaseFactory create(
      Provider<DetectCheckmateUseCase> detectCheckmateUseCaseProvider,
      Provider<DetectCheckUseCase> detectCheckUseCaseProvider,
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider) {
    return new UseCaseModule_ProvideConvertMoveToNotationUseCaseFactory(detectCheckmateUseCaseProvider, detectCheckUseCaseProvider, generateLegalMovesUseCaseProvider);
  }

  public static ConvertMoveToNotationUseCase provideConvertMoveToNotationUseCase(
      DetectCheckmateUseCase detectCheckmateUseCase, DetectCheckUseCase detectCheckUseCase,
      GenerateLegalMovesUseCase generateLegalMovesUseCase) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideConvertMoveToNotationUseCase(detectCheckmateUseCase, detectCheckUseCase, generateLegalMovesUseCase));
  }
}
