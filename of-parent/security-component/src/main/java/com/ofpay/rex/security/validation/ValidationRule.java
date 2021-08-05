package com.ofpay.rex.security.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 检查器
 *
 * @author of546
 */
public class ValidationRule {
    protected List<Pattern> whitelistPatterns = new ArrayList<Pattern>();
    protected List<Pattern> blacklistPatterns = new ArrayList<Pattern>();
    private boolean allowNull = false;
    private int minLength = 0;
    private int maxLength = Integer.MAX_VALUE;
    private String typeName = null;

    public ValidationRule(String typeName) {
        setTypeName(typeName);
    }

    public String getTypeName() {
        return typeName;
    }

    public final void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }

    public void setMinimumLength(int length) {
        minLength = length;
    }

    public void setMaximumLength(int length) {
        maxLength = length;
    }

    public void addWhitelistPattern(Pattern p) {
        if (p == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        whitelistPatterns.add(p);
    }

    public void addBlacklistPattern(Pattern p) {
        if (p == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        blacklistPatterns.add(p);
    }

    String checkEmpty(String context, String input) throws ValidationException {
        if (input != null)
            return input;
        if (allowNull)
            return null;
        throw new ValidationException(context + ": Input required.");
    }

    String checkLength(String context, String input) throws ValidationException {
        if (input.length() < minLength) {
            throw new ValidationException(context + ": Invalid input. The minimum length of " + minLength + " characters was not met.");
        }

        if (input.length() > maxLength) {
            throw new ValidationException(context + ": Invalid input. The maximum length of " + maxLength + " characters was exceeded.");
        }

        return input;
    }

    String checkWhitelist(String context, String input) throws ValidationException {
        // check whitelist patterns
        for (Pattern p : whitelistPatterns) {
            if (!p.matcher(input).matches()) {
                throw new ValidationException(context + ": Invalid input. Please conform to regex " + p.pattern() + (maxLength == Integer.MAX_VALUE ? "" : " with a maximum length of " + maxLength));
            }
        }

        return input;
    }

    String checkBlacklist(String context, String input) throws ValidationException {
        // check blacklist patterns
        for (Pattern p : blacklistPatterns) {
            if (p.matcher(input).matches()) {
                throw new ValidationException(context + ": Invalid input. Dangerous input matching " + p.pattern() + " detected.");
            }
        }

        return input;
    }

    public String getValid(String context, String input) throws ValidationException {
        if (checkEmpty(context, input) == null)
            return null;

//        checkLength(context, input);

//        checkWhitelist(context, input);
//
//        checkBlacklist(context, input);

        // validation passed
        return input;
    }
}
