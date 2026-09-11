package com.example.chessgame.presentation.viewmodel;

import com.example.chessgame.domain.repository.ChessRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ChessGameViewModel_Factory implements Factory<ChessGameViewModel> {
  private final Provider<ChessRepository> repositoryProvider;

  public ChessGameViewModel_Factory(Provider<ChessRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ChessGameViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ChessGameViewModel_Factory create(Provider<ChessRepository> repositoryProvider) {
    return new ChessGameViewModel_Factory(repositoryProvider);
  }

  public static ChessGameViewModel newInstance(ChessRepository repository) {
    return new ChessGameViewModel(repository);
  }
}
