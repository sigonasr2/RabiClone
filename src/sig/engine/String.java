package sig.engine;

import java.util.regex.Pattern;

public class String{
    private StringBuilder sb;
    private Point bounds = new Point(0,1);
    private int currentLineWidth;
    public String() {
        this.sb=new StringBuilder();
    }
    public String(java.lang.String str) {
        this.sb=new StringBuilder(str);
        updateBounds(str);
    }
    public String(Object obj) {
        this.sb=new StringBuilder(obj.toString());
        updateBounds(obj.toString());
    }
    public String(double d) {
        this.sb=new StringBuilder(Double.toString(d));
        updateBounds(Double.toString(d));
    }
    public String append(char c) {
        this.sb=new StringBuilder(c);
        updateBounds(Character.toString(c));
        return this;
    }
    public String append(java.lang.Object...obj) {
        for (java.lang.Object o : obj) {
            this.sb.append(o.toString());
            updateBounds(o.toString());
        }
        return this;
    }

    public StringBuilder getBuilder() {
        return this.sb;
    }
    
    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring.
     *
     * <p>The returned index is the smallest value {@code k} for which:
     * <pre>{@code
     * this.toString().startsWith(str, k)
     * }</pre>
     * If no such value of {@code k} exists, then {@code -1} is returned.
     *
     * @param   str   the substring to search for.
     * @return  the index of the first occurrence of the specified substring,
     *          or {@code -1} if there is no such occurrence.
     */
    public int indexOf(java.lang.String str) {
        return this.sb.indexOf(str);
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring, starting at the specified index.
     *
     * <p>The returned index is the smallest value {@code k} for which:
     * <pre>{@code
     *     k >= Math.min(fromIndex, this.length()) &&
     *                   this.toString().startsWith(str, k)
     * }</pre>
     * If no such value of {@code k} exists, then {@code -1} is returned.
     *
     * @param   str         the substring to search for.
     * @param   fromIndex   the index from which to start the search.
     * @return  the index of the first occurrence of the specified substring,
     *          starting at the specified index,
     *          or {@code -1} if there is no such occurrence.
     */
    public int indexOf(java.lang.String str,int fromIndex) {
        return this.sb.indexOf(str,fromIndex);
    }
    /**
     * Replaces the characters in a substring of this sequence
     * with characters in the specified {@code String}. The substring
     * begins at the specified {@code start} and extends to the character
     * at index {@code end - 1} or to the end of the
     * sequence if no such character exists. First the
     * characters in the substring are removed and then the specified
     * {@code String} is inserted at {@code start}. (This
     * sequence will be lengthened to accommodate the
     * specified String if necessary.)
     *
     * @param      start    The beginning index, inclusive.
     * @param      end      The ending index, exclusive.
     * @param      str   String that will replace previous contents.
     * @return     This object.
     * @throws     StringIndexOutOfBoundsException  if {@code start}
     *             is negative, greater than {@code length()}, or
     *             greater than {@code end}.
     */
    public String replace(int start,int end,java.lang.String str) {
        this.sb.replace(start,end,str);
        bounds = new Point(0,1);
        updateBounds(this.sb.toString());
        return this;
    }
    public int length() {
        return this.sb.length();
    }
    public java.lang.String toString() {
        return this.sb.toString();
    }
    public Point getBounds(Font f) {
        return new Point(bounds.x*f.getGlyphWidth(),length()>0?Math.max(bounds.y*f.getGlyphHeight(),f.getGlyphHeight()):0);
    }
    private void updateBounds(java.lang.String string) {
        for (int i=0;i<string.length();i++) {
            if (string.charAt(i)=='\n') {
                bounds.x=i+1>bounds.x?i+1:bounds.x;
                bounds.y++;
                currentLineWidth=0;
            } else 
            if (string.charAt(i)==(char)26&&i<string.length()-1) {
                byte nextCol=Byte.parseByte(string.substring(i+1, string.indexOf(' ',i+1)));
                string=string.replaceFirst(Pattern.quote(Character.valueOf((char)26)+Byte.toString(nextCol)+" "),"");
                i--;
            } else {
                currentLineWidth++;
                bounds.x=currentLineWidth>bounds.x?currentLineWidth:bounds.x;
            }
        }
    }
}
