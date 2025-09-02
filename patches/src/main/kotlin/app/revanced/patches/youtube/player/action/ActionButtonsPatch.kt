package app.revanced.patches.youtube.player.action

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patches.shared.litho.addLithoFilter
import app.revanced.patches.shared.litho.lithoFilterPatch
import app.revanced.patches.youtube.utils.auth.authHookPatch
import app.revanced.patches.youtube.utils.compatibility.Constants.COMPATIBLE_PACKAGE
import app.revanced.patches.youtube.utils.componentlist.hookElementList
import app.revanced.patches.youtube.utils.componentlist.lazilyConvertedElementHookPatch
import app.revanced.patches.youtube.utils.extension.Constants.COMPONENTS_PATH
import app.revanced.patches.youtube.utils.extension.Constants.PLAYER_PATH
import app.revanced.patches.youtube.utils.fix.hype.hypeButtonIconPatch
import app.revanced.patches.youtube.utils.fix.litho.lithoLayoutPatch
import app.revanced.patches.youtube.utils.patch.PatchList.HIDE_ACTION_BUTTONS
import app.revanced.patches.youtube.utils.settings.ResourceUtils.addPreference
import app.revanced.patches.youtube.utils.settings.settingsPatch
import app.revanced.patches.youtube.video.information.videoInformationPatch
import app.revanced.patches.youtube.video.videoid.hookPlayerResponseVideoId
import app.revanced.patches.youtube.video.videoid.videoIdPatch

private const val FILTER_CLASS_DESCRIPTOR =
    "$COMPONENTS_PATH/ActionButtonsFilter;"
private const val ACTION_BUTTONS_CLASS_DESCRIPTOR =
    "$PLAYER_PATH/ActionButtonsPatch;"

@Suppress("unused")
val actionButtonsPatch = bytecodePatch(
    HIDE_ACTION_BUTTONS.title,
    HIDE_ACTION_BUTTONS.summary,
) {
    compatibleWith(COMPATIBLE_PACKAGE)

    dependsOn(
        settingsPatch,
        lithoFilterPatch,
        lithoLayoutPatch,
        lazilyConvertedElementHookPatch,
        videoInformationPatch,
        videoIdPatch,
        authHookPatch,
        hypeButtonIconPatch,
    )

    execute {
        addLithoFilter(FILTER_CLASS_DESCRIPTOR)

        // region patch for hide action buttons by index

        hookPlayerResponseVideoId("$ACTION_BUTTONS_CLASS_DESCRIPTOR->fetchRequest(Ljava/lang/String;Z)V")
        hookElementList("$ACTION_BUTTONS_CLASS_DESCRIPTOR->hideActionButtonByIndex")

        // endregion

        // region add settings

        addPreference(
            arrayOf(
                "PREFERENCE_SCREEN: PLAYER",
                "SETTINGS: HIDE_ACTION_BUTTONS"
            ),
            HIDE_ACTION_BUTTONS
        )

        // endregion

    }
}
