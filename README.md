# java11-StringJoiner
Showcase of `StringJoiner` API.

# preface
Please refer my other github project about joining Strings 
(using Collectors.joining or String.join): 
[Collectors.joining](https://github.com/mtumilowicz/java11-collectors-joining).

* constructors
    ```
    public StringJoiner(CharSequence delimiter)
    
    public StringJoiner(CharSequence delimiter,
                            CharSequence prefix,
                            CharSequence suffix)
    ```
* methods
    * `public StringJoiner setEmptyValue(CharSequence emptyValue)` - 
    Sets the sequence of characters to be used when determining the string
    representation of this `StringJoiner` and no elements have been
    added yet, that is, when it is empty. **Note that once an add method 
    has been called, the `StringJoiner` is no longer considered empty, 
    even if the element(s) added correspond to the empty `String`.**
    * `public StringJoiner add(CharSequence newElement)`
    * `public StringJoiner merge(StringJoiner other)` - 
    Merge adds the contents of the given `StringJoiner`
    without prefix and suffix as the next element if it is non-empty. 
    If the given `StringJoiner` is empty, the call has no effect.
    
# project description
We provide tests for mentioned above methods:
* "a", "b", "c" -> "a,b,c"
    ```
    var stringJoiner = new StringJoiner(",");
        
    List.of("a", "b", "c").forEach(stringJoiner::add);    
    ```
* "a", "b", "c" -> "prefix-a,b,c-suffix"
    ```
    var stringJoiner = new StringJoiner(",", "prefix-", "-suffix");

    List.of("a", "b", "c").forEach(stringJoiner::add);    
    ```
* merge two different `StringJoiners`
    ```
    var stringJoinerComma = new StringJoiner(",");
    List.of("a", "b", "c").forEach(stringJoinerComma::add);
    
    var stringJoinerDot = new StringJoiner(".");
    List.of("d", "e", "f").forEach(stringJoinerDot::add);
    
    var merged = stringJoinerComma.merge(stringJoinerDot);
    
    assertThat(merged.toString(), is("a,b,c,d.e.f"));
    ```
* empty `StringJoiner` with / without prefix and suffix
    ```
    var stringJoiner = new StringJoiner(",");
        
    assertThat(stringJoiner.toString(), is(""));    
    ```
    ```
    var stringJoiner = new StringJoiner(",", "prefix-", "suffix");
    
    assertThat(stringJoiner.toString(), is("prefix-suffix"));
    ```
* configure empty `StringJoiner` - `setEmptyValues`
    ```
    var stringJoiner = new StringJoiner(",");
    stringJoiner.setEmptyValue("empty");
            
    assertThat(stringJoiner.toString(), is("empty"));
    ```
    ```
    var stringJoiner = new StringJoiner(",", "prefix-", "-suffix");
    stringJoiner.setEmptyValue("empty");
    
    assertThat(stringJoiner.toString(), is("empty"));
    ```
# remark
`Collectors.joining(...)` and `String.join(...)` are using internally
`StringJoiner` (or `StringBuilder` in the easiest case):
* **Collectors**
    ```
    public static Collector<CharSequence, ?, String> joining() {
        return new CollectorImpl<CharSequence, StringBuilder, String>(
                StringBuilder::new, StringBuilder::append,
                (r1, r2) -> { r1.append(r2); return r1; },
                StringBuilder::toString, CH_NOID);
    }
    ```
    ```
    public static Collector<CharSequence, ?, String> joining(CharSequence delimiter,
                                                             CharSequence prefix,
                                                             CharSequence suffix) {
        return new CollectorImpl<>(
                () -> new StringJoiner(delimiter, prefix, suffix),
                StringJoiner::add, StringJoiner::merge,
                StringJoiner::toString, CH_NOID);
    }
    ```
* **String**
    ```
    public static String join(CharSequence delimiter, CharSequence... elements) {
            Objects.requireNonNull(delimiter);
            Objects.requireNonNull(elements);
            // Number of elements not likely worth Arrays.stream overhead.
            StringJoiner joiner = new StringJoiner(delimiter);
            for (CharSequence cs: elements) {
                joiner.add(cs);
            }
            return joiner.toString();
        }
    ```