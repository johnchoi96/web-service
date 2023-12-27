package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.clients.CfbClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CfbService {

    private final CfbClient cfbClient;

    public void runUpsetReport() throws JsonProcessingException {
        // make sure time is during the football season on Sunday
        throw new NotImplementedException();
    }

    private Object findCurrentWeek(@NonNull final List<Object> entries, Instant current) {
//        if (current == null) {
//            current = Instant.now();
//        }
//        int start = 0, end = entries.size() - 1;
//        while (start < end) {
//            int midpoint = start + (end - start) / 2;
//            var startDate = entries.get(midpoint).getStartDate();
//            var endDate = entries.get(midpoint).getEndDate();
//            if (currentIsInRange(startDate, endDate, null)) {
//                return entries.get(midpoint);
//            }
//            if (endDate.isBefore(current)) {
//                end = midpoint - 1;
//            } else if (startDate.isAfter(current)) {
//                start = midpoint;
//            }
//        }
//        // date range not found so return null
//        return null;
        throw new NotImplementedException();
    }

    /**
     * Returns true if current time is between start and end.
     *
     * @param start   starting time
     * @param end     ending time
     * @param current current time. If null, Instant.now() is used.
     * @return true if between range
     */
    private boolean currentIsInRange(@NonNull final Instant start, @NonNull final Instant end, Instant current) {
        if (current == null) {
            current = Instant.now();
        }
        return current.isBefore(start) || current.isAfter(end);
    }
}
