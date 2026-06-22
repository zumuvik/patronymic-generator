{ pkgs ? import <nixpkgs> {} }:

let
  # Android SDK platform and build tools
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
  ];

  shellHook = ''
    export ANDROID_HOME="${androidComposition.androidsdk}/libexec/android-sdk"
    export ANDROID_SDK_ROOT="$ANDROID_HOME"

    # Gradle needs to know where JAVA_HOME is
    export JAVA_HOME="${pkgs.jdk17.home}"

    echo "✅ Nix shell ready!"
    echo "   ANDROID_HOME = $ANDROID_HOME"
    echo "   JAVA_HOME    = $JAVA_HOME"
    echo ""
    echo "Run: cd android && gradle wrapper --gradle-version=8.7 && ./gradlew assembleDebug"
  '';
}
