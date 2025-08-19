package app.revanced.patches.music.general.splash

import app.revanced.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.music.utils.compatibility.Constants.YOUTUBE_MUSIC_PACKAGE_NAME
import app.revanced.patches.music.utils.extension.Constants.GENERAL_PATH
import app.revanced.patches.music.utils.patch.PatchList.DISABLE_CAIRO_SPLASH_ANIMATION
import app.revanced.patches.music.utils.playservice.is_7_06_or_greater
import app.revanced.patches.music.utils.playservice.is_7_20_or_greater
import app.revanced.patches.music.utils.playservice.versionCheckPatch
import app.revanced.patches.music.utils.resourceid.mainActivityLaunchAnimation
import app.revanced.patches.music.utils.resourceid.sharedResourceIdPatch
import app.revanced.patches.music.utils.settings.CategoryType
import app.revanced.patches.music.utils.settings.ResourceUtils.updatePatchStatus
import app.revanced.patches.music.utils.settings.addSwitchPreference
import app.revanced.patches.music.utils.settings.settingsPatch
import app.revanced.util.Utils.printWarn
import app.revanced.util.fingerprint.injectLiteralInstructionBooleanCall
import app.revanced.util.fingerprint.methodOrThrow
import app.revanced.util.getReference
import app.revanced.util.indexOfFirstInstructionOrThrow
import app.revanced.util.indexOfFirstInstructionReversedOrThrow
import app.revanced.util.indexOfFirstLiteralInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private const val EXTENSION_METHOD_DESCRIPTOR =
    "$GENERAL_PATH/CairoSplashAnimationPatch;->disableCairoSplashAnimation(Z)Z"

@Suppress("unused")
val cairoSplashAnimationPatch = bytecodePatch(
    DISABLE_CAIRO_SPLASH_ANIMATION.title,
    DISABLE_CAIRO_SPLASH_ANIMATION.summary,
) {
    compatibleWith(
        YOUTUBE_MUSIC_PACKAGE_NAME(
            "7.06.54",
            "7.16.53",
            "7.25.53",
            "8.05.51",
            "8.12.53",
        ),
    )

    dependsOn(
        settingsPatch,
        sharedResourceIdPatch,
        versionCheckPatch,
    )

    execute {
        if (!is_7_06_or_greater) {
            printWarn("\"${DISABLE_CAIRO_SPLASH_ANIMATION.title}\" is not supported in this version. Use YouTube Music 7.06.54 or later.")
            return@execute
        } else if (!is_7_20_or_greater) {
            cairoSplashAnimationConfigFingerprint.injectLiteralInstructionBooleanCall(
                CAIRO_SPLASH_ANIMATION_FEATURE_FLAG,
                EXTENSION_METHOD_DESCRIPTOR
            )
        } else {
            cairoSplashAnimationConfigFingerprint.methodOrThrow().apply {
                val literalIndex = indexOfFirstLiteralInstructionOrThrow(
                    mainActivityLaunchAnimation
                )
                val insertIndex = indexOfFirstInstructionReversedOrThrow(literalIndex) {
                    opcode == Opcode.INVOKE_VIRTUAL &&
                            getReference<MethodReference>()?.name == "setContentView"
                } + 1
                val freeIndex = indexOfFirstInstructionOrThrow(insertIndex, Opcode.CONST)
                val freeRegister =
                    getInstruction<OneRegisterInstruction>(freeIndex).registerA
                val jumpIndex = indexOfFirstInstructionOrThrow(insertIndex) {
                    opcode == Opcode.INVOKE_VIRTUAL &&
                            getReference<MethodReference>()?.parameterTypes?.firstOrNull() == "Ljava/lang/Runnable;"
                } + 1

                val moveInstructionIndex = indexOfFirstInstructionOrThrow(jumpIndex) {
                    opcode == Opcode.MOVE_FROM16
                }
                val uninitializedRegister =
                    getInstruction<TwoRegisterInstruction>(moveInstructionIndex).registerB

                addInstructionsWithLabels(
                    insertIndex, """
                        const/4 v$uninitializedRegister, 0x0        
                        const/4 v$freeRegister, 0x1
                        invoke-static {v$freeRegister}, $EXTENSION_METHOD_DESCRIPTOR
                        move-result v$freeRegister
                        if-eqz v$freeRegister, :skip
                    """, ExternalLabel("skip", getInstruction(jumpIndex))
                )
            }
        }

        addSwitchPreference(
            CategoryType.GENERAL,
            "revanced_disable_cairo_splash_animation",
            "false"
        )

        updatePatchStatus(DISABLE_CAIRO_SPLASH_ANIMATION)

    }
}
