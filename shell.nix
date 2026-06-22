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
    # AAPT2 на NixOS требует эти библиотеки
    libcxx
    zlib
  ];

  shellHook = ''
    export ANDROID_HOME="${androidComposition.androidsdk}/libexec/android-sdk"
    export ANDROID_SDK_ROOT="$ANDROID_HOME"
    export JAVA_HOME="${pkgs.jdk17.home}"

    # AAPT2 daemon на NixOS не находит библиотеки без LD_LIBRARY_PATH
    export LD_LIBRARY_PATH="${pkgs.libcxx}/lib:${pkgs.zlib}/lib:${pkgs.stdenv.cc.cc.lib}/lib"

    echo "✅ Nix shell ready!"
    echo "   ANDROID_HOME = $ANDROID_HOME"
    echo "   JAVA_HOME    = $JAVA_HOME"
    echo ""
    echo "Run:"
    echo "  cd android"
    echo "  ./gradlew assembleDebug"
  '';
}
