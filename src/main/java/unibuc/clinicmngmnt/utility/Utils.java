package unibuc.clinicmngmnt.utility;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Utils {

    public static List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> orders = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            orders.add(new Sort.Order(direction, sort));
        }
        return orders;
    }
}
