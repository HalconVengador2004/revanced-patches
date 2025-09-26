package app.revanced.extension.reddit.patches;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.revanced.extension.reddit.settings.Settings;

@SuppressWarnings("unused")
public class FlairFilterPatch {
    
    public static List<?> filterPostsByFlair(List<?> posts) {
        if (!Settings.FILTER_POSTS_BY_FLAIR.get()) {
            return posts;
        }
        
        String flairListStr = Settings.FILTER_POSTS_BY_FLAIR_LIST.get();
        if (flairListStr == null || flairListStr.trim().isEmpty()) {
            return posts;
        }
        
        // Parse comma-separated flair list
        List<String> flairsToFilter = Arrays.asList(flairListStr.split(","));
        for (int i = 0; i < flairsToFilter.size(); i++) {
            flairsToFilter.set(i, flairsToFilter.get(i).trim().toLowerCase());
        }
        
        List<Object> filteredPosts = new ArrayList<>();
        
        for (Object post : posts) {
            String flairText = extractFlairText(post);
            
            // If no flair text found, keep the post
            if (flairText == null || flairText.isEmpty()) {
                filteredPosts.add(post);
                continue;
            }
            
            // Check if post flair matches any filtered flair
            boolean shouldFilter = false;
            String flairLower = flairText.toLowerCase();
            
            for (String filterFlair : flairsToFilter) {
                if (flairLower.contains(filterFlair)) {
                    shouldFilter = true;
                    break;
                }
            }
            
            // Only add post if it shouldn't be filtered
            if (!shouldFilter) {
                filteredPosts.add(post);
            }
        }
        
        return filteredPosts;
    }
    
    private static String extractFlairText(Object post) {
        if (post == null) return null;
        
        try {
            // Try common flair-related method names using reflection
            String[] possibleMethods = {
                "getFlairText", "getFlair", "getPostFlair", 
                "getLinkFlairText", "getAuthorFlairText"
            };
            
            Class<?> postClass = post.getClass();
            
            for (String methodName : possibleMethods) {
                try {
                    Method method = postClass.getMethod(methodName);
                    Object result = method.invoke(post);
                    if (result instanceof String) {
                        return (String) result;
                    }
                } catch (Exception ignored) {
                    // Try next method
                }
            }
            
            // Try to find any method that returns flair-like data
            Method[] methods = postClass.getMethods();
            for (Method method : methods) {
                String methodName = method.getName().toLowerCase();
                if (methodName.contains("flair") && 
                    method.getParameterCount() == 0 && 
                    method.getReturnType() == String.class) {
                    try {
                        Object result = method.invoke(post);
                        if (result instanceof String) {
                            return (String) result;
                        }
                    } catch (Exception ignored) {
                        // Continue trying other methods
                    }
                }
            }
            
        } catch (Exception e) {
            // If reflection fails, return null
        }
        
        return null;
    }
}