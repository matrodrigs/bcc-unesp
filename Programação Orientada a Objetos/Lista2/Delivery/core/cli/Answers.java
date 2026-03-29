package core.cli;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Answers {
    private final Map<String, Object> data;

    public Answers(Map<String, Object> data) {
        this.data = data;
    }

    public String getString(Object key) {
        return (String) data.get(key.toString());
    }

    public Integer getInteger(Object key) {
        return (Integer) data.get(key.toString());
    }

    public BigDecimal getDecimal(Object key) {
        return (BigDecimal) data.get(key.toString());
    }

    @SuppressWarnings("unchecked")
    public List<Answers> getList(Object key) {
        List<Map<String, Object>> raw = (List<Map<String, Object>>) data.get(key.toString());
        List<Answers> result = new ArrayList<>();
        for (Map<String, Object> item : raw) {
            result.add(new Answers(item));
        }
        return result;
    }

    public Map<String, Object> raw() {
        return data;
    }
}
