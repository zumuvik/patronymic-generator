{ pkgs ? import <nixpkgs> {
  config = {
    allowUnfree = true;
    android_sdk.accept_license = true;
  };
} }:

let
  androidComposition = pkgs.androidenv.composeAndroidPackages {
    toolsVersion = "latest";
    platformToolsVersion = "latest";
    buildToolsVersions = [ "34.0.0" ];
    platformVersions = [ "34" ];
    cmakeVersions = [ ];
    includeEmulator = false;
    includeSystemImages = false;
    includeSources = false;
  };
in
pkgs.mkShell {
  name = "patronymic-android-build";

  buildInputs = with pkgs; [
    jdk17
    gradle
    androidComposition.androidsdk
    libcxx
    zlib
    stdenv.cc.cc.lib  # libstdc++
  ];

  shellHook = ''
    export ANDROID_HOME="${androidComposition.androidsdk}/libexec/android-sdk"
    export ANDROID_SDK_ROOT="$ANDROID_HOME"
    export JAVA_HOME="${pkgs.jdk17.home}"

    # AAPT2 на NixOS: все библиотеки в нестандартных путях
    export LD_LIBRARY_PATH="${pkgs.libcxx}/lib:${pkgs.zlib}/lib:${pkgs.stdenv.cc.cc.lib}/lib"

    # Заставляем AGP использовать SDK-шный AAPT2 вместо своего (пропатчен под NixOS)
    export ORG_GRADLE_PROJECT_android_aapt2FromMavenOverride="$ANDROID_HOME/build-tools/34.0.0/aapt2"

    # Проверка AAPT2
    AAPT2="$ANDROID_HOME/build-tools/34.0.0/aapt2"
    if [ -f "$AAPT2" ]; then
      echo "🔍 Проверка AAPT2..."
      "$AAPT2" version 2>/dev/null && echo "   ✅ AAPT2 работает" || echo "   ⚠️ AAPT2 не запускается"
    else
      echo "⚠️ AAPT2 не найден в $AAPT2"
    fi

    echo ""
    echo "✅ Nix shell ready!"
    echo "   ANDROID_HOME = $ANDROID_HOME"
    echo "   JAVA_HOME    = $JAVA_HOME"
    echo ""
    echo "Run: cd android && ./gradlew assembleDebug -Pandroid.aapt2FromMavenOverride=\$ANDROID_HOME/build-tools/34.0.0/aapt2"
  '';
}
