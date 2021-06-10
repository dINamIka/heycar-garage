package car.hey.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class VehicleSpecification implements Specification<VehicleEntity> {

    private final Pair<String, String> searchParameter;

    @Override
    public Predicate toPredicate(Root<VehicleEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (searchParameter == null) return null;
        return builder.equal(root.get(searchParameter.getFirst()), searchParameter.getSecond());
    }
}
