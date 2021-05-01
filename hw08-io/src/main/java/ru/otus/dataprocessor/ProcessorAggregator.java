package ru.otus.dataprocessor;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует значение value
        Map<String, Double> grouped = data.stream()
            .collect( Collectors.collectingAndThen
                        (
                        Collectors.groupingBy( Measurement::getName ),
                        groupedMap -> groupedMap.entrySet().stream().collect
                            (
                            Collectors.toMap
                              (
                                e -> e.getKey(),
                                e -> e.getValue().stream().mapToDouble( Measurement::getValue ).sum(),
                                (e1, e2) -> e1,
                                TreeMap::new // sorted map
                              )
                            )
                        )
                    );
        return grouped;
    }
}
