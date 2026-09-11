package com.example.chessgame.data.repository;

import com.example.chessgame.domain.usecase.GenerateLegalMovesUseCase;
import com.example.chessgame.domain.usecase.MakeMoveUseCase;
import com.example.chessgame.domain.usecase.UndoMoveUseCase;
import com.example.chessgame.domain.usecase.ValidateMoveUseCase;
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
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class ChessRepositoryImpl_Factory implements Factory<ChessRepositoryImpl> {
  private final Provider<MakeMoveUseCase> makeMoveUseCaseProvider;

  private final Provider<UndoMoveUseCase> undoMoveUseCaseProvider;

  private final Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider;

  private final Provider<ValidateMoveUseCase> validateMoveUseCaseProvider;

  public ChessRepositoryImpl_Factory(Provider<MakeMoveUseCase> makeMoveUseCaseProvider,
      Provider<UndoMoveUseCase> undoMoveUseCaseProvider,
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider,
      Provider<ValidateMoveUseCase> validateMoveUseCaseProvider) {
    this.makeMoveUseCaseProvider = makeMoveUseCaseProvider;
    this.undoMoveUseCaseProvider = undoMoveUseCaseProvider;
    this.generateLegalMovesUseCaseProvider = generateLegalMovesUseCaseProvider;
    this.validateMoveUseCaseProvider = validateMoveUseCaseProvider;
  }

  @Override
  public ChessRepositoryImpl get() {
    return newInstance(makeMoveUseCaseProvider.get(), undoMoveUseCaseProvider.get(), generateLegalMovesUseCaseProvider.get(), validateMoveUseCaseProvider.get());
  }

  public static ChessRepositoryImpl_Factory create(
      Provider<MakeMoveUseCase> makeMoveUseCaseProvider,
      Provider<UndoMoveUseCase> undoMoveUseCaseProvider,
      Provider<GenerateLegalMovesUseCase> generateLegalMovesUseCaseProvider,
      Provider<ValidateMoveUseCase> validateMoveUseCaseProvider) {
    return new ChessRepositoryImpl_Factory(makeMoveUseCaseProvider, undoMoveUseCaseProvider, generateLegalMovesUseCaseProvider, validateMoveUseCaseProvider);
  }

  public static ChessRepositoryImpl newInstance(MakeMoveUseCase makeMoveUseCase,
      UndoMoveUseCase undoMoveUseCase, GenerateLegalMovesUseCase generateLegalMovesUseCase,
      ValidateMoveUseCase validateMoveUseCase) {
    return new ChessRepositoryImpl(makeMoveUseCase, undoMoveUseCase, generateLegalMovesUseCase, validateMoveUseCase);
  }
}
