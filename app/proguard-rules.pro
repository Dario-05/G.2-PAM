# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the AGP default rules file.
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}
