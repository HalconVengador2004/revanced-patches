package app.revanced.extension.reddit.settings;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import app.revanced.extension.shared.settings.BaseSettings;
import app.revanced.extension.shared.settings.BooleanSetting;
import app.revanced.extension.shared.settings.StringSetting;

public class Settings extends BaseSettings {
    // Ads
    public static final BooleanSetting HIDE_COMMENT_ADS = new BooleanSetting("revanced_hide_comment_ads", TRUE, true);
    public static final BooleanSetting HIDE_OLD_POST_ADS = new BooleanSetting("revanced_hide_old_post_ads", TRUE, true);
    public static final BooleanSetting HIDE_NEW_POST_ADS = new BooleanSetting("revanced_hide_new_post_ads", TRUE, true);

    // Layout
    public static final BooleanSetting DISABLE_SCREENSHOT_POPUP = new BooleanSetting("revanced_disable_screenshot_popup", TRUE, true);
    public static final BooleanSetting FILTER_POSTS_BY_FLAIR = new BooleanSetting("revanced_filter_posts_by_flair", FALSE, true);
    public static final StringSetting FILTER_POSTS_BY_FLAIR_LIST = new StringSetting("revanced_filter_posts_by_flair_list", "");
    public static final BooleanSetting HIDE_CHAT_BUTTON = new BooleanSetting("revanced_hide_chat_button", FALSE, true);
    public static final BooleanSetting HIDE_CREATE_BUTTON = new BooleanSetting("revanced_hide_create_button", FALSE, true);
    public static final BooleanSetting HIDE_DISCOVER_BUTTON = new BooleanSetting("revanced_hide_discover_button", FALSE, true);
    public static final BooleanSetting HIDE_RECENTLY_VISITED_SHELF = new BooleanSetting("revanced_hide_recently_visited_shelf", FALSE);
    public static final BooleanSetting HIDE_RECOMMENDED_COMMUNITIES_SHELF = new BooleanSetting("revanced_hide_recommended_communities_shelf", FALSE, true);
    public static final BooleanSetting HIDE_TOOLBAR_BUTTON = new BooleanSetting("revanced_hide_toolbar_button", FALSE, true);
    public static final BooleanSetting HIDE_TRENDING_TODAY_SHELF = new BooleanSetting("revanced_hide_trending_today_shelf", FALSE, true);
    public static final BooleanSetting REMOVE_NSFW_DIALOG = new BooleanSetting("revanced_remove_nsfw_dialog", FALSE, true);
    public static final BooleanSetting REMOVE_NOTIFICATION_DIALOG = new BooleanSetting("revanced_remove_notification_dialog", FALSE, true);

    // Miscellaneous
    public static final BooleanSetting OPEN_LINKS_DIRECTLY = new BooleanSetting("revanced_open_links_directly", TRUE);
    public static final BooleanSetting OPEN_LINKS_EXTERNALLY = new BooleanSetting("revanced_open_links_externally", TRUE);
    public static final BooleanSetting SANITIZE_URL_QUERY = new BooleanSetting("revanced_sanitize_url_query", TRUE);
}
