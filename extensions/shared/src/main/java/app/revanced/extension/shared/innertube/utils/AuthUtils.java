package app.revanced.extension.shared.innertube.utils;

import androidx.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import app.revanced.extension.shared.utils.Logger;
import kotlin.Pair;

@SuppressWarnings("unused")
public class AuthUtils {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    // Used to identify brand accounts.
    private static final String PAGE_ID_HEADER = "X-Goog-PageId";
    private static final String VISITOR_ID_HEADER = "X-Goog-Visitor-Id";
    private static final String DATA_SYNC_ID_HEADER = "X-YouTube-DataSync-Id";
    private static final Map<String, String> REQUEST_HEADER = new LinkedHashMap<>(3);
    private static Pair<String, String> dataSyncIdPair = null;
    private static String authorization = "";
    private static String pageId = "";
    private static String visitorId = "";
    private static boolean incognitoStatus = false;

    /**
     * Injection point.
     */
    public static void setRequestHeaders(String url, Map<String, String> requestHeaders) {
        if (requestHeaders == null) {
            return;
        }
        String newlyLoadedAuthorization = requestHeaders.get(AUTHORIZATION_HEADER);
        String newlyLoadedVisitorId = requestHeaders.get(VISITOR_ID_HEADER);
        boolean authorizationNeedsUpdating = StringUtils.isNotEmpty(newlyLoadedAuthorization) && !authorization.equals(newlyLoadedAuthorization);
        boolean visitorIdNeedsUpdating = StringUtils.isNotEmpty(newlyLoadedVisitorId) && !visitorId.equals(newlyLoadedVisitorId);

        if (authorizationNeedsUpdating && visitorIdNeedsUpdating) {
            REQUEST_HEADER.put(AUTHORIZATION_HEADER, newlyLoadedAuthorization);
            authorization = newlyLoadedAuthorization;
            REQUEST_HEADER.put(VISITOR_ID_HEADER, newlyLoadedVisitorId);
            visitorId = newlyLoadedVisitorId;
            Logger.printDebug(() -> "new Authorization loaded: " + newlyLoadedAuthorization);
            Logger.printDebug(() -> "new VisitorId loaded: " + newlyLoadedVisitorId);
        }

        // dataSyncId must match with visitorId.
        String newlyLoadedDataSyncId = requestHeaders.get(DATA_SYNC_ID_HEADER);
        if (StringUtils.isNotEmpty(newlyLoadedDataSyncId) && StringUtils.isNotEmpty(newlyLoadedVisitorId)) {
            dataSyncIdPair = new Pair<>(newlyLoadedDataSyncId, newlyLoadedVisitorId);
        }
    }

    @Nullable
    public static Pair<String, String> getDataSyncIdPair() {
        return dataSyncIdPair;
    }


    /**
     * Injection point.
     */
    public static void setAccountIdentity(@Nullable String newlyLoadedPageId,
                                          boolean newlyLoadedIncognitoStatus) {
        if (StringUtils.isEmpty(newlyLoadedPageId)) {
            REQUEST_HEADER.remove(PAGE_ID_HEADER);
            pageId = "";
        } else if (!pageId.equals(newlyLoadedPageId)) {
            REQUEST_HEADER.put(PAGE_ID_HEADER, newlyLoadedPageId);
            pageId = newlyLoadedPageId;
            Logger.printDebug(() -> "new PageId loaded: " + newlyLoadedPageId);
        }
        incognitoStatus = newlyLoadedIncognitoStatus;
    }

    public static Map<String, String> getRequestHeader() {
        return REQUEST_HEADER;
    }

    public static boolean isNotLoggedIn() {
        return authorization.isEmpty() || (pageId.isEmpty() && incognitoStatus);
    }

}
