package com.example.chessgame.di;

import com.example.chessgame.domain.usecase.ConvertMoveToNotationUseCase;
import com.example.chessgame.domain.usecase.DetectCheckUseCase;
import com.example.chessgame.domain.usecase.DetectCheckmateUseCase;
import com.example.chessgame.domain.usecase.DetectStalemateUseCase;
import com.example.chessgame.domain.usecase.GenerateLegalMovesUseCase;
import com.example.chessgame.domain.usecase.MakeMoveUseCase;
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
public final class UseCaseModule_ProvideMakeMoveUseCaseFactory implements Factory<MakeMoveUseCase> {
  private final Provider<ValidateMoveUseCase> validateMoveUseCaseProvider;

  private final Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider;

  private final Provider<DetectCheckUseCase> detectCheckUseCaseProvider;

  private final Provider<DetectCheckmateUseCase> detectCheckmateUseCaseProvider;

  private final Provider<DetectStalemateUseCase> detectStalemateUseCaseProvider;

  private final Provider<ConvertMoveToNotationUseCase> convertMoveToNotationUseCaseProvider;

  public UseCaseModule_ProvideMakeMoveUseCaseFactory(
      Provider<ValidateMoveUseCase> validateMoveUseCaseProvider,
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider,
      Provider<DetectCheckUseCase> detectCheckUseCaseProvider,
      Provider<DetectCheckmateUseCase> detectCheckmateUseCaseProvider,
      Provider<DetectStalemateUseCase> detectStalemateUseCaseProvider,
      Provider<ConvertMoveToNotationUseCase> convertMoveToNotationUseCaseProvider) {
    this.validateMoveUseCaseProvider = validateMoveUseCaseProvider;
    this.generateLegalMovesUseCaseProvider = generateLegalMovesUseCaseProvider;
    this.detectCheckUseCaseProvider = detectCheckUseCaseProvider;
    this.detectCheckmateUseCaseProvider = detectCheckmateUseCaseProvider;
    this.detectStalemateUseCaseProvider = detectStalemateUseCaseProvider;
    this.convertMoveToNotationUseCaseProvider = convertMoveToNotationUseCaseProvider;
  }

  @Override
  public MakeMoveUseCase get() {
    return provideMakeMoveUseCase(validateMoveUseCaseProvider.get(), generateLegalMovesUseCaseProvider.get(), detectCheckUseCaseProvider.get(), detectCheckmateUseCaseProvider.get(), detectStalemateUseCaseProvider.get(), convertMoveToNotationUseCaseProvider.get());
  }

  public static UseCaseModule_ProvideMakeMoveUseCaseFactory create(
      Provider<ValidateMoveUseCase> validateMoveUseCaseProvider,
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider,
      Provider<DetectCheckUseCase> detectCheckUseCaseProvider,
      Provider<DetectCheckmateUseCase> detectCheckmateUseCaseProvider,
      Provider<DetectStalemateUseCase> detectStalemateUseCaseProvider,
      Provider<ConvertMoveToNotationUseCase> convertMoveToNotationUseCaseProvider) {
    return new UseCaseModule_ProvideMakeMoveUseCaseFactory(validateMoveUseCaseProvider, generateLegalMovesUseCaseProvider, detectCheckUseCaseProvider, detectCheckmateUseCaseProvider, detectStalemateUseCaseProvider, convertMoveToNotationUseCaseProvider);
  }

  public static MakeMoveUseCase provideMakeMoveUseCase(ValidateMoveUseCase validateMoveUseCase,
      GenerateLegalMovesUseCase generateLegalMovesUseCase, DetectCheckUseCase detectCheckUseCase,
      DetectCheckmateUseCase detectCheckmateUseCase, DetectStalemateUseCase detectStalemateUseCase,
      ConvertMoveToNotationUseCase convertMoveToNotationUseCase) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideMakeMoveUseCase(validateMoveUseCase, generateLegalMovesUseCase, detectCheckUseCase, detectCheckmateUseCase, detectStalemateUseCase, convertMoveToNotationUseCase));
  }
}
