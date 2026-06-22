# Keep Retrofit interfaces
-keep,allowobfuscation interface com.patronymic.generator.data.PatronymicApi

# Keep Gson serialized classes
-keep class com.patronymic.generator.data.PatronymicResult { *; }
-keep class com.patronymic.generator.data.NamesResponse { *; }

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
