package app.revanced.patches.reddit.layout.flairfilter

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.reddit.layout.ads.fingerprints.PostListProcessingFingerprint
import app.revanced.patches.reddit.utils.patch.PatchList.FILTER_POSTS_BY_FLAIR
import app.revanced.patches.reddit.utils.settings.addPatchPreference
import app.revanced.patches.reddit.utils.settings.settingsStatus
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Patch(
    name = FILTER_POSTS_BY_FLAIR.title,
    description = FILTER_POSTS_BY_FLAIR.summary,
    compatiblePackages = [
        CompatiblePackage("com.reddit.frontpage", ["2024.17.0", "2025.05.1", "2025.12.1"])
    ]
)
@Suppress("unused")
object FlairFilterPatch : BytecodePatch(
    setOf(PostListProcessingFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        PostListProcessingFingerprint.result?.let { result ->
            result.mutableMethod.apply {
                val insertIndex = result.scanResult.patternScanResult!!.endIndex
                val listRegister = getInstruction<OneRegisterInstruction>(insertIndex - 1).registerA
                
                addInstructions(
                    insertIndex, """
                        invoke-static {v$listRegister}, Lapp/revanced/extension/reddit/patches/FlairFilterPatch;->filterPostsByFlair(Ljava/util/List;)Ljava/util/List;
                        move-result-object v$listRegister
                    """
                )
            }
        } ?: throw PostListProcessingFingerprint.exception

        settingsStatus = settingsStatus.addPatchPreference(FILTER_POSTS_BY_FLAIR)
    }
}