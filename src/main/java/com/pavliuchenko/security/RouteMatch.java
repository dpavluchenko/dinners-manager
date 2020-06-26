package com.pavliuchenko.security;

public class RouteMatch {

    private final String pattern;
    private final boolean isMultipleRoute;

    public RouteMatch(String pattern) {
        this.isMultipleRoute = pattern.endsWith("*");
        this.pattern = !isMultipleRoute ? pattern : pattern.replaceFirst("\\*", "");
    }

    public boolean matches(String pattern) {
       return isMultipleRoute ? checkForMultiplePattern(pattern) : checkForSinglePattern(pattern);
    }

    private boolean checkForMultiplePattern(String pattern) {
       return pattern.startsWith(this.pattern);
    }

    private boolean checkForSinglePattern(String pattern) {
        return this.pattern.equals(pattern);
    }

}
