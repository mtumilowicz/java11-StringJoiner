import org.junit.Test;

import java.util.List;
import java.util.StringJoiner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by mtumilowicz on 2018-11-07.
 */
public class StringJoinerTest {
    
    @Test
    public void commaSeparated() {
        var stringJoiner = new StringJoiner(",");
        
        List.of("a", "b", "c").forEach(stringJoiner::add);
        
        assertThat(stringJoiner.toString(), is("a,b,c"));
    }

    @Test
    public void commaSeparatedWithPrefixSuffix() {
        var stringJoiner = new StringJoiner(",", "prefix-", "-suffix");

        List.of("a", "b", "c").forEach(stringJoiner::add);

        assertThat(stringJoiner.toString(), is("prefix-a,b,c-suffix"));
    }
    
    @Test
    public void merge() {
        var stringJoinerComma = new StringJoiner(",");
        List.of("a", "b", "c").forEach(stringJoinerComma::add);
        
        var stringJoinerDot = new StringJoiner(".");
        List.of("d", "e", "f").forEach(stringJoinerDot::add);

        var merged = stringJoinerComma.merge(stringJoinerDot);
        
        assertThat(merged.toString(), is("a,b,c,d.e.f"));
    }
    
    @Test
    public void empty() {
        var stringJoiner = new StringJoiner(",");
        
        assertThat(stringJoiner.toString(), is(""));
    }

    @Test
    public void emptyPrefixSuffix() {
        var stringJoiner = new StringJoiner(",", "prefix-", "suffix");

        assertThat(stringJoiner.toString(), is("prefix-suffix"));
    }
    
    @Test
    public void setEmptyValues() {
        var stringJoiner = new StringJoiner(",");
        stringJoiner.setEmptyValue("empty");
        
        assertThat(stringJoiner.toString(), is("empty"));
        
    }

    @Test
    public void setEmptyValuesPrefixSuffix() {
        var stringJoiner = new StringJoiner(",", "prefix-", "-suffix");
        stringJoiner.setEmptyValue("empty");

        assertThat(stringJoiner.toString(), is("empty"));

    }
    
}
