package com.example.chessgame.di;

import com.example.chessgame.domain.usecase.UndoMoveUseCase;
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
public final class UseCaseModule_ProvideUndoMoveUseCaseFactory implements Factory<UndoMoveUseCase> {
  @Override
  public UndoMoveUseCase get() {
    return provideUndoMoveUseCase();
  }

  public static UseCaseModule_ProvideUndoMoveUseCaseFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static UndoMoveUseCase provideUndoMoveUseCase() {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideUndoMoveUseCase());
  }

  private static final class InstanceHolder {
    private static final UseCaseModule_ProvideUndoMoveUseCaseFactory INSTANCE = new UseCaseModule_ProvideUndoMoveUseCaseFactory();
  }
}
