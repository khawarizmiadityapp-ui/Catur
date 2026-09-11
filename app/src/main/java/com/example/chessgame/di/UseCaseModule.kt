package com.example.chessgame.di

import com.example.chessgame.domain.usecase.ConvertMoveToNotationUseCase
import com.example.chessgame.domain.usecase.DetectCheckUseCase
import com.example.chessgame.domain.usecase.DetectCheckmateUseCase
import com.example.chessgame.domain.usecase.DetectStalemateUseCase
import com.example.chessgame.domain.usecase.GenerateLegalMovesUseCase
import com.example.chessgame.domain.usecase.MakeMoveUseCase
import com.example.chessgame.domain.usecase.UndoMoveUseCase
import com.example.chessgame.domain.usecase.ValidateMoveUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideDetectCheckUseCase(): DetectCheckUseCase = DetectCheckUseCase()

    @Provides
    @Singleton
    fun provideGenerateLegalMovesUseCase(): GenerateLegalMovesUseCase = GenerateLegalMovesUseCase()

    @Provides
    @Singleton
    fun provideValidateMoveUseCase(
        generateLegalMovesUseCase: GenerateLegalMovesUseCase
    ): ValidateMoveUseCase = ValidateMoveUseCase(generateLegalMovesUseCase)

    @Provides
    @Singleton
    fun provideDetectCheckmateUseCase(
        generateLegalMovesUseCase: GenerateLegalMovesUseCase,
        detectCheckUseCase: DetectCheckUseCase
    ): DetectCheckmateUseCase = DetectCheckmateUseCase(generateLegalMovesUseCase, detectCheckUseCase)

    @Provides
    @Singleton
    fun provideDetectStalemateUseCase(
        generateLegalMovesUseCase: GenerateLegalMovesUseCase,
        detectCheckUseCase: DetectCheckUseCase
    ): DetectStalemateUseCase = DetectStalemateUseCase(generateLegalMovesUseCase, detectCheckUseCase)

    @Provides
    @Singleton
    fun provideConvertMoveToNotationUseCase(
        detectCheckmateUseCase: DetectCheckmateUseCase,
        detectCheckUseCase: DetectCheckUseCase,
        generateLegalMovesUseCase: GenerateLegalMovesUseCase
    ): ConvertMoveToNotationUseCase = ConvertMoveToNotationUseCase(
        detectCheckmateUseCase,
        detectCheckUseCase,
        generateLegalMovesUseCase
    )

    @Provides
    @Singleton
    fun provideMakeMoveUseCase(
        validateMoveUseCase: ValidateMoveUseCase,
        generateLegalMovesUseCase: GenerateLegalMovesUseCase,
        detectCheckUseCase: DetectCheckUseCase,
        detectCheckmateUseCase: DetectCheckmateUseCase,
        detectStalemateUseCase: DetectStalemateUseCase,
        convertMoveToNotationUseCase: ConvertMoveToNotationUseCase
    ): MakeMoveUseCase = MakeMoveUseCase(
        validateMoveUseCase,
        generateLegalMovesUseCase,
        detectCheckUseCase,
        detectCheckmateUseCase,
        detectStalemateUseCase,
        convertMoveToNotationUseCase
    )

    @Provides
    @Singleton
    fun provideUndoMoveUseCase(): UndoMoveUseCase = UndoMoveUseCase()
}
