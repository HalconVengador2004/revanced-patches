package app.revanced.extension.shared.patches.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Filters litho based components.
 * <p>
 * Callbacks to filter content are added using {@link #addIdentifierCallbacks(StringFilterGroup...)}
 * and {@link #addPathCallbacks(StringFilterGroup...)}.
 * <p>
 * To filter {@link FilterContentType#PROTOBUFFER}, first add a callback to
 * either an identifier or a path.
 * Then inside {@link #isFiltered(String, String, String, byte[], StringFilterGroup, FilterContentType, int)}
 * search for the buffer content using either a {@link ByteArrayFilterGroup} (if searching for 1 pattern)
 * or a {@link ByteArrayFilterGroupList} (if searching for more than 1 pattern).
 * <p>
 * All callbacks must be registered before the constructor completes.
 */
@SuppressWarnings("unused")
public abstract class Filter {

    public enum FilterContentType {
        IDENTIFIER,
        PATH,
        ALLVALUE,
        PROTOBUFFER
    }

    /**
     * Identifier callbacks.  Do not add to this instance,
     * and instead use {@link #addIdentifierCallbacks(StringFilterGroup...)}.
     */
    protected final List<StringFilterGroup> identifierCallbacks = new ArrayList<>();
    /**
     * Path callbacks. Do not add to this instance,
     * and instead use {@link #addPathCallbacks(StringFilterGroup...)}.
     */
    protected final List<StringFilterGroup> pathCallbacks = new ArrayList<>();
    /**
     * Path callbacks. Do not add to this instance,
     * and instead use {@link #addAllValueCallbacks(StringFilterGroup...)}.
     */
    protected final List<StringFilterGroup> allValueCallbacks = new ArrayList<>();

    /**
     * Adds callbacks to {@link #isFiltered(String, String, String, byte[], StringFilterGroup, FilterContentType, int)}
     * if any of the groups are found.
     */
    protected final void addIdentifierCallbacks(StringFilterGroup... groups) {
        identifierCallbacks.addAll(Arrays.asList(groups));
    }

    /**
     * Adds callbacks to {@link #isFiltered(String, String, String, byte[], StringFilterGroup, FilterContentType, int)}
     * if any of the groups are found.
     */
    protected final void addPathCallbacks(StringFilterGroup... groups) {
        pathCallbacks.addAll(Arrays.asList(groups));
    }

    /**
     * Adds callbacks to {@link #isFiltered(String, String, String, byte[], StringFilterGroup, FilterContentType, int)}
     * if any of the groups are found.
     */
    protected final void addAllValueCallbacks(StringFilterGroup... groups) {
        allValueCallbacks.addAll(Arrays.asList(groups));
    }

    /**
     * Called after an enabled filter has been matched.
     * Default implementation is to always filter the matched component and log the action.
     * Subclasses can perform additional or different checks if needed.
     * <p>
     * Method is called off the main thread.
     *
     * @param matchedGroup The actual filter that matched.
     * @param contentType  The type of content matched.
     * @param contentIndex Matched index of the identifier or path.
     * @return True if the litho component should be filtered out.
     */
    public boolean isFiltered(String path, String identifier, String allValue, byte[] buffer,
                              StringFilterGroup matchedGroup, FilterContentType contentType, int contentIndex) {
        return true;
    }
}
