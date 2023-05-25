package me.msile.app.androidapp.common.utils;

import android.os.Build;
import android.text.Html;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 加码/解码工具类
 */
public final class EncodeUtils {

    private EncodeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the urlencoded string.
     *
     * @param input The input.
     * @return the urlencoded string
     */
    public static String urlEncode(final String input) {
        return urlEncode(input, "UTF-8");
    }

    /**
     * Return the urlencoded string.
     *
     * @param input       The input.
     * @param charsetName The name of charset.
     * @return the urlencoded string
     */
    public static String urlEncode(final String input, final String charsetName) {
        if (input == null || input.length() == 0) return "";
        try {
            return URLEncoder.encode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Return the string of decode urlencoded string.
     *
     * @param input The input.
     * @return the string of decode urlencoded string
     */
    public static String urlDecode(final String input) {
        return urlDecode(input, "UTF-8");
    }

    /**
     * Return the string of decode urlencoded string.
     *
     * @param input       The input.
     * @param charsetName The name of charset.
     * @return the string of decode urlencoded string
     */
    public static String urlDecode(final String input, final String charsetName) {
        if (input == null || input.length() == 0) return "";
        try {
            String safeInput = input.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B");
            return URLDecoder.decode(safeInput, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String base64Encode2String(String input) {
        return base64Encode2String(input, Base64.DEFAULT);
    }

    /**
     * Return Base64-encode string.
     *
     * @param input The input.
     * @return Base64-encode string
     */
    public static String base64Encode2String(String input, int flags) {
        if (input == null) {
            return "";
        }
        return Base64.encodeToString(input.getBytes(), flags);
    }

    public static String base64Decode2String(String input) {
        return base64Decode2String(input, Base64.DEFAULT);
    }

    /**
     * Return Base64-decode string.
     *
     * @param input The input.
     * @return Base64-decode string
     */
    public static String base64Decode2String(String input, int flags) {
        if (input == null) {
            return "";
        }
        return new String(Base64.decode(input, flags));
    }

    /**
     * Return html-encode string.
     *
     * @param input The input.
     * @return html-encode string
     */
    public static String htmlEncode(final CharSequence input) {
        if (input == null || input.length() == 0) return "";
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0, len = input.length(); i < len; i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;"); //$NON-NLS-1$
                    break;
                case '>':
                    sb.append("&gt;"); //$NON-NLS-1$
                    break;
                case '&':
                    sb.append("&amp;"); //$NON-NLS-1$
                    break;
                case '\'':
                    //http://www.w3.org/TR/xhtml1
                    // The named character reference &apos; (the apostrophe, U+0027) was
                    // introduced in XML 1.0 but does not appear in HTML. Authors should
                    // therefore use &#39; instead of &apos; to work as expected in HTML 4
                    // user agents.
                    sb.append("&#39;"); //$NON-NLS-1$
                    break;
                case '"':
                    sb.append("&quot;"); //$NON-NLS-1$
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Return the string of decode html-encode string.
     *
     * @param input The input.
     * @return the string of decode html-encode string
     */
    public static CharSequence htmlDecode(final String input) {
        if (input == null || input.length() == 0) return "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }

    /**
     * Return the binary encoded string padded with one space
     *
     * @param input The input.
     * @return binary string
     */
    public static String binaryEncode(final String input) {
        if (input == null || input.length() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (char i : input.toCharArray()) {
            sb.append(Integer.toBinaryString(i)).append(" ");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * Return UTF-8 String from binary
     *
     * @param input binary string
     * @return UTF-8 String
     */
    public static String binaryDecode(final String input) {
        if (input == null || input.length() == 0) return "";
        String[] splits = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String split : splits) {
            sb.append(((char) Integer.parseInt(split, 2)));
        }
        return sb.toString();
    }
}
