package dev.dworks.libs.widget;

import java.util.Arrays;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateUtils;
import android.widget.GridView;

public class Utils {

    public static final long KB_IN_BYTES = 1024;
    public static final long MB_IN_BYTES = KB_IN_BYTES * 1024;
    public static final long GB_IN_BYTES = MB_IN_BYTES * 1024;

    
	private static final StringBuilder sBuilder = new StringBuilder(50);
	private static final java.util.Formatter sFormatter = new java.util.Formatter(
	            sBuilder, Locale.getDefault());

	public static String formatDateRange(Context context, long start, long end) {
		final int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH;

		synchronized (sBuilder) {
			sBuilder.setLength(0);
			return DateUtils.formatDateRange(context, sFormatter, start, end,
					flags, null).toString();
		}
	}

	public static class Preconditions {

	    /**
	     * Ensures that an object reference passed as a parameter to the calling
	     * method is not null.
	     *
	     * @param reference an object reference
	     * @return the non-null reference that was validated
	     * @throws NullPointerException if {@code reference} is null
	     */
	    public static <T> T checkNotNull(T reference) {
	        if (reference == null) {
	            throw new NullPointerException();
	        }
	        return reference;
	    }

	    /**
	     * Ensures that an object reference passed as a parameter to the calling
	     * method is not null.
	     *
	     * @param reference an object reference
	     * @param errorMessage the exception message to use if the check fails; will
	     *     be converted to a string using {@link String#valueOf(Object)}
	     * @return the non-null reference that was validated
	     * @throws NullPointerException if {@code reference} is null
	     */
	    public static <T> T checkNotNull(T reference, Object errorMessage) {
	        if (reference == null) {
	            throw new NullPointerException(String.valueOf(errorMessage));
	        }
	        return reference;
	    }
	}

	/**
	 * Object utility methods.
	 */
	public static class Objects {

	    /**
	     * Determines whether two possibly-null objects are equal. Returns:
	     *
	     * <ul>
	     * <li>{@code true} if {@code a} and {@code b} are both null.
	     * <li>{@code true} if {@code a} and {@code b} are both non-null and they are
	     *     equal according to {@link Object#equals(Object)}.
	     * <li>{@code false} in all other situations.
	     * </ul>
	     *
	     * <p>This assumes that any non-null objects passed to this function conform
	     * to the {@code equals()} contract.
	     */
	    public static boolean equal(Object a, Object b) {
	        return a == b || (a != null && a.equals(b));
	    }

	    /**
	     * Generates a hash code for multiple values. The hash code is generated by
	     * calling {@link Arrays#hashCode(Object[])}.
	     *
	     * <p>This is useful for implementing {@link Object#hashCode()}. For example,
	     * in an object that has three properties, {@code x}, {@code y}, and
	     * {@code z}, one could write:
	     * <pre>
	     * public int hashCode() {
	     *   return Objects.hashCode(getX(), getY(), getZ());
	     * }</pre>
	     *
	     * <b>Warning</b>: When a single object is supplied, the returned hash code
	     * does not equal the hash code of that object.
	     */
	    public static int hashCode(Object... objects) {
	        return Arrays.hashCode(objects);
	    }

	}
}
