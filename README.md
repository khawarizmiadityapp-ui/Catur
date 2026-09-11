# Chess Game (2 Player Local)

Aplikasi catur Android 2 pemain lokal (1 device) dengan Kotlin, Jetpack Compose, MVVM, dan Clean Architecture.

## Tech Stack

- Kotlin + Jetpack Compose (100% Compose UI)
- Coroutines + StateFlow
- MVVM + Clean Architecture (domain / data / presentation)
- Hilt (Dependency Injection)

## Cara Menjalankan

1. Buka folder proyek ini di **Android Studio** (Ladybug atau lebih baru disarankan).
2. Tunggu Gradle sync selesai (Android Studio akan mengunduh dependency otomatis).
3. Sambungkan device Android atau jalankan emulator (min SDK 26).
4. Klik **Run** (▶) pada konfigurasi `app`.

### Menjalankan Unit Test

```bash
./gradlew test
```

Atau di Android Studio: klik kanan folder `app/src/test` → **Run Tests**.

## Cara Bermain

1. **Tap bidak** milik pemain yang sedang giliran untuk memilih.
2. Kotak tujuan legal akan di-highlight.
3. **Tap kotak tujuan** untuk bergerak.
4. Untuk promosi pion, pilih bidak di dialog (Ratu / Benteng / Gajah / Kuda).
5. Gunakan **Undo** untuk membatalkan langkah (mendukung castling, en passant, promosi).
6. **New Game** untuk reset papan.

## Struktur Proyek

```
com.example.chessgame/
├── domain/          # Pure Kotlin — model & use case
├── data/            # Repository implementation
├── presentation/    # ViewModel & Compose UI
└── di/              # Hilt modules
```

## Fitur

- Papan 8×8 interaktif (tap-to-move)
- Highlight gerakan legal
- Validasi lengkap semua bidak + aturan tidak boleh skak sendiri
- Castling, en passant, promosi pion
- Deteksi skak, skak mat, stalemate
- Indikator giliran Putih/Hitam
- Undo multi-langkah via history GameState
- Riwayat langkah notasi algebraik (LazyColumn)
- New Game
"# catur" 
"# Catur" 
